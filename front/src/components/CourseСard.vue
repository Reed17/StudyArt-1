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

      <v-card-row v-if="userType==='STUDENT' && !subscribed.includes(course.id)" actions>
        <v-btn @click.native="subscribe">Subscribe</v-btn>
      </v-card-row>
      <v-card-text v-else>
        <p class="text-xs-center">Subscribed</p>
      </v-card-text>

      <v-alert success v-if="userType==='STUDENT'" v-bind:value="subscribeOk">
        {{subscribedOkInfo}}
      </v-alert>

      <v-alert error v-if="userType==='STUDENT'" v-bind:value="subscribeFailed">
        Already subscribed
      </v-alert>

    </v-card>

  </v-expansion-panel-content>

</template>

<script>
  import AppLesson from "./Lesson";
  import axios from 'axios';
  import properties from '../properties'
  export default{
    components: {AppLesson},
    name: 'course-card',
    data() {
      return {
        subscribeOk: false,
        subscribedOkInfo: 'Subscribed',
        subscribeFailed: false,
        userType: this.$cookie.get('userType'),
        subscribed: [],
      }
    },
    computed: {
      link: function () {
        return '/course/' + this.course.id;
      }
    },
    props: {
      course: {
        required: true,
      }
    },

    mounted(){
      this.fetchSubscribed();
    },

    methods: {
      subscribe(){
        if (this.subscribeOk) {
          this.subscribedOkInfo = "Already subscribed.";
          return;
        }

        axios.get(properties.host +
          '/subscribe?courseId=' + this.course.id +
          '&userId=' + this.$cookie.get('userId'))
          .then((response) => {
            response.data.type === 'INFO' ?
              this.subscribeOk = true : this.subscribeFailed = true;
          });
      },

      fetchSubscribed(){
        axios.get(properties.host +
          '/getUserByAccessKey?key=' +
          this.$cookie.get('accessKey'))
          .then((response) => this.subscribed = response.data.subscribed.map((currentValue) => currentValue.id))
      }
    }
  }
</script>
