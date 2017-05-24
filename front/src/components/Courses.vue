<template>
  <div id="container">
    <div align="center">
      <mu-text-field v-model="filterKey" hintText="enter filter key"></mu-text-field>
    </div>

    <br/>

    <div class="wrapper">
      <course v-for="course in filteredCourses" :course="course" :key="course.id"></course>
    </div>

  </div>
</template>

<script>
  import axios from "axios";
  import Course from "./Course";
  export default {
    components: {Course},
    name: 'courses',

    data() {
      return {
        courseData: [],
        filterKey: '',
      }
    },

    computed: {
      filteredCourses: function () {
        return this.filterCourses()
      }
    },

    mounted()
    {
      this.fetchCourses();
    },

    methods: {
      fetchCourses() {
        axios.get('http://localhost:8080/courses/getAll') // todo extract to props
          .then((response) => {
            this.courseData = response.data;
          });
      },

      filterCourses() {
        let courses = this.courseData;
        let filterKeyIgnoreCase = this.filterKey.toLowerCase();

        return courses.filter((course) =>
          course.name.toLowerCase().includes(filterKeyIgnoreCase) ||
          course.description.toLowerCase().includes(filterKeyIgnoreCase)
        );
      },
    }
  }
</script>

<style>
  .wrapper > div {
    display: inline-block;
    box-sizing: border-box;
    width: 25%;
    letter-spacing: normal;
  }
</style>
