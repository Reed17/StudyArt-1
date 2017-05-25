<template>
  <div>
    <mu-card>

      <mu-card-header :title=course.author :subTitle=course.url></mu-card-header>

      <mu-card-title :title=course.name></mu-card-title>

      <mu-card-text>Description : {{course.description}}</mu-card-text>

      <mu-card-actions>
        <mu-flat-button label="Learn more" :to="link"/>
      </mu-card-actions>

    </mu-card>
  </div>
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
