Hi there

**First of all you have to**
1. Set path for git project and for external code (if you want to send a class)
2. Create git repository if you want to run "lessons" or send solutions. You can use my test repo https://github.com/v21k/TestGitProject.git
3. Install Postman or another app to send GET/POST requests

**Git repo**

If you want to test all features with your own repo, here are few conventions:
1. root package - src/
2. lessons MUST be located in src/_LESSON-NUMBER_lesson. Example - src/_01_lesson/
3. in order to run lesson you need Main class in it. Class must contain word "main". Example: mainclass, MAIN, class_main
4. in order to run lesson with solution you need Solution class. Class name must contain word "solution". Same to previous example

See my example to understand - https://github.com/v21k/TestGitProject.git

**Algorithm of using app**
1. Run ua.artcode.controller.Application class
2. Add git repo by using POST request to localhost:8080/courses/add with next content:
```json
{
	"id" : 0,
	"name" : "Primitives",
	"author" : "VK",
	"url" : "https://github.com/v21k/TestGitProject.git",
	"localPath:" : null,
	"lessons" : null
}
```
3. You can check if everything is ok by sending GET requests: 
*localhost:8080/courses/get?id=1

4. After this you have 3 ways to use app:
* send any class and run it (it must contain "main" method) <br>
POST localhost:8080/run-class <br>
example for request body
```json
{
	"sourceCode" : "public class test {\npublic static void main(String[] args) {\nSystem.out.println(2+2);\n}\n}\n"
}
```
* run main class from any lesson you want <br>
GET localhost:8080/courses/lessons/run?courseId=COURSE_ID&lessonNumber=LESSON_NUMBER
* run lesson with solution <br>
POST localhost:8080/courses/lessons/send-solution-and-run?courseId=1&lessonNumber=2 <br>
request body example: 
```json
{
	"sourceCode" : "public static int sum(int a, int b){return a+2;}"
}
```


  
// add tests using "spring boot test" - 0%
    test created endpoints , send some data and check actual result
    look at the example src/test/java/ua/artcode/controller/GreetingControllerTests.java
    
    
// Validation - 100%

// Exceptions - 100%

// Consider all TOODs - 100%

// Add next func. - 100%
Pass class with main method which will be ran with our system

public class A {

    //
    //
    // 
    //
    //
    //

    // will be processed
    main(...){
        
    
    }



}


// see how to connect swagger to the project - 0%