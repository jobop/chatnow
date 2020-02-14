package com.chatnow.core.processors.inbound;

import com.chatnow.core.dao.UserDao;
import com.chatnow.core.domain.protobuf.command.SendToUser;
import com.chatnow.core.domain.protobuf.push.PushToUser;
import com.chatnow.core.processors.InboundProcessor;
import com.chatnow.core.processors.ResultCode;
import com.chatnow.core.route.RouteManager;
import com.chatnow.core.session.Session;
import com.chatnow.core.session.SessionManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class SendToUserProcessor implements InboundProcessor<SendToUser.SendToUserCommand> {

    @Override
    public void process(ChannelHandlerContext ctx, SendToUser.SendToUserCommand msg) {

        String authToken = msg.getToken();
        Session session = SessionManager.getInstance().getSession(authToken);

        SendToUser.SendToUserCommandResp resp = null;
        if (null == session) {
            //没有登录，返回错误码
            resp=SendToUser.SendToUserCommandResp.newBuilder().setResultCode(ResultCode.NO_LOGIN.ordinal()).build();
            ctx.channel().writeAndFlush(resp);
            return;

        }
        String userName = session.getByKey("userName");

        //看对方是否在线
        Channel receiverChannel = RouteManager.getInstance().getChannelByUserName(msg.getReceiveUserName());
        if (null != receiverChannel && receiverChannel.isActive()) {
            //组装发送信息

            PushToUser.PushToUserMsg pushToUserMsg = PushToUser.PushToUserMsg.newBuilder()
                    .setSenderName(userName)
                    .setContent(msg.getContent()).build();

            receiverChannel.writeAndFlush(pushToUserMsg);

        } else {
            //不在线的话，看下是否注册用户
            String receiver = UserDao.getInstance().queryPwdByUserName(msg.getReceiveUserName());
            if (null != receiver && !receiver.equals("")) {
                //TODO:放到待发送队列里面等待对方上线拉取

            }
        }

        resp=SendToUser.SendToUserCommandResp.newBuilder().setResultCode(ResultCode.SUCCESS.ordinal()).build();
        ctx.channel().writeAndFlush(resp);

    }
}
