package sample.orgin;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Enzo Cotter on 2019/11/7.
 */
public class Client {
    public static void main(String[] args) throws IOException {
        final SocketChannel sc = SocketChannel.open();
        sc.configureBlocking(false);
        final Selector selector = Selector.open();

        sc.register(selector, SelectionKey.OP_CONNECT);

        new Thread(new Runnable() {
            public void run() {
                //连上就注册读事件，并发送信息
                while (true) {
                    try {
                        //XXX:这里一定要sellect,否则后面取不到key
                        selector.select(1000);
                        Set<SelectionKey> keys = selector.selectedKeys();
                        Iterator<SelectionKey> it = keys.iterator();
                        while (it.hasNext()) {
                            SelectionKey key = it.next();

                            if (key.isConnectable()) {
                                System.out.println("connected!");

                                key.channel().register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, ByteBuffer.allocateDirect(256));
                                final SocketChannel sc = (SocketChannel) key.channel();
                                //XXX:connectable并不是connect完成。。这里要判断是否已经connect完成
                                if (sc.finishConnect()) {
                                    it.remove();
                                    //每500毫秒写一个
                                    new Thread(new Runnable() {
                                        public void run() {
                                            int j = 0;
                                            while (true) {
                                                try {
                                                    ByteBuffer bb = ByteBuffer.allocateDirect(256);
                                                    j++;
                                                    String hello1 = "HelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHello" + j + "\r";
                                                    j++;
                                                    String hello2 = "HelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHello" + j + "\r";
                                                    byte[] bytes = (hello1 + hello2).getBytes();
                                                    for (int i = 0; i < bytes.length; i++) {
                                                        bb.put(bytes[i]);
                                                    }
                                                    //XXX:注意这些地方要读写转换
                                                    //转为读。。
                                                    bb.flip();
                                                    while (bb.hasRemaining()) {
                                                        sc.write(bb);
                                                    }
                                                    Thread.sleep(100);

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }).start();


                                }
                            } else if (key.isReadable()) {

                                List<ByteBuffer> readByteBuffers = new ArrayList<ByteBuffer>();

                                System.out.println("reading");

//                                ByteBuffer bb = ByteBuffer.allocateDirect(256);
                                SocketChannel sc = (SocketChannel) key.channel();
//                                //这里直接read进去就行，不用一个个get
//                                int hasReadLength = sc.read(bb);
//                                bb.flip();
//                                byte[] bytes = new byte[bb.remaining()];
//                                bb.get(bytes);
//
//                                System.out.println(new String(bytes));

                                ByteBuffer readByteBuffer = (ByteBuffer) key.attachment();
                                int readLength = sc.read(readByteBuffer);
                                if (readLength > 0) {
                                    readByteBuffer.flip();
                                    int oldPos = readByteBuffer.position();
                                    int oldLimit = readByteBuffer.limit();

                                    for (int i = oldPos; i < oldLimit; i++) {
                                        if (readByteBuffer.get(i) == '\r') {
                                            ByteBuffer message = ByteBuffer.allocate(i - readByteBuffer.position() + 1);
                                            //遇到结束符号，把他读进去，没有遇到则不处理，留到下一个包处理
                                            //手动处理
                                            readByteBuffer.limit(i + 1);
                                            message.put(readByteBuffer);
                                            message.flip();
                                            readByteBuffer.position(i + 1);
                                            readByteBuffer.limit(oldLimit);
                                            readByteBuffers.add(message);
                                        }
                                    }

                                }

                                for (ByteBuffer bb : readByteBuffers) {
                                    System.out.println(new String(bb.array()));
                                }
                                readByteBuffer.compact();
                                it.remove();
                            } else {
                                it.remove();
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }


            }
        }).start();


        //连接
        sc.connect(new InetSocketAddress("192.168.1.87", 9998));
        System.out.println("started");
    }
}
