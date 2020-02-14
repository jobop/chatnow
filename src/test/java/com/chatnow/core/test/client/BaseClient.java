package com.chatnow.core.test.client;

import com.chatnow.core.domain.inboundmsg.InboundMsg;
import com.chatnow.core.handlers.protobuf.ProcessorHandler;
import com.chatnow.core.handlers.protobuf.ProtobufAnyEncoder;
import com.chatnow.core.test.client.handler.ClientInboundMsgDecoder;
import com.chatnow.core.test.client.handler.ClientInboundMsgHandler;
import com.chatnow.core.test.client.handler.ClientOutboundMsgEncoder;
import com.chatnow.core.test.client.handler.ClientProcessHandler;
import com.chatnow.core.test.client.utils.CommandUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.jline.reader.*;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;

/**
 * Created by Enzo Cotter on 2020/2/14.
 */
public class BaseClient {


    static Completer commandCompleter = new StringsCompleter("regiest", "login", "sendtouser", "creategroup", "joingroup", "sendtogroup");

    private String host;
    private int port;

    public BaseClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws InterruptedException, IOException {

        Bootstrap bs = new Bootstrap();
        EventLoopGroup worker = new NioEventLoopGroup(1);
        bs.group(worker).channel(NioSocketChannel.class).handler(new ChannelInitializer() {

            protected void initChannel(Channel ch) throws Exception {
                //处理proto半包
                ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());

                //解析数据proto数据，并分发给业务
                ch.pipeline().addLast(new ClientProcessHandler());


                //输出增加proto半包标示
                ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());

                //任意类型序列化
                ch.pipeline().addLast(new ProtobufAnyEncoder());
            }

        });


        ChannelFuture future = bs.connect(host, port).sync();


        System.out.println("成功连接服务器，请输入命令\n" +
                "注册 regiest userName password \n" +
                "登录 login userName password \n" +
                "发送消息给用户 sendtouser receiveUserName content authToken\n" +
                "创建群 creategroup groupName authToken\n" +
                "加入群 joingroup groupName authToken\n" +
                "群发消息 sendtogroup groupName content autoToken\n");

        Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .build();


        LineReader lineReader = LineReaderBuilder.builder()
                .terminal(terminal).completer(commandCompleter)
                .build();


        String prompt = "client> ";
        while (true) {
            String line;
            try {
                line = lineReader.readLine(prompt);
                Object command = CommandUtils.parseCommand(line);
                if (null == command) {
                    continue;
                }

//                future.channel().writeAndFlush(command);
                future.channel().pipeline().writeAndFlush(command);
            } catch (UserInterruptException e) {
                // Do nothing
            } catch (EndOfFileException e) {
                System.out.println("\nBye.");
                return;
            }
        }
    }
}
