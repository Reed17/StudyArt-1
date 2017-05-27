<template>
  <v-expansion-panel-content>
    <div slot="header">Name: {{ lesson.name }}</div>
    <v-card>
      <v-card-text class="grey lighten-3">
        Description: {{ lesson.description }}
      </v-card-text>
    </v-card>
  </v-expansion-panel-content>
</template>

<script>
  export default {
    name: 'app-lesson',
    props: {
      lesson: {
        required: true,
      }
    }
  }
</script>

<style lang="stylus">
  @import '../stylus/main'
</style>

<style>
</style>
