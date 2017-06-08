<template>
  <div>
    <brace style="height: 500px"
           :fontsize="'12px'"
           :theme="'github'"
           :mode="'java'"
           :codefolding="'markbegin'"
           :softwrap="'free'"
           :selectionstyle="'text'"
           :highlightline="true"
           @code-change="getCode($event)">
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
  import Brace from 'vue-bulma-brace'
  import axios from "axios";
  import PROPERTIES from '../properties'

  export default {
    data(){
      return {
        response: '',
        input: '',
      }
    },
    components: {
      Brace
    },
    computed: {
      formattedResponse() {
        return this.response ?
          this.response.methodResult ?
            `${this.response.methodResult.systemOut}`
            : `Error: ${this.response.generalResponse.message}` : ''
      }
    },

    methods: {
      getCode(target){
        this.$emit('code-change', target);
        this.input = target;
      },

      runCode() {
        const code = {sourceCode: this.input};
        axios.post(PROPERTIES.HOST + '/run-class', code)
          .then((response) => {
            this.response = response.data;
          });
      },
    }

  }
</script>
