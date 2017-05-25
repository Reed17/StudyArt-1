<template>
  <div>
    <mu-card>

      <mu-card-title :title=course.name></mu-card-title>

      <mu-card-header :title=course.author :subTitle=course.url></mu-card-header>

      <mu-list>

        <mu-sub-header>Lessons:</mu-sub-header>

        <mu-list-item v-for="lesson in course.lessons">
          Lesson name: {{lesson.name}}
          <br/>
          Description: {{lesson.description}}
        </mu-list-item>

      </mu-list>
      <mu-card-text>Course description : {{course.description}}</mu-card-text>

      <mu-card-actions>
        <mu-flat-button label="START"/>
      </mu-card-actions>

    </mu-card>
  </div>
</template>

<script>
  import axios from "axios";
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
          axios.get('http://localhost:8080/courses/get?id=' + this.$route.params.id)
          .then((response) => {
            this.course = response.data;
          });
      },
    }
  }
</script>
