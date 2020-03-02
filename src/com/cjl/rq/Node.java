package com.cjl.rq;

/**
 * 用于hashmap的结点
 */
public class Node<K,V> {
    int hash;
    K key;
    V value;
    Node next;
}
