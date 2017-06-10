package com.workday.warp.handlers

import java.io.{InputStream, OutputStream}

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler, RequestStreamHandler}
import com.workday.warp.models.{mutable, immutable}
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.pmw.tinylog.Logger

import scala.io.Source

/**
  * Ugly mutable version of request handler.
  *
  * Json input is deserialized by lambda.
  *
  * Created by tomas.mccandless on 6/7/17.
  */
object MutableDotProductHandler extends RequestHandler[mutable.DotProductRequest, Double] {

  override def handleRequest(request: mutable.DotProductRequest, context: Context): Double = {
    Logger.info(s"invoked with $request")
    request.vectorA dot request.vectorB
  }
}


/**
  * Immutable version of request handler.
  *
  * We read all the json input and handle deserialization ourselves.
  */
object ImmutableDotProductHandler extends RequestStreamHandler {

  override def handleRequest(input: InputStream, output: OutputStream, context: Context): Unit = {
    implicit val formats = DefaultFormats
    val json: JValue = parse(Source.fromInputStream(input).mkString)
    input.close()

    Logger.info(s"invoked with $json")

    val request: immutable.DotProductRequest = json.extract[immutable.DotProductRequest]
    val result: Double = request.vectorA dot request.vectorB

    output.write(4)
    output.close()
  }
}