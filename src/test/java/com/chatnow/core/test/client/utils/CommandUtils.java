package com.chatnow.core.test.client.utils;

import com.chatnow.core.domain.enums.InboundMsgTypeEnums;
import com.chatnow.core.domain.inboundmsg.InboundMsg;
import com.chatnow.core.domain.inboundmsg.command.*;
import com.chatnow.core.domain.protobuf.command.*;

/**
 * Created by Enzo Cotter on 2020/2/14.
 */
public class CommandUtils {
    public static Object parseCommand(String commandLine) {

        String[] commandArray = commandLine.split("\\s+");


        if (commandArray[0].equals("regiest")) {

            Regiest.RegiestCommand command = Regiest.RegiestCommand.newBuilder().setUserName(commandArray[1]).setPwd(commandArray[2]).setType(InboundMsgTypeEnums.REGISTER.ordinal()).build();
            return command;

        } else if (commandArray[0].equals("login")) {

            Login.LoginCommand command = Login.LoginCommand.newBuilder().setUserName(commandArray[1]).setPwd(commandArray[2]).setType(InboundMsgTypeEnums.LOGIN.ordinal()).build();
            return command;

        } else if (commandArray[0].equals("sendtouser")) {
            SendToUser.SendToUserCommand command = SendToUser.SendToUserCommand.newBuilder().setReceiveUserName(commandArray[1]).setContent(commandArray[2]).setToken(commandArray[3]).setType(InboundMsgTypeEnums.SEND_TO_USER.ordinal()).build();
            return command;

        } else if (commandArray[0].equals("creategroup")) {


            CreateGroup.CreateGroupCommand command = CreateGroup.CreateGroupCommand.newBuilder().setGroupName(commandArray[1]).setToken(commandArray[2]).setType(InboundMsgTypeEnums.CREATE_GROUP.ordinal()).build();
            return command;

        } else if (commandArray[0].equals("joingroup")) {

            JoinGroup.JoinGroupCommand command = JoinGroup.JoinGroupCommand.newBuilder().setGroupName(commandArray[1]).setToken(commandArray[2]).setType(InboundMsgTypeEnums.JOIN_GROUP.ordinal()).build();
            return command;

        } else if (commandArray[0].equals("sendtogroup")) {

            SendToGroup.SendToGroupCommand command = SendToGroup.SendToGroupCommand.newBuilder().setGroupName(commandArray[1]).setContent(commandArray[2]).setToken(commandArray[3]).setType(InboundMsgTypeEnums.SEND_TO_GROUP.ordinal()).build();
            return command;

        } else {
            return null;
        }


    }
}
