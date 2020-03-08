package com.cjl.zymst;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 集合类不安全问题
 */
public class ContainerNotSafeDemo {
    public static void main(String[] args) {
        //Map<String,String> map=new HashMap<>();
        Map<String,String> map=new ConcurrentHashMap<>();
        for(int i =1;i<=30;i++){
            new Thread(()->{
                map.put("线程"+Thread.currentThread().getName(),UUID.randomUUID().toString().substring(0,4));
                System.out.println(map);
            },String.valueOf(i)).start();
        }
    }

    private static void SetNotSafe() {
        //Set<String> set=new HashSet<>();
        //Set<String> set=Collections.synchronizedSet(new HashSet<>());
        Set<String> set=new CopyOnWriteArraySet<>();
        for(int i =1;i<=100;i++){
            new Thread(()->{
                set.add(UUID.randomUUID().toString().substring(0,4));
                System.out.println("线程"+Thread.currentThread().getName()+":"+set);
            },String.valueOf(i)).start();
        }
    }

    private static void ListNotSafe() {
        //List<String> list=new Vector<>();
        //List<String> list=Collections.synchronizedList(new ArrayList<>());
        List<String> list= new CopyOnWriteArrayList<>();
        for(int i =1;i<=20;i++){
            new Thread(()->{
                list.add(UUID.randomUUID().toString().substring(0,4));
                System.out.println("线程"+Thread.currentThread().getName()+":"+list);
            },String.valueOf(i)).start();
        }
    }
}
