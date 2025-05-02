package io.lyqing64.github.superbi.config;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import io.lyqing64.github.superbi.listener.FileChatModelListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Configuration
public class AssistantConfiguration {

    @Bean
    @Scope(SCOPE_PROTOTYPE)
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.withMaxMessages(10);
    }


    /**
     * This listener will be injected into every {@link ChatLanguageModel} and {@link StreamingChatLanguageModel}
     * bean found in the application context.
     * It will listen for {@link ChatLanguageModel} in the {@link ChatLanguageModelController} as well as
     * {@link Assistant} and {@link StreamingAssistant}.
     */
    @Bean
    ChatModelListener chatModelListener() {
        return new FileChatModelListener();
    }
}