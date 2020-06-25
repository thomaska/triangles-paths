package com.suprnation

import java.io

import cats.effect.{ExitCode, IO, IOApp}
import com.suprnation.model.{EmptyTriangle, Leaf, MutableNode, Node, TriangleError, TriangleParsingError}

import scala.annotation.tailrec
import scala.io.StdIn
import scala.util.{Failure, Success, Try}
import model._

object Main extends IOApp {
  val Eof = "EOF"

  def readInput(readLine: () => String, cur: List[String] = List.empty): List[String] = {
    val line = readLine()
    if (line == Eof)
      cur
    else readInput(readLine, cur :+ line)
  }

  def validateInput(input: List[String]): Either[TriangleError, List[List[Int]]] = {
    val parsedInts = Try {
      input.map(_.split("\\s").toList.map(_.toInt))
    }.toTriangleError()

    parsedInts.flatMap((l: List[List[Int]]) => {
      Try {
        l.foldLeft(List.empty[Int])((list1, list2) => {
          if (list1.length + 1 != list2.length) throw new Error(s"List has invalid length: $list2")
          list2
        })
      }.toTriangleError()
        .map(_ => l)
    })
  }

  def calculateMaxPath(input: List[List[Int]]): List[Int] = {
    ???
  }

  def run(args: List[String]): IO[ExitCode] = {
    IO(println("Application minimum triangle path running"))
      .map(_ => readInput(StdIn.readLine))
      .map(validateInput)
      .map(_.map(calculateMaxPath))
      .map(_ => ExitCode.Success)
  }

}
