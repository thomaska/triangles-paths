package com.suprnation

import java.io

import cats.effect.{ExitCode, IO, IOApp}
import com.suprnation.model.{EmptyTriangle, Leaf, MutableNode, Node, TriangleError, TriangleParsingError}

import scala.io.StdIn
import scala.util.{Failure, Success, Try}

object Main extends IOApp {
  val Eof = "EOF"

//  > 7
//  > 6 3
//  > 3 8 5
//  > 11 2 10 9
  def parseRoot(nodes: Array[Int]): Try[MutableNode] = {
    if (nodes.length != 1)
      Failure(new Throwable("There must be exactly one value as root of the triangle"))
    else Success(MutableNode(nodes.head))
  }

  def toImmutableTriangle: MutableNode => Node = ???

  def readTriangle(getLine: () => String, prev: List[MutableNode], root: Option[MutableNode]): Either[TriangleError, Node] = {
    val line = getLine()
    val res: Either[TriangleError, MutableNode] =
      if (line == Eof)
        root.toRight(EmptyTriangle)
      else {
        Try {
          val nodes: Array[Int] = line.split(" ").map(_.toInt)
          root.fold(parseRoot(nodes)){r => Try(r)}
        }.flatten.toEither.left.map(TriangleParsingError)
      }
    res.map(toImmutableTriangle)
  }

  def run(args: List[String]): IO[ExitCode] = {
    IO(println("Application minimum triangle path running"))
      .map(_ => readTriangle(StdIn.readLine, List.empty, None))
      .map(_ => ExitCode.Success)
  }

}
