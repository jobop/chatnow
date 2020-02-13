package com.chatnow.core.processors.inbound;

import com.chatnow.core.dao.GroupDao;
import com.chatnow.core.domain.inboundmsg.InboundMsg;
import com.chatnow.core.domain.inboundmsg.command.JoinGroupCommand;
import com.chatnow.core.domain.inboundmsg.command.SendToGroupCommand;
import com.chatnow.core.domain.outboundmsg.push.PushToGroupUserMsg;
import com.chatnow.core.domain.outboundmsg.push.PushToUserMsg;
import com.chatnow.core.domain.outboundmsg.resp.SendToGroupCommandResp;
import com.chatnow.core.processors.InboundProcessor;
import com.chatnow.core.processors.ResultCode;
import com.chatnow.core.route.RouteManager;
import com.chatnow.core.session.Session;
import com.chatnow.core.session.SessionManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.Set;

/**
 * Created by Enzo Cotter on 2020/2/10.
 */
public class SendToGroupProcessor implements InboundProcessor {
    public void process(ChannelHandlerContext ctx, InboundMsg msg) {
        SendToGroupCommand command = (SendToGroupCommand) msg;
        String authToken = command.getToken();
        Session session = SessionManager.getInstance().getSession(authToken);

        SendToGroupCommandResp resp = new SendToGroupCommandResp();
        if (null == session) {
            //没有登录，返回错误码
            resp.setResultCode(ResultCode.NO_LOGIN.ordinal());
            ctx.channel().writeAndFlush(resp);
            return;

        }

        //查询是否在group中
        if (GroupDao.getInstance().hasJoinGroup(command.getGroupName(), (String) session.getByKey("userName"))) {
            Set<String> users = GroupDao.getInstance().queryGroupByName(command.getGroupName());
            //FIXME:这里直接扇出会有性能问题
            for (String user : users) {
                if (!user.equals(session.getByKey("userName"))) {
                    Channel subChannel = RouteManager.getInstance().getChannelByUserName(user);
                    if (null != subChannel && subChannel.isActive()) {
                        //发送给对方
                        PushToGroupUserMsg pushToGroupUserMsg = new PushToGroupUserMsg();
                        pushToGroupUserMsg.setGroupName(command.getGroupName());
                        pushToGroupUserMsg.setSenderName((String) session.getByKey("userName"));
                        pushToGroupUserMsg.setContent(command.getContent());

                        subChannel.writeAndFlush(pushToGroupUserMsg);

                    } else {
                        //TODO:写群消息让其主动拉取
                    }

                }
            }

        }
        //返回成功
        resp.setResultCode(ResultCode.SUCCESS.ordinal());
        ctx.channel().writeAndFlush(resp);
    }
}
