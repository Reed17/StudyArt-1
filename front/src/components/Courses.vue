<template>
  <div>
    <div v-for="course in courseData">
      <mu-card>
        <mu-card-header :title=course.author :subTitle=course.url>
        </mu-card-header>
        <mu-card-title :title=course.name></mu-card-title>
        <mu-card-text>
          Description : {{course.description}}
        </mu-card-text>
      </mu-card>
    </div>
  </div>
</template>

<script>
  import axios from "axios";
  export default {
    name: 'courses',
    data() {
      return {
        courseData: []
      }
    },
    mounted() {
      axios.get('http://localhost:8080/courses/getAll')
        .then((response) => {
          this.courseData = response.data;
        });
    },
  }
</script>
