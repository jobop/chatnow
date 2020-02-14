package com.chatnow.core;

import com.chatnow.core.handlers.*;
import com.chatnow.core.handlers.protobuf.ProcessorHandler;
import com.chatnow.core.handlers.protobuf.ProtobufAnyEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * Created by Enzo Cotter on 2020/2/7.
 */
public class ChatNowServer {

    public static void main(String[] args) throws InterruptedException {

        NioEventLoopGroup bossLoopGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerLoopGroup = new NioEventLoopGroup(2 * Runtime.getRuntime().availableProcessors());
        try {
            ServerBootstrap sbs = new ServerBootstrap();

            sbs.group(bossLoopGroup, workerLoopGroup).channel(NioServerSocketChannel.class).childHandler(
                    new ChannelInitializer() {
                        protected void initChannel(Channel ch) throws Exception {

                            //空闲检测
                            ch.pipeline().addLast(new IdleStateHandler(2, 0, 0, TimeUnit.MINUTES));
                            ch.pipeline().addLast(new IdleEventHandler());
                            ch.pipeline().addLast(new OfflineHandler());


                            //处理proto半包
                            ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());

                            //解析数据proto数据，并分发给业务
                            ch.pipeline().addLast(new ProcessorHandler());


                            //输出增加proto半包标示
                            ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());

                            //任意类型序列化
                            ch.pipeline().addLast(new ProtobufAnyEncoder());


                        }
                    }

            );

            ChannelFuture channelFuture = sbs.bind(12350).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossLoopGroup.shutdownGracefully();
            workerLoopGroup.shutdownGracefully();
        }

    }
}
