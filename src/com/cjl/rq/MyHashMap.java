package com.cjl.rq;

import java.util.Arrays;

public class MyHashMap<K,V> {
    //位桶数组
    private transient Node[] table;
    //存放键值对的个数
    private transient int size;

    public MyHashMap(){
        table=new Node[16];
    }

    public void put(K key,V value){
        Node newNode=new Node();
        newNode.hash=myHash(key.hashCode(),table.length);//将key对象的hashcode和数组长度-1经过位运算得到hash值
        newNode.key=key;
        newNode.value=value;

        //位桶数组的第一个
        Node temp=table[newNode.hash];
        //链表的最后一个
        Node iterLast=null;
        boolean keyRepeat=false;
        if(temp==null){
            //如果此处数组为空，将新结点放入
            table[newNode.hash]=newNode;
        }else {
            //不为空，遍历链表
            while (temp!=null){
                //如果key重复则覆盖
                if(temp.key.equals(key)){
                    keyRepeat=true;
                    temp.value=value;
                    break;
                }else {
                    //找到最后一个
                    iterLast=temp;
                    temp=temp.next;
                }
            }
            if(!keyRepeat){
                iterLast.next=newNode;
            }

        }
        size++;

    }

    public V get(K key){
        int hash=myHash(key.hashCode(),table.length);
        V value=null;
        if(table[hash]!=null){
            Node temp=table[hash];
            while (temp!=null){
                if(temp.key.equals(key)){
                    value=(V) temp.value;
                    break;
                }else {
                    temp=temp.next;
                }
            }
        }
        return value;
    }

    public int size(){
        return size;
    }

    public int myHash(int v,int length){
        return v&(length-1);
    }

    public void show(){
        /*for(int i=0;i<table.length;i++){
            Node temp=table[i];
            if(temp==null){
                System.out.println(i+":null");
            }else {
                System.out.print((temp.key + ":" + temp.value + "->"));
                while (temp!=null){
                    temp=temp.next;
                }
            }

        }*/

        for(int i=0;i<table.length;i++){
            if(table[i]==null){
                System.out.println("table["+i+"]:null");
            }else {
                Node temp=table[i];
                while (temp!=null){
                    System.out.print("table["+i+"]:"+temp.key+":"+temp.value+"->");
                    temp=temp.next;
                }
                System.out.println();

            }

        }
    }
}
