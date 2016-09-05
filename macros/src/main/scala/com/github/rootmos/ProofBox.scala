package com.github.rootmos

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

object ProofBox {
  def apply[Context, Proof]: Proof = macro apply_impl[Context, Proof]

  def apply_impl[Context: c.WeakTypeTag, Proof: c.WeakTypeTag](c: blackbox.Context): c.Tree = {
    import c.universe._
    q"""new ${weakTypeOf[Context]} { val proof = implicitly[${weakTypeOf[Proof]}] }.proof"""
  }
}
