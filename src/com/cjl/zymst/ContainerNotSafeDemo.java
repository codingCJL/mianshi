package com.cjl.zymst;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 集合类不安全问题
 */
public class ContainerNotSafeDemo {
    public static void main(String[] args) {
        List<String> list= new CopyOnWriteArrayList<>();
        for(int i =1;i<=20;i++){
            new Thread(()->{
                list.add(UUID.randomUUID().toString().substring(0,4));
                System.out.println("线程"+Thread.currentThread().getName()+":"+list);
            },String.valueOf(i)).start();
        }


    }
}
