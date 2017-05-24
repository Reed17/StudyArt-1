<template>
  <div id="container" align="center">

    <!--inputs-->
    <br>
    <mu-text-field
      v-model="login"
      hintText='login'
      :errorText='inputErrorText'
      @textOverflow='handleInputOverflow'
      :maxLength='30'>
    </mu-text-field>

    <br/>
    <mu-text-field
      type="password"
      v-model="password"
      hintText='password'
      :errorText='inputErrorText'
      @textOverflow='handleInputOverflow'
      :maxLength='30'>
    </mu-text-field>

    <!--buttons-->
    <br/>
    <mu-flat-button label="Submit" align="left"></mu-flat-button>
    <router-link to="register">
      <mu-flat-button label="Register"></mu-flat-button>
    </router-link>
  </div>
</template>

<script>
  export default {
    data () {
      return {
        login: '',
        password: '',
        input: '',
        inputErrorText: '',
      }
    },
    methods: {
      handleInputOverflow (isOverflow) {
        this.inputErrorText = isOverflow ? 'limit reached!' : ''
      },
    }
  }
</script>
