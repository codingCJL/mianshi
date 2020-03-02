package com.cjl.zymst;
/**
 * Volatile 禁止指令重排优化，从而避免多线程环境下程序出现乱序执行的现象，导致数据不一致不准确的问题
 */
public class ReSortDemo {
    int a=0;
    boolean flag=false;

    public void method1(){
        this.a=1;
        this.flag=true;
    }
    public void method2(){
        if(flag){
            a=a+5;
            System.out.println(Thread.currentThread().getName()+"   a:"+a);
        }
    }

    public static void main(String[] args) {
        ReSortDemo reSortDemo=new ReSortDemo();
        new Thread(()->{
            reSortDemo.method1();
        },"A").start();
        new Thread(()->{
            reSortDemo.method2();
        },"B").start();
    }
}
