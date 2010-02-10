package com.twitter.gizzard

import java.util.{AbstractList => JAbstractList, List => JList}


class ScalaSeqAdapter[A, B](protected val list: JList[A])(f: A => B) extends Seq[B] {
  def length = list.size

  def apply(i: Int) = f(list.get(i))

  def elements = new Iterator[B] {
    val iterator = list.iterator

    def next = f(iterator.next)
    def hasNext = iterator.hasNext
  }

  override def equals(other: Any) = {
    other match {
      case other: ScalaSeqAdapter[_, _] =>
        list == other.list
      case _ =>
        false
    }
  }
}

class JavaListAdapter[A, B](seq: Seq[A])(f: A => B) extends JAbstractList[B] {
  def size = seq.size
  def get(i: Int) = f(seq(i))
}

object Conversions {
  class RichSeq[A <: AnyRef](seq: Seq[A]) {
    def parallel(future: Future) = new ParallelSeq(seq, future)
    def toJavaList = new JavaListAdapter(seq)(x => x)
  }
  implicit def seqToRichSeq[A <: AnyRef](seq: Seq[A]) = new RichSeq(seq)

  class RichIntSeq(seq: Seq[Int]) {
    def toJavaList = new JavaListAdapter(seq)(_.asInstanceOf[java.lang.Integer])
  }
  implicit def seqToRichIntSeq(seq: Seq[Int]) = new RichIntSeq(seq)

  class RichJavaList[T <: AnyRef](list: JList[T]) {
    def toSeq = new ScalaSeqAdapter(list)(id => id)
    def toList = toSeq.toList
  }
  implicit def javaListToRichSeq[T <: AnyRef](list: JList[T]) = new RichJavaList(list)

  class RichJavaIntList(list: JList[java.lang.Integer]) {
    def toSeq = new ScalaSeqAdapter(list)(_.asInstanceOf[Int])
    def toList = toSeq.toList
  }
  implicit def javaIntListToRichSeq(list: JList[java.lang.Integer]) = new RichJavaIntList(list)
}

