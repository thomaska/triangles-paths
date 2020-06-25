package com.suprnation

import java.io

import cats.effect.{ExitCode, IO, IOApp}
import com.suprnation.model.{EmptyTriangle, Leaf, MutableNode, Node, TriangleError, TriangleParsingError}

import scala.annotation.tailrec
import scala.io.StdIn
import scala.util.{Failure, Success, Try}

object Main extends IOApp {
  val Eof = "EOF"

  @tailrec
  def parseTriangleLevel(prev: List[MutableNode],
                         nodes: List[Int],
                         acc: List[MutableNode]): Either[TriangleError, List[MutableNode]] = {
    if (nodes.length != prev.length + 1)
      Left(TriangleParsingError(new Throwable(s"Nodes: $nodes are invalid")))
    else {
      (prev, nodes) match {
        case (Nil, last :: Nil) => Right(acc :+ MutableNode(last))
        case (parent :: prevLvlTail, left :: right :: newLvlTail) => {
          val l = MutableNode(left)
          val r = MutableNode(right)
          parent.left = l
          parent.right = r
          parseTriangleLevel(prevLvlTail, right :: newLvlTail, acc :+ l)
        }
        case _ => Left(TriangleParsingError(new Throwable(s"Nodes: $nodes are invalid")))
      }
    }
  }

  @tailrec
  def readTriangle(getLine: () => String,
                   prev: List[MutableNode],
                   root: Option[MutableNode]): Either[TriangleError, MutableNode] = {
    val line = getLine().stripMargin
    if (line == Eof)
      root.toRight(EmptyTriangle)
    else {
      val parsed = Try {
        line.split("\\s+").map(_.toInt)
      }.toEither

      // using `fold` in Either is not tail recursive
      if (parsed.isLeft)
        Left(TriangleParsingError(parsed.left.get))
      else {
        val nodes = parsed.right.get
        val cur = parseTriangleLevel(prev, nodes.toList, List.empty)

        if (cur.isLeft)
          Left(cur.left.get)
        else if (cur.isRight && root.isEmpty && cur.right.get.length == 1)
          readTriangle(getLine, cur.right.get, Some(cur.right.get.head))
        else {
          readTriangle(getLine, cur.right.get, root)
        }
      }
    }
  }

  def run(args: List[String]): IO[ExitCode] = {
    IO(println("Application minimum triangle path running"))
      .map(_ => readTriangle(StdIn.readLine, List.empty, None))
      .map(_ => ExitCode.Success)
  }

}
