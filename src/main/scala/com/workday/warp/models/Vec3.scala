package com.workday.warp

/**
  * A simple vector in 3-space.
  *
  * Created by tomas.mccandless on 6/8/17.
  */
// TODO mutable, ugly
case class Vec3(var x: Double, var y: Double, var z: Double) {

  // ugly, but it looks like we need an empty constructor
  def this() = this(0, 0, 0)

  def dot(that: Vec3): Double = this.x * that.x + this.y * that.y + this.z * that.y
}
