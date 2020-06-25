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

  @tailrec
  def parseTriangleLevel(prev: List[MutableNode],
                         nodes: List[Int],
                         acc: List[MutableNode]): Either[TriangleError, List[MutableNode]] = {
    (prev, nodes) match {
      case (Nil, root :: Nil) => Right(acc :+ MutableNode(root))
      case (parent :: prevLvlTail, left :: right :: newLvlTail) => {
        val l = MutableNode(left)
        val r = MutableNode(right)
        parent.left = l
        parent.right = r
        if (prevLvlTail.nonEmpty)
          parseTriangleLevel(prevLvlTail, right :: newLvlTail, acc ++ List(l, r))
        else
          parseTriangleLevel(prevLvlTail, right :: newLvlTail, acc ++ List(l))

      }
      case _ => Left(TriangleParsingError(new Throwable(s"Error - Nodes: $nodes are invalid")))
    }
  }

  @tailrec
  def readTriangle(getLine: () => String,
                   prev: List[MutableNode],
                   root: Option[MutableNode]): Either[TriangleError, MutableNode] = {
    val line = getLine().trim
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
        val cur   = parseTriangleLevel(prev, nodes.toList, List.empty)

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

  def run(args: List[String]): IO[ExitCode] = {
    IO(println("Application minimum triangle path running"))
      .map(_ => readInput(StdIn.readLine))
      .map(validateInput)
      .map(_ => ExitCode.Success)
  }

}
