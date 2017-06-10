package com.workday.warp.models

/**
  * A simple vector in 3-space.
  *
  * Created by tomas.mccandless on 6/9/17.
  */
trait Vec3Like {
  def x: Double
  def y: Double
  def z: Double

  def dot(that: Vec3Like): Double = this.x * that.x + this.y * that.y + this.z * that.y
}
