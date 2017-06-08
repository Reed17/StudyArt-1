<template>
  <v-app>
    <app-navbar :miniVariant="miniVariant" :clipped="clipped" :drawer="drawer"></app-navbar>

    <v-toolbar>
      <v-toolbar-side-icon @click.native.stop="drawer = !drawer"></v-toolbar-side-icon>

      <v-btn icon @click.native.stop="miniVariant = !miniVariant">
        <v-icon v-html="miniVariant ? 'chevron_right' : 'chevron_left'"></v-icon>
      </v-btn>

      <v-btn icon @click.native.stop="clipped = !clipped">
        <v-icon>web</v-icon>
      </v-btn>

      <v-btn icon @click.native.stop="fixed = !fixed">
        <v-icon>remove</v-icon>
      </v-btn>

      <v-btn flat large router href="/">
        {{title}}
      </v-btn>

      <v-spacer></v-spacer>

      <v-btn v-if="!this.$cookie.get('token')" flat router href="/login">
        Sign in
      </v-btn>

      <v-menu v-if="this.$cookie.get('token')" >
        <v-btn primary slot="activator">{{this.$cookie.get('username')}}</v-btn>
        <v-list>
          <v-list-item>
            <v-list-tile router href="/user">
              <v-list-tile-title>Info</v-list-tile-title>
            </v-list-tile>
          </v-list-item>
          <v-list-item>
            <v-list-tile @click.native="logout">
              <v-list-tile-title>Log out</v-list-tile-title>
            </v-list-tile>
          </v-list-item>
        </v-list>
      </v-menu>

      <v-btn icon @click.native.stop="rightDrawer = !rightDrawer">
        <v-icon>menu</v-icon>
      </v-btn>

    </v-toolbar>

    <main>
      <v-container fluid>
        <router-view></router-view>
      </v-container>
    </main>

    <v-navigation-drawer temporary :right="right" v-model="rightDrawer">
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
  import AppNavbar from "./components/NavBar";
  export default {
    components: {AppNavbar},
    data () {
      return {
        clipped: false,
        drawer: true,
        fixed: false,

        miniVariant: true,
        right: true,
        rightDrawer: false,
        title: 'StudyArt'
      }
    },
    methods: {
        logout(){
            this.$cookie.delete('token');
            this.$cookie.delete('username');
            this.$forceUpdate();
        }
    },
  }
</script>

<style lang="stylus">
  @import './stylus/main'
</style>
