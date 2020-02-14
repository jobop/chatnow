package com.chatnow.core.domain.protobuf;

import com.chatnow.core.domain.protobuf.command.Login;
import com.chatnow.core.domain.protobuf.command.SendToUser;
import com.google.protobuf.Any;
import com.google.protobuf.Descriptors;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * Created by Enzo Cotter on 2020/2/14.
 */
public class Test {
    public static void main(String[] args) throws InvalidProtocolBufferException, NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        byte[] byteArray=Login.LoginCommand.newBuilder().setType(1).setUserName("fanxuemin").setPwd("111").build().toByteArray();

        Login.LoginCommand command=Login.LoginCommand.parseFrom(byteArray);
        System.out.println(command.getUserName());
        System.out.println(command.getPwd());
        System.out.println(command.getType());




        byte[] byteArray2= Any.pack(command).toByteArray();
        Any any2=Any.parseFrom(byteArray);
        System.out.println(any2.getTypeUrl().split("/")[1]);



        if(any2.is(Login.LoginCommand.class)){
            Login.LoginCommand command2=any2.unpack(Login.LoginCommand.class);

            System.out.println(command2.getType());
            System.out.println(command2.getUserName());
            System.out.println(command2.getPwd());
        }






    }
}
