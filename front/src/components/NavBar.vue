<template>
  <v-navigation-drawer
    persistent
    :mini-variant="miniVariant"
    :clipped="clipped"
    v-model="drawer"
  >
    <v-list>
      <v-list-item
        v-for="(item, i) in items"
        :key="i"
        icon
      >
        <v-list-tile router value="true" :href="item.path">
          <v-list-tile-action>
            <v-icon light v-html="item.icon"></v-icon>
          </v-list-tile-action>
          <v-list-tile-content>
              <v-list-tile-title v-text="item.title"></v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
      </v-list-item>
    </v-list>
  </v-navigation-drawer>
</template>

<script>
  export default{
    data(){
      return {
        items: [
          {title: 'Courses', path: '/courses', icon: 'list'},
          {title: 'About', path: '/about', icon: 'info'},
          {title: 'Contacts', path: '/contacts', icon: 'contacts'},
          {title: 'AceEditor', path: '/editor', icon: 'contacts'},
        ],
      }
    },
    props: ['miniVariant', 'clipped', 'drawer'],
    name: "app-navbar"
  }
</script>
