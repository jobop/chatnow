package sample.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;


/**
 * Created by Enzo Cotter on 2020/2/5.
 */
public class NettyEchoServer {
    public static void main(String[] args) throws InterruptedException {
        ServerBootstrap sbt = new ServerBootstrap();
        EventLoopGroup bossEventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup workerEventLoopGroup = new NioEventLoopGroup();

        try {
            sbt.group(bossEventLoopGroup, workerEventLoopGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<io.netty.channel.socket.SocketChannel>() {
                protected void initChannel(io.netty.channel.socket.SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            //读取信息，写回去
                            ByteBuf bb = (ByteBuf) msg;


                            ctx.writeAndFlush(bb);
                        }
                    });
                }

            });
            ChannelFuture future = sbt.bind(12307).sync();
            future.channel().closeFuture().sync();
        } finally {
            bossEventLoopGroup.shutdownGracefully();
            workerEventLoopGroup.shutdownGracefully();
        }
    }
}
