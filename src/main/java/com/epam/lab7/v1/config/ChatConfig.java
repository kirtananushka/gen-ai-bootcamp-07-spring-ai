package com.epam.lab7.v1.config;

import com.epam.lab7.v1.chat.Chat;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder.build();
    }

    @Bean
    public Chat chat(ChatClient chatClient, VectorStore vectorStore) {
        return new Chat(chatClient, vectorStore);
    }
}
