package com.ms.horizon.fitnesse.fixture

import scala.io.Source;
import java.io.File
import java.io.FileInputStream
import scala.collection.JavaConversions.asMap
import scala.actors.Futures

import org.slf4j.{Logger,LoggerFactory}

import com.ms.horizon.fitnesse.fixtures.HorizonConnectionFactory;
import kx.K;
import kx.Dumper;

class DynamicInputFixture(env : String, arg : String, grp : Int, chunk : Int, to : Int) {
    val colMap = Map("name" -> "NAME", 
                 "pass?" -> "S", 
                 "msgs" -> "MSGS", 
                 "eval" -> "EVAL", 
                 "pass" -> "PASS")
    val colRevMap =  Map() ++ (colMap map {case (k,v) => (v,k)})

    val stream = new FileInputStream(arg)
    val groups = Source.fromInputStream(stream).getLines().grouped(grp).toStream

    def doTable(t: java.util.List[java.util.List[java.lang.String]]) : java.util.List[java.util.List[java.lang.String]] = {
        val hdr = List.fromArray(t.get(0).toArray).map((x) => colMap.get(x.asInstanceOf[String])).flatten
        val r1 = List.fromArray(t.get(1).toArray)
        val tmpl = Map() ++ (hdr zip r1 )
        val results = doChunks(to, List[Map[Object, Object]](), hdr, tmpl, 0, groups)
        val lst = hdr.map((x) => colRevMap.getOrElse(x, "")) :: results.map((r) => doRow(r, hdr))
        val ret = java.util.Arrays.asList(lst.map((x) => java.util.Arrays.asList(x.toArray: _*)): _*)
        ret
    }
    
    def mkRow(i : Int, tmpl : Map[String, Object], arg : Pair[Seq[String], Int]) = {
        val s = arg._1.map((x) => "`" + x)
        List(tmpl.getOrElse("NAME", "U").asInstanceOf[String]+i+"_"+arg._2, 
                "", 
                tmpl.getOrElse("MSGS", "Results").asInstanceOf[String] + "At" + i.toString + arg._2,
                tmpl.getOrElse("EVAL", "").asInstanceOf[String] + s.toList.mkString, 
                tmpl.getOrElse("PASS", "{0b}").asInstanceOf[String])
    }
    
    def doChunks(to : Int, l : List[Map[Object, Object]], hdr: List[String], tmpl : Map[String, Object], i : Int, xs: Stream[Seq[String]]) : List[Map[Object, Object]] = {
        if (!xs.isEmpty) { 
            val futures = (xs take chunk).zipWithIndex.map((x) => new RowActor(env, hdr, mkRow(i, tmpl, x)) !! Start)
            val r =  Futures.awaitAll(to, futures: _*).map((x) => x.get.asInstanceOf[Map[Object, Object]])
            l ::: r ::: doChunks(to, l, hdr, tmpl, i+1, xs drop chunk)
        }
        else { l }
    }
    
    def doRow(m: Map[Object, Object], h: List[String]) : List[String]= {
        h.map(m.getOrElse(_, "").toString).toList
    }
}