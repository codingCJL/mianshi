package com.cjl.zymst;


public class SingletonDemo {
    /**
     * 懒汉式
     */
    private static volatile SingletonDemo instance=null;

    private SingletonDemo(){
        System.out.println(Thread.currentThread().getName()+"我是构造函数SingletonDemo()");
    }
    /**
     * 多线程模式下使用dcl（double check lock 双端检锁机制）保证线程安全
     */
    public static SingletonDemo getInstance(){
        if(instance==null){
            synchronized (SingletonDemo.class){
                if(instance==null){
                    instance=new SingletonDemo();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        for(int i =1;i<=20;i++){
            new Thread(()->{
                SingletonDemo.getInstance();
            },String.valueOf(i)).start();
        }
    }
}
