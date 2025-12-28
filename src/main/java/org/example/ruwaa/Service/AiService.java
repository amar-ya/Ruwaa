package org.example.ruwaa.Service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ruwaa.DTOs.WorkPostDTO;
import org.example.ruwaa.Model.Attachments;
import org.example.ruwaa.Model.Post;
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


    public String dtoPost(Post p){
       WorkPostDTO dto = new WorkPostDTO(p.getContent(),true,p.getAttachments(),p.getCategory().getName());
//        StringBuilder sb = new StringBuilder();
//
//        sb.append("Work Post Details:\n");
//        sb.append("- Content: ").append(dto.getContent()).append("\n");
//        sb.append("- Category: ").append(dto.getCategory()).append("\n");
//        if (dto.getAttachments() == null || dto.getAttachments().isEmpty()) {
//            sb.append("- Attachments: None\n");
//        } else {
//            sb.append("- Attachments:\n");
//            dto.getAttachments().forEach(a ->
//                    sb.append("  • ").append(a.getName()).append(" (")
//                            .append(a.getDate()).append(")\n")
//            );
//        }
//        System.out.println(sb.toString());
    //   return sb.toString();
        String dtoString = "Work Post Details: Content: "+
                dto.getContent()+", possible category : "+dto.getCategory()+", Attachments: ";

        if (dto.getAttachments() == null || dto.getAttachments().isEmpty()) {
           dtoString+= " none";
        } else {

           for(Attachments a: dto.getAttachments()){
               dtoString += "*"+a.getName()+"( "+
                       a.getDate()+")";
           }
        }
        System.out.println(dtoString);
        return dtoString;
    }

}
