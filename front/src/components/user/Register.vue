<template>
  <v-container fluid class="px-3">

    <v-layout row wrap>

      <v-flex sm6 offset-sm3>
        <form>
          <v-flex xs12>
            <v-text-field label="Login" counter v-model="username" max="16"
            ></v-text-field>
          </v-flex>
          <v-flex xs12>
            <v-text-field label="Email" counter v-model="email" max="30"
            ></v-text-field>
          </v-flex>
          <v-flex xs12>
            <v-text-field type="password" label="Password" counter v-model="password" max="16"
            ></v-text-field>
          </v-flex>

          <v-flex xs12>
            <v-select v-bind:items="types" v-model="userType" label="User type" dark single-line auto></v-select>
          </v-flex>

          <v-btn primary @click.native="submitRegister">Submit</v-btn>
          <v-btn primary router href="/username">Back</v-btn>
        </form>

        <v-alert success v-bind:value="registerOk">
          {{registerOkText}}
          <v-btn flat white router href="/username">To username page</v-btn>
        </v-alert>

        <v-alert error v-bind:value="registerFail">
          {{registerFailText}}
        </v-alert>

      </v-flex>

    </v-layout>

  </v-container>
</template>

<script>
  import axios from 'axios';
  import properties from '../../properties'
  export default {
    data () {
      return {
        username: '',
        email: '',
        password: '',
        userType: '',
        types: ['Student', 'Teacher'],
        registerOk: false,
        registerFail: false,
        registerOkText: '',
        registerFailText: '',
      }
    },

    methods: {
      submitRegister(){
        if (this.registerOk) {
          this.registerOkText = 'Already registered.';
          return;
        }

        axios.post(properties.HOST + '/register',
          {
            login: this.username,
            email: this.email,
            pass: this.password,
            type: this.userType.toUpperCase(),
          }).then(() => {
          this.registerOkText = 'Register done.';
          this.registerFail = false;
          this.registerOk = true;
        }).catch((reason) => {
          this.registerFailText = 'Failed. Reason: ' + reason;
          this.registerFail = true;
          this.registerOk = false;
        })
      }
    }
  }
</script>
