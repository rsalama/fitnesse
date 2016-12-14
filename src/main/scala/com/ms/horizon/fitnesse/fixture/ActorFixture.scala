package com.ms.horizon.fitnesse.fixture

import scala.actors.Actor
import scala.actors.Actor._
import scala.actors.Futures
import scala.collection;
import scala.collection.mutable._
import scala.collection.JavaConversions._

import org.slf4j.{Logger,LoggerFactory}

import com.ms.horizon.fitnesse.fixtures.HorizonConnectionFactory;
import kx.K;
import kx.Dumper;

case class Start

class ActorFixture(env: String) {
    val logger = LoggerFactory getLogger(this.getClass)
    val colMap = Map("name" -> "NAME", 
                     "pass?" -> "S", 
                     "msgs" -> "MSGS", 
                     "eval" -> "EVAL", 
                     "pass" -> "PASS")
    val colRevMap =  Map() ++ (colMap map {case (k,v) => (v,k)})
        
    def doTable(t: java.util.List[java.util.List[java.lang.String]]) : java.util.List[java.util.List[java.lang.String]] = {
        val hdr = List.fromArray(t.get(0).toArray).map((x) => colMap.get(x.asInstanceOf[String])).flatten
        Console.println("hdr: " + hdr)
        logger.info("Header: {}", hdr)
        
        val rows = List.range(1, t.size()).map((i) => 
            List.fromArray(t.get(i).toArray().map(_.asInstanceOf[String]) ))
        logger.info("Rows: {}", rows)

        val futures = rows.map((r) => new RowActor(env, hdr, r) !! Start)
        val results = Futures.awaitAll(5000, futures: _*).map((x) => asMap(x.get.asInstanceOf[K.Dict].toMap))
        logger.info("Results size: {}", results.size)
        Console.println("Results: " + results)
        
        val lst =  List[String](List().padTo(hdr.size, ""): _*) ::  results.map((r) => doRow(r, hdr))
        val ret = java.util.Arrays.asList(lst.map((x) => java.util.Arrays.asList(x.toArray: _*)): _*)

        logger.info("All Done")
        ret
    }
    
    def doRow(m: Map[Object, Object], h: List[String]) : List[String]= {
    	h.map(m.getOrElse(_, "").toString).toList
    }
}

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
    
    def callQ(dict: K.Dict) : K.Dict = {
        try {
            val hrz = HorizonConnectionFactory.getInstance().create("M", env)
            hrz.loadScript("fixture.q")
            val o = hrz.execute("execute", dict)
            hrz.exit()
            o.asInstanceOf[K.Dict]
        } catch {
            case e => 
                logger.error("callQ", e)
                throw e
        }
    }
}

