<template>
  <v-container fluid class="px-3">
    <v-layout row wrap>
      <v-flex sm6 offset-sm3>
        <v-flex xs12>
          <v-text-field
            label="Login"
            counter
            v-model="login"
            max="16"
          ></v-text-field>
        </v-flex>
        <v-flex xs12>
          <v-text-field
            label="Email"
            counter
            v-model="email"
            max="30"
          ></v-text-field>
        </v-flex>
        <v-flex xs12>
          <v-text-field
            label="Password"
            counter
            v-model="pass"
            max="16"
          ></v-text-field>
        </v-flex>
        <v-btn primary light>Submit</v-btn>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
  export default {
    data () {
      return {
        login: '',
        email: '',
        pass: ''
      }
    }
  }
</script>

<style lang="stylus">
  @import '../stylus/main'
</style>
