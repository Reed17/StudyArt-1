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
      <v-btn dark default>Run code</v-btn>

      <v-text-field
        name="input-7-1"
        label="Result"
        :value="response"
        @click="runCode()"
        multi-line
        disabled>
      </v-text-field>
    </div>

  </div>
</template>

<script>
  import Brace from 'vue-bulma-brace'

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
        const headers = {
          'Content-Type': 'application/json',
          'Authorization': this.$cookie.get('token'),
        };

        axios.post(PROPERTIES.HOST + '/run-code', this.input, headers)
          .then((response) => {
            this.response = response.data;
          });
      },
    }

  }
</script>
