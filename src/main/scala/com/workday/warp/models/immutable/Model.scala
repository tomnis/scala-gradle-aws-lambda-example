package com.workday.warp.models.immutable

import com.workday.warp.models.{RequestLike, Vec3Like}

/**
  * Immutable versions of request and 3d vector.
  *
  * Created by tomas.mccandless on 6/9/17.
  */
case class DotProductRequest(vectorA: Vec3, vectorB: Vec3) extends RequestLike

case class Vec3(x: Double, y: Double, z: Double) extends Vec3Like