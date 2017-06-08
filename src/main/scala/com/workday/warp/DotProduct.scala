package com.workday.warp

import org.pmw.tinylog.Logger
import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}


/**
  * Created by tomas.mccandless on 6/7/17.
  */
object DotProduct extends RequestHandler[String, String] {

  override def handleRequest(input: String, context: Context): String = {
    Logger.info(s"invoked with $input")

    input + " some other shit"
  }
}
