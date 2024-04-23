package com.example.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public ResponseEntity<Account> addAccountHandler(@RequestBody Account account) {
        if (accountService.getAccountByUsername(account.getUsername()) != null){
            return ResponseEntity.status(409).build();
        } else if (account.getUsername() == "" || account.getPassword().length() < 4) {
            return ResponseEntity.status(400).build();
        } else {
            Account addedAccount = accountService.addAccount(account);
            return ResponseEntity.status(200).body(addedAccount);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Account> LoginHandler(@RequestBody Account account) {
        Account existingAccount = accountService.getAccountByUsername(account.getUsername());
        if (existingAccount == null || !existingAccount.getPassword().equals(account.getPassword())) {
            return ResponseEntity.status(401).build();
            
        } else {
            return ResponseEntity.status(200).body(existingAccount);
        }
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> createNewMessage(@RequestBody Message message) {
        Account account = accountService.getAccountById(message.getPostedBy());
        if (message.getMessageText() == "" || message.getMessageText().length() > 255 || account == null) {
            return ResponseEntity.status(400).build();
        } else {
            Message addedMessage = messageService.createNewMessage(message);
            return ResponseEntity.status(200).body(addedMessage);
        }
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessage(@PathVariable int messageId, @RequestBody Message message) {
        Message originalMessage = messageService.getMessageById(messageId);
        if (originalMessage != null && message.getMessageText() != "" && message.getMessageText().length() < 255) {
            messageService.updateMessageById(messageId, message);
            return ResponseEntity.status(200).body(1);
        } else {
            return ResponseEntity.status(400).build();
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.status(200).body(messages);
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable int messageId) {
        Message existingMessage = messageService.getMessageById(messageId);
        return ResponseEntity.status(200).body(existingMessage);
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getAllMessagesByAccountId(@PathVariable int accountId) {
        List<Message> messages = messageService.getAllMessagesByUserId(accountId);
        return ResponseEntity.status(200).body(messages);
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable int messageId) {
        Message message = messageService.getMessageById(messageId);
        if (message != null) {
            messageService.deleteMessageById(messageId);
            return ResponseEntity.status(200).body(1);
        }
        return ResponseEntity.status(200).build();
    }

}
