<template>

  <v-card>
    <!-- <v-card-row class="grey lighten-3"> -->
      <!-- <v-card-title> -->
        <v-btn block class="lesson-link" router :href='link'>
          <span class="lesson-name">{{ lesson.name }}</span>
        </v-btn>
      <!-- </v-card-title> -->
    <!-- </v-card-row> -->
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
