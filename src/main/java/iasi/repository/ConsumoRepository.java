package iasi.repository;

import iasi.model.Consumo;
import java.util.List;

public interface ConsumoRepository {

    int save(Consumo book);

    Consumo findById(long id);

    int deleteById(long id);

    List<Consumo> findByEquipamentoId(long idEquipamento);

    List<Consumo> findAll();

    int deleteAll();

    Float getSomaConsumoPorEquipamentoId(long idEquipamento);

}
