package com.fthdgn.books.utils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static com.fthdgn.books.utils.ArrayUtils.*;

public class ArrayUtilsTest {

    private String[] strings;

    @Before
    public void set() {
        strings = new String[]{
                "home", "nexus", "android",
                "home", null, "", "here"
        };
    }

    @Test
    public void indexOfTest() throws Exception {
        assertEquals(0, indexOf(strings, "home"));
        assertEquals(4, indexOf(strings, null));
    }

    @Test
    public void indexOf1Test() throws Exception {
        assertEquals(3, indexOf(strings, "home", 1));
    }

    @Test
    public void containsTest() throws Exception {
        assertTrue(contains(strings, null));
        assertTrue(contains(strings, "he" + "re"));
        assertTrue(contains(strings, "endroid".replace("e", "a")));
    }

    @Test
    public void replaceTest() throws Exception {
        replace(strings, null, "");
        assertTrue(strings[4].equals(""));
        replace(strings, "", "a");
        assertTrue(strings[4].equals("a"));
        assertTrue(strings[5].equals("a"));
        replace(strings, "home", null);
        assertTrue(strings[0] == null);
        assertTrue(strings[3] == null);
    }

}