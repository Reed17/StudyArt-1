Hi there
To run app you need to have:
1. Git repo with appropriate test Classes
Test class example:
public class Test{
    public static List<String> methodName(){
            list.add("Result: <true/false>, expected: <expected value>, actual: <actual_value>")
            return list;
        }   
    }
    
    How tests should look like:
    "Result: <true/false>, expected: <expected value>, actual: <actual_value>";
    
    You can use another structure, but method has to return List<String>
    and have <true/false> in text;
2. Postman or other app to send GET/POST requests

Algorithm of using app:
1. Change path for saving Git projects in /resources/app.properties file.
2. Run Application class in ua.artcode.controller
3. Add course by sending POST request to localhost:8080/courses/add with JSON body like this:
    {
      "id": 0, (CAN BE ANY NUMBER, IT WILL BE GENERATED ON THE SERVER SIDE)
      "gitURL": "https://github.com/v21k/TestGitProject", (MUST BE VALID URL)
      "courseLocalPath": null, (GENERATED ON THE SERVER SIDE)
      "author": "VK", (MUST BE VALID STRING - 2 OR MORE LETTERS)
      "lessons": [] (CAN BE EMPTY, DOESN'T WORK YET)
    }
4. If you want to run class with check() (method is hardcoded for now - you
can pass any method, but it won't work) method - use next POST request:
localhost:8080/run-class?packageName=PACKAGE_NAME&mainClass=CLASS_NAME&methodName=check&courseId=ID

5. If you want to send your solution which has to be added to certain class - use next POST request:
localhost:8080/send-solution?packageName=PACKAGE_NAME&mainClass=CLASS_NAME&methodName=check&courseId=ID
    
That's all for now!
  
// add tests using "spring boot test" - 0%
    test created endpoints , send some data and check actual result
    look at the example src/test/java/ua/artcode/controller/GreetingControllerTests.java
    
    
// Validation - 50%

// Exceptions - 100%

// Consider all TOODs - 80%

// Add next func. - 25%
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