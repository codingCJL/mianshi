package com.cjl.zymst;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 多个线程同时读一个资源类没有问题，所以为了满足并发量，读取共享资源应该可以同时进行。
 * 但是
 * 如果有一个线程想去写共享资源，就不应该再有其他线程可以读该资源进行读或写
 * 读和读能共存
 * 读写和写写都不能共存
 * 写操作：原子性+独占
 *
 * 读写锁用处
 * 1 准备写入k1
 * 1 写入完成
 * 0 准备写入k0
 * 0 写入完成
 * 2 准备写入k2
 * 2 写入完成
 * 3 准备写入k3
 * 3 写入完成
 * 4 准备写入k4
 * 4 写入完成
 * 0 准备读取
 * 1 准备读取
 * 2 准备读取
 * 3 准备读取
 * 4 准备读取
 * 0 读取完成v0
 * 2 读取完成v2
 * 4 读取完成v4
 * 1 读取完成v1
 * 3 读取完成v3
 *
 */
//资源类
class MyCache{
    private volatile Map<String,Object> map=new HashMap<>();
    private ReentrantReadWriteLock rwlock=new ReentrantReadWriteLock();

    public void put(String key,Object value){
        //写锁，线程进来后独占锁
        rwlock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName()+" 准备写入"+key);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            map.put(key, value);
            System.out.println(Thread.currentThread().getName()+" 写入完成");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            rwlock.writeLock().unlock();
        }

    }

    public void get(String key){
        //读锁，线程进来后可以共享锁
        rwlock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName()+" 准备读取");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Object result=map.get(key);
            System.out.println(Thread.currentThread().getName()+" 读取完成"+result);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            rwlock.readLock().unlock();
        }

    }
}
public class ReadWriteLockDemo {
    public static void main(String[] args) {
        MyCache myCache=new MyCache();
        for (int i = 0; i < 5; i++) {
            final int temp=i;
            new Thread(()->{
                myCache.put("k"+temp,"v"+temp);
            },String.valueOf(i)).start();
        }


        for (int i = 0; i < 5; i++) {
            final int temp=i;
            new Thread(()->{
                myCache.get("k"+temp);
            },String.valueOf(i)).start();
        }

    }
}
