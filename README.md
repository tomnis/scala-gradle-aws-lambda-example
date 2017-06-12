# dot-product-as-a-service
An example of packaging a scala project suitable for execution in aws lambda.
Gradle is used to build an uberjar, create a lambda function using that artifact, and invoke the newly created function.
Our lambda function is a simple vector dot product (restricted to 3-space for simplicity)

### Getting started:
- Make sure your aws credentials can be located by the credentials toolchain

  http://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html
- I put mine in `~/.aws/credentials`, like this:

  ```
  ~ » cat ~/.aws/credentials
  [default]
  aws_access_key_id=abcdefg
  aws_secret_access_key=12345
  ```
  
- Your aws account id should be accessible as a project property by gradle. I put mine in `~/.gradle/gradle.properties`
  ```
  ~ » grep aws ~/.gradle/gradle.properties                                                          
  aws.accountId=3141592653589
  ```

### Lambda has 2 different ways to implement request handlers:
- POJO-based, where jackson is used to deserialize request into a mutable class.
  
  http://docs.aws.amazon.com/lambda/latest/dg/java-handler-io-type-pojo.html
  
  Your request class should have an empty-arg constructor and javabean standard getters/setters. See `com.workday.warp.mutable.Model.scala`. In our case, we use `scala.beans.BeanProperty` annotation to generate the correct accessors and mutators:
  ```
  class DotProductRequest(@BeanProperty var vectorA: Vec3,
                          @BeanProperty var vectorB: Vec3) extends RequestLike {

    def this() = this(new Vec3, new Vec3)
  }
  
  class Vec3(@BeanProperty var x: Double,
             @BeanProperty var y: Double,
             @BeanProperty var z: Double) extends Vec3Like {

    def this() = this(0, 0, 0)
  }
  ```
  The handler accepts an instance of `DotProductRequest` automatically deserialized by jackson:
  ```
  object PojoDotProductHandler extends RequestHandler[mutable.DotProductRequest, Double] {

    override def handleRequest(request: mutable.DotProductRequest, context: Context): Double = {
      request.vectorA dot request.vectorB
    }
  }
  ```

- Stream-based, where your request handler is passed an `InputStream` to read request data from, and an `OutputStream` to write response to.
  
  http://docs.aws.amazon.com/lambda/latest/dg/java-handler-io-type-stream.html
  
  We read all data from the input stream and use `json4s` to deserialize into immutable case classes:
  ```
  case class DotProductRequest(vectorA: Vec3, vectorB: Vec3) extends RequestLike

  case class Vec3(x: Double, y: Double, z: Double) extends Vec3Like
  
  object StreamDotProductHandler extends RequestStreamHandler {

    override def handleRequest(input: InputStream, output: OutputStream, context: Context): Unit = {
      // some lines omitted for brevity
      val json: JValue = parse(Source.fromInputStream(input).mkString)

      val request: immutable.DotProductRequest = json.extract[immutable.DotProductRequest]
      val result: Double = request.vectorA dot request.vectorB
      val response: String = compact(render("result" -> result))

      val dataOutput: DataOutputStream = new DataOutputStream(output)
      dataOutput.writeChars(response)
    }
  }
  ```
  
    
We provide examples of both handler types in `com.workday.warp.Handlers.scala`. We also provide gradle tasks for uploading, deleting, and invoking lambda functions corresponding to both handler types. 

### Usage
Sample output for the stream-based handler using immutable case classes looks like this:

```
~/dot-product-as-a-service » gradle clean deleteStreamLambda uploadStreamLambda invokeStreamLambda
:clean
:deleteStreamLambda
:compileJava UP-TO-DATE
:compileScala
:processResources UP-TO-DATE
:classes
:shadowJar
:uploadStreamLambda
Function not found: arn:aws:lambda:us-west-1:123456789:function:dot-product-stream (Service: AWSLambda; Status Code: 404; Error Code: ResourceNotFoundException; Request ID: cafebabe-deadbeef-1234)
Creating function... dot-product-stream
:invokeStreamLambda
Lambda function (using stream json4s deserialization with immutable case classes) results:
  status code: 200
  payload: {"result":3.0}

BUILD SUCCESSFUL
```

This sequence of gradle tasks builds a shadowjar, uploads that artifact to create a lambda function, and invokes that function with sample input data:
```
{
  "vectorA": {
    "x": 1.0,
    "y": 1.0,
    "z": 1.0
  },
  "vectorB": {
    "x": 1.0,
    "y": 1.0,
    "z": 1.0
  }
}
```

Similar tasks `deletePojoLambda`, `uploadPojoLambda`, `invokePojoLambda` are defined for the POJO-based handler.

### Gradle upload task
This task will overwrite an existing lambda function with the same name, or create one if it does not exist.
```
task uploadStreamLambda(type: AWSLambdaMigrateFunctionTask, dependsOn: shadowJar) {
    functionName = 'dot-product-stream'
    role = "arn:aws:iam::${aws.accountId}:role/lambda"
    runtime = Runtime.Java8
    timeout = 5 // seconds
    memorySize = 512 // MB
    zipFile = shadowJar.archivePath
    handler = 'com.workday.warp.handlers.StreamDotProductHandler::handleRequest'
}
```
