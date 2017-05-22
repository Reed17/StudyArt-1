<template>
  <mu-appbar title="Welcome to StudyArt!">
    <mu-text-field class="appbar-search-field" slot="right" hintText="find course"></mu-text-field>
    <mu-flat-button color="white" label="Find" slot="right"></mu-flat-button>
    <mu-flat-button label="Login" slot="right"></mu-flat-button>
    <mu-flat-button label="Register" slot="right"></mu-flat-button>
  </mu-appbar>
</template>

<script>
  export default {
    name: 'app-bar',
  }
</script>
