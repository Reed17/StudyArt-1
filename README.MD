# StudyArt

Dependencies:
* Maven
* Spring boot


Before:
* Edit properties (application.yml) Set:
  * paths for downloading course from Git, for saving dependencies and external code
* Use [test git repository](https://github.com/v21k/TestGitProject.git) - it's already in test properties
* Main and test props must be the same (except test properties)
* to use API you can use [Swagger](http://localhost:8080/swagger-ui.html) (http://localhost:8080/swagger-ui.html) or Postman

Lifecycle:
1. Login/Register POST `/login`, `/register`. Example:
```json
{
  "login": "string",
  "pass": "string"
}
```
```json
{
  "email": "string",
  "login": "string",
  "pass": "string"
}
```
2. For teacher
	* add course POST `/courses/add`. Dependencies, id, lessons, localpath will be generated on server side.
	```json
	{
      "author": "string",
      "dependencies": [
        "string"
      ],
      "description": "string",
      "id": 0,
      "lessons": [],
      "localPath": "string",
      "name": "string",
      "sourcesRoot": "string",
      "testsRoot": "string",
      "url": "string"
    }
    ```
    * add lessons POST `courses/lessons/add`. ID will be generated on server side. You can set sourcesRoot or baseClasses (same for tests), not both.
    ```json
    {
      "baseClasses": [
        "string"
      ],
      "courseID": 0,
      "description": "string",
      "id": 0,
      "localPath": "string",
      "name": "string",
      "requiredClasses": [
        "string"
      ],
      "sourcesRoot": "string",
      "testsClasses": [
        "string"
      ],
      "testsRoot": "string"
    }
    ```
3. Get all courses/lessons. GET `/courses/getAll`, `/courses/lessons/getAll`.
4. For student. First of all you need to:
    * get course
    * clone it from git
    * perform lessons
    * push (to your own repo)
5. Check lessons POST `/courses/lessons/send-solution-and-run-tests?courseId=ID&lessonNumber=NUMBER`. 
* Request params: course ID and lesson ID
* Request body:
    ```json
    {
      "id": 0,
      "name": "string",
      "url": "string"
    }
    ```
* Response:
    ```json
    {
      "generalResponse": {
        "message": "string",
        "type": "INFO"
      },
      "methodResult": {
        "systemErr": "string",
        "systemOut": "string"
      },
      "methodStats": {
        "failedTests": 0,
        "failures": [],
        "overallTests": 0,
        "passedTests": 0
      }
    }
    ```


