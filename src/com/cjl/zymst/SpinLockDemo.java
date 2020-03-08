package com.cjl.zymst;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 自旋锁是指当一个线程尝试获取某个锁时，
 * 如果该锁已被其他线程占用，就一直循环检测锁是否被释放，
 * 而不是进入线程挂起或睡眠状态。
 */
public class SpinLockDemo {
    //原子引用线程
    AtomicReference<Thread> atomicReference=new AtomicReference<>();

    public void Lock(){
        Thread thread=Thread.currentThread();
        System.out.println("线程"+thread.getName()+"进入lock方法");
        //自旋
        while (!atomicReference.compareAndSet(null,thread)){
            System.out.println("线程"+thread.getName()+"  is detected");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("线程"+thread.getName()+"已占有锁");
    }

    public void Unlock(){
        Thread thread=Thread.currentThread();
        atomicReference.compareAndSet(thread,null);
        System.out.println("线程"+Thread.currentThread().getName()+"已解除锁");
    }
    public static void main(String[] args) {
        SpinLockDemo spinLockDemo=new SpinLockDemo();
        new Thread(()->{
            spinLockDemo.Lock();
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            spinLockDemo.Unlock();
        },"a").start();


        new Thread(()->{
            spinLockDemo.Lock();
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            spinLockDemo.Unlock();
        },"b").start();



    }
}
