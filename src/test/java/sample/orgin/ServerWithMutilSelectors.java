package sample.orgin;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

/**
 * Created by Enzo Cotter on 2019/11/7.
 * 考虑到同一个thread里要负责accept和io的读写，影响效率
 * 这里分离boss和worker的线程，提高性能
 */

public class ServerWithMutilSelectors {
    public static Selector chooseSelector(Selector[] selectors) {
        return selectors[new Random().nextInt(10) % selectors.length];

    }

    public static void handlerSubSelector(final Selector subSelector) {
        Thread workerThread = new Thread() {
            @Override
            public void run() {

                try {
                    while (true) {
                        subSelector.select(1000);
                        Set<SelectionKey> keys = subSelector.selectedKeys();
                        Iterator<SelectionKey> it = keys.iterator();
                        while (it.hasNext()) {
                            SelectionKey key = it.next();
                            it.remove();
                            if (key.isReadable()) {
                                //一次报文可能会包含多条消息（粘包的情况），这里要用list存储
                                List<ByteBuffer> writeByteBuffers = new ArrayList<ByteBuffer>();
                                System.out.println("reading");
                                SocketChannel channel = (SocketChannel) key.channel();
                                //读到附件里，以便下次可以携带
                                ByteBuffer bb = (ByteBuffer) key.attachment();
                                int hasReadLength = channel.read(bb);



                                if (hasReadLength == -1) {
                                    //读不到东西关闭,cancel这个key
                                    key.cancel();
                                    channel.close();
                                } else {
                                    bb.flip();
                                    //转为读
                                    int oldpos = bb.position();
                                    int oldlimit = bb.limit();
                                    for (int i = oldpos; i < oldlimit; i++) {
                                        byte _byte = bb.get(i);
                                        //如果没有遇到\r(拆包的情况)，忽略其
                                        if (_byte == '\r') {
                                            ByteBuffer message = ByteBuffer.allocate(i - bb.position() + 1);
                                            //遇到结束符号，把他读进去，没有遇到则不处理，留到下一个包处理
                                            //手动处理
                                            bb.limit(i + 1);
                                            message.put(bb);
                                            message.flip();
                                            bb.position(i + 1);
                                            bb.limit(oldlimit);
                                            writeByteBuffers.add(message);

                                        }

                                    }
                                }


                                //最后通过一个死循环把message写出去，因为在系统缓冲区满的时候，会直接返回写不出去，所有这里要循环到写完为止
                                for (ByteBuffer wbb : writeByteBuffers) {
                                    System.out.println(new String(wbb.array()));
                                    while (wbb.hasRemaining()) {
                                        channel.write(wbb);
                                    }
                                }
                                //压缩
                                bb.compact();
                            } else if (key.isWritable()) {


                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        workerThread.start();
    }

    public static void main(String[] args) throws IOException {
        final ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.socket().bind(new InetSocketAddress("192.168.1.87", 9998));

        final Selector[] selectors = new Selector[Runtime.getRuntime().availableProcessors() - 1];
        for (int i = 0; i < selectors.length; i++) {
            selectors[i] = Selector.open();
            handlerSubSelector(selectors[i]);

        }


        final Selector mainSelector = Selector.open();
        ssc.register(mainSelector, SelectionKey.OP_ACCEPT);

        Thread bossThread = new Thread(
        ) {
            @Override
            public void run() {
                while (true) {
                    try {
                        //XXX:这里一定要sellect,否则后面取不到key
                        mainSelector.select(1000);
                        Set<SelectionKey> selectionKeys = mainSelector.selectedKeys();
                        Iterator<SelectionKey> iterator = selectionKeys.iterator();

                        while (iterator.hasNext()) {
                            SelectionKey key = iterator.next();
                            if (key.isAcceptable()) {
                                System.out.println("accept");
                                //XXX:才去accept，才去accept
                                SocketChannel sc = ssc.accept();
                                sc.configureBlocking(false);
                                //给他个附件，用于处理粘包
                                sc.register(chooseSelector(selectors), SelectionKey.OP_READ, ByteBuffer.allocate(2 * 128));
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
