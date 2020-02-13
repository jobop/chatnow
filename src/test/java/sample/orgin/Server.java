package sample.orgin;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Enzo Cotter on 2019/11/7.
 */
public class Server {

    public static void main(String[] args) throws IOException {
        final ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.socket().bind(new InetSocketAddress("192.168.1.87", 9998));

        final Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        Thread bossThread = new Thread(
        ) {
            @Override
            public void run() {
                while (true) {
                    try {
                        //XXX:这里一定要sellect,否则后面取不到key
                        selector.select(1000);
                        Set<SelectionKey> selectionKeys = selector.selectedKeys();
                        Iterator<SelectionKey> iterator = selectionKeys.iterator();

                        while (iterator.hasNext()) {
                            SelectionKey key = iterator.next();
                            if (key.isAcceptable()) {
                                System.out.println("accept");
                                //XXX:才去accept，才去accept
                                SocketChannel sc = ssc.accept();
                                sc.configureBlocking(false);
                                sc.register(selector, SelectionKey.OP_READ);
                            } else if (key.isReadable()) {
                                System.out.println("reading");
                                SocketChannel channel = (SocketChannel) key.channel();
                                ByteBuffer bb = ByteBuffer.allocateDirect(128);
                                int hasReadLength = channel.read(bb);
                                if (hasReadLength == -1) {
                                    //读不到东西关闭,cancel这个key
                                    key.cancel();
                                    channel.close();
                                } else {

                                    //读到就回显
                                    //反转bb为读
                                    bb.flip();
                                    ByteBuffer message = ByteBuffer.allocate(bb.limit() - bb.position());
                                    message.put(bb);
                                    message.flip();
                                    System.out.println(new String(message.array()));
                                    //写到channel中
                                    channel.write(message);
                                }
                            }
                            iterator.remove();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        bossThread.start();
        System.out.println("started");


    }
}
