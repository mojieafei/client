package com.example.client.controller.param;

import lombok.Data;

@Data
public class ChatSendMsgParam {

    private long fromUserId ;
    private long toUserId;
    private String text;
}
