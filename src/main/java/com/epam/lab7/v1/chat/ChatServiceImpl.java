package com.epam.lab7.v1.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.epam.lab7.v1.common.SystemPrompt.SYSTEM_PROMPT;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final Chat chat;

    @Override
    public String chat(String message) {
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(SYSTEM_PROMPT);
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("prompt", message));
        Prompt prompt = new Prompt(List.of(systemMessage));
        return chat.aiClient().prompt(prompt).call().content();
    }
}
