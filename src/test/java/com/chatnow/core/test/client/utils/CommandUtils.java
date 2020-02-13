package com.chatnow.core.test.client.utils;

import com.chatnow.core.domain.enums.InboundMsgTypeEnums;
import com.chatnow.core.domain.inboundmsg.InboundMsg;
import com.chatnow.core.domain.inboundmsg.command.*;

/**
 * Created by Enzo Cotter on 2020/2/14.
 */
public class CommandUtils {
    public static InboundMsg parseCommand(String commandLine) {

        String[] commandArray = commandLine.split("\\s+");


        InboundMsg command = null;
        if (commandArray[0].equals("regiest")) {
            command = new RegiestCommand();
            ((RegiestCommand) command).setUserName(commandArray[1]);
            ((RegiestCommand) command).setPwd(commandArray[2]);

            command.setType(InboundMsgTypeEnums.REGISTER);
        } else if (commandArray[0].equals("login")) {
            command = new LoginCommand();
            ((LoginCommand) command).setUserName(commandArray[1]);
            ((LoginCommand) command).setPwd(commandArray[2]);
            command.setType(InboundMsgTypeEnums.LOGIN);

        } else if (commandArray[0].equals("sendtouser")) {
            command = new SendToUserCommand();
            ((SendToUserCommand) command).setReceiveUserName(commandArray[1]);
            ((SendToUserCommand) command).setContent(commandArray[2]);


            ((SendToUserCommand) command).setToken(commandArray[3]);
            command.setType(InboundMsgTypeEnums.SEND_TO_USER);

        } else if (commandArray[0].equals("creategroup")) {


            command = new CreateGroupCommand();
            ((CreateGroupCommand) command).setGroupName(commandArray[1]);

            ((CreateGroupCommand) command).setToken(commandArray[2]);
            command.setType(InboundMsgTypeEnums.CREATE_GROUP);

        } else if (commandArray[0].equals("joingroup")) {


            command = new JoinGroupCommand();
            ((JoinGroupCommand) command).setGroupName(commandArray[1]);


            ((JoinGroupCommand) command).setToken(commandArray[2]);
            command.setType(InboundMsgTypeEnums.JOIN_GROUP);

        } else if (commandArray[0].equals("sendtogroup")) {


            command = new SendToGroupCommand();
            ((SendToGroupCommand) command).setGroupName(commandArray[1]);

            ((SendToGroupCommand) command).setContent(commandArray[2]);


            ((SendToGroupCommand) command).setToken(commandArray[3]);
            command.setType(InboundMsgTypeEnums.SEND_TO_GROUP);

        } else {
            return null;
        }

        command.setVersion(1);


        return command;
    }
}
