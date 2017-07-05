<template>

  <div>

    <v-expansion-panel expand>
      <v-expansion-panel-content class="white" v-model=expanded>
        <div slot="header" >
          <v-card-title >{{lesson.name}}</v-card-title>
        </div>
        <v-card-text class="white">
          <div v-html="compiledMarkdown"></div>
        </v-card-text>
      </v-expansion-panel-content>
    </v-expansion-panel>

    <div class="example-2">
      <div id="one" class="split split-horizontal">


      </div>
      <div id="two" class="split split-horizontal">
        <div id="three" class="split split-vertical">

          <brace style="height: 500px"
                 :fontsize="'12pt'"
                 :theme="'github'"
                 :mode="java"
                 :codefolding="'markbegin'"
                 :softwrap="'free'"
                 :selectionstyle="'text'"
                 :highlightline="true"
          >
          </brace>
        </div>
        <div id="four" class="split split-vertical">

        </div>
      </div>
    </div>




  </div>
</template>

<script>
  import axios from "axios";
  import PROPERTIES from '../properties';
  import marked from 'marked';
  import Split from 'split.js'
  import Brace from 'xen-brace'


  export default {
    name: 'lesson-card',
    data(){
      return {
        lesson: Object,
        expanded: 'True'
      }
    },
    computed: {
      compiledMarkdown: function () {
        if (this.lesson.description != undefined)
          return marked(this.lesson.description, {sanitize: true})
      }
    },
    components: {
      Brace
    },
    mounted(){
      this.fetchLesson();
      this.split();
    },
    methods: {
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

