package com.example.client.controller;

//import com.example.server.gen.proto.TestGrpc;
import com.example.client.controller.param.ChatSendMsgParam;
import com.example.client.server.ChatService;
import com.example.server.gen.proto.ChatGrpcService;
import com.example.server.gen.proto.ChatServiceGrpc;
import com.example.server.gen.proto.SendMessageRequest;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ChatController {

    @Resource
    private ChatService chatService;


    @PostMapping("sendMsgToSomeOne")
     public String sendMsgToSomeOne(@RequestBody ChatSendMsgParam chatSendMsgParam){
         return chatService.sendMsgToSomeOne(chatSendMsgParam);
     }




}
