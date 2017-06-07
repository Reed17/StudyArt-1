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
        :value="response"
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

    methods: {
      getCode(target){
        this.$emit('code-change', target);
        this.input = target;
      },

      runCode() {
        var j ={"sourceCode":this.input};
        var f= JSON.stringify(j);
        var s =f;
        axios.post(PROPERTIES.HOST + '/run-class', j)
          .then((response) => {
            this.response = JSON.stringify(response.data);
          });
      },
    }

  }
</script>
