package io.lyqing64.github.superbi.service.impl;

import io.lyqing64.github.superbi.manager.agent.GeminiAssistant;
import io.lyqing64.github.superbi.service.AiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AiServiceImpl implements AiService {

    private final GeminiAssistant geminiAssistant;

    public AiServiceImpl(GeminiAssistant geminiAssistant) {
        this.geminiAssistant = geminiAssistant;
    }

    @Override
    public Object assistant(String message) {
        return geminiAssistant.chat(message);
    }

}
