package com.suprnation

package object model {

  sealed trait Node {
    def value: Int
  }
  case class Intermediate(value: Int, left: Node, right: Node) extends Node
  case class Leaf(value: Int)                                  extends Node
}
