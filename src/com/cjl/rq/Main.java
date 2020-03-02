package com.cjl.rq;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        MyHashMap<Integer,String> myHashMap=new MyHashMap<>();
        for(int i=1;i<20;i++){
            myHashMap.put(i,i+"V");
        }
        HashMap map=new HashMap();
        for(int i=1;i<20;i++){
            map.put(i+"K",i+"V");
        }
        //System.out.println(myHashMap);
        //myHashMap.show();
        System.out.println(myHashMap.get(1));

    }

    public static void test(){
        Map<String,String> map=new ConcurrentHashMap<>();
        for(int i=1;i<100;i++){
            int temp=i;
            new Thread(()->{
                map.put(temp+"k",temp+"v");
            },"线程"+i).start();
        }

        System.out.println(map);
    }
}
