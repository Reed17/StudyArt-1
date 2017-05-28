<template>
  <div>
    <brace style="height: 500px"
           :fontsize="'12px'"
           :theme="'github'"
           :mode="'json'"
           :codefolding="'markbegin'"
           :softwrap="'free'"
           :selectionstyle="'text'"
           :highlightline="true"
           @code-change="getCode($event)">
    </brace>
    <div>
      <v-btn dark default @click.native="greet">Run code</v-btn>

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
        axios.post(properties.host + '/run-code', this.input)
          .then((response) => {
            this.response = response.data;
          });
      },
    }

  }
</script>
