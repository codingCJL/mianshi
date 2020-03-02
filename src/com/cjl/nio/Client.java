package com.cjl.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * 单连接
 */
public class Client {
    ByteBuffer writeBuffer=ByteBuffer.allocate(1024);
    ByteBuffer readBuffer=ByteBuffer.allocate(1024);

    public static void main(String[] args) throws IOException {
        new Client().start();
    }
    public void start() throws IOException {
        //打开socket通道
        SocketChannel sc=SocketChannel.open();
        sc.configureBlocking(false);
        sc.connect(new InetSocketAddress("localhost",3400));
        //创建选择器
        Selector selector=Selector.open();
        //将channel注册到selector中
        sc.register(selector, SelectionKey.OP_CONNECT);
        Scanner scanner=new Scanner(System.in);

        while (true){
            selector.select();
            Set<SelectionKey> keys=selector.selectedKeys();
            Iterator<SelectionKey> iterator=keys.iterator();
            while (iterator.hasNext()){
                SelectionKey key=iterator.next();
                iterator.remove();
                //判断此通道上是否在进行连接操作
                if(key.isConnectable()){
                    sc.finishConnect();
                    //注册写操作
                    sc.register(selector,SelectionKey.OP_WRITE);
                    System.out.println("连接服务器成功");
                    break;
                }else if(key.isWritable()){
                    System.out.print("请输入消息：");
                    String message=scanner.nextLine();
                    writeBuffer.clear();
                    writeBuffer.put(message.getBytes());
                    //将缓冲区各标志复位,因为向里面put了数据标志被改变要想从中读取数据发向服务器,就要复位
                    writeBuffer.flip();
                    sc.write(writeBuffer);
                    //注册写操作,每个chanel只能注册一个操作，最后注册的一个生效
                    //如果你对不止一种事件感兴趣，那么可以用“位或”操作符将常量连接起来
                    //int interestSet = SelectionKey.OP_READ | SelectionKey.OP_WRITE;
                    //使用interest集合
                    sc.register(selector,SelectionKey.OP_WRITE | SelectionKey.OP_READ);
                }else if(key.isReadable()){
                    System.out.print("接收到服务器消息");
                    SocketChannel client= (SocketChannel) key.channel();
                    //将缓冲区清空以备下次读取
                    readBuffer.clear();
                    int num=client.read(readBuffer);
                    String reMsg=new String(readBuffer.array(),0,num);
                    System.out.println(reMsg);
                    //注册写操作，下一次写
                    sc.register(selector, SelectionKey.OP_WRITE);
                }


            }
        }

    }
}
