package com.workday.warp.models

import com.workday.warp.models.mutable.Vec3
import org.scalatest.Matchers
import org.scalatest.junit.JUnitSuite
import org.junit.Test

/**
  * Created by tomas.mccandless on 6/8/17.
  */
class Vec3Spec extends JUnitSuite with Matchers {

  @Test
  def dot(): Unit = {
    Vec3(1,1,1) dot Vec3(1,1,1) should be (3)
  }
}
