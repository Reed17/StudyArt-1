<template>
  <v-expansion-panel-content>
    <div slot="header">{{ course.name }}</div>
    <v-card>
      <v-card-text class="grey lighten-3">
        Author: {{course.author}}
      </v-card-text>
      <v-divider></v-divider>
      <v-card-text class="grey lighten-3">
        URL: {{course.url}}
      </v-card-text>
      <v-divider></v-divider>
      <v-card-text class="grey lighten-3">
        Description: {{ course.description }}
      </v-card-text>
      <v-card-text>
        <v-container>
          <h4>Lessons</h4>
          <v-expansion-panel>
            <app-lesson v-for="lesson in course.lessons" :lesson="lesson" :key="lesson.name"></app-lesson>
          </v-expansion-panel>
        </v-container>
      </v-card-text>
    </v-card>
  </v-expansion-panel-content>
</template>

<script>
  import AppLesson from "./Lesson";
  export default{
    components: {AppLesson},
    name: 'course-card',
    computed: {
        link: function () {
          return '/course/' + this.course.id;
        }
    },
    props: {
      course: {
        required: true,
      }
    }
  }
</script>
