<template>
  <v-layout row>
    <v-dialog v-model="dialog" persistent>
      <v-btn primary light slot="activator">{{buttonName}}</v-btn>
      <v-card>
        <v-card-row>
          <v-card-title>{{title}}</v-card-title>
        </v-card-row>
        <v-card-row>
          <v-card-text>{{text}}</v-card-text>
        </v-card-row>
        <v-card-row actions>
          <v-btn class="green--text darken-1" flat="flat" @click.native="dialog = false">{{agreeButton}}</v-btn>
        </v-card-row>
      </v-card>
    </v-dialog>
  </v-layout>
</template>

<script>
  export default {
    name: 'app-modal',
    data () {
      return {
        dialog: false
      }
    },
    props: ['buttonName', 'title', 'text', 'agreeButton']
  }
</script>
