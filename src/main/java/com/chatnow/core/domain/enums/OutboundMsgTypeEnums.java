package com.chatnow.core.domain.enums;

/**
 * Created by Enzo Cotter on 2020/2/8.
 */
public enum OutboundMsgTypeEnums {
    RESULT_TO_REGISTER, RESULT_TO_LOGIN, RESULT_TO_SEND_TO_USER, PUSH_TO_USER, RESULT_TO_CREATE_GROUP, RESULT_TO_JOIN_GROUP, RESULT_TO_SEND_TO_GROUP, PUSH_GROUP_MSG_TO_USER, HEARTBEAT;

    public static OutboundMsgTypeEnums valueOf(int index) {
        for (OutboundMsgTypeEnums e : OutboundMsgTypeEnums.values()) {
            if (e.ordinal() == index) {
                return e;
            }
        }
        return null;
    }
}
