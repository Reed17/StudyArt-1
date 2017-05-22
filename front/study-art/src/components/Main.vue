<template>
  <div id="main">
    <app-bar></app-bar>
    <router-link to="test">TO TEST</router-link>
    <router-view></router-view>
  </div>
</template>

<script>
  import AppBar from "./AppBar";
  export default {
    components: {
      AppBar,
    },
    name: 'main',
  }
</script>
