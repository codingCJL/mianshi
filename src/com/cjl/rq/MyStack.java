package com.cjl.rq;

import java.util.Arrays;

/**
 * 自己实现一个栈，要求这个栈具有push()、pop()（返回栈顶元素并出栈）、peek() （返回栈顶元素不出栈）、isEmpty()、size()这些基本的方法。
 *
 * 提示：每次入栈之前先判断栈的容量是否够用，如果不够用就用Arrays.copyOf()进行扩容；
 */
public class MyStack {
    private int[] storage;//存放栈中元素的数组
    private int capacity;//栈的容量
    private int count;//元素的数目
    private static final int GROW_FACTOR=2;//扩容因子

    public MyStack(){
        this.capacity=8;
        this.storage=new int[8];
        this.count=0;
    }

    public MyStack(int initialCapacity){
        if(initialCapacity<1){
            throw new IllegalArgumentException("Capacity is too small");
        }
        this.capacity=initialCapacity;
        this.storage=new int[initialCapacity];
        this.count=0;
    }
    /**
     * 入栈
     */
    public void push(int value){
        if(count==capacity){
            ensureCapacity();
        }
        storage[count++]=value;
    }

    /**
     * 出栈
     */
    public int pop(){
        count--;
        if(count==-1){
            throw new IllegalArgumentException("Stack is Empty");
        }
        return storage[count];
    }
    /**
     * 确保容量大小，即扩容
     */
    private void ensureCapacity() {
        int newCapacity=capacity*GROW_FACTOR;
        storage= Arrays.copyOf(storage,newCapacity);
        capacity=newCapacity;
    }
    /**
     * 返回栈顶元素不出栈
     */
    private int peek(){
        if(count==0){
            throw new IllegalArgumentException("Stack is Empty");
        }else {
            return storage[count-1];
        }
    }

    private boolean isEmpty() {
        return count == 0;
    }


    private int size() {
        return count;
    }

    public static void main(String[] args) {
        MyStack stack=new MyStack();
        stack.push(1);
        stack.push(4);
        stack.push(6);
        System.out.println(stack.peek());
        System.out.println(stack.size());
    }

}
