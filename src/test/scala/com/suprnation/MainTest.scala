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
        if (arr.length != 1) actual shouldBe a[Failure[_]]
        else actual shouldBe a[Success[_]]
      }
    }

  }
}
