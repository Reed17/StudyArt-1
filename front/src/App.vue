<template>
  <v-app>
    <app-navbar
      :miniVariant="miniVariant"
      :clipped="clipped"
      :drawer="drawer"
    ></app-navbar>

    <v-toolbar>
      <v-toolbar-side-icon @click.native.stop="drawer = !drawer"></v-toolbar-side-icon>

      <v-btn
        icon
        @click.native.stop="miniVariant = !miniVariant"

      >
        <v-icon v-html="miniVariant ? 'chevron_right' : 'chevron_left'"></v-icon>
      </v-btn>

      <v-btn
        icon
        @click.native.stop="clipped = !clipped"
      >
        <v-icon>web</v-icon>
      </v-btn>

      <v-btn
        icon
        @click.native.stop="fixed = !fixed"
      >
        <v-icon>remove</v-icon>
      </v-btn>

      <v-btn
        flat
        large
        router
        href="/"
      >
        {{title}}
      </v-btn>

      <v-spacer></v-spacer>
      <v-btn
        align-right
        flat
        router
        href="/login"
      >
        Login
      </v-btn>

      <v-btn
        align-right
        flat
        router
        href="/register"
      >
        Register
      </v-btn>

      <v-btn
        icon
        @click.native.stop="rightDrawer = !rightDrawer"
      >
        <v-icon>menu</v-icon>
      </v-btn>

    </v-toolbar>

    <main>
      <v-container fluid>
        <router-view></router-view>
      </v-container>
    </main>

    <v-navigation-drawer
      temporary
      :right="right"
      v-model="rightDrawer"
    >
      <v-list>
        <v-list-item>
          <v-list-tile @click.native="right = !right">
            <v-list-tile-action>
              <v-icon light>compare_arrows</v-icon>
            </v-list-tile-action>
            <v-list-tile-title>Switch drawer (click me)</v-list-tile-title>
          </v-list-tile>
        </v-list-item>
      </v-list>

    </v-navigation-drawer>

    <v-footer :fixed="fixed">
      <span>&copy; 2017</span>
    </v-footer>

  </v-app>
</template>

<script>
  import AppNavbar from "./components/main/NabBar";
  export default {
    components: {AppNavbar},
    data () {
      return {
        clipped: false,
        drawer: true,
        fixed: false,

        miniVariant: false,
        right: true,
        rightDrawer: false,
        title: 'StudyArt'
      }
    }
  }
</script>

<style lang="stylus">
  @import './stylus/main'
</style>
