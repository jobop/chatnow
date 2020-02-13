package com.chatnow.core.test.client.handler;

import com.chatnow.core.domain.enums.OutboundMsgTypeEnums;
import com.chatnow.core.domain.outboundmsg.OutboundMsg;
import com.chatnow.core.domain.outboundmsg.push.PushToGroupUserMsg;
import com.chatnow.core.domain.outboundmsg.push.PushToUserMsg;
import com.chatnow.core.domain.outboundmsg.resp.*;
import com.chatnow.core.utils.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

/**
 * Created by Enzo Cotter on 2020/2/12.
 */
public class ClientInboundMsgDecoder extends ChannelInboundHandlerAdapter {
    private Charset charset = Charset.forName("UTF-8");

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf byteBuf = (ByteBuf) msg;
        try {
            //序列化返回结果
            //获取版本号
            int version = byteBuf.readInt();
            //获取业务type
            int bizType = byteBuf.readInt();
            OutboundMsgTypeEnums type = OutboundMsgTypeEnums.valueOf(bizType);

            OutboundMsg outboundMsg = null;

            if (type == OutboundMsgTypeEnums.RESULT_TO_SEND_TO_GROUP) {
                outboundMsg = new SendToGroupCommandResp();
                ((SendToGroupCommandResp) outboundMsg).setResultCode(byteBuf.readInt());
            } else if (type == OutboundMsgTypeEnums.HEARTBEAT) {
                outboundMsg = new HeartBeatCommandResp();
                ((HeartBeatCommandResp) outboundMsg).setResultCode(byteBuf.readInt());

            } else if (type == OutboundMsgTypeEnums.PUSH_GROUP_MSG_TO_USER) {
                outboundMsg = new PushToGroupUserMsg();

                ((PushToGroupUserMsg) outboundMsg).setGroupName(ByteBufUtils.readNextTagValue(byteBuf, charset));
                ((PushToGroupUserMsg) outboundMsg).setSenderName(ByteBufUtils.readNextTagValue(byteBuf, charset));
                ((PushToGroupUserMsg) outboundMsg).setContent(ByteBufUtils.readNextTagValue(byteBuf, charset));
            } else if (type == OutboundMsgTypeEnums.PUSH_TO_USER) {
                outboundMsg = new PushToUserMsg();
                ((PushToUserMsg) outboundMsg).setSenderName(ByteBufUtils.readNextTagValue(byteBuf, charset));
                ((PushToUserMsg) outboundMsg).setContent(ByteBufUtils.readNextTagValue(byteBuf, charset));

            } else if (type == OutboundMsgTypeEnums.RESULT_TO_CREATE_GROUP) {
                outboundMsg = new CreateGroupCommandResp();
                ((CreateGroupCommandResp) outboundMsg).setResultCode(byteBuf.readInt());

            } else if (type == OutboundMsgTypeEnums.RESULT_TO_JOIN_GROUP) {
                outboundMsg = new JoinGroupCommandResp();
                ((JoinGroupCommandResp) outboundMsg).setResultCode(byteBuf.readInt());

            } else if (type == OutboundMsgTypeEnums.RESULT_TO_LOGIN) {
                outboundMsg = new LoginCommandResp();
                ((LoginCommandResp) outboundMsg).setResultCode(byteBuf.readInt());
                ((LoginCommandResp) outboundMsg).setAuthToken(ByteBufUtils.readNextTagValue(byteBuf, charset));
            } else if (type == OutboundMsgTypeEnums.RESULT_TO_REGISTER) {
                outboundMsg = new RegiestCommandResp();
                ((RegiestCommandResp) outboundMsg).setResultCode(byteBuf.readInt());
            } else if (type == OutboundMsgTypeEnums.RESULT_TO_SEND_TO_USER) {
                outboundMsg = new SendToUserCommandResp();
                ((SendToUserCommandResp) outboundMsg).setResultCode(byteBuf.readInt());

            } else {
                System.out.println("服务端推送了未知类型");
                return;
            }


            ctx.fireChannelRead(outboundMsg);

        } finally {
            byteBuf.release();
        }

    }
}
