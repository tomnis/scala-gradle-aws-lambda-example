package com.workday.warp.models

import scala.beans.BeanProperty

/**
  * A simple vector in 3-space.
  *
  * Created by tomas.mccandless on 6/8/17.
  */
// TODO mutable, ugly
case class Vec3(@BeanProperty var x: Double,
                @BeanProperty var y: Double,
                @BeanProperty var z: Double) {

  def this() = this(0, 0, 0)

  def dot(that: Vec3): Double = this.x * that.x + this.y * that.y + this.z * that.y
}
