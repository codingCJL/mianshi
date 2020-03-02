package com.cjl.zymst;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


class MyData{
    volatile int number=0;
    public void addT060(){
        this.number=60;
    }
    public void addPlusPlus(){
        this.number++;
    }
    AtomicInteger atomicInteger=new AtomicInteger();
    public void addAtomic(){
        this.atomicInteger.getAndIncrement();
    }
}

/**
 * 1.1 假如 int number=0;number变量没有添加volatile关键字
 */
public class VolatileDemo {
    public static void main(String[] args) {


    }

    private static void bubaozhengyzx() {
        /**
         * Volatile 演示 不保证原子性
         * 原子性指的是什么意思
         * 不可分割，完整性，也即某个线程正在做某个具体业务时，中间不可以被加塞或者被分割，需要整体完整
         * 要么同时成功，要么同时失败
         */

        MyData myData=new MyData();
        for(int i =1;i<=20;i++){
            new Thread(()->{
                for (int j = 0; j <1000 ; j++) {
                    myData.addPlusPlus();
                    myData.addAtomic();
                }
            },String.valueOf(i)).start();
        }
        //需要等待上面20个线程全都计算完成后，再用main线程取得结果
        /*try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        while (Thread.activeCount()>2){
            Thread.yield();
        }
        System.out.println(Thread.currentThread().getName() + " finally number value:" + myData.number);
        System.out.println(Thread.currentThread().getName() + " finally atomicNumber value:" + myData.atomicInteger);
    }

    /**
     * Volatile 演示可见性
     * 线程操作完变量之后要及时通知别的线程主内存中的变量已变化
     */
    private static void kejianxing() {
        MyData myData=new MyData();
        new Thread(()->{
            System.out.println(Thread.currentThread().getName()+"\tcome in");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myData.addT060();
            System.out.println(Thread.currentThread().getName()+"\tupdate value to"+myData.number);
        },"a").start();
        //第二个线程为主线程
        //当主线程知道myData.number不为0等于60的时候，退出循环
        while (myData.number==0){
            System.out.println("main is running");
        }
        System.out.println(Thread.currentThread().getName()+"\t know number is "+myData.number);
    }
}
