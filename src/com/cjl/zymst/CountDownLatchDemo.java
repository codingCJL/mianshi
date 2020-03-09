package com.cjl.zymst;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch:待其他线程结束，主线程才结束,计数是减法
 */
public class CountDownLatchDemo {
    public static void main(String[] args) {
        method2();
    }

    public static void method1() {
        for(int i=1;i<=6;i++){
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+"结束");
            },"线程"+String.valueOf(i)).start();

        }
        System.out.println(Thread.currentThread().getName()+" 结束");
    }
    public static void method2(){
        CountDownLatch countDownLatch=new CountDownLatch(6);
        for(int i=1;i<=6;i++){
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+"结束");
                //减一
                countDownLatch.countDown();
            },"线程"+String.valueOf(i)).start();
        }
        //主线程停住，待CountDownLatch得参数为0，解除await
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+" 结束");
    }
}
