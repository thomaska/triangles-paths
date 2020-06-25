package com.suprnation

import cats.effect.{ExitCode, IO, IOApp}
import com.suprnation.model.{TriangleError, _}

import scala.io.StdIn
import scala.util.Try

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

  def calculateMaxPath(input: List[List[Int]]): Path = {
    val paths = input.foldRight(List.empty[Path]) { (first: List[Int], second: List[Path]) =>

      val headSafe = if (second.nonEmpty) second.dropRight(1) else second
      val tailSafe = if (second.nonEmpty) second.tail else second
      first.zipWithIndex.map { case (node, index) =>
        val left = if(headSafe.isEmpty) Path(node) else headSafe(index) + node
        val right = if(tailSafe.isEmpty) Path(node) else tailSafe(index) + node
        if(left.cost < right.cost) left else right
      }
    }
    println(paths.mkString("\n"))
    paths.headOption.getOrElse(Path.empty)
  }

  def printPath: Path => String = path => s"Minimal path is: ${path.nodes.mkString(" + ")} = ${path.cost}"

  def run(args: List[String]): IO[ExitCode] = {
    IO(println("Application minimum triangle path running"))
      .map(_ => readInput(StdIn.readLine))
      .map(validateInput)
      .map(_.map(calculateMaxPath))
      .map(_.map(p => println(printPath(p))))
      .map(_ => ExitCode.Success)
  }

}
