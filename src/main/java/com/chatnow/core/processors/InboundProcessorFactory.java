package com.chatnow.core.processors;

import com.chatnow.core.domain.enums.InboundMsgTypeEnums;
import com.chatnow.core.domain.inboundmsg.InboundMsg;
import com.chatnow.core.domain.protobuf.command.*;
import com.chatnow.core.processors.inbound.*;
import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Enzo Cotter on 2020/2/10.
 */
public class InboundProcessorFactory {
    private static Map<String, Pair> m2 = new HashMap<String, Pair>();


    static {


        m2.put(CreateGroup.CreateGroupCommand.getDescriptor().getFullName(), Pair.CreateGroup);
        m2.put(HeartBeat.HeartBeatCommand.getDescriptor().getFullName(), Pair.HeartBeat);
        m2.put(JoinGroup.JoinGroupCommand.getDescriptor().getFullName(), Pair.JoinGroup);
        m2.put(Login.LoginCommand.getDescriptor().getFullName(), Pair.Login);
        m2.put(Regiest.RegiestCommand.getDescriptor().getFullName(), Pair.Regiest);
//        m2.put(CreateGroup.CreateGroupCommand.getDescriptor().getFullName(), new ResultFromUserReceivedMsgProcessor());
        m2.put(SendToGroup.SendToGroupCommand.getDescriptor().getFullName(), Pair.SendToGroup);
        m2.put(SendToUser.SendToUserCommand.getDescriptor().getFullName(), Pair.SendToUser);


    }

    enum Pair {
        CreateGroup(CreateGroup.CreateGroupCommand.class, new CreateGroupProcessor()),
        HeartBeat(HeartBeat.HeartBeatCommand.class, new HeartBeatProcessor()),
        JoinGroup(JoinGroup.JoinGroupCommand.class, new JoinGroupProcessor()),
        Login(Login.LoginCommand.class, new LoginProcessor()),
        Regiest(Regiest.RegiestCommand.class, new RegiestProcessor()),
        //        CREATE_GROUP(CreateGroup.CreateGroupCommand.class, new ResultFromUserReceivedMsgProcessor()),
        SendToGroup(SendToGroup.SendToGroupCommand.class, new SendToGroupProcessor()),
        SendToUser(SendToUser.SendToUserCommand.class, new SendToUserProcessor()),


        ;

        private Pair(Class clazz, InboundProcessor processor) {
            this.clazz = clazz;
            this.processor = processor;
        }

        Class clazz;
        InboundProcessor processor;

    }

    public static void main(String[] args) {
        System.out.println(SendToUser.SendToUserCommand.class.getName());
    }


    public static void process(ChannelHandlerContext ctx, Any msg) {
        Pair pair = m2.get(msg.getTypeUrl().split("/")[1]);

        Object command = null;
        try {
            command = msg.unpack(pair.clazz);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return;
        }

        pair.processor.process(ctx, command);
    }
}
