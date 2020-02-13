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

public class ServerWithMutilSelectorsAndWriteSync {
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
                                //XXX:一次报文可能会包含多条消息（粘包的情况），这里要用list存储
                                System.out.println("reading");
                                SocketChannel channel = (SocketChannel) key.channel();
                                //读到附件里，以便下次可以携带
                                ChannelBuffer bb = (ChannelBuffer) key.attachment();
                                int hasReadLength = channel.read(bb.readByteBuffer);


                                if (hasReadLength == -1) {
                                    //读不到东西关闭,cancel这个key
                                    key.cancel();
                                    channel.close();
                                } else {
                                    bb.readByteBuffer.flip();
                                    //转为读
                                    int oldpos = bb.readByteBuffer.position();
                                    int oldlimit = bb.readByteBuffer.limit();
                                    for (int i = oldpos; i < oldlimit; i++) {
                                        byte _byte = bb.readByteBuffer.get(i);
                                        //如果没有遇到\r(拆包的情况)，忽略其
                                        if (_byte == '\r') {
                                            ByteBuffer message = ByteBuffer.allocate(i - bb.readByteBuffer.position() + 1);
                                            //遇到结束符号，把他读进去，没有遇到则不处理，留到下一个包处理
                                            //手动处理
                                            bb.readByteBuffer.limit(i + 1);
                                            message.put(bb.readByteBuffer);
                                            message.flip();
                                            bb.readByteBuffer.position(i + 1);
                                            bb.readByteBuffer.limit(oldlimit);
                                            bb.writeByteBuffers.add(message);
                                            //XXX:这里在有消息的情况下才去注册，避免只要通道可用就唤醒写程序逻辑

                                        }


                                    }
                                    if (bb.writeByteBuffers.size() > 0) {
                                        key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
                                    }

                                }

                                //压缩
                                bb.readByteBuffer.compact();
                            } else if (key.isWritable()) {
                                ChannelBuffer bb = (ChannelBuffer) key.attachment();
                                SocketChannel sc = (SocketChannel) key.channel();


                                if (hasRemain(bb.writeByfferArray)) {
                                    sc.write(bb.writeByfferArray);
                                }
                                //再判断，如果没有Remain了的话，writeByfferArray清了，把list的塞进去
                                if (!hasRemain(bb.writeByfferArray)) {
                                    //如果list也为空，那么就取消关注事件
                                    if (bb.writeByteBuffers.isEmpty()) {
                                        key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
                                    } else {
                                        bb.writeByfferArray = bb.writeByteBuffers.toArray(new ByteBuffer[bb.writeByteBuffers.size()]);
                                        bb.writeByteBuffers.clear();
                                        //这里顺便再写一次
                                        sc.write(bb.writeByfferArray);
                                    }

                                }


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


    public static boolean hasRemain(ByteBuffer[] wba) {
        if (null == wba) {
            return false;
        }
        if (wba.length == 0) {
            return false;
        }
        boolean result = false;
        for (ByteBuffer wb : wba) {
            if (wb.hasRemaining()) {
                result = true;
                break;
            }
        }
        return result;
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
                                //XXX:给他个附件，用于处理粘包和写出环形队列

                                sc.register(chooseSelector(selectors), SelectionKey.OP_READ, new ChannelBuffer());
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

    static class ChannelBuffer {
        ByteBuffer readByteBuffer = ByteBuffer.allocate(2 * 128);
        List<ByteBuffer> writeByteBuffers = new ArrayList<ByteBuffer>();
        //XXX:这里使用一个数组，是因为channel可以一次性写array，如果是list的话，就要for循环，所有写的时候要从list移动到array里面
        ByteBuffer[] writeByfferArray;
    }
}
