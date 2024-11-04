package iasi.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;

import iasi.model.Empresa;

@Repository
public class JdbcEmpresaRepository implements EmpresaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int save(Empresa empresa) {
        return jdbcTemplate.update("INSERT INTO TB_IASI_EMPRESA (NOME_EMPRESA, SETOR_INDUSTRIAL_EMPRESA, LOCALIZACAO_EMPRESA, TIPO_EMPRESA, CONFORMIDADE_REGULAR) VALUES(?,?,?,?,?)",
                new Object[] { empresa.getNome(), empresa.getSetorIndustrial(), empresa.getLocalizacao(), empresa.getTipo(), empresa.getConformidadeRegular() });
    }

    @Override
    public int update(Empresa empresa) {
        return jdbcTemplate.update("UPDATE TB_IASI_EMPRESA SET NOME_EMPRESA=?, SETOR_INDUSTRIAL_EMPRESA=?, LOCALIZACAO_EMPRESA=?, TIPO_EMPRESA=?, CONFORMIDADE_REGULAR=? WHERE ID=?",
                new Object[] { empresa.getNome(), empresa.getSetorIndustrial(), empresa.getLocalizacao(), empresa.getTipo(), empresa.getConformidadeRegular(), empresa.getId() });
    }

    @Override
    public Empresa findById(Long id) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM TB_IASI_EMPRESA WHERE ID=?",
                    new Object[]{id},
                    (rs, rowNum) -> {
                        Empresa empresa = new Empresa();
                        empresa.setId(rs.getLong("ID"));
                        empresa.setNome(rs.getString("NOME_EMPRESA"));
                        empresa.setSetorIndustrial(rs.getString("SETOR_INDUSTRIAL_EMPRESA"));
                        empresa.setLocalizacao(rs.getString("LOCALIZACAO_EMPRESA"));
                        empresa.setTipo(rs.getString("TIPO_EMPRESA"));
                        empresa.setConformidadeRegular(rs.getString("CONFORMIDADE_REGULAR"));
                        return empresa;
                    }
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    @Override
    public int deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM TB_IASI_EMPRESA WHERE ID=?", id);
    }

    @Override
    public List<Empresa> findAll() {
        return jdbcTemplate.query("SELECT * FROM TB_IASI_EMPRESA", (rs, rowNum) -> {
            Empresa empresa = new Empresa();
            empresa.setId(rs.getLong("ID"));
            empresa.setNome(rs.getString("NOME_EMPRESA"));
            empresa.setSetorIndustrial(rs.getString("SETOR_INDUSTRIAL_EMPRESA"));
            empresa.setLocalizacao(rs.getString("LOCALIZACAO_EMPRESA"));
            empresa.setTipo(rs.getString("TIPO_EMPRESA"));
            empresa.setConformidadeRegular(rs.getString("CONFORMIDADE_REGULAR"));
            return empresa;
        });
    }

    @Override
    public List<Empresa> findByNameContaining(String nome) {
        // Use a sintaxe adequada para o LIKE com parâmetros
        String sql = "SELECT * FROM TB_IASI_EMPRESA WHERE NOME_EMPRESA LIKE ?";
        String parametro = "%" + nome + "%";

        return jdbcTemplate.query(sql, new Object[]{parametro}, (rs, rowNum) -> {
            Empresa empresa = new Empresa();
            empresa.setId(rs.getLong("ID"));
            empresa.setNome(rs.getString("NOME_EMPRESA"));
            empresa.setSetorIndustrial(rs.getString("SETOR_INDUSTRIAL_EMPRESA"));
            empresa.setLocalizacao(rs.getString("LOCALIZACAO_EMPRESA"));
            empresa.setTipo(rs.getString("TIPO_EMPRESA"));
            empresa.setConformidadeRegular(rs.getString("CONFORMIDADE_REGULAR"));
            return empresa;
        });
    }


    @Override
    public int deleteAll() {
        return jdbcTemplate.update("DELETE from TB_IASI_EMPRESA");
    }
}
