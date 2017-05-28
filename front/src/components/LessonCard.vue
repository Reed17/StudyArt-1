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
  import properties from '../properties'
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
        axios.get(properties.host + '/courses/lessons/get?id=' + this.$route.params.id)
          .then((response) => {
            this.lesson = response.data;
          });
      }
    }
  }
</script>
