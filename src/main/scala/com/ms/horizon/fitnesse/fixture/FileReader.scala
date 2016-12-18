package com.ms.horizon.fitnesse.fixture

import scala.io.Source;
import java.io.File
import java.io.FileInputStream

class FileReader(arg : String, grp : Int, chunk : Int) {
    val stream = new FileInputStream(arg)
    val groups = Source.fromInputStream(stream).getLines().grouped(grp).toStream
    
    def get() = { groups }
    
    def test() : Stream[Seq[String]] = {
        groups.toList.map((x) => Console.println("group: " + x))
        Console.println("groups size: " + groups.toList.size) 
        groups
    }
    
    def test10 = {
       val g = groups.take(10)
       g.toList.map((x) => Console.println("group: " + x))
       g
    }
    
    def print(lbl: String, xs: Stream[Seq[String]]) {
        if (!xs.isEmpty) { Console.println(lbl + xs.head); print(lbl, xs.tail) }
    }
    
    def split(xs: Stream[Seq[String]]) {
        if (!xs.isEmpty) { print("split: ", xs take chunk); split(xs drop chunk) }
    }
    
    def otherSplit(xs: Stream[Seq[String]]) {
        if (!xs.isEmpty) {val p = xs splitAt 10; print("splitAt: ", p._1); otherSplit(p._2) }
    }
}