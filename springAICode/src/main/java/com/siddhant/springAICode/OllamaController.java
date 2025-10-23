//package com.siddhant.springAICode;
//
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
//import org.springframework.ai.chat.memory.ChatMemory;
//import org.springframework.ai.chat.memory.MessageWindowChatMemory;
//import org.springframework.ai.chat.model.ChatResponse;
//import org.springframework.ai.ollama.OllamaChatModel;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class OllamaController {
//
//
////    private OpenAiChatModel chatModel;
////
////    public OpenAIController(OpenAiChatModel chatModel){
////        this.chatModel=chatModel;
////    }
//
//    private ChatClient chatClient;
//
//    private ChatMemory chatMemory = MessageWindowChatMemory.builder().build();
//
//    //Use it when dealing with multiple models
//    public OllamaController(OllamaChatModel chatModel){
//        this.chatClient=ChatClient.create(chatModel);
//    }
//
////    public OllamaController (OllamaChatModel chatModel){
////        this.chatClient = builder
////                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
////                .build();
////    }
//
//
//
////    @GetMapping("/api/{message}")
//    public ResponseEntity<String> getAnswer(@PathVariable String message){
//        ChatResponse chatResponse = chatClient
//                .prompt(message)
//                .call()
//                .chatResponse();
//        if(chatResponse==null){
//            return new ResponseEntity<>("Something went wrong while processing your request", HttpStatus.BAD_GATEWAY);
//        }
//        System.out.println(chatResponse.getMetadata().getModel());
//        String response = chatResponse.getResult().getOutput().getText();
//        return ResponseEntity.ok(response);
// spring.ai.ollama.chat.options.model=llama3.2:latest add this to application properties
//    }
//}
