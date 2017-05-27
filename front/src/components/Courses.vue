<template>
  <div id="container">
    <div align="center">
      <v-text-field
        label="Filter key"
        v-model="filterKey"
      ></v-text-field>
    </div>

    <div class="wrapper">
      <course-card
        v-for="course in filteredCourses"
        :course="course"
        :key="course.id"
      ></course-card>
    </div>

  </div>
</template>

<script>
  import axios from "axios";
  import CourseCard from "./CourseСard";
  import properties from "../properties"
  export default {
    components: {CourseCard},
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
      },
      filterKeyLowerCase: function () {
        return this.filterKey.toLowerCase();
      }
    },

    mounted() {
      this.fetchCourses();
    },

    methods: {
      fetchCourses() {
        axios.get(properties.host + '/courses/getAll')
          .then((response) => {
            this.courseData = response.data;
          });
      },

      filterCourses() {
        let courses = this.courseData;
        return courses.filter((course) =>
          course.name.toLowerCase().includes(this.filterKeyLowerCase) ||
          course.description.toLowerCase().includes(this.filterKeyLowerCase)
        ).sort(this.sortCoursesComparator);
      },

      sortCoursesComparator(course1, course2){
        return course1.name.localeCompare(course2.name) > 0;
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
