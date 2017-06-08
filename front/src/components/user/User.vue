<template>
  <v-layout row wrap>
    <v-flex xs6>
      <v-card>
        <v-card-row>
          <v-card-title>
            <span>Username: {{user.username}}</span>
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
          <v-btn flat router href='/user/change-personal-info'>Change personal info</v-btn>
          <v-dialog v-model="deleteAccountDialog">
            <v-btn flat slot="activator">Delete account</v-btn>
            <v-card>
              <v-card-row>
                <v-card-title>Delete account</v-card-title>
              </v-card-row>
              <v-card-row>
                <v-card-text>
                  Are you sure? This action is irreversible.
                </v-card-text>
              </v-card-row>
              <v-card-row actions>
                <v-btn class="green--text darken-1" flat="flat" @click.native="deleteAccountDialog = false">Disagree
                </v-btn>
                <v-btn class="green--text darken-1" flat="flat" @click.native="deleteAccount">Agree</v-btn>
              </v-card-row>
            </v-card>
          </v-dialog>
        </v-card-row>
      </v-card>
    </v-flex>

    <v-flex xs6>
      <v-container v-if="user.userType==='TEACHER'">
        <h3>Courses</h3>
        <v-expansion-panel v-if="user.courses.length">
          <v-expansion-panel-content v-for="course in user.courses" :key="course.id">
            <div slot="header">Course name: {{course.name}}</div>
            <v-card>
              <v-card-text class="grey lighten-3">Descrition: {{course.description}}</v-card-text>
            </v-card>
          </v-expansion-panel-content>
        </v-expansion-panel>
        <p class="text-xs-left" v-else>
          No courses yet.
          <router-link to="/courses">Create your own course!</router-link>
        </p>
      </v-container>

      <v-container v-if="user.userType==='STUDENT'">
        <h5>Subscribed</h5>
        <v-expansion-panel v-if="user.subscribed.length">
          <v-expansion-panel-content v-for="course in user.subscribed" :key="course.id">
            <div slot="header">Course name: {{course.name}}</div>
            <v-card>
              <v-card-text class="grey lighten-3">Descrition: {{course.description}}</v-card-text>
            </v-card>
          </v-expansion-panel-content>
        </v-expansion-panel>
        <p class="text-xs-left" v-else>
          No courses yet.
          <router-link to="/courses">Find courses</router-link>
        </p>
      </v-container>

      <v-container v-if="user.userType==='STUDENT'">
        <h5>Completed</h5>
        <v-expansion-panel v-if="user.completed.length">
          <v-expansion-panel-content v-for="course in user.completed" :key="course.id">
            <div slot="header">Course name: {{course.name}}</div>
            <v-card>
              <v-card-text class="grey lighten-3">Descrition: {{course.description}}</v-card-text>
            </v-card>
          </v-expansion-panel-content>
        </v-expansion-panel>
        <p class="text-xs-left" v-else>
          No courses yet.
        </p>
      </v-container>
    </v-flex>


  </v-layout>
</template>

<script>
  import axios from 'axios';
  import properties from '../../properties'
  export default {

    name: 'app-user',
    data(){
      return {
        user: Object,
        deleteAccountDialog: false,
      }
    },

    mounted(){
      this.fetchUserInfo()
    },

    methods: {
      fetchUserInfo(){
        axios.get(properties.HOST + '/getUserByAccessKey' + '?key=' + this.$cookie.get('accessKey'))
          .then((response) => {
            this.user = response.data;
          })
      },

      deleteAccount(){
        axios.get(properties.HOST + '/user/delete' + '?userId=' + this.$cookie.get('userId'))
          .then((response) => {
            // delete cookies
            this.$cookie.delete('accessKey');
            this.$cookie.delete('userId');
            this.$cookie.delete('userType');

            // router push to new page
            this.$router.push('/');
          })
      }
    }

  }

</script>
