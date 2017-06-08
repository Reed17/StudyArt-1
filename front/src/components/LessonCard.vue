<template>
  <v-card>
    <v-card-title>
      {{lesson.name}}
    </v-card-title>
    <v-card-text>
      <div v-html="compiledMarkdown"></div>
    </v-card-text>
  </v-card>
</template>

<script>
  import axios from "axios";
  import PROPERTIES from '../properties'
  import marked from 'marked';
  export default {
    name: 'lesson-card',
    data(){
      return {
        lesson: Object,
      }
    },
    computed: {
      compiledMarkdown: function () {
        if (this.lesson.description != undefined)
          return marked(this.lesson.description, {sanitize: true})
      }
    },
    mounted(){
      this.fetchLesson();
    },
    methods: {
      fetchLesson() {

        const headers = {
          'Content-Type': 'application/json',
          'Authorization': this.$cookie.get('token'),
        };

        axios.get(PROPERTIES.HOST + '/courses/lessons/get', {
          params: {
            id: this.$route.params.id
          },
          headers
        })
          .then((response) => {
            this.lesson = response.data;
          });
      }
    }
  }
</script>
