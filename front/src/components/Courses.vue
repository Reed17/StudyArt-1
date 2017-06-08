<template>
  <div id="container">

    <v-container>
      <v-text-field
        label="Filter key"
        v-model="filterKey"
      ></v-text-field>
    </v-container>

    <v-container>
      <h3>Courses</h3>
      <v-expansion-panel>
        <course-card
          v-for="course in filteredCourses"
          :course="course"
          :key="course.id"
        >
        </course-card>
      </v-expansion-panel>
    </v-container>

  </div>
</template>

<script>
  import axios from "axios";
  import CourseCard from "./CourseСard";
  import PROPERTIES from "../properties"
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
        const headers = {
          'Content-Type': 'application/json',
          'Authorization': this.$cookie.get('token'),
        };

        axios.get(PROPERTIES.HOST + '/courses/getAll', {
          headers
        })
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
