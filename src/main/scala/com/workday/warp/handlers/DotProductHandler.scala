package com.workday.warp

import org.pmw.tinylog.Logger
import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}


/**
  * Created by tomas.mccandless on 6/7/17.
  */
object DotProductHandler extends RequestHandler[DotProductRequest, Double] {

  override def handleRequest(request: DotProductRequest, context: Context): Double = {
    Logger.info(s"invoked with $request")

    request.vectorA dot request.vectorB
  }
}
