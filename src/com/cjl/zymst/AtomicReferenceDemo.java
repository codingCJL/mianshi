package com.cjl.zymst;

import java.util.concurrent.atomic.AtomicReference;

class User{
    private String userName;
    private int age;

    public User(String userName, int age) {
        this.userName = userName;
        this.age = age;
    }

    public String getUserName() {
        return userName;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", age=" + age +
                '}';
    }
}

/**
 * 原子引用
 */
public class AtomicReferenceDemo {
    public static void main(String[] args) {
        User z3=new User("z3",22);
        User l4=new User("l4",25);
        AtomicReference<User> atomicReference=new AtomicReference<>();
        atomicReference.set(z3);
        System.out.println(atomicReference.compareAndSet(z3, l4)+"\t"+atomicReference.get().toString());
        System.out.println(atomicReference.compareAndSet(z3, l4)+"\t"+atomicReference.get().toString());


    }
}
