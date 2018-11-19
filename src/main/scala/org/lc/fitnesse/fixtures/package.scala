package org.lc.fitnesse

import scala.util._
import java.net.ServerSocket
import kx.c.Dict
import scala.reflect._
import com.typesafe.scalalogging.LazyLogging
import java.sql.Timestamp
import com.github.nscala_time.time.Imports._
import org.joda.time.format.DateTimeParser
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.DateTimeFormatterBuilder
import java.text.SimpleDateFormat

package object fixtures {
  trait Logger extends LazyLogging

  implicit class RichMap[K, V](m: Map[K, V]) {
    def apply(ks: Seq[K]): Seq[V] = ks.collect { case k if (m.contains(k)) => m(k) }
    def take(ks: Seq[K]): Map[K, V] = ks.collect { case k if (m.contains(k)) => (k -> m(k)) } toMap
    def drop(ks: Seq[K]): Map[K, V] = take(m.keys.filterNot(ks.toSet).toSeq)
    def toDict: kx.c.Dict = new kx.c.Dict(m.keys.toArray(classTag[Any]), m.values.toArray(classTag[Any]))
  }

  implicit class RichDict(d: kx.c.Dict) {
    def toMap[K, V] = (d.x.asInstanceOf[Array[K]] zip d.y.asInstanceOf[Array[V]]) toMap
    def size: Int = d.x.asInstanceOf[Array[_]].length
    def isEmpty: Boolean = size == 0
    override def toString = stringOf(d)
  }

  object Month {
    // 2000.01m
    def valueOf(s: String) = new kx.c.Month(s.take(4).toInt - 2000 + s.drop(5).take(2).toInt)
  }

  object Dict {
    def apply(x: Array[_], y: Array[_]) = new kx.c.Dict(x, y)
  }

  val stringOf: Any => String = { o =>
    o match {
      case s: Array[Char]         => s.mkString
      case a: Array[_]            => a.collect { case x => stringOf(x) } mkString ("(", ";", ")")
      case d: kx.c.Dict           => d.x.asInstanceOf[Array[_]].mkString("`", "`", "!") + stringOf(d.y)
      case m: Map[_, _]           => m.collect { case e => s"(${e._1} -> ${stringOf(e._2)})" } mkString ("(", ", ", ")")
      case t: kx.c.Flip           => kx.Dumper.toString(t)
      case _                      => if (o == null) "NULL" else o.toString
    }
  }

  val className: Any => String = a => if (a == null) "NULL" else a.getClass.getName

  def getFreePort(): Int = {
    Try(new ServerSocket(0)) match {
      case Success(socket) =>
        socket.setReuseAddress(true)
        val port = socket.getLocalPort
        socket.close()
        port
      case _ => 0
    }
  }

  // TODO -- only works for java processes atm
  def getProcessId(p: scala.sys.process.Process) = {
    Try {
      p.getClass.getName match {
        case "java.lang.UNIXProcess" =>
          val f = p.getClass.getDeclaredField("pid")
          f.setAccessible(true)
          val pid = f.getLong(p)
          f.setAccessible(false)
          pid
        case _ => throw new UnsupportedOperationException("nyi")
      }
    } recoverWith (Success(-1))
  }

  implicit def toMap[K, V](o: Object): Map[K, V] = {
    o match {
      case d: kx.c.Dict => (d.x.asInstanceOf[Array[K]] zip d.y.asInstanceOf[Array[V]]) toMap
      case _            => throw new IllegalArgumentException(s"$o is not a Dict, it is a ${o.getClass.getName}")
    }
  }

  implicit def toShort(i: Int) = i.toShort

  val lwrap: String => String => String = w => s => w + s
  val rwrap: String => String => String = w => s => s + w
  val wrap: String => String => String = w => s => w + s + w
  val string: String => Array[Char] = s => s.toCharArray
  val hsym = lwrap("`:")
  val symbol = lwrap("`")
  val quote = wrap("\"")

  object Timestamp {
    def parse(s: String, df: DateTimeFormatter): java.sql.Timestamp = {
      new java.sql.Timestamp(DateTime.parse(s.trim, datetimeFormatter).getMillis)
    }
    def valueOf(s: String) = java.sql.Timestamp.valueOf(s)
  }

  object DateTimeFormatter {
    def apply(fs: List[String]): DateTimeFormatter = {
      val dtpf: String => DateTimeParser = f => DateTimeFormat.forPattern(f).getParser
      val ys: Array[DateTimeParser] = fs.toArray[String] map (dtpf)
      (new DateTimeFormatterBuilder()).append(null, ys).toFormatter()
    }
  }

  val dateFormats = "yyyy.MM.dd" :: "yyyyMMdd" :: "yyyy-MM-dd" :: Nil
  val dateFormatter = DateTimeFormatter(dateFormats)

  val dateTimeFormats = "yyyy.MM.dd'T'HH:mm:ss" :: "yyyyMMdd HH:mm:ss" :: "yyyy-MM-dd HH:mm:ss" :: Nil flatMap (f => f :: f + ".SSS" :: Nil)
  val datetimeFormatter = DateTimeFormatter(dateTimeFormats)

  case class QType(qtype: Short, cnv: String => Any, zero: Any) {
    def apply(a: String) = if (a == null || a.isEmpty) zero else cnv(a)
  }

  // Basic types
  var qtypes: Map[String, QType] = Map(
    ("*" -> QType(10, (a => a.trim.toCharArray), kx.c.NULL(10))),
    ("b" -> QType(-1, (b => b.trim.toBoolean), kx.c.NULL(1))),
    ("g" -> QType(-2, (g => java.util.UUID.fromString(g.trim)), kx.c.NULL(2))),
    ("x" -> QType(-4, (x => x.trim.toByte), kx.c.NULL(4))),
    ("h" -> QType(-5, (h => h.trim.toShort), kx.c.NULL(5))),
    ("i" -> QType(-6, (i => i.trim.toInt), kx.c.NULL(6))),
    ("j" -> QType(-7, (j => j.trim.toLong), kx.c.NULL(7))),
    ("e" -> QType(-8, (e => e.trim.toFloat), kx.c.NULL(8))),
    ("f" -> QType(-9, (f => f.trim.toDouble), kx.c.NULL(9))),
    ("c" -> QType(-10, (c => c.trim.head), kx.c.NULL(10))),
    ("s" -> QType(-11, (s => s.trim), kx.c.NULL(11))),
    ("p" -> QType(-12, (p => Timestamp.valueOf(p.trim)), kx.c.NULL(12))),
    ("m" -> QType(-13, (m => Month.valueOf(m.trim)), kx.c.NULL(13))),
    ("d" -> QType(-14, (d => DateTime.parse(d.trim, dateFormatter).toDate), kx.c.NULL(14))),
    ("z" -> QType(-16, (z => Timestamp.parse(z.trim, datetimeFormatter)), kx.c.NULL(16))),
    ("t" -> QType(-19, (t => java.sql.Time.valueOf(t.trim)), kx.c.NULL(19))))

  // Upper case -- collection types
  qtypes = qtypes ++ qtypes.collect {
    case (k, qt) if (k.head.isLetter) => (k.toUpperCase -> QType(-qt.qtype, (x => x.split(",").map(y => qt(y))), Array(qt.zero)))
  }

  // Overrides
  qtypes = qtypes ++ Map(
    ("C" -> QType(10, (s => if (s.contains(",")) s.split(",").map(x => x.trim.head) else s.toCharArray), Array(kx.c.NULL(10)))))
  //("s" -> QType(-11, (s => s.trim), kx.c.NULL(11))),
  //("S" -> QType(11, (s => s.split(",").map(x => x.trim)), Array(kx.c.NULL(11)))))

  val toQ: String => String => Any = t => s => qtypes(t)(s)
  
  import Converters._
  Converters.init()
}
