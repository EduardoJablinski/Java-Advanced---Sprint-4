package iasi.controller;

import iasi.model.Empresa;
import iasi.model.Equipamento;
import iasi.repository.EmpresaRepository;
import iasi.repository.EquipamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class EmpresaController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    EmpresaRepository empresaRepository;

    @Autowired
    EquipamentoRepository equipamentoRepository;

    @GetMapping("/empresas")
    public String getAllEmpresas(@RequestParam(required = false) String nome, Model model) {
        List<Empresa> empresas = nome == null ?
                empresaRepository.findAll() :
                empresaRepository.findByNameContaining(nome.trim());

        model.addAttribute("empresas", empresas);
        model.addAttribute("pageTitle",
                messageSource.getMessage("empresa.lista.titulo", null, LocaleContextHolder.getLocale()));
        return "empresas";
    }

    @GetMapping("/empresas/{id}")
    public String getEmpresaById(@PathVariable("id") long id, Model model) {
        Empresa empresa = empresaRepository.findById(id);
        if (empresa == null) {
            return "redirect:/admin/empresas"; // Redireciona de volta se a empresa não for encontrada
        }
        model.addAttribute("empresa", empresa);
        List<Equipamento> equipamentos = equipamentoRepository.findByEmpresaId(id);
        model.addAttribute("equipamentos", equipamentos);
        return "empresa-detalhes"; // Nome do template HTML para detalhes da empresa
    }

    @PostMapping("/empresas")
    public String createEmpresa(@ModelAttribute Empresa empresa) {
        empresaRepository.save(empresa);
        return "redirect:/admin/empresas"; // Redireciona para a lista de empresas
    }

    @PostMapping("/empresas/{id}")
    public String updateEmpresa(@PathVariable("id") long id, @ModelAttribute Empresa empresa) {
        Empresa _empresa = empresaRepository.findById(id);
        if (_empresa != null) {
            empresa.setId(id);
            empresaRepository.save(empresa);
        }
        return "redirect:/admin/empresas"; // Redireciona para a lista de empresas
    }

    @PostMapping("/empresas/{id}/delete")
    public String deleteEmpresa(@PathVariable("id") long id) {
        empresaRepository.deleteById(id);
        return "redirect:/admin/empresas"; // Redireciona para a lista de empresas
    }

    @GetMapping("/empresas/{id}/equipamentos")
    public String getEquipamentosByEmpresaId(@PathVariable("id") long id, Model model) {
        Empresa empresa = empresaRepository.findById(id);
        if (empresa == null) {
            return "redirect:/admin/empresas"; // Redireciona de volta se a empresa não for encontrada
        }
        model.addAttribute("empresa", empresa);
        List<Equipamento> equipamentos = equipamentoRepository.findByEmpresaId(id);
        model.addAttribute("equipamentos", equipamentos);
        return "equipamentos"; // Nome do template HTML para listar equipamentos
    }

    @PostMapping("/empresas/delete")
    public String deleteEmpresaById(@RequestParam("id") long id) {
        empresaRepository.deleteById(id);
        return "redirect:/admin/empresas"; // Redireciona para a lista de empresas
    }
}
