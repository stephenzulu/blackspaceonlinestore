package com.example.blackspace.Chatbot;

import com.example.blackspace.Chatbot.ChatbotService.ChatbotResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    private static final Logger log = LoggerFactory.getLogger(ChatbotController.class);

    private final ChatbotService chatbotService;

    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    /**
     * Text message endpoint — handles typed and voice-transcribed messages.
     */
    @PostMapping("/message")
    public ResponseEntity<ChatbotResponse> handleMessage(@RequestBody Map<String, String> body) {
        String message = body.getOrDefault("message", "");
        log.debug("Chatbot text query: {}", message);
        ChatbotResponse response = chatbotService.processMessage(message);
        return ResponseEntity.ok(response);
    }

    /**
     * Image search endpoint — receives labels detected client-side via browser APIs
     * and matches them against products.
     */
    @PostMapping("/image")
    public ResponseEntity<ChatbotResponse> handleImage(@RequestBody Map<String, List<String>> body) {
        List<String> labels = body.getOrDefault("labels", List.of());
        log.debug("Chatbot image labels: {}", labels);
        ChatbotResponse response = chatbotService.processImageLabels(labels);
        return ResponseEntity.ok(response);
    }
}
