<template>

  <div>

    <v-app-bar>
      <v-btn-dropdown v-bind:options="dropdown_font" v-model="fontSize" max-height="auto" editable="editable" overflow></v-btn-dropdown>
    </v-app-bar>

    <brace style="height: 500px"
           :fontsize="fontSize"
           :theme="'github'"
           :mode="'json'"
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

    <v-text-field
      name="input-7-1"
      label="Result"
      :value="formattedResponse"
      multi-line
      disabled>
    </v-text-field>
  </div>

  </div>
</template>


<script>

  import axios from "axios";
  import PROPERTIES from '../properties'
  import Brace from 'xen-brace'

  export default {

    data(){
      return {
        response: '',
        content: 'public class HelloWorld {\n public static void main(String[] args) {\n    System.out.println("Hello, World");\n }\n}',
        dropdown_font: [
            { text: '12pt' },
            { text: '14pt' },
            { text: '16pt' },
            { text: '18pt' },
            { text: '20pt' }
          ],
        fontSize: null,
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
      Brace
    },

    methods:{

      getCode(target){
        this.content = target;
      },

      runCode() {
        this.fontSize = "20pt" //manual test
        const code = {sourceCode: this.content};
        axios.post(PROPERTIES.HOST + '/run-class', code)
          .then((response) => {
            this.response = response.data;
          });
      },

    },
  }


</script>
