package sample.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

import java.nio.charset.Charset;

/**
 * Created by Enzo Cotter on 2020/2/5.
 */
public class NettyEchoClientSync {

    private static ClientHandler clientHandler = new ClientHandler();

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup workerEventLoopGroup = new NioEventLoopGroup();

        Bootstrap bst = new Bootstrap();
        try {
            bst.group(workerEventLoopGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer() {
                protected void initChannel(Channel ch) throws Exception {
                    ByteBuf b = PooledByteBufAllocator.DEFAULT.buffer(8);
                    b.writeCharSequence("@", Charset.defaultCharset());
                    ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1000, b));

                    ch.pipeline().addLast(clientHandler);
                }
            });
            ChannelFuture future = bst.connect("127.0.0.1", 12307).sync();


            for (int i = 0; i < 10; i++) {
               Thread t= new Thread() {
                    @Override
                    public void run() {
                        ByteBuf buffer = PooledByteBufAllocator.DEFAULT.buffer(8);//代码一
                        buffer.writeCharSequence("asdfasjuiuydf12345678@", Charset.defaultCharset());
                        ChannelPromise promise = clientHandler.sendMessage(buffer);
                        try {
                            promise.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(clientHandler.getData());
                    }
                };
               t.start();

            }


            future.channel().closeFuture().sync();


        } finally {
            workerEventLoopGroup.shutdownGracefully();
        }

    }
}
