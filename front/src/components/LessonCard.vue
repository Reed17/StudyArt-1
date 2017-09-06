<template>

  <div>

    <v-expansion-panel expand>
      <v-expansion-panel-content class="white" v-model="expanded">
        <div slot="header" >
          <v-card-title >

            <v-btn icon :disabled="!prevLessonLink" class="indigo--text lesson-navigation-links" router :href="prevLessonLink">
              <v-icon>arrow_back</v-icon>
            </v-btn>

            {{lesson.name}}

            <v-btn icon :disabled="!nextLessonLink" class="indigo--text lesson-navigation-links" router :href="nextLessonLink">
              <v-icon>arrow_forward</v-icon>
            </v-btn>

          </v-card-title>
        </div>
        <!-- <v-card-text class="white" v-if="compiledMarkdownDescr != ''">
          <div v-html="compiledMarkdownDescr"></div>
        </v-card-text> -->
        <v-card-text class="white">
          <!-- <v-expansion-panel> -->
            <app-lesson v-for="lessonObj in allLessons" :current="lessonObj.id == lesson.id" :lesson="lessonObj" :key="lessonObj.name"></app-lesson>
          <!-- </v-expansion-panel> -->
        </v-card-text>
      </v-expansion-panel-content>
    </v-expansion-panel>

    <div class="example-2">
      <div id="one" class="split split-horizontal">

        <div v-html="compiledMarkdownTheory"></div>

      </div>
      <div id="two" class="split split-horizontal">
        <div id="three" class="split split-vertical">
          <v-app-bar>
            <v-btn-dropdown v-bind:options="dropdown_font" v-model="fontSize"
                            max-height="auto" editable="editable" label="Font size" overflow></v-btn-dropdown>
            <v-btn-dropdown v-bind:options="dropdown_lang" v-model="lang"
                            max-height="auto" editable="editable" label="Programming lang" overflow></v-btn-dropdown>
            <v-btn flat primary dark default @click.native="runCode()">Run code</v-btn>
            <v-progress-circular indeterminate class="primary--text" v-show="show_progress"></v-progress-circular>
          </v-app-bar>
          <brace style="height: 500px"
                 :fontsize="fontSize.text"
                 :theme="'github'"
                 :mode="lang.text"
                 :codefolding="'markbegin'"
                 :softwrap="'free'"
                 :selectionstyle="'text'"
                 :highlightline="true"
                 :dataedit="content"
                 @change="getCode($event)"
          >
          </brace>

        </div>
        <div id="four" class="split split-vertical">
          <v-text-field
            name="input-7-1"
            label="Result"
            :value="formattedResponse"
            multi-line
            disabled>
          </v-text-field>
        </div>
      </div>
    </div>




  </div>
</template>

<script>
  import Vue from 'vue';
  import AppLesson from "./Lesson";
  import axios from "axios";
  import PROPERTIES from '../properties';
  import marked from 'marked';
  import Split from 'split.js'
  import Brace from 'xen-brace'

  Vue.component('app-lesson', AppLesson);

  export default {
    // components: {AppLesson},
    name: 'lesson-card',

    data(){
      return {
        response: '',
        lesson: Object,
        content: 'public class HelloWorld {\n public static void main(String[] args) {\n    System.out.println("Hello, World");\n }\n}',
        expanded: false,
        dropdown_font: [
          { text: '12pt' },
          { text: '14pt' },
          { text: '16pt' },
          { text: '18pt' },
          { text: '20pt' }
        ],
        dropdown_lang: [
          { text: 'java' },
          { text: 'html' },
          { text: 'javascript' },
          { text: 'json' }
        ],
        fontSize: {text: '12pt'},
        lang: { text : 'java'},
        show_progress: false,
        allLessons: []
      }
    },

    computed: {
      compiledMarkdownDescr: function () {
        if (this.lesson.description != undefined)
          return marked(this.lesson.description, {sanitize: true})
      },

      compiledMarkdownTheory: function () {
        if (this.lesson.theory != undefined)
          return marked(this.lesson.theory, {sanitize: true})
      },

      formattedResponse() {
        return this.response ?
          this.response.methodResult ?
            `${this.response.methodResult.systemOut}`
            : `Error: ${this.response.generalResponse.message}` : ''
      },

      nextLessonLink() {
        let ind = -1

        this.allLessons.forEach((l, i) => {
          if(l.id == this.$route.params.id) ind = i;
        });
        ind++;

        return ((ind > 0) && (ind < this.allLessons.length)) ? `/lessonCard/${this.allLessons[ind].id}` : '';
      },

      prevLessonLink() {
        let ind = -1

        this.allLessons.forEach((l, i) => {
          if(l.id == this.$route.params.id) ind = i;
        });
        ind--;

        return (ind >= 0) ? `/lessonCard/${this.allLessons[ind].id}` : '';
      }
    },

    components: {
      Brace
    },

    mounted(){
      this.fetchLesson();
      this.fetchAllLessonsOfCourse();
      this.split();
    },

    methods: {
      fetchAllLessonsOfCourse() {
        const headers = {
          'Content-Type': 'application/json',
          'Authorization': this.$cookie.get('token'),
        };

        axios.get(PROPERTIES.HOST + '/courses/lessons/getAllLessonsOfCourse', {
          params: {
            id: this.$route.params.id
          },
          headers
        })
          .then((response) => {
            console.log(response);
            this.allLessons = response.data;
          });
      },

      fetchLesson() {
        const headers = {
          'Content-Type': 'application/json',
          'Authorization': this.$cookie.get('token'),
        };

        axios.get(PROPERTIES.HOST + '/courses/lessons/get', {
          params: {
            id: this.$route.params.id
          },
          headers
        })
          .then((response) => {
            this.lesson = response.data;
          });
      },

      split() {
        Split(['#three', '#four'], {
          direction: 'vertical',
          sizes: [85,15],
          minSize: [0,0],
          gutterSize: 5
        });
        Split(['#one', '#two'], {
          sizes: [30,70],
          minSize: [0,0],
          gutterSize: 5
        });
      },

      getCode(target){
        this.content = target;
      },

      runCode() {
        this.show_progress = !this.show_progress;
        const code = {sourceCode: this.content};
        axios.post(PROPERTIES.HOST + '/run-class', code)
          .then((response) => {
            this.response = response.data;
            this.show_progress = !this.show_progress;
          });

      }
    },
  }
</script>

<style>
  .big-div {
    min-height: 80vh !important;
  }
   td {
     padding: 8px 10px;
   }

  td:first-child {
    font-family: monospace;
  }

  h3 {
    margin-top: 100px;
  }

  h5 {
    margin-bottom: 0;
  }

  hr {
    margin-top: 2px;
  }


  .example-2, .example-3, .example-4, .example-5, .example-6 {
    height: 600px;
    border: 1px solid #ddd;
    border-radius: 4px;
  }

  .example-flex {
    height: 200px;
    border: 1px solid #ddd;
    border-radius: 4px;
    display: flex;
    flex-direction: row;
  }

  .example-flex-reverse {
    flex-direction: row-reverse;
  }

  #one, #two {
    padding: 0px;
  }

  #one p {
    padding: 0;
  }

  .example-4, .example-5 {
    height: 400px;
  }

  .split p, .split-flex p {
    padding: 20px;
  }

  .split, .split-flex {
    -webkit-box-sizing: border-box;
    -moz-box-sizing: border-box;
    box-sizing: border-box;

    overflow-y: auto;
    overflow-x: hidden;
  }

  .gutter {
    background-color: #eee;

    background-repeat: no-repeat;
    background-position: 50%;
  }

  .gutter.gutter-horizontal {
    cursor: ew-resize;
  }

  .gutter.gutter-vertical {
    cursor: ns-resize;
  }

  .split.split-horizontal, .gutter.gutter-horizontal {
    height: 100%;
    float: left;
  }
</style>
