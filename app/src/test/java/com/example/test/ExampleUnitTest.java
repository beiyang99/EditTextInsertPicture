package com.example.test;

import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        System.out.println("xxx");
        test(new String[]{"1","2","3"});
        assertEquals(4, 2 + 2);
    }

    private void test(String... args){
        System.out.println(args.getClass().getSimpleName());
//        System.out.println(args);
    }
}