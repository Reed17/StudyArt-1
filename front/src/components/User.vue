<template>
  <div>
    {{user}}
  </div>

</template>

<script>
  import axios from 'axios';
  import properties from '../properties'
  export default {

    name: 'app-user',
    data(){
      return {
        user: Object,
      }
    },

    mounted(){
        this.fetchUserInfo()
    },

    methods: {
      fetchUserInfo(){
        axios.get(properties.host + '/getUserByAccessKey' + '?key=' + this.$cookie.get('accessKey'))
          .then((response) => {
            this.user = response.data;
          })
      }
    }

  }

</script>
