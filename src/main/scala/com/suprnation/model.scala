package com.suprnation

import scala.util.Try

package object model {

  sealed trait TriangleError
  case object EmptyTriangle                        extends TriangleError
  case class InvalidListSizeError(list: List[Int]) extends TriangleError
  case class TriangleParsingError(ex: Throwable)   extends TriangleError

  implicit class TriangleParsingErrorOps[A](t: Try[A]) {
    def toTriangleError(): Either[TriangleParsingError, A] = {
      t.toEither.left.map(TriangleParsingError)
    }
  }
}
