package com.siddhant.springAICode;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class OpenAIController {

//    @Autowired
//    private OpenAiChatModel chatModel;
//
//    public OpenAIController(OpenAiChatModel chatModel){
//        this.chatModel=chatModel;
//    }

    private ChatClient chatClient;

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    @Qualifier("openAiEmbeddingModel")
    private EmbeddingModel embeddingModel;

    public OpenAIController(OpenAiChatModel chatModel){
        this.chatClient=ChatClient.create(chatModel);
    }
//    private ChatMemory chatMemory = MessageWindowChatMemory.builder().build();

    //Use it when dealing with multiple models


    //Use it only when dealing with a single model, NOTE: Constructor injection takes prior to field injection
//    public OpenAIController (ChatClient.Builder builder, ChatMemory chatMemory){
//        this.chatClient = builder
//                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
//                .build();
//    }

//    public OpenAIController (ChatClient.Builder builder){
//        this.chatClient = builder
//                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
//                .build();
//    }



    @GetMapping("/api/{message}")
    public ResponseEntity<String> getAnswer(@PathVariable String message){
        ChatResponse chatResponse = chatClient
                .prompt(message)
                .call()
                .chatResponse();
        if(chatResponse==null){
            return new ResponseEntity<>("Something went wrong while processing your request",HttpStatus.BAD_GATEWAY);
        }
        System.out.println(chatResponse.getMetadata().getModel());
        String response = chatResponse.getResult().getOutput().getText();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/recommend")
    public String recommend(@RequestParam String type, @RequestParam String year, @RequestParam String language){

        String tempt = """
                Suggest me one good {language} movie with good rating of type {type}
                released around the year {year}. Also tell me the cast and length of the movie.
                """;
        PromptTemplate promptTemplate= new PromptTemplate(tempt);
        Prompt prompt = promptTemplate.create(Map.of("type", type, "year", year, "language", language));
        System.out.println(prompt.getContents());
        String response = chatClient
                .prompt(prompt)
                .call()
                .content();
        return response;
    }

    @PostMapping("/api/embedding")
    public float[] embedding(@RequestParam String text){
        return embeddingModel.embed(text);
    }

    @PostMapping("/api/similarity")
    public double getSimilarity(@RequestParam String text1, @RequestParam String text2){

        float[] embedding1 = embeddingModel.embed(text1);
        float[] embedding2 = embeddingModel.embed(text2);

        double dotProduct=0;
        double norm1 =0;
        double norm2=0;

        for(int i=0;i<embedding1.length; i++){
            dotProduct += embedding1[i]*embedding2[i];
            norm1 += Math.pow(embedding1[i],2);
            norm2 += Math.pow(embedding2[i],2);
        }
        return dotProduct*100/(Math.sqrt(norm1)*Math.sqrt(norm2));
    }

    @PostMapping("/api/product")
    public List<Document> getProducts(@RequestParam String text){

          return vectorStore.similaritySearch(SearchRequest.builder().query(text).topK(2).build());
//        return vectorStore.similaritySearch(text);
    }
}
