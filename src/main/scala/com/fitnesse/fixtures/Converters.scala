package com.fitnesse.fixtures

import fitnesse.slim.{ Converter => FitConverter, SlimError }
import java.text.SimpleDateFormat
import scala.util.{ Try, Success, Failure }
import fitnesse.slim.converters.ConverterRegistry
import scala.reflect.ClassTag

object Converters {
  case class Converter[T](val qtype: String, val to: T => String)(implicit val clasz: ClassTag[T]) extends FitConverter[T] {
    override def toString(o: T) = to(o)
    override def fromString(arg: String): T = {
      Try(toQ(qtype)(arg).asInstanceOf[T]) match {
        case Success(a) => a
        case Failure(e) => throw new SlimError(s"message:<<Can't convert %s to type '$qtype' (${clasz.runtimeClass.getSimpleName}) :: $arg", e);
      }
    }
  }

  def init() = {
    val timestampFmt = new SimpleDateFormat("yyyy.MM.dd'T'HH:mm:ss.SSS")
    val dateFmt = new SimpleDateFormat("yyyy.MM.dd")
    val dateConverter = Converter[java.util.Date]("d", d => dateFmt.format(d)) 
    val timestampConverter = Converter[java.sql.Timestamp]("z", z => timestampFmt.format(z))

    ConverterRegistry.addConverter(classOf[java.util.Date], dateConverter)
    ConverterRegistry.addConverter(classOf[java.sql.Timestamp], timestampConverter)
  }
}
