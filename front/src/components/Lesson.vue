<template>
  <v-card>
    <v-card-row class="grey lighten-3">
      <v-card-title>
        <span class="white--text">Name: {{ lesson.name }}</span>
        <v-spacer></v-spacer>
        <v-menu id="space" bottom left origin="top right" transition="v-scale-transition">
          <v-btn icon="icon" slot="activator" class="white--text" router :href='link'>
            <v-icon>info</v-icon>
          </v-btn>
        </v-menu>
      </v-card-title>
    </v-card-row>
  </v-card>
</template>

<script>
  // todo es6
  var marked = require('marked');
  export default {
    name: 'app-lesson',
    computed: {
      link: function () {
        return "/lessonCard/" + this.lesson.id;
      }
    },
    props: {
      lesson: {
        required: true,
      }
    },

  }
</script>
