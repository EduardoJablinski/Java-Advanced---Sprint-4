package iasi.controller;

import iasi.model.Consumo;
import iasi.model.Equipamento;
import iasi.model.Empresa;
import iasi.repository.ConsumoRepository;
import iasi.repository.EquipamentoRepository;
import iasi.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    @Autowired
    private ConsumoRepository consumoRepository;

    @Autowired
    private ChatbotController chatbotController;


    @GetMapping
    public String dashboard(Model model) {
        // Mantém a lógica existente
        model.addAttribute("pageTitle",
                messageSource.getMessage("app.dashboard", null, LocaleContextHolder.getLocale()));

        // Adiciona as mensagens do chat se existirem no flash attribute
        if (!model.containsAttribute("chatMessages")) {
            model.addAttribute("chatMessages", chatbotController.getChatMessages());
        }

        return "dashboard";
    }


    @GetMapping("/empresas")
    public String empresas(Model model) {
        List<Empresa> empresas = empresaRepository.findAll();
        model.addAttribute("empresas", empresas);
        model.addAttribute("pageTitle",
                messageSource.getMessage("empresa.lista.titulo", null, LocaleContextHolder.getLocale()));
        return "empresas";
    }


    @GetMapping("/equipamentos")
    public String equipamentos(Model model) {
        List<Equipamento> equipamentos = equipamentoRepository.findAll();
        model.addAttribute("equipamentos", equipamentos);
        model.addAttribute("pageTitle",
                messageSource.getMessage("equipamento.lista.titulo", null, LocaleContextHolder.getLocale()));
        return "equipamentos";
    }

    @GetMapping("/consumos")
    public String consumos(Model model) {
        List<Consumo> consumos = consumoRepository.findAll();
        model.addAttribute("consumos", consumos);
        model.addAttribute("pageTitle",
                messageSource.getMessage("consumo.lista.titulo", null, LocaleContextHolder.getLocale()));
        return "consumos";
    }

}
