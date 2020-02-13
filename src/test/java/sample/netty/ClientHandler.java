package sample.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Enzo Cotter on 2020/2/13.
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {
    private ChannelHandlerContext ctx;
    //这里简单用一个promise存储来测试，但是实际上服务端一些业务处理快，一些慢，返回的resp有可能不是按顺序的，
    private ConcurrentLinkedQueue<ChannelPromise> queue=new ConcurrentLinkedQueue<ChannelPromise>();
    private String data;
    private long readByte;
    private long contentLength;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.ctx = ctx;
    }

    public ChannelPromise sendMessage(Object message) {
        if (ctx == null)
            throw new IllegalStateException();
        ChannelPromise promise = ctx.writeAndFlush(message).channel().newPromise();
        queue.add(promise);
        return promise;

    }

    public String getData() {
        return data;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf bb = (ByteBuf) msg;
        byte[] bytes = new byte[bb.readableBytes()];
        bb.readBytes(bytes);
        data = new String(bytes);
        ChannelPromise promise=queue.poll();
        promise.setSuccess();
    }
}
