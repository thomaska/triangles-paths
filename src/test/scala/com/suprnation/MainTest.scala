package com.suprnation

import org.scalacheck.Gen
import org.scalatest.{FreeSpec, Matchers}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class MainTest extends FreeSpec with ScalaCheckPropertyChecks with Matchers {

  val arrayGen: Gen[Array[Int]] = Gen.listOf(Gen.choose(0, 99999999)).map(_.toArray)

  "Methods tests" - {
    "readInput should return the correct output" in {
      val input =
        """7
          |6 3
          |3 8 5
          |11 2 10 9
          |EOF
          |""".stripMargin.split("\n").iterator
      val actual = Main.readInput(input.next)
      val expected = List(
        "7",
        "6 3",
        "3 8 5",
        "11 2 10 9"
      )
      actual shouldBe expected
    }

    "validateInput should return an error when input is not integers" in {
      val input =
        """7
          |6 3
          |3 8 aa
          |11 2 10 9
          |EOF
          |""".stripMargin.split("\n")
      val actual = Main.validateInput(input.toList)
      actual shouldBe a[Left[_, _]]
    }

    "validateInput should return an error when list sizes are not correct" in {
      val input =
        """7
          |6 3
          |3 8
          |11 2 10 9
          |EOF
          |""".stripMargin.split("\n")
      val actual = Main.validateInput(input.toList)
      actual shouldBe a[Left[_, _]]
    }

    "validateInput should the correct input when there is no error" in {
      val input =
        """7
          |6 3
          |3 8 109
          |11 2 10 9
          |""".stripMargin.split("\n")
      val actual = Main.validateInput(input.toList)
      val expected = List(
        List(7),
        List(6, 3),
        List(3, 8, 109),
        List(11, 2, 10, 9)
      )
      actual.right.get shouldBe expected
    }

    "calculateMaxPath should return the correct result" in {
      val input = List(
        List(7),
        List(6, 3),
        List(3, 8, 109),
        List(11, 2, 10, 9)
      )
      val actual   = Main.calculateMaxPath(input)
      val expected = List(7, 3, 109, 9)
      actual shouldBe expected
    }

    "calculateMaxPath should return an empty path for an empty input" in {
      val actual   = Main.calculateMaxPath(List.empty)
      val expected = List.empty
      actual shouldBe expected
    }

    "printpath should return the correct string" in {
      val actual = Main.printPath(List(3, 4, 4,12))
      val expected = "Minimal path is: 3 + 4 + 4 + 12 = 23"
      actual shouldBe expected
    }
  }
}
