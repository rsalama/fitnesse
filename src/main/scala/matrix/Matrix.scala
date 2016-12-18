package matrix

import scala.collection.mutable.ListBuffer
import scala.collection.immutable.TreeMap
import java.io.PrintWriter
import java.io.StringWriter

sealed case class DataContainer[T](name: String, cells: ListBuffer[Option[T]]) {
  def apply(j: Int) = cells(j)
  def equals(d: DataContainer[T]): Boolean = (name equals d.name) && (cells equals d.cells)
}
sealed case class Dimension(m: Int, n: Int) // m rows X n cols

abstract class Major
case object Row extends Major
case object Column extends Major

sealed case class Matrix[T](private var _major: Major = Row) {
  def major = _major
  def major_=(m: Major) = _major = m

  private var _captions = Seq[String]()
  def captions = _captions
  def captions_=(xs: Seq[String]) = _captions = xs

  private var rows = ListBuffer[DataContainer[T]]()
  private var columns = ListBuffer[DataContainer[T]]()
  private def sliceOf(m: Major) = m match {
    case Column => columns
    case _ => rows
  }
  def apply(i: Int)(j: Int) = _major match {
    case Column => columns(j)(i)
    case _ => rows(i)(j)
  }
  def apply(s: String)(t: String) = _major match {
    case Column => {
      val j = columns.findIndexOf(_.name eq t)
      val i = _captions.indexOf(s)
      columns(j)(i)
    }
    case Row => {
      val i = rows.findIndexOf(_.name eq s)
      val j = _captions.indexOf(t)
      rows(i)(j)
    }
  }
  def header(i: Int) = sliceOf(_major)(i).name
  def headers = sliceOf(_major) map (_.name)
  def isEmpty = (rows.size == 0 && columns.size == 0)
  def dim = (isEmpty, major) match {
    case (true, _) => Dimension(0, 0)
    case (false, c: Major) if (c == Column) => Dimension(columns(0).cells.size, columns.size)
    case _ => Dimension(rows.size, rows(0).cells.size)
  }

  def +|(name: String, data: Seq[(String, T)]) = _major match {
    case Column => add(name, data)
    case _ => throw new IllegalAccessException("Matrix is not in column major")
  }
  def +-(name: String, data: Seq[(String, T)]) = _major match {
    case Row => add(name, data)
    case _ => throw new IllegalAccessException("Matrix is not in row major")
  }
  private def add(name: String, data: Seq[(String, T)]): Unit = {
    val m = Map() ++ data map (x => x._1 -> x._2)
    val d = DataContainer[T](name, ListBuffer[Option[T]]())
    _captions foreach { c =>
      d.cells.append(m.get(c))
    }
    add(_major)(d)
  }

  def +|(col: DataContainer[T]) = add(Column)(col)

  def +-(row: DataContainer[T]) = add(Row)(row)
  private def add(m: Major)(d: DataContainer[T]): Unit = sliceOf(major) += d

  def foreach(f: DataContainer[T] => Unit) = sliceOf(_major) foreach f
  def map[R](f: DataContainer[T] => R) = sliceOf(_major) map f
  def flip: Matrix[T] = _major match {
    case Column => {
      val m = Matrix[T](Row)
      m.captions = captions
      columns foreach (c => m +- c)
      m
    }
    case Row => {
      val m = Matrix[T](Column)
      m.captions = captions
      rows foreach (r => m +| r)
      m
    }
  }

  private def stringOf(x: Any): String = x match {
    case Some(a) => a.toString()
    case None => "-"
    case _ => x.toString
  }

  override def toString = {
    val sw: StringWriter = new StringWriter
    val pw: PrintWriter = new PrintWriter(sw, true)
    val fmax = { (xs: Seq[_ >: { def toString: String }]) => (0 /: xs)(_ max stringOf(_).length) }

    _major match {
      case Column => {
        val ns = columns map (_.name)
        val cfmt = columns map (c => "%" + fmax(c.name +: c.cells) + "s ")
        val rfmt = "%" + fmax(captions) + "s| "

        if (captions.size > 0) pw.print(rfmt.format(" "))
        ns zip cfmt foreach (h => pw.print(h._2.format(h._1)))
        pw.println
        (0 to this.dim.n - 1) foreach { i =>
          if (captions.size > 0) pw.print(rfmt.format(captions(i)))
          columns zip cfmt foreach (c => pw.print(c._2.format(c._1.cells(i).getOrElse(""))))
          pw.println
        }
        sw.toString
      }
      case Row => {
        val xs = (rows map (r => r.cells)).transpose
        val ys = if (captions.size > 0) xs zip captions map (x => x._2 +: x._1) else xs
        val cfmt = ys map (c => "%" + fmax(c) + "s ")
        val rfmt = "%" + fmax(rows map (_.name)) + "s| "
        if (captions.size > 0) {
          pw.print(rfmt.format(" "))
          captions zip cfmt foreach (c => pw.print(c._2.format(c._1)))
          pw.println
        }
        rows foreach { r =>
          pw.print(rfmt.format(r.name))
          r.cells zip cfmt foreach (c => pw.print(c._2.format(c._1.getOrElse(""))))
          pw.println
        }
        sw.toString
      }
    }
  }
  override def equals(that: Any) = that match {
    case om: Matrix[T] => {
      //      (true /: Seq((dim, om.dim), (major, om.major), (sliceOf(major), om.sliceOf(om.major))))((b, p) => b && (p._1 equals p._2))
      (dim equals om.dim) && (major equals om.major) && (sliceOf(major) equals om.sliceOf(om.major))
    }
    case _ => false
  }
}

