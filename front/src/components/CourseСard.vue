<template>
  <div>
    <mu-card>

      <mu-card-header :title=course.author :subTitle=course.url></mu-card-header>

      <mu-card-title :title=course.name></mu-card-title>

      <mu-card-text>Description : {{course.description}}</mu-card-text>

      <mu-card-actions>
        <mu-flat-button label="Learn more"/>
      </mu-card-actions>

    </mu-card>
  </div>
</template>

<script>
  export default{
    name: 'course-card',
    props: {
      course: {
        required: true,
      }
    }
  }
</script>
