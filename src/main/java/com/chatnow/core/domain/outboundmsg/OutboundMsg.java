package com.chatnow.core.domain.outboundmsg;

import com.chatnow.core.domain.Message;
import com.chatnow.core.domain.Serializable;
import com.chatnow.core.domain.enums.InboundMsgTypeEnums;
import io.netty.buffer.ByteBuf;


/**
 * Created by Enzo Cotter on 2020/2/11.
 */
public abstract class OutboundMsg implements Message, Serializable {
    //默认为1
    private int version = 1;

    public int getVersion() {
        return version;
    }


    public void setVersion(int version) {
        this.version = version;
    }


}
