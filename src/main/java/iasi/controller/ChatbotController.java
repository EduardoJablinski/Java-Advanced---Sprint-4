package iasi.controller;

import iasi.service.ChatbotService;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Component
@Controller
public class ChatbotController {
    private final ChatbotService chatbotService;
    @Getter
    private List<String> chatMessages = new ArrayList<>();

    public ChatbotController(ChatbotService chatbotService) {
        this.chatMessages = new ArrayList<>();
        this.chatbotService = chatbotService;
    }


    @PostMapping("/chat")
    public String chat(@RequestParam String userMessage,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        try {
            chatMessages.add("Você: " + userMessage);

            String response = chatbotService.obterResposta(userMessage)
                    .block();

            chatMessages.add("Chatbot: " + response);

            redirectAttributes.addFlashAttribute("chatMessages", chatMessages);
            redirectAttributes.addFlashAttribute("message", "Mensagem enviada com sucesso!");

            return "redirect:/dashboard";

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error",
                    "Ocorreu um erro ao processar sua mensagem. Por favor, tente novamente.");
            redirectAttributes.addFlashAttribute("chatMessages", chatMessages);

            return "redirect:/dashboard";
        }
    }

}