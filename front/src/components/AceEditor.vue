<template>

  <div>

    <v-app-bar>
      <v-btn-dropdown v-bind:options="dropdown_font" v-model="fontSize"
                      max-height="auto" editable="editable" label="Font size" overflow></v-btn-dropdown>
      <v-btn-dropdown v-bind:options="dropdown_lang" v-model="lang"
                      max-height="auto" editable="editable" label="Programming lang" overflow></v-btn-dropdown>
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


  <div>
    <v-btn dark default @click.native="runCode()">Run code</v-btn>

    <!-- <v-text-field
      name="input-7-1"
      label="Result"
      :value="formattedResponse"
      multi-line
      disabled>
    </v-text-field> -->
    <!-- <result :response="response"></result> -->
    <div id="four" class="split split-vertical">
      <div class="result">
        <p v-if="sout">{{ sout }}</p>

        <p class="fail" v-if="errors">{{ errors }}</p>

        <p class="fail" v-if="failures" v-for="failure in failures">{{ failure }}</p>

        <p class="good" v-if="succeeded">Success: {{ succeeded }}</p>

        <p class="fail" v-if="failed">Failed: {{ failed }}</p>
      </div>
  </div>

  </div>
</div>
</template>


<script>

  import axios from 'axios';
  import PROPERTIES from '../properties';
  import Brace from 'xen-brace';
  import result from './Result.vue';
  import AjaxUtils from '../utils/axiosUtils';

  export default {

    data(){
      return {
        response: '',
        errors: '',
        sout: '',
        failures: [],
        failed: '',
        succeeded: '',
        // probably we should delete it, or take from servers according to choosen language
        content: 'public class HelloWorld {\n public static void main(String[] args) {\n    System.out.println("Hello, World");\n }\n}',
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
        fontSize: {text: '14pt'},
        lang: { text : 'java'},
      }
    },

    computed: {
      formattedResponse() {
        return this.response ?
          this.response.methodResult ?
            `${this.response.methodResult.systemOut}`
            : `Error: ${this.response.generalResponse.message}` : ''
      }
    },

    components: {
      Brace,
      result
    },

    methods:{
      getCode(target){
        this.content = target;
      },

      runCode() {
        const code = {sourceCode: this.content};

        axios.post(PROPERTIES.HOST + '/run-class', code)
          .then((response) => {

            this.response = response.data;
            this.show_progress = !this.show_progress;

            if (response.data) {

              if (response.data.methodResult) {
                this.sout = `${response.data.methodResult.systemOut}`;
              } else {
                this.errors = `Error: ${response.data.generalResponse.message}`;
              }

              if (response.data.methodStats) {
                this.failures = response.data.methodStats.failures.map(f => f.message);
                this.succeeded = response.data.methodStats.passedTests;
                this.failed = response.data.methodStats.failedTests;
              }
            }
          });
      },

    },
  }


</script>
