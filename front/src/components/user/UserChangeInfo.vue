<template>
  <v-container fluid class="px-3">

    <v-layout row wrap>

      <v-flex sm6 offset-sm3>
        <form>
          <v-flex xs12>
            <v-text-field label="Old password" counter type='password' v-model="oldPass" max="16"
            ></v-text-field>
          </v-flex>
          <v-flex xs12>
            <v-text-field label="New password" counter type='password' v-model="newPass" max="30"
            ></v-text-field>
          </v-flex>
          <v-flex xs12>
            <v-text-field v-bind:rules="rules" label="New password again" counter type='password' v-model="newPassAgain"
                          max="30"
            ></v-text-field>
          </v-flex>
          <v-flex xs12>
            <v-text-field type="email" label="Email(Left blank if you don't want to change it)"
                          counter v-model="email" max="16"
            ></v-text-field>
          </v-flex>

          <v-btn primary @click.native="changePersonalInfo">Submit</v-btn>
          <v-btn primary router href="/user">Back</v-btn>

          <v-alert success v-bind:value="changeOk">
            Done.
            <v-btn flat white router href="/user">Back</v-btn>
          </v-alert>

          <v-alert error v-bind:value="changeFailed">
            Incorrect data.
          </v-alert>
        </form>

      </v-flex>

    </v-layout>


  </v-container>
</template>

<script>
  import axios from 'axios';
  import properties from '../../properties';
  export default{
    data(){
      return {
        oldPass: '',
        newPass: '',
        newPassAgain: '',
        email: '',
        result: '',
        rules: [() => this.newPass !== this.newPassAgain ? 'Passwords do not match' : true],
        changeOk: false,
        changeFailed: false,
      }
    },

    methods: {
      changePersonalInfo(){

        const headers = {
          'Content-Type': 'application/json',
          'Authorization': this.$cookie.get('token'),
        };

        axios.post(properties.HOST + "/user/change-personal-info", {
            oldPass: this.oldPass,
            newPass: this.newPass,
            email: this.email,
            userId: this.$cookie.get('userId'),
            userType: this.$cookie.get('userType')
          },
          headers)
          .then((response) => {
            if (response.data) {
              this.changeOk = true;
              this.changeFailed = false;
              this.result = "FAILED";
            } else {
              this.changeFailed = true;
              this.changeOk = false;
              this.result = "OK";
            }
          })
      }
    }
  }
</script>
