<template>
  <div id="app">
    <app-bar></app-bar>

    <div>
      <router-view></router-view> <!--todo how to extract this?-->
    </div>

  </div>
</template>

<script>
  import AppBar from "./components/AppBar";
  export default {
    components: {AppBar},
    name: 'app',

  }
</script>
