<template>
  <div id="main">
    <app-bar></app-bar>
    <app-menu></app-menu>
  </div>
</template>

<script>
  import AppMenu from "./AppMenu";
  import AppBar from "./AppBar";
  export default {
    components: {
      AppBar,
      AppMenu
    },
    name: 'main',
  }
</script>
