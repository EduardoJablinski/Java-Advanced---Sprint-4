package iasi.controller;

import iasi.model.Consumo;
import iasi.model.Equipamento;
import iasi.repository.ConsumoRepository;
import iasi.repository.EquipamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consumo")
public class ConsumoController {

    @Autowired
    ConsumoRepository consumoRepository;

    @Autowired
    EquipamentoRepository equipamentoRepository;


    @GetMapping("/{idEquipamento}")
    public ResponseEntity<List<Consumo>> getConsumoByEquipamentoId(@PathVariable("idEquipamento") long id) {
        List<Consumo> consumos = consumoRepository.findByEquipamentoId(id);
        if (consumos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(consumos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addConsumo(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") java.util.Date dataConsumo,
                                             @RequestParam Float quantidadeConsumo,
                                             @RequestParam String tipoEnergiaConsumo,
                                             @RequestParam Float emissaoGasConsumo,
                                             @RequestParam long equipamentoId) {
        Equipamento optionalEquipamento = equipamentoRepository.findById(equipamentoId);
        // Converte java.util.Date para java.sql.Date
        java.sql.Date sqlDate = new java.sql.Date(dataConsumo.getTime());

        Consumo consumo = new Consumo(
                optionalEquipamento,
                sqlDate,
                quantidadeConsumo,
                tipoEnergiaConsumo,
                emissaoGasConsumo
        );
        consumoRepository.save(consumo);

        return new ResponseEntity<>("Consumo adicionado.", HttpStatus.CREATED);
    }

    @PostMapping("/{idConsumo}")
    public ResponseEntity<String> deleteConsumo(@PathVariable("idConsumo") long id) {
        if (consumoRepository.findById(id) == null) {
            return new ResponseEntity<>("Consumo não encontrado.", HttpStatus.NOT_FOUND);
        }
        consumoRepository.deleteById(id);
        return new ResponseEntity<>("Consumo deletado.", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/soma")
    public ResponseEntity<Float> getSomaConsumoPorNomeEquipamento(@RequestParam String nomeEquipamento) {
        // Encontrar o equipamento pelo nome
        List<Equipamento> equipamentos = equipamentoRepository.findByNameContaining(nomeEquipamento);
        if (equipamentos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Equipamento equipamento = equipamentos.get(0);

        // Somar os consumos para esse equipamento
        List<Consumo> consumos = consumoRepository.findByEquipamentoId(equipamento.getIdEquipamento());
        float soma = (float) consumos.stream()
                .mapToDouble(Consumo::getQuantidadeConsumo)
                .sum();

        return new ResponseEntity<>(soma, HttpStatus.OK);
    }


}


