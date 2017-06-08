package com.workday.warp

/**
  * A simple vector in 3-space.
  *
  * Created by tomas.mccandless on 6/8/17.
  */
case class Vec3(x: Double, y: Double, z: Double) {

  def +(other: Vec3): Vec3 = Vec3(this.x + other.x, this.y + other.y, this.z + other.z)

  def -(that: Vec3): Vec3 = Vec3(this.x - that.x, this.y - that.y, this.z - that.z)

  def *(scalar: Double): Vec3 = Vec3(scalar * this.x, scalar * this.y, scalar * this.z)

  def dot(that: Vec3): Double = this.x * that.x + this.y * that.y + this.z * that.y
}
