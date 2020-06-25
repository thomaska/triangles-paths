package com.suprnation

import com.suprnation.model.{EmptyTriangle, MutableNode}
import org.scalacheck.Gen
import org.scalatest.{FreeSpec, Matchers}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class MainTest extends FreeSpec with ScalaCheckPropertyChecks with Matchers {

  val arrayGen: Gen[Array[Int]] = Gen.listOf(Gen.choose(0, 99999999)).map(_.toArray)

  "Methods tests" - {
    "parseTriangleLevel should return the correct nodes for the new level if the previous level is the root" in {
      val node     = MutableNode(1)
      val prev     = List(node)
      val nodes    = List(2, 3)
      val expected = Right(List(MutableNode(2), MutableNode(3)))
      val actual   = Main.parseTriangleLevel(prev, nodes, List.empty)
      actual shouldBe expected
      node shouldBe (MutableNode(1, MutableNode(2), MutableNode(3)))
    }

    val prev     = List(MutableNode(1), MutableNode(2), MutableNode(3))
    val nodes    = List(4, 5, 6, 7)
    val expected = Right(List(MutableNode(4), MutableNode(5), MutableNode(6), MutableNode(7)))
    val actual   = Main.parseTriangleLevel(prev, nodes, List.empty)
    actual shouldBe expected
    prev shouldBe List(MutableNode(1, MutableNode(4), MutableNode(5)),
                       MutableNode(2, MutableNode(5), MutableNode(6)),
                       MutableNode(3, MutableNode(6), MutableNode(7)))
  }

  "parseTriangleLevel should return an error if next level is too small" in {
    val prev   = List(MutableNode(1), MutableNode(2))
    val nodes  = List(3, 4)
    val actual = Main.parseTriangleLevel(prev, nodes, List.empty)
    actual shouldBe a[Left[_, _]]
  }

  "parseTriangleLevel should return an error if next level is too big" in {
    val prev   = List(MutableNode(1), MutableNode(2))
    val nodes  = List(3, 4, 5, 6)
    val actual = Main.parseTriangleLevel(prev, nodes, List.empty)
    actual shouldBe a[Left[_, _]]
  }

  "readTriangle should parse a triangle correctly for valid input" in {
    val input =
      """7
          |6 3
          |3 8 5
          |11 2 10 9
          |EOF
          |""".stripMargin.split("\n").iterator
    val actual = Main.readTriangle(input.next, List.empty, None)
    val expected = Right(
      MutableNode(
        7,
        MutableNode(6,
                    MutableNode(3, MutableNode(11, null, null), MutableNode(2, null, null)),
                    MutableNode(8, MutableNode(2, null, null), MutableNode(10, null, null))),
        MutableNode(3,
                    MutableNode(8, MutableNode(2, null, null), MutableNode(10, null, null)),
                    MutableNode(5, MutableNode(10, null, null), MutableNode(9, null, null)))
      ))
    actual shouldBe expected
  }
}