<template>
  <v-container fluid class="px-3">
    <v-layout row wrap>
      <v-flex sm6 offset-sm3>
        <v-flex xs12>
          <v-text-field label="Login" counter v-model="login" max="16"></v-text-field>
        </v-flex>
        <v-flex xs12>
          <v-text-field label="Password" counter v-model="pass" max="30"></v-text-field>
          <app-modal buttonName='submit' title='Result' text='OK' agreeButton='OK'></app-modal>
        </v-flex>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
  import axios from 'axios';
  import properties from '../properties'
  import AppModal from "./Modal";
  export default {

    components: {AppModal},
    data () {
      return {
        login: '',
        pass: '',
        responseText: ''
      }
    },
    methods: {
      submitLogin(){
        axios.post(properties.host + '/login', {
          username: this.login,
          password: this.pass
        })
      }
    }
  }
</script>

<style lang="stylus">
  @import '../stylus/main'
</style>
