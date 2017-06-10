package com.workday.warp.models.mutable

import com.workday.warp.models.{RequestLike, Vec3Like}

import scala.beans.BeanProperty

/**
  * Ugly mutable versions of request input.
  *
  * Lambda will use jackson to deserialize json into these classes.
  *
  * Unfortunately we do need these empty-arg auxiliary constructors, as
  * lambda calls empty-arg constructors and then uses mutators to set members
  * to the desired state.
  *
  * We use [[BeanProperty]] annotations to generate java-bean compliant accessor methods.
  *
  * Created by tomas.mccandless on 6/8/17.
  */
class DotProductRequest(@BeanProperty var vectorA: Vec3,
                             @BeanProperty var vectorB: Vec3) extends RequestLike {

  def this() = this(new Vec3, new Vec3)
}


class Vec3(@BeanProperty var x: Double,
                @BeanProperty var y: Double,
                @BeanProperty var z: Double) extends Vec3Like {

  def this() = this(0, 0, 0)
}