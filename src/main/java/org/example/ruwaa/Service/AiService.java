package org.example.ruwaa.Service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AiService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.openai.com/v1/chat/completions")
            .build();

    private final ObjectMapper mapper = new ObjectMapper();

    public String askAI(String prompt){
        String body = "{\n" +
                "  \"model\": \"gpt-4o-mini\",\n" +
                "  \"messages\": [\n" +
                "    {\"role\": \"user\", \"content\": \"" + prompt.replace("\"","\\\"") + "\"}\n" +
                "  ]\n" +
                "}";

        String raw = webClient.post()
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            JsonNode json = mapper.readTree(raw);
            return json.get("choices")
                    .get(0)
                    .get("message")
                    .get("content")
                    .asText();

        } catch (Exception e) {
            return "AI parsing error: " + e.getMessage() + "\n Raw: " + raw;
        }

    }


//    public String testAI(){
//    return askAI("can you replay me with hello in arabic?");
//    }



}
