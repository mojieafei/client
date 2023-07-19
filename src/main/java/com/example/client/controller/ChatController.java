package com.example.client.controller;


import com.example.client.controller.param.ChatSendMsgParam;
import com.example.client.server.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;


    @PostMapping("/sendMsgToSomeOne")
     public String sendMsgToSomeOne(@RequestBody ChatSendMsgParam chatSendMsgParam){
         return chatService.sendMsgToSomeOne(chatSendMsgParam);
     }


    @GetMapping("/getConnection")
    public Boolean getConnection(@RequestParam(value = "userId") Long userId){
        return chatService.getConnection(userId);
    }

    @GetMapping("/giveUpConnection")
    public Boolean giveUpConnection(@RequestParam(value = "userId") Long userId){
        return chatService.giveUpConnection(userId);
    }




}
