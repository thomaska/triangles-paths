package com.suprnation

import scala.util.Try

package object model {

  sealed trait TriangleError
  case object EmptyTriangle                        extends TriangleError
  case class InvalidListSizeError(list: List[Int]) extends TriangleError
  case class TriangleParsingError(ex: Throwable)   extends TriangleError
  implicit class TriangleParsingErrorOps[A](t: Try[A]) {
    def toTriangleError() = {
      t.toEither.left.map(TriangleParsingError)
    }
  }

  case class Path(nodes: List[Int], cost: Int) {
    def +(node: Int): Path = {
      Path(List(node) ++ nodes, cost + node)
    }
  }
  object Path {
    val empty = Path(List.empty)

    def apply(nodes: List[Int]): Path = new Path(nodes, nodes.sum)

    def apply(nodes: Int*): Path = new Path(nodes.toList, nodes.sum)
  }
}
