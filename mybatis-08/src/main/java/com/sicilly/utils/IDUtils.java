package com.sicilly.utils;
import org.junit.Test;

import java.util.UUID;

@SuppressWarnings("all")
public class IDUtils {

    public static String getId(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    @Test
    public void  test(){
        System.out.println(getId());
    }

}