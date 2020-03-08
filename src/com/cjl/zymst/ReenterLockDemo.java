package com.cjl.zymst;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 可重入锁(递归锁)
 * 指的是同一线程外层函数获得锁之后，内层递归函数仍然能获取该锁
 * 在同一个线程在外层方法获得锁的时候，在进入内层方法会自动获得锁
 * synchronized和ReentrantLock都是典型的可重入锁
 * 作用避免死锁
 */
class Phone implements Runnable{
    public synchronized void sendSMS(){
        System.out.println(Thread.currentThread().getName()+" invoked sendSMS");
        sendEmail();
    }
    public synchronized void sendEmail(){
        System.out.println(Thread.currentThread().getName()+" invoked sendEmail");
    }

    Lock lock=new ReentrantLock();
    @Override
    public void run() {
        get();
    }
    public void get(){
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName()+" invoked get");
            set();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    public void set(){
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName()+" invoked set");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
public class ReenterLockDemo {
    public static void main(String[] args) {
        Phone phone = new Phone();
        /*new Thread(()->{
            phone.sendSMS();
        },"t1").start();
        new Thread(()->{
            phone.sendSMS();
        },"t2").start();*/
        new Thread(phone).start();
        new Thread(phone).start();
    }
}
