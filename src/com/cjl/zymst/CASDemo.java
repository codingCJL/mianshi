package com.cjl.zymst;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * CAS(compareAndSet)
 * 比较并交换
 * 在线程将变量写回到主内存时，会将期望值与主内存中的值进行比较，如果相等，说明这段时间内主线程的值没有被修改，就修改成功
 * 否则失败，需要重新获得主内存中的值进行比较
 * 底层用的是unsafe类调用native方法进行操作
 */
public class CASDemo {
    public static void main(String[] args) {
        AtomicInteger atomicInteger=new AtomicInteger(5);
        System.out.println(atomicInteger.compareAndSet(5, 20) + " current data:" + atomicInteger.get());
        System.out.println(atomicInteger.compareAndSet(5, 90) + " current data:" + atomicInteger.get());

    }
}
