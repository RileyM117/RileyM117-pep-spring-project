package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message createNewMessage(Message message) {
        return messageRepository.save(message);
    }

    public Message getMessageById(int id) {
        Optional <Message> message = messageRepository.findById(id);
        if (message.isPresent()) {
            return message.get();
        }
        return null;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    // public List<Message> getAllMessagesByUserId(int id) {
        // return messageRepository.findByPosted_By(id);
    // }

    public Message deleteMessageById(int id){
        Optional<Message> message = messageRepository.findById(id);
        if (message.isPresent()) {
            Message existingMessage = message.get();
            messageRepository.delete(existingMessage);
            return existingMessage;
        } else {
            return null;
        }
    }
}
