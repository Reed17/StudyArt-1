<template>
  <div>
    <div align="center">
      <mu-text-field v-model="filterKey" hintText="enter filter key"></mu-text-field>
    </div>
    <br/>
    <div v-for="course in filteredCourses" align="center">
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
      }
    }
  }
</script>
