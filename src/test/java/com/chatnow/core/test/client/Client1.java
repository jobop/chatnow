package com.chatnow.core.test.client;

import com.chatnow.core.domain.enums.InboundMsgTypeEnums;
import com.chatnow.core.domain.inboundmsg.InboundMsg;
import com.chatnow.core.domain.inboundmsg.command.*;
import com.chatnow.core.domain.inboundmsg.resp.ResultFromUserReceivedMsg;
import com.chatnow.core.test.client.handler.ClientInboundMsgDecoder;
import com.chatnow.core.test.client.handler.ClientInboundMsgHandler;
import com.chatnow.core.test.client.handler.ClientOutboundMsgEncoder;
import com.chatnow.core.test.client.utils.CommandUtils;
import com.sun.xml.internal.rngom.parse.host.Base;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.jline.reader.*;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import sample.netty.ClientHandler;

import java.io.IOException;

/**
 * Created by Enzo Cotter on 2020/2/12.
 */
public class Client1 {


    public static void main(String[] args) throws InterruptedException, IOException {

        BaseClient client = new BaseClient("127.0.0.1", 12350);
        client.start();

    }


}
