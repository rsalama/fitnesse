package org.lc.fitnesse.fixtures

import scala.util.{ Try, Failure, Success }
import java.net.InetAddress

class KDBGenericFixture(sreInstance: String, repo: String, script: String, env: String, qver: String, E: String = "1") extends Logger with HasQProcess {
  private val c = "25 1000"
  // private val e = (Map(("DEV" -> "1"), ("QA" -> "1")) withDefaultValue ("0"))(env)
  private val reserved = "name" :: "msgs" :: "feval" :: "fpass" :: Nil
  private val resultKeys = "pass" :: "result" :: Nil
  private var k: kx.c = _
  private var fixtureValues: Map[String, Any] = Map.empty

  def set(fld: String, value: Any) = {
    logger.info(s"set($fld, ${stringOf(value)})")
    fixtureValues += (fld -> value)
  }

  private val resolve: ((String, Any)) => (String, Any) = { e =>
    val v = e._2.asInstanceOf[String]
    e._1.split(":").map(_.trim) match {
      case Array(k, t) => (k -> toQ(t)(v))
      case _           => (e._1 -> toQ("*")(v))
    }
  }

  def execute(): Unit = {
    logger.trace(">>> execute")
    logger.trace(s"fixtureValues: ${fixtureValues}")
    val args = fixtureValues.drop(reserved).map(resolve).toDict

    val a = if (args.isEmpty) "" else "0N!x"
    for {
      feval <- fixtureValues.get("feval")
      fpass <- fixtureValues.get("fpass")
      f = s"{p:($fpass) r:$feval[$a];0N!`pass`result!(p;r)}"
      _ = logger.info(s">>> q: $f[${stringOf(args)}]")
      r <- Try(toMap[String, Any](k.k(s"$f", args))) match {
        case Success(d) =>
          logger.info(s"Success(d): ${stringOf(d)}")
          resultKeys map (k => (k, d(k)))
        case Failure(e) =>
          logger.info(s"Failure(e): ${stringOf(e)}")
          resultKeys map (k => (k, e))
      }
      _ = set(r._1, r._2)
    } fixtureValues.get("pass") collect {case e: Throwable => throw(e) }
    logger.trace("<<< execute")
  }

  def get(k: String): Any = {
    val a = fixtureValues(k) match {
      case d: kx.c.Dict => d.toMap
      case r @ _        => r
    }
    logger.info(s"get($k) ${stringOf(a)} (${className(a)})")
    a
  }

  def beginTable(): Unit = {
    logger.trace(">>> beginTable")
    val host = InetAddress.getLocalHost
    val port = getFreePort

    startQProcess(E, c, port)

    k = new kx.c(host.getHostAddress, port)
    logger.info(s"Connected to ${host.getHostName}::$port: $k")

    val d = Try(k.k("0N!`ts`pid`hp`e`c!(.z.Z;.z.i;hsym `$\":\" sv string (.z.h;system\"p\")),system@/:\"ec\"")) match {
      case Success(d) => d.asInstanceOf[kx.c.Dict].toMap
      case Failure(e) => e
    }
    logger.info(s"Connection: ${stringOf(d)}")
    logger.trace("<<< beginTable")
  }

  def endTable(): Unit = {
    logger.trace(">>> endTable...")
    E match {
      case "0" => Try(k.k("exit[0]")) recoverWith { case _ => Success("exit") }
      case _   =>
    }
    endQProcess()
    logger.trace("<<< endTable...")
  }

  def reset(): Unit = {
    logger.trace(">>> reset")
    logger.info(s"--------------------------")
    fixtureValues = Map.empty
    logger.trace("<<< reset")
  }
}
