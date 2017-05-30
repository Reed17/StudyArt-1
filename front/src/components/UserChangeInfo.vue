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
            <v-text-field label="New password again" counter type='password' v-model="newPassAgain" max="30"
            ></v-text-field>
          </v-flex>
          <v-flex xs12>
            <v-text-field type="password" label="Email(Left blank if you don't want to change it)"
                          counter v-model="email" max="16"
            ></v-text-field>
          </v-flex>

          <h6>Result: {{result}}</h6>

          <v-btn primary @click.native="changePersonalInfo">Submit</v-btn>
          <v-btn primary router href="/user">Back</v-btn>
        </form>

      </v-flex>

    </v-layout>

  </v-container>
</template>

<script>
  import axios from 'axios';
  import properties from '../properties';
  export default{
    data(){
      return {
        oldPass: '',
        newPass: '',
        newPassAgain: '',
        email: '',
        result: '',
      }
    },

    methods: {
        changePersonalInfo(){
            axios.post(properties.host + "/user/change-personal-info", {
                oldPass: this.oldPass,
                newPass: this.newPass,
                email: this.email,
                userId: this.$cookie.get('userId'),
                userType: this.$cookie.get('userType')
            }).then((response) => {
                this.result = response.data==undefined ? "FAILED" : "OK";
            })
        }
    }
  }
</script>
