<template>
  <div>
    <mu-card>

      <mu-card-title :title=course.name></mu-card-title>

      <mu-divider/>

      <mu-card-header :title=course.author :subTitle=course.url></mu-card-header>

      <mu-divider/>

      <mu-list>
        <mu-sub-header>Lessons:</mu-sub-header>
        <mu-list-item v-for="lesson in course.lessons" :key="lesson.id">
          Lesson name: {{lesson.name}}
          <br/>
          Description: {{lesson.description}}
        </mu-list-item>
      </mu-list>

      <mu-divider/>

      <mu-card-text>Course description : {{course.description}}</mu-card-text>

      <mu-divider/>

      <mu-card-actions>
        <mu-flat-button label="START"/>
      </mu-card-actions>

    </mu-card>
  </div>
</template>

<script>
  import axios from "axios";
  import PROPERTIES from '../properties'
  export default {
    name: 'course',
    data(){
      return {
        course: Object,
      }
    },
    mounted(){
      this.fetchCourse();
    },
    methods: {
      fetchCourse() {
        axios.get(PROPERTIES.HOST + '/courses/get?id=' + this.$route.params.id)
          .then((response) => {
            this.course = response.data;
          });
      },
    }
  }
</script>
