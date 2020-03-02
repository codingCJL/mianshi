package com.cjl.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * NIO提供了与传统BIO模型中的 Socket 和 ServerSocket
 * 相对应的 SocketChannel 和 ServerSocketChannel
 * 多人聊天
 */
public class NioServer {
    private int port;

    // 用于字符集编解码
    private Charset charset=Charset.forName("UTF-8");

    // 用于接收数据的缓冲区
    private ByteBuffer rBuffer=ByteBuffer.allocate(1024);

    // 用于发送数据的缓冲区
    private ByteBuffer sBuffer=ByteBuffer.allocate(1024);

    // 用于存放客户端SocketChannel集合
    private Map<String, SocketChannel> clientMap=new HashMap<>();

    // 用于监听通道事件
    private static Selector selector;

    public NioServer(int port) {
        this.port = port;
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 初始化服务器
    public void init() throws IOException {
        ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket=serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(port));
        //开启监听通道事件
        selector=Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务器启动，端口为："+port);
    }

    /**
     * 服务器端轮询监听，select 方法会一直阻塞直到有相关事件发生或超时
     */
    public void listen(){
        while (true){
            try {
                selector.select(); // 返回值为本次触发的事件数
                Set<SelectionKey> selectionKeys=selector.selectedKeys();
                selectionKeys.forEach(selectionKey -> handle(selectionKey));
                selectionKeys.clear(); // 清除处理过的事件
            } catch (IOException e) {
                System.out.println("客户端退出了连接");
                break;
            }
        }
    }

    /**
     * 处理事件
     */
    private void handle(SelectionKey selectionKey) {
        try {
            // 有客户端要连接
            if(selectionKey.isAcceptable()){
                ServerSocketChannel server=(ServerSocketChannel)selectionKey.channel();
                SocketChannel client=server.accept();
                System.out.println("客户端["+client.getRemoteAddress()+"]连接成功");
                client.configureBlocking(false);
                client.register(selector,SelectionKey.OP_READ);
                clientMap.put(getClientName(client),client);
            }
            // 客户端发送了消息
            else if(selectionKey.isReadable()){
                SocketChannel client=(SocketChannel)selectionKey.channel();
                rBuffer.clear();
                int bytes=client.read(rBuffer);
                if(bytes>0){
                    rBuffer.flip();
                    String receiveText=String.valueOf(charset.decode(rBuffer));
                    System.out.println("客户端["+client.getRemoteAddress()+"]发出消息:"+receiveText);
                    //转发消息给各个客户端
                    dispatch(client, receiveText);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 转发消息给各个客户端
     */
    private void dispatch(SocketChannel client, String info) throws IOException{
        if(!clientMap.isEmpty()){
            for(Map.Entry<String,SocketChannel> entry:clientMap.entrySet()){
                SocketChannel temp=entry.getValue();
                if(!client.equals(temp)){
                    sBuffer.clear();
                    sBuffer.put(charset.encode(getClientName(client)+":"+info));
                    sBuffer.flip();
                    temp.write(sBuffer);
                }
            }
        }
    }
    /**
     * 生成客户端名字
     */
    private String getClientName(SocketChannel client) {
        Socket socket=client.socket();
        return "["+socket.getInetAddress().toString().substring(1)+":"+Integer.toHexString(client.hashCode())+"]";

    }

    public static void main(String[] args) {
        NioServer server=new NioServer(8888);
        server.listen();
    }
}
