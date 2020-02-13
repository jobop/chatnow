package com.chatnow.core.handlers;

import com.chatnow.core.domain.enums.InboundMsgTypeEnums;
import com.chatnow.core.domain.inboundmsg.InboundMsg;
import com.chatnow.core.domain.inboundmsg.command.*;
import com.chatnow.core.domain.inboundmsg.resp.ResultFromUserReceivedMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.Buffer;
import java.nio.charset.Charset;

/**
 */
public class InboundMsgDecoder extends ChannelInboundHandlerAdapter {

    Charset charset = Charset.forName("UTF-8");

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf byteBuf = (ByteBuf) msg;
        try {
            //获取版本号
            int version = byteBuf.readInt();
            //获取业务type
            int bizType = byteBuf.readInt();

            //根据不同的type获取命令内容，传给下一个handler

            InboundMsg inboundMsg = parseMsg(version, bizType, byteBuf);

            ctx.fireChannelRead(inboundMsg);
        } finally {
            //这里byteBuf不再通过fireChannelRead传递下去，tail handler无法帮助释放，所以要自己释放
            byteBuf.release();
        }
    }

    private InboundMsg parseMsg(int version, int bizType, ByteBuf byteBuf) {
        InboundMsg msg = null;
        InboundMsgTypeEnums type = InboundMsgTypeEnums.valueOf(bizType);


        if (type == InboundMsgTypeEnums.REGISTER) {
            msg = new RegiestCommand();
            int nameLength = byteBuf.readInt();
            String name = parseStr(byteBuf, nameLength);
            ((RegiestCommand) msg).setUserName(name);
            int pwdLength = byteBuf.readInt();
            String pwd = parseStr(byteBuf, pwdLength);
            ((RegiestCommand) msg).setPwd(pwd);

        } else if (type == InboundMsgTypeEnums.LOGIN) {
            msg = new LoginCommand();
            int nameLength = byteBuf.readInt();
            String name = parseStr(byteBuf, nameLength);
            ((LoginCommand) msg).setUserName(name);
            int pwdLength = byteBuf.readInt();
            String pwd = parseStr(byteBuf, pwdLength);
            ((LoginCommand) msg).setPwd(pwd);

        } else if (type == InboundMsgTypeEnums.SEND_TO_USER) {
            msg = new SendToUserCommand();
            int nameLength = byteBuf.readInt();
            String name = parseStr(byteBuf, nameLength);
            ((SendToUserCommand) msg).setReceiveUserName(name);
            int contentLength = byteBuf.readInt();
            String content = parseStr(byteBuf, contentLength);
            ((SendToUserCommand) msg).setContent(content);


            int tokenLength = byteBuf.readInt();
            String token = parseStr(byteBuf, tokenLength);
            ((SendToUserCommand) msg).setToken(token);

        } else if (type == InboundMsgTypeEnums.RESULT_FROM_USER_RECEIVED_MSG) {
            msg = new ResultFromUserReceivedMsg();
            ((ResultFromUserReceivedMsg) msg).setResultCode(byteBuf.readInt());

        } else if (type == InboundMsgTypeEnums.CREATE_GROUP) {
            msg = new CreateGroupCommand();
            int nameLength = byteBuf.readInt();
            String groupName = parseStr(byteBuf, nameLength);
            ((CreateGroupCommand) msg).setGroupName(groupName);

            int tokenLength = byteBuf.readInt();
            String token = parseStr(byteBuf, tokenLength);
            ((CreateGroupCommand) msg).setToken(token);

        } else if (type == InboundMsgTypeEnums.JOIN_GROUP) {
            msg = new JoinGroupCommand();
            int nameLength = byteBuf.readInt();
            String groupName = parseStr(byteBuf, nameLength);
            ((JoinGroupCommand) msg).setGroupName(groupName);


            int tokenLength = byteBuf.readInt();
            String token = parseStr(byteBuf, tokenLength);
            ((JoinGroupCommand) msg).setToken(token);

        } else if (type == InboundMsgTypeEnums.SEND_TO_GROUP) {
            msg = new SendToGroupCommand();
            int nameLength = byteBuf.readInt();
            String groupName = parseStr(byteBuf, nameLength);
            ((SendToGroupCommand) msg).setGroupName(groupName);

            int contentLength = byteBuf.readInt();
            String content = parseStr(byteBuf, contentLength);
            ((SendToGroupCommand) msg).setContent(content);


            int tokenLength = byteBuf.readInt();
            String token = parseStr(byteBuf, tokenLength);
            ((SendToGroupCommand) msg).setToken(token);

        } else if (type == InboundMsgTypeEnums.HEARTBEAT) {
            msg = new HeartBeatCommand();

        } else {
            return null;
        }
        msg.setVersion(version);
        msg.setType(type);
        return msg;
    }

    private String parseStr(ByteBuf byteBuf, int strLength) {
        CharSequence sequence = byteBuf.readCharSequence(strLength, charset);
        return sequence.toString();
    }
}
