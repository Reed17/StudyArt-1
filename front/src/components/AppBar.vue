<template>
  <mu-appbar id="container" title="Welcome to StudyArt!" align="center">
    <mu-flat-button color="white" label="Courses" slot="left" to="courses"></mu-flat-button>
    <mu-flat-button color="white" label="Contacts" slot="left"></mu-flat-button>
    <mu-flat-button color="white" label="About" slot="left"></mu-flat-button>

    <router-link to="/">
      <mu-flat-button color="white" to="/">Welcome to Study Art!</mu-flat-button>
    </router-link>

    <mu-text-field class="appbar-search-field" slot="right" hintText="find course"></mu-text-field>
    <mu-flat-button color="white" label="Find" slot="right"></mu-flat-button>

    <mu-flat-button label="Login" slot="right" to="login" class="mu-banner-button"></mu-flat-button>
    <mu-flat-button label="Register" slot="right" to="register"></mu-flat-button>
  </mu-appbar>
</template>

<script>
  export default {
    name: 'app-bar',
  }
</script>
