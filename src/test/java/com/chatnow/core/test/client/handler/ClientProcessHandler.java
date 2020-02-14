package com.chatnow.core.test.client.handler;

import com.alibaba.fastjson.JSON;
import com.chatnow.core.domain.protobuf.command.*;
import com.chatnow.core.domain.protobuf.push.PushToGroup;
import com.chatnow.core.domain.protobuf.push.PushToUser;
import com.google.protobuf.Any;
import com.googlecode.protobuf.format.JsonFormat;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by Enzo Cotter on 2020/2/14.
 */
public class ClientProcessHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;


        try {
            byte[] bytes = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(bytes);
            Any any = Any.parseFrom(bytes);


            if (any.is(Login.LoginCommandResp.class)) {
                Login.LoginCommandResp resp = any.unpack(Login.LoginCommandResp.class);
                System.out.println(resp.getResultCode() + " " + resp.getAuthToken());
            } else if (any.is(Regiest.RegiestCommandResp.class)) {
                Regiest.RegiestCommandResp resp = any.unpack(Regiest.RegiestCommandResp.class);

                System.out.println(resp.getResultCode());

            } else if (any.is(SendToUser.SendToUserCommandResp.class)) {
                SendToUser.SendToUserCommandResp resp = any.unpack(SendToUser.SendToUserCommandResp.class);

                System.out.println(resp.getResultCode());

            } else if (any.is(SendToGroup.SendToGroupCommandResp.class)) {
                SendToGroup.SendToGroupCommandResp resp = any.unpack(SendToGroup.SendToGroupCommandResp.class);

                System.out.println(resp.getResultCode());

            } else if (any.is(CreateGroup.CreateGroupCommandResp.class)) {
                CreateGroup.CreateGroupCommandResp resp = any.unpack(CreateGroup.CreateGroupCommandResp.class);

                System.out.println(resp.getResultCode());

            } else if (any.is(JoinGroup.JoinGroupCommandResp.class)) {
                JoinGroup.JoinGroupCommandResp resp = any.unpack(JoinGroup.JoinGroupCommandResp.class);

                System.out.println(resp.getResultCode());

            } else if (any.is(PushToGroup.PushToGroupUserMsg.class)) {
                PushToGroup.PushToGroupUserMsg resp = any.unpack(PushToGroup.PushToGroupUserMsg.class);

                System.out.println(resp.getGroupName() + " " + resp.getSenderName() + " " + resp.getContent());

            } else if (any.is(PushToUser.PushToUserMsg.class)) {
                PushToUser.PushToUserMsg resp = any.unpack(PushToUser.PushToUserMsg.class);

                System.out.println(resp.getSenderName() + " " + resp.getContent());

            }

        } finally {
            byteBuf.release();
        }

    }

}