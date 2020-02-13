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
public class NettyEchoClient {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup workerEventLoopGroup = new NioEventLoopGroup();

        Bootstrap bst = new Bootstrap();
        try {
            bst.group(workerEventLoopGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer() {
                protected void initChannel(Channel ch) throws Exception {
                    ByteBuf b = PooledByteBufAllocator.DEFAULT.buffer(8);
                    b.writeCharSequence("@", Charset.defaultCharset());
                    ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1000, b)).addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            super.channelActive(ctx);
                        }

                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            ByteBuf bb = (ByteBuf) msg;
                            byte[] bytes = new byte[bb.readableBytes()];
                            bb.readBytes(bytes);
                            System.out.println(new String(bytes));
                        }
                    });
                }
            });
            ChannelFuture future = bst.connect("127.0.0.1", 12307);

            future.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) throws Exception {
                    //XXX:这是promise模式，所以可以返回异常和错误，这里如果不判断success，可能会是失败的future
                    if (future.isSuccess()) {
                        for (int i = 0; i < 10; i++) {
                            ByteBuf buffer = PooledByteBufAllocator.DEFAULT.buffer(8);//代码一
                            buffer.writeCharSequence("asdfasjuiuydf12345678@", Charset.defaultCharset());
                            future.channel().writeAndFlush(buffer);
                        }
                    }

                }
            });

            future.channel().closeFuture().sync();


        } finally {
            workerEventLoopGroup.shutdownGracefully();
        }

    }
}
