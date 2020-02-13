package com.chatnow.core.utils;

import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

/**
 * Created by Enzo Cotter on 2020/2/11.
 */
public class ByteBufUtils {

    public static void writeString(ByteBuf byteBuf, String str) {
        byte[] strBytes = str.getBytes();
        byteBuf.writeInt(strBytes.length);
        byteBuf.writeBytes(strBytes);

    }

    public static String readNextTagValue(ByteBuf byteBuf,Charset charset) {
        int length = byteBuf.readInt();
        String value = byteBuf.readCharSequence(length, charset).toString();
        return value;
    }
}
