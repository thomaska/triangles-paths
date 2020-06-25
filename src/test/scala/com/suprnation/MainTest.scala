package com.suprnation

import com.suprnation.model.MutableNode
import org.scalacheck.Gen
import org.scalatest.{FreeSpec, Matchers}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

import scala.util.{Failure, Success}

class MainTest extends FreeSpec with ScalaCheckPropertyChecks with Matchers {

  val arrayGen: Gen[Array[Int]] = Gen.listOf(Gen.choose(0, 99999999)).map(_.toArray)

  "Methods tests" - {
    "parseRoot returns an error when the input array does not contain exactly one value" in {
      forAll(arrayGen) { arr =>
        val actual = Main.parseRoot(arr)
        if (arr.length != 1) actual shouldBe a[Left[_, _]]
        else actual shouldBe a[Right[_, _]]
      }
    }

    "parseTriangleLevel should return the correct nodes for the new level if the previous level is the root" in {
      val node     = MutableNode(1)
      val prev     = List(node)
      val nodes    = List(2, 3)
      val expected = Right(List(MutableNode(2), MutableNode(3)))
      val actual   = Main.parseTriangleLevel(prev, nodes, List.empty)
      actual shouldBe expected
      node shouldBe (MutableNode(1, MutableNode(2), MutableNode(3)))
    }

    "parseTriangleLevel should return the correct nodes for the new level" in {
      val prev     = List(MutableNode(1), MutableNode(2))
      val nodes    = List(3, 4, 5)
      val expected = Right(List(MutableNode(3), MutableNode(4), MutableNode(5)))
      val actual   = Main.parseTriangleLevel(prev, nodes, List.empty)
      actual shouldBe expected
      prev shouldBe List((MutableNode(1, MutableNode(3), MutableNode(4))),
                         (MutableNode(2, MutableNode(4), MutableNode(5))))
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
  }
}
