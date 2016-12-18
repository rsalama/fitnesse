package com.ms.horizon.fitnesse.fixture;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class DynamicInputFixtureTest
{
    private  String[][] data =
    {{"name","pass?","msgs","eval","pass"},
     {"T", "", "Results", "count", "{x}"}};

    private List<List<String>> l;
    
    @Before
    public void setUp()
    {
        System.setProperty("local.qpath", "/home/rs/q");
        System.setProperty("local.q.usescreen", Boolean.TRUE.toString());
    
        l = new ArrayList<List<String>>();
        for (String[] ls : data)
        {
            l.add(java.util.Arrays.asList(ls));
        }
        System.out.println("input: " + l);
    }
    
    @Test
    public void testDynamicInputFixture()
    {
    
        DynamicInputFixture dif = new DynamicInputFixture("default", "/home/rs/tmp/filemedium.dat", 53, 10, 5000); 
        List<List<String>> rows = dif.doTable(l);
        
        System.out.println("output: " + rows);
    }
}
