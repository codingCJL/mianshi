package com.cjl.zymst;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * CAS
 * 比较并交换
 */
public class CASDemo {
    public static void main(String[] args) {
        AtomicInteger atomicInteger=new AtomicInteger();
        atomicInteger.getAndIncrement();
    }
}
