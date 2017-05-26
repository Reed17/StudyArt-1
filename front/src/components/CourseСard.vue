<template>
  <v-card>
    <v-card-row class="blue">
      <v-card-title>
        <span class="white--text">{{course.name}}</span>
        <v-spacer></v-spacer>
        <div>
          <v-menu id="marriot" bottom left origin="top right">
            <v-btn icon="icon" slot="activator" class="white--text">
              <v-icon>more_vert</v-icon>
            </v-btn>
            <v-list>
              <v-list-item>
                <v-list-tile>
                  <v-list-tile-title>Add to my courses</v-list-tile-title>
                </v-list-tile>
              </v-list-item>
              <v-list-item>
                <v-list-tile>
                  <v-list-tile-title>...</v-list-tile-title>
                </v-list-tile>
              </v-list-item>
            </v-list>
          </v-menu>
        </div>
      </v-card-title>
    </v-card-row>
    <v-card-text>
      <v-card-row height="75px">
        <v-icon class="mr-5" dark>info_outline</v-icon>
        <div>
          <div>Description</div><strong>{{course.description}}</strong>
        </div>
      </v-card-row>
      <v-card-row height="75px">
        <v-icon class="mr-5" dark>account_circle</v-icon>
        <div>
          <div>Author</div><strong>{{course.author}}</strong>
        </div>
      </v-card-row>
      <v-card-row height="75px">
        <v-icon class="mr-5" dark>find_in_page</v-icon>
        <div>
          <div>URL</div><strong>{{course.url}}</strong>
        </div>
      </v-card-row>
    </v-card-text>
    <v-divider></v-divider>

    <v-card-row actions>
      <v-btn flat router class="blue--text" :href="link">View more</v-btn>
    </v-card-row>
  </v-card>
</template>

<script>
  export default{
    name: 'course-card',
    computed: {
        link: function () {
          return '/course/' + this.course.id;
        }
    },
    props: {
      course: {
        required: true,
      }
    }
  }
</script>
