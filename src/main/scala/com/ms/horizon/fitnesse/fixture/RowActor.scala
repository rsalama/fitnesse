package com.ms.horizon.fitnesse.fixture

import scala.actors.Actor
import scala.actors.Actor._
import scala.actors.Futures
import scala.collection;
import scala.collection.JavaConversions._

import org.slf4j.{Logger,LoggerFactory}

import com.ms.horizon.fitnesse.fixtures.HorizonConnectionFactory;
import kx.K;
import kx.Dumper;

case class Start

class RowActor(env: String, hdr: List[String], r: List[String]) extends Actor {
    val logger = LoggerFactory getLogger(this.getClass)
    logger.info("Created RowActor: {} :: {}", hdr, r)
    start()
    
    def act() {
        react {
            case Start =>
                val dict = new K.Dict(hdr.toArray, r.toArray)
                Console.println("[" + Thread.currentThread().getName + "]" + " Dict: " + dict.toMap)
                val o = callQ(dict)
                Console.println(Dumper.toString(o))
                reply(o)
        }
    }
    
    def callQ(dict: K.Dict) : Map[Object, Object] = {
        try {
            val hrz = HorizonConnectionFactory.getInstance().create("M", env)
            hrz.loadScript("fixture.q")
            val o = hrz.execute("execute", dict)
            hrz.exit()
            Map() ++ dict.toMap ++ o.asInstanceOf[K.Dict].toMap
        } catch {
            case e => 
                logger.error("callQ", e)
                throw e
        }
    }
}
