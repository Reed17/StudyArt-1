<template>
  <v-container fluid class="px-3">
    <v-layout row wrap>
      <v-flex sm6 offset-sm3>
        <form>
          <v-flex xs12>
            <v-text-field label="Login" type="text" v-model="login" max="16"></v-text-field>
          </v-flex>
          <v-flex xs12>
            <v-text-field label="Password" type="password" counter v-model="pass" max="30"></v-text-field>
          </v-flex>
          <v-btn primary @click.native="submitLogin">Submit</v-btn>
          <v-btn primary router href="/register">Register</v-btn>
        </form>
        <v-alert v-if="loginOk" success v-bind:value="true">
          {{loginOkText}}
          <v-btn flat white router href="/">To main page</v-btn>
        </v-alert>
        <v-alert v-if="loginFail" error v-bind:value="true">
          {{loginFailText}}
        </v-alert>
      </v-flex>

    </v-layout>
  </v-container>
</template>

<script>
  import axios from 'axios';
  import properties from '../properties'
  export default {

    data () {
      return {
        login: '',
        pass: '',
        text: 'Logged in!',
        loginOk: false,
        loginFail: false,
        loginFailText: 'Wrong info. Please, try again.',
        loginOkText: ''
      }
    },
    methods: {
      submitLogin(){
        if (this.$cookie.get('accessKey')) {
          this.loginOk = true;
          this.loginOkText = "Already logged in!";
          return;
        }

        axios.post(properties.host + '/login', {
          login: this.login,
          password: this.pass
        }).then((response) => {

          axios.get(properties.host + '/getUserByAccessKey' + '?key=' + response.data)
            .then((response) => {
              this.$cookie.set('userId', response.data.id);
              this.$cookie.set('userType', response.data.userType);
            });

          this.$cookie.set('accessKey', response.data);
          this.$cookie.set('username', this.login);

          this.loginOkText = 'Welcome to StudyArt!';
          this.loginFail = false;
          this.loginOk = true;
        }).catch(() => {
          this.loginFail = true;
          this.loginOk = false;
        })
      },
    }
  }
</script>
