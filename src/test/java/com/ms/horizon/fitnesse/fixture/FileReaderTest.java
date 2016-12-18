package com.ms.horizon.fitnesse.fixture;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import scala.collection.immutable.Stream;


public class FileReaderTest
{
    private FileReader fr;
    
    @Before
    public void setUp()
    {
        fr = new FileReader("/tmp/file.dat", 53, 10);
    }
    
    @Test
    public void testFileReader()
    {
        Stream strm = fr.test();
        Assert.assertEquals(3774, strm.length());
    }
    
    @Test
    public void test10FileReader()
    {
        Stream strm = fr.test10();
        Assert.assertEquals(10, strm.length());
    }
    
    // @Test
    public void testPrint()
    {
        fr.print("head: ", fr.get());
    }
    
    @Test
    public void testSplit()
    {
        fr.split(fr.get());
    }
    
    // @Test
    public void testOtherSplit()
    {
        fr.otherSplit(fr.get());
    }
}
