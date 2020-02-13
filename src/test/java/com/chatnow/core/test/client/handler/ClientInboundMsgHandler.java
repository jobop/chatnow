package com.chatnow.core.test.client.handler;

import com.alibaba.fastjson.JSON;
import com.chatnow.core.domain.enums.OutboundMsgTypeEnums;
import com.chatnow.core.domain.outboundmsg.OutboundMsg;
import com.chatnow.core.domain.outboundmsg.push.PushToGroupUserMsg;
import com.chatnow.core.domain.outboundmsg.push.PushToUserMsg;
import com.chatnow.core.domain.outboundmsg.resp.*;
import com.chatnow.core.utils.ByteBufUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by Enzo Cotter on 2020/2/12.
 */
public class ClientInboundMsgHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(JSON.toJSONString(msg));

//        OutboundMsg outboundMsg = (OutboundMsg) msg;
//        OutboundMsgTypeEnums type = OutboundMsgTypeEnums.valueOf(outboundMsg.getType());
//
//        if (type == OutboundMsgTypeEnums.RESULT_TO_SEND_TO_GROUP) {
//            outboundMsg = (SendToGroupCommandResp) outboundMsg;
//            System.out.println();
//        } else if (type == OutboundMsgTypeEnums.HEARTBEAT) {
//            outboundMsg = (HeartBeatCommandResp) outboundMsg;
//            ((HeartBeatCommandResp) outboundMsg).setResultCode(byteBuf.readInt());
//
//        } else if (type == OutboundMsgTypeEnums.PUSH_GROUP_MSG_TO_USER) {
//            outboundMsg = (PushToGroupUserMsg) outboundMsg;
//
//            ((PushToGroupUserMsg) outboundMsg).setGroupName(ByteBufUtils.readNextTagValue(byteBuf, charset));
//            ((PushToGroupUserMsg) outboundMsg).setSenderName(ByteBufUtils.readNextTagValue(byteBuf, charset));
//            ((PushToGroupUserMsg) outboundMsg).setContent(ByteBufUtils.readNextTagValue(byteBuf, charset));
//        } else if (type == OutboundMsgTypeEnums.PUSH_TO_USER) {
//            outboundMsg = (PushToUserMsg) outboundMsg;
//            ((PushToUserMsg) outboundMsg).setSenderName(ByteBufUtils.readNextTagValue(byteBuf, charset));
//            ((PushToUserMsg) outboundMsg).setContent(ByteBufUtils.readNextTagValue(byteBuf, charset));
//
//        } else if (type == OutboundMsgTypeEnums.RESULT_TO_CREATE_GROUP) {
//            outboundMsg = (CreateGroupCommandResp) outboundMsg;
//            ((CreateGroupCommandResp) outboundMsg).setResultCode(byteBuf.readInt());
//
//        } else if (type == OutboundMsgTypeEnums.RESULT_TO_JOIN_GROUP) {
//            outboundMsg = (JoinGroupCommandResp) outboundMsg;
//            ((JoinGroupCommandResp) outboundMsg).setResultCode(byteBuf.readInt());
//
//        } else if (type == OutboundMsgTypeEnums.RESULT_TO_LOGIN) {
//            outboundMsg = (LoginCommandResp) outboundMsg;
//            ((LoginCommandResp) outboundMsg).setResultCode(byteBuf.readInt());
//            ((LoginCommandResp) outboundMsg).setAuthToken(ByteBufUtils.readNextTagValue(byteBuf, charset));
//        } else if (type == OutboundMsgTypeEnums.RESULT_TO_REGISTER) {
//            outboundMsg = (RegiestCommandResp) outboundMsg;
//            ((RegiestCommandResp) outboundMsg).setResultCode(byteBuf.readInt());
//        } else if (type == OutboundMsgTypeEnums.RESULT_TO_SEND_TO_USER) {
//            outboundMsg = (SendToUserCommandResp) outboundMsg;
//            ((SendToUserCommandResp) outboundMsg).setResultCode(byteBuf.readInt());
//
//        } else {
//            System.out.println("未知返回值");
//            return;
//        }
    }
}
