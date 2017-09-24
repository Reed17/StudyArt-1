<template>

  <v-card>
    <v-btn block :warning="current" class="lesson-link" router :href='link'>
      <span class="lesson-name">{{ lesson.name }}</span>
    </v-btn>
  </v-card>
</template>

<script>
  import marked from 'marked';

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
      },
      current: {
        required: false
      }
    },
  }
</script>
