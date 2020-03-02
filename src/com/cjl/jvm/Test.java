package com.cjl.jvm;

public class Test {
    public static void main(String[] args) {
        System.out.println(f(2));

    }

    public static int f(int value) {
        try {
            return value * value;
        } finally {
            if (value == 2) {
                return 0;
            }
        }
    }

    public void test(){
        final int a;
    }

    public static void test1(){
        final int a;
    }
}
