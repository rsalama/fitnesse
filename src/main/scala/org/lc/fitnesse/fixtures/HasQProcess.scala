package org.lc.fitnesse.fixtures

import scala.sys.process._

trait HasQProcess extends Logger {
  private var qprocess: Option[Process] = None

  def startQProcess(e : String, c : String, port : Int) = {
    val n = sys.props.getOrElse("user.name", "Generic.Fixture")

    val screen = e match {
      case "1" => s"screen -S fitnesse-${n} -d -m "
      case _   => ""
    }
    val cmd = s"$screen /home/rs/local/packages/q/l64/q -e $e -c $c -p $port"
    qprocess = Some(cmd.run(ProcessLogger(line => logger.info(s"<<< q: $line"))))
    logger.info(s"Started: $cmd")
    Thread.sleep(200)
  }

  def endQProcess() = {
    qprocess.map(p => p.destroy)
    qprocess = None
  }
}
