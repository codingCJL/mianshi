package com.cjl.zymst;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * ABA问题
 * 两个线程ab修改变量，b线程将变量修改为其他变量之后又修改为原来的变量，
 * 此时a线程对比主内存的变量一样修改成功，但这个过程中变量是变化过的，不代表没有问题
 *
 * 解决ABA问题，在变量上加上一个版本号，类似于时间戳，对比版本号就可以发现主内存中的变量是否被修改过
 * 解决类：AtomicStampedReference
 */
public class ABADemo {

    static AtomicReference<Integer> atomicReference=new AtomicReference<>(100);
    static AtomicStampedReference<Integer> atomicStampedReference=new AtomicStampedReference<>(100,1);
    public static void main(String[] args) {
        System.out.println("=====ABA问题产生=====");
        new Thread(()->{
            atomicReference.compareAndSet(100,101);
            System.out.println(Thread.currentThread().getName()+"将100修改为101");
            atomicReference.compareAndSet(101,100);
            System.out.println(Thread.currentThread().getName()+"将101修改为100");
        },"t1").start();

        new Thread(()->{
            //暂停1秒钟t2线程，保证t1线程完成了一次ABA操作
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+"即将要把100修改为2019");
            System.out.println(Thread.currentThread().getName()+"修改"+atomicReference.compareAndSet(100, 2019)+"\t"+atomicReference.get());


        },"t2 ").start();

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("=====ABA问题解决=====");

        new Thread(()->{
            System.out.println(Thread.currentThread().getName()+"\t当前版本号"+atomicStampedReference.getStamp()+"变量值："+atomicStampedReference.getReference());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            atomicStampedReference.compareAndSet(100,101,atomicStampedReference.getStamp(),atomicStampedReference.getStamp()+1);
            System.out.println(Thread.currentThread().getName()+"\t当前版本号"+atomicStampedReference.getStamp()+"变量值："+atomicStampedReference.getReference());
            atomicStampedReference.compareAndSet(101,100,atomicStampedReference.getStamp(),atomicStampedReference.getStamp()+1);
            System.out.println(Thread.currentThread().getName()+"\t当前版本号"+atomicStampedReference.getStamp()+"变量值："+atomicStampedReference.getReference());
        },"t3").start();

        new Thread(()->{
            int stamp=atomicStampedReference.getStamp();
            System.out.println(Thread.currentThread().getName()+"\t当前版本号"+stamp+"变量值："+atomicStampedReference.getReference());
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean result=atomicStampedReference.compareAndSet(100,2019,stamp,stamp+1);
            System.out.println(Thread.currentThread().getName()+"\t修改"+result);
            System.out.println(Thread.currentThread().getName()+"\t当前版本号"+atomicStampedReference.getStamp()+"变量值："+atomicStampedReference.getReference());
        },"t4").start();
    }
}
