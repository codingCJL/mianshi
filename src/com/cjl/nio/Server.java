package com.cjl.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * 单连接
 */
public class Server {
    private Selector selector;
    private ByteBuffer sendBuffer=ByteBuffer.allocate(1024);
    private ByteBuffer readBuffer=ByteBuffer.allocate(1024);
    String str;

    public void start() throws IOException{
        //打开服务器套接字通道
        ServerSocketChannel ssc=ServerSocketChannel.open();
        ssc.configureBlocking(false); //服务器配置为非阻塞 即异步IO
        ssc.bind(new InetSocketAddress(3400));//绑定本地端口

        //创建选择器
        selector=Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);//ssc注册到selector准备连接

        //无限判断当前线程状态，如果没有中断，就一直执行while内容
        while (!Thread.currentThread().isInterrupted()){
            selector.select();//select()方法返回的值表示有多少个 Channel 可操作
            Set<SelectionKey> keys=selector.selectedKeys();
            Iterator<SelectionKey> keyIterator=keys.iterator();
            //处理客户端连接
            while (keyIterator.hasNext()){
                //一个SelectionKey表示一个到达的事件
                SelectionKey key=keyIterator.next();
                if(!key.isValid()){
                    continue;
                }
                if(key.isAcceptable()){
                    accept(key);
                }
                if(key.isReadable()){
                    read(key);
                }
                if(key.isWritable()){
                    write(key);
                }
                keyIterator.remove();//移除当前的key
            }
        }
    }


    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel ssc=(ServerSocketChannel)key.channel();
        SocketChannel clientChannel =ssc.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector,SelectionKey.OP_READ);
        System.out.println("一个新的客户端已连接 "+clientChannel.getRemoteAddress());
    }
    private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel= (SocketChannel) key.channel();
        this.readBuffer.clear();//清除缓冲区，准备接受新数据
        int numRead;
        try {
            numRead=socketChannel.read(this.readBuffer);
        } catch (IOException e) {
            key.cancel();
            socketChannel.close();
            return;
        }
        str=new String(readBuffer.array(),0,numRead);
        System.out.println("收到客户端数据："+str);
        //注册写操作 下一次进行写
        socketChannel.register(selector,SelectionKey.OP_WRITE);
    }
    private void write(SelectionKey key)  throws IOException, ClosedChannelException {
        String message="这是来自服务器的消息";
        SocketChannel channel= (SocketChannel) key.channel();
        sendBuffer.clear();
        sendBuffer.put(message.getBytes());
        sendBuffer.flip();//反转，由写变为读
        //注册读操作 下一次进行读
        channel.register(selector,SelectionKey.OP_READ);

    }

    public static void main(String[] args) throws IOException {
        Server server=new Server();
        System.out.println("服务器已启动。。。");
        server.start();

    }
}
