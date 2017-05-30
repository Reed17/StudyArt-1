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
        </v-card-text>
        <v-divider></v-divider>
        <v-card-row actions>
          <v-btn flat>Change personal info</v-btn>
          <v-btn flat>Delete account</v-btn> <!--todo modal!-->
        </v-card-row>
      </v-card>

      <v-divider></v-divider>
      <br/>

      <v-container v-if="user.userType==='TEACHER' && user.courses.length">
        <h3>Courses</h3>
        <v-expansion-panel>
          <v-expansion-panel-content v-for="course in user.courses" :key="course.id">
            <div slot="header">Course name: {{course.name}}</div>
            <v-card>
              <v-card-text class="grey lighten-3">Descrition: {{course.description}}</v-card-text>
            </v-card>
          </v-expansion-panel-content>
        </v-expansion-panel>
      </v-container>

      <v-container v-if="user.userType==='STUDENT' && user.subscribed.length">
        <h5>Subscribed</h5>
        <v-expansion-panel>
          <v-expansion-panel-content v-for="course in user.subscribed" :key="course.id">
            <div slot="header">Course name: {{course.name}}</div>
            <v-card>
              <v-card-text class="grey lighten-3">Descrition: {{course.description}}</v-card-text>
            </v-card>
          </v-expansion-panel-content>
        </v-expansion-panel>
      </v-container>

      <v-container v-if="user.userType==='STUDENT' && user.completed.length">
        <h5>Completed</h5>
        <v-expansion-panel v-if="user.userType==='STUDENT' && user.completed.length">
          <v-expansion-panel-content v-for="course in user.completed" :key="course.id">
            <div slot="header">Course name: {{course.name}}</div>
            <v-card>
              <v-card-text class="grey lighten-3">Descrition: {{course.description}}</v-card-text>
            </v-card>
          </v-expansion-panel-content>
        </v-expansion-panel>
      </v-container>


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
