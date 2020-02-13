package com.chatnow.core.processors.inbound;

import com.chatnow.core.dao.UserDao;
import com.chatnow.core.domain.inboundmsg.InboundMsg;
import com.chatnow.core.domain.inboundmsg.command.SendToUserCommand;
import com.chatnow.core.domain.outboundmsg.push.PushToUserMsg;
import com.chatnow.core.domain.outboundmsg.resp.SendToUserCommandResp;
import com.chatnow.core.processors.InboundProcessor;
import com.chatnow.core.processors.ResultCode;
import com.chatnow.core.route.RouteManager;
import com.chatnow.core.session.Session;
import com.chatnow.core.session.SessionManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class SendToUserProcessor implements InboundProcessor {
    public void process(ChannelHandlerContext ctx, InboundMsg msg) {
        SendToUserCommand command = (SendToUserCommand) msg;
        String authToken = command.getToken();
        Session session = SessionManager.getInstance().getSession(authToken);

        SendToUserCommandResp resp = new SendToUserCommandResp();
        if (null == session) {
            //没有登录，返回错误码
            resp.setResultCode(ResultCode.NO_LOGIN.ordinal());
            ctx.channel().writeAndFlush(resp);
            return;

        }
        String userName = session.getByKey("userName");

        //看对方是否在线
        Channel receiverChannel = RouteManager.getInstance().getChannelByUserName(command.getReceiveUserName());
        if (null != receiverChannel && receiverChannel.isActive()) {
            //组装发送信息

            PushToUserMsg pushToUserMsg = new PushToUserMsg();
            pushToUserMsg.setSenderName(userName);
            pushToUserMsg.setContent(command.getContent());

            receiverChannel.writeAndFlush(pushToUserMsg);

        } else {
            //不在线的话，看下是否注册用户
            String receiver = UserDao.getInstance().queryPwdByUserName(command.getReceiveUserName());
            if (null != receiver && !receiver.equals("")) {
                //TODO:放到待发送队列里面等待对方上线拉取

            }
        }

        resp.setResultCode(ResultCode.SUCCESS.ordinal());
        ctx.channel().writeAndFlush(resp);
    }
}
