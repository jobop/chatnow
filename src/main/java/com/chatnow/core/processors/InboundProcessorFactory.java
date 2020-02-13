package com.chatnow.core.processors;

import com.chatnow.core.domain.enums.InboundMsgTypeEnums;
import com.chatnow.core.domain.inboundmsg.InboundMsg;
import com.chatnow.core.processors.inbound.*;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Enzo Cotter on 2020/2/10.
 */
public class InboundProcessorFactory {
    private static Map<Integer, InboundProcessor> m = new HashMap<Integer, InboundProcessor>();

    static {
        m.put(InboundMsgTypeEnums.CREATE_GROUP.ordinal(), new CreateGroupProcessor());
        m.put(InboundMsgTypeEnums.HEARTBEAT.ordinal(), new HeartBeatProcessor());
        m.put(InboundMsgTypeEnums.JOIN_GROUP.ordinal(), new JoinGroupProcessor());
        m.put(InboundMsgTypeEnums.LOGIN.ordinal(), new LoginProcessor());
        m.put(InboundMsgTypeEnums.REGISTER.ordinal(), new RegiestProcessor());
        m.put(InboundMsgTypeEnums.RESULT_FROM_USER_RECEIVED_MSG.ordinal(), new ResultFromUserReceivedMsgProcessor());
        m.put(InboundMsgTypeEnums.SEND_TO_GROUP.ordinal(), new SendToGroupProcessor());
        m.put(InboundMsgTypeEnums.SEND_TO_USER.ordinal(), new SendToUserProcessor());

    }


    public static void process(ChannelHandlerContext ctx, InboundMsg msg) {
        m.get(msg.getType()).process(ctx, msg);
    }
}
