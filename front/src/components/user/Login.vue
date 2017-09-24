<template>
  <v-container fluid class="px-3">
    <v-layout row wrap>
      <v-flex sm6 offset-sm3>
        <form>
          <v-flex xs12>
            <v-text-field label="Login" type="text" v-model="username" max="16"></v-text-field>
          </v-flex>
          <v-flex xs12>
            <v-text-field label="Password" type="password" counter v-model="password" max="30"></v-text-field>
          </v-flex>
          <v-btn primary @click.native="submitLogin">Submit</v-btn>
          <v-btn primary router href="/register">Register</v-btn>
        </form>
        <v-alert success v-bind:value="loginOk">
          {{loginOkText}}
          <v-btn flat white router href="/">To main page</v-btn>
        </v-alert>
        <v-alert error v-bind:value="loginFail">
          {{loginFailText}}
        </v-alert>
      </v-flex>

    </v-layout>
  </v-container>
</template>

<script>
  import axios from 'axios';
  import cryptoJS from 'crypto-js';
  import $ from 'jquery';
  import PROPERTIES from '../../properties'
  export default {

    data () {
      return {
        username: '',
        password: '',
        text: 'Logged in!',
        loginOk: false,
        loginFail: false,
        loginFailText: 'Wrong info. Please, try again.',
        loginOkText: ''
      }
    },
    methods: {

      login(){
        const pass = cryptoJS.MD5(this.password).toString();

        const axiosConfig = {
          baseURL: PROPERTIES.HOST,
          data: {"username": this.username, "password": pass},
          headers: {
            'Content-Type': 'text/plain'
          },
          method: "post",
          url: "/login",
          withCredentials: true,
        };

        return axios(axiosConfig);
      },


      // todo optimize
      submitLogin(){

        /* // todo replace with token
         if (this.$cookie.get('accessKey')) {
         this.loginOk = true;
         this.loginOkText = "Already logged in!";
         return;
         }*/

        this.login()
          .then((response) => {

            this.$cookie.set('token', response.headers.authorization);
            this.$cookie.set('username', this.username);

            const headers = {
              'Content-Type': 'application/json',
              'Authorization': this.$cookie.get('token'),
            };

            axios.get(PROPERTIES.HOST + '/findByUsername', {
              params: {
                username: this.username
              },
              headers
            }).then((response) => {

              this.$cookie.set('userId', response.data.id);
              this.$cookie.set('userType', response.data.userType);

              this.loginOkText = 'Welcome to StudyArt!';
              this.loginFail = false;
              this.loginOk = true;

            }).catch((error) => {
              console.log(error);
              this.loginFail = true;
              this.loginOk = false;
            });


          })
          .catch((error) => {
            console.log(error);
            this.loginFail = true;
            this.loginOk = false;
          })
      },
    }
  }
</script>
