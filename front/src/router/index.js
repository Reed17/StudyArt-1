import Vue from "vue";
import Router from "vue-router";
import Login from "@/components/Login";
import Register from "@/components/Register";
import Courses from "@/components/Courses";
import Course from "@/components/Course";
import AceEditor from "@/components/AceEditor";
import LessonCard from "@/components/LessonCard"


Vue.use(Router);

export default new Router({
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/courses',
      name: 'Courses',
      component: Courses,
    },
    {
      path: '/course/:id',
      name: 'Course',
      component: Course,
    },
    {
      path: '/register',
      name: 'Register',
      component: Register
    },
    {
      path: '/editor',
      name: 'AceEditor',
      component: AceEditor
    },
    {
      path: '/lessonCard/:id',
      name: 'LessonCard',
      component: LessonCard
    },
  ],
  mode: 'history'
})
