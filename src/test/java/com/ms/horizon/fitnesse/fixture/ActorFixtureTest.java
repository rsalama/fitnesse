package com.ms.horizon.fitnesse.fixture;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class ActorFixtureTest
{
   private  String[][] data =
        {{"name","pass?","msgs","eval","pass"},
         {"T1","true","Foo1","1+1","{2~x}"}, 
         {"T2","true","Foo2","(`a`b`c)!(1 2 3)","{x}"}, 
         {"T3","true","Foo3","til 10","{count x}"}, 
         {"T4","true","Foo4","([] dt: 2010.01.01+til 365; tkr: 365?`3; tp: 365?100. )","{x}"}};

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
        System.out.println(l);
    }
    
    @Test
    public void testActorFixture()
    {
        ActorFixture af = new ActorFixture("default"); 
        List<List<String>> rows = af.doTable(l);
        Assert.assertNotNull(rows);
        Assert.assertEquals(5, rows.size());
        int i = 0;
        for (List<String> r : rows)
		{
			if (i == 0)
			{
				for (String s : r)
				{
					Assert.assertEquals("", s);
				}
			}
			else
			{
				Assert.assertNotSame("", r.get(0));
			}
			i++;
		}
        System.out.printf("Assert: %s\n", rows);
    }

    @After
    public void tearDown()
    {
        l = null;
    }
}
