<template>
  <v-layout row wrap>
    <v-flex xs12>
      <v-card>
        <v-card-row>
          <v-card-title>
            <span>Username: {{user.login}}</span>
            <v-spacer></v-spacer>

          </v-card-title>
        </v-card-row>
        <v-card-text>
          <v-card-row height="75px">
            <v-icon class="mr-5" dark>email</v-icon>
            <div>
              <div>Email</div>
              <strong>{{user.email}}</strong>
            </div>
          </v-card-row>
          <v-card-row height="75px">
            <v-icon class="mr-5" dark>face</v-icon>
            <div>
              <div>Type</div>
              <strong>{{user.userType}}</strong>
            </div>
          </v-card-row>
          <v-card-row height="75px">
            <v-icon class="mr-5" dark>done</v-icon>
            <div>
              <div>Activated</div>
              <strong>{{user.activated ? 'Yes' : 'No'}}</strong>
            </div>
          </v-card-row>

          <v-card-row v-if="user.userType==='STUDENT'" height="75px">
            <v-icon class="mr-5" dark>subscriptions</v-icon>
            <div>
              <div>Subscribed</div>
              <strong v-if="!user.subscribed.length">No courses yet</strong>
              <app-lesson v-for="lesson in user.subscribed" :lesson="lesson" :key="lesson.id"></app-lesson>
            </div>
          </v-card-row>

          <v-card-row v-if="user.userType==='STUDENT'" height="75px">
            <v-icon class="mr-5" dark>done_all</v-icon>
            <div>
              <div>Completed</div>
              <strong v-if="!user.completed.length">No courses yet</strong>
              <app-lesson v-for="lesson in user.completed" :lesson="lesson" :key="lesson.id"></app-lesson>
            </div>
          </v-card-row>

          <v-card-row v-if="user.userType==='TEACHER'" height="75px">
            <v-icon class="mr-5" dark>list</v-icon>
            <div>
              <div>Courses</div>
              <strong v-if="!user.courses.length">No courses yet</strong>
              <app-lesson v-for="lesson in user.courses" :lesson="lesson" :key="lesson.id"></app-lesson>
            </div>
          </v-card-row>

        </v-card-text>


        <v-divider></v-divider>
        <v-card-row actions>
          <v-btn flat>Change personal info</v-btn>
          <v-btn flat>Delete account</v-btn> <!--todo modal!-->
        </v-card-row>
      </v-card>
    </v-flex>
  </v-layout>
</template>

<script>
  import axios from 'axios';
  import properties from '../properties'
  export default {

    name: 'app-user',
    data(){
      return {
        user: Object,
      }
    },

    mounted(){
      this.fetchUserInfo()
    },

    methods: {
      fetchUserInfo(){
        axios.get(properties.host + '/getUserByAccessKey' + '?key=' + this.$cookie.get('accessKey'))
          .then((response) => {
            this.user = response.data;
          })
      }
    }

  }

</script>
