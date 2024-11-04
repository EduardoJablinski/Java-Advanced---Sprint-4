package iasi.controller;

import iasi.model.Equipamento;
import iasi.model.Empresa;
import iasi.repository.EquipamentoRepository;
import iasi.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipamentos")
public class EquipamentoController {

    @Autowired
    EquipamentoRepository equipamentoRepository;

    @Autowired
    EmpresaRepository empresaRepository;

    @GetMapping
    public ResponseEntity<List<Equipamento>> getAllEquipamentos(@RequestParam(required = false) String nomeEquipamento) {
        List<Equipamento> equipamentos = nomeEquipamento == null ? equipamentoRepository.findAll() : equipamentoRepository.findByNameContaining(nomeEquipamento);
        if (equipamentos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(equipamentos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addEquipamento(@RequestParam String nomeEquipamento,
                                                 @RequestParam String tipoEquipamento,
                                                 @RequestParam String localizacaoEquipamento,
                                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") java.util.Date dataInstalacaoEquipamento,
                                                 @RequestParam String estadoEquipamento,
                                                 @RequestParam long empresaId) {
        Empresa optionalEmpresa = empresaRepository.findById(empresaId);
        // Converte java.util.Date para java.sql.Date
        java.sql.Date sqlDate = new java.sql.Date(dataInstalacaoEquipamento.getTime());

        Equipamento equipamento = new Equipamento(
                nomeEquipamento,
                tipoEquipamento,
                localizacaoEquipamento,
                sqlDate, // Usa java.sql.Date
                estadoEquipamento,
                optionalEmpresa
        );
        equipamentoRepository.save(equipamento);

        return new ResponseEntity<>("Equipamento adicionado.", HttpStatus.CREATED);
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> deleteEquipamento(@PathVariable("id") long id) {
        if (equipamentoRepository.findById(id) == null) {
            return new ResponseEntity<>("Equipamento não encontrado.", HttpStatus.NOT_FOUND);
        }
        equipamentoRepository.deleteById(id);
        return new ResponseEntity<>("Equipamento deletado.", HttpStatus.NO_CONTENT);
    }
}
