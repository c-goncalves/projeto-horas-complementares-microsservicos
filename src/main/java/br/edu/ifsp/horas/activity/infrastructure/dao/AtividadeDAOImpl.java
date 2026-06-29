package br.edu.ifsp.horas.activity.infrastructure.dao;

import br.edu.ifsp.horas.activity.domain.dao.AtividadeDAO;
import br.edu.ifsp.horas.activity.domain.model.Atividade;
import br.edu.ifsp.horas.activity.domain.vo.CargaHoraria;
import br.edu.ifsp.horas.activity.domain.vo.RaAluno;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class AtividadeDAOImpl implements AtividadeDAO {

    private final JdbcTemplate jdbcTemplate;

    public AtividadeDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void salvar(Atividade atividade) {
        String sql = """
            INSERT INTO tb_atividade (
                codigo_acesso, ra_aluno, titulo, carga_horaria_minutos, 
                data_realizacao, descricao, status, path_comprovante
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        jdbcTemplate.update(sql,
            atividade.getCodigoAcesso(),
            atividade.getRaValor(),
            atividade.getTitulo(),
            atividade.getCargaHoraria().minutos(),
            atividade.getDataRealizacao(),
            atividade.getDescricao(),
            atividade.getStatus(),
            atividade.getPathComprovante()
        );
    }

    @Override
    public Optional<Atividade> buscarPorToken(String codigoAcesso) {
        String sql = "SELECT * FROM tb_atividade WHERE codigo_acesso = ?";
        try {
            Atividade activity = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                var ra = new RaAluno(rs.getString("ra_aluno"));
                var carga = new CargaHoraria(rs.getInt("carga_horaria_minutos"));
                
                Atividade act = new Atividade(ra, rs.getString("titulo"), carga, rs.getDate("data_realizacao").toLocalDate(), rs.getString("descricao"));
                act.setId(rs.getLong("id"));
                
                try {
                    var statusField = Atividade.class.getDeclaredField("status");
                    statusField.setAccessible(true);
                    statusField.set(act, rs.getString("status"));
                    
                    var codeField = Atividade.class.getDeclaredField("codigoAcesso");
                    codeField.setAccessible(true);
                    codeField.set(act, rs.getString("codigo_acesso"));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                act.anexarComprovante(rs.getString("path_comprovante"));
                return act;
            }, codigoAcesso);
            return Optional.ofNullable(activity);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Atividade> buscarPorId(Long id) {
        String sql = "SELECT * FROM tb_atividade WHERE id = ?";
        try {
            Atividade activity = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                var ra = new RaAluno(rs.getString("ra_aluno"));
                var carga = new CargaHoraria(rs.getInt("carga_horaria_minutos"));
                
                Atividade act = new Atividade(ra, rs.getString("titulo"), carga, rs.getDate("data_realizacao").toLocalDate(), rs.getString("descricao"));
                act.setId(rs.getLong("id"));
                
                act.reconstruirStatus(rs.getString("status"), rs.getString("justificativa"));
                try {
                    var codeField = Atividade.class.getDeclaredField("codigoAcesso");
                    codeField.setAccessible(true);
                    codeField.set(act, rs.getString("codigo_acesso"));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                
                act.anexarComprovante(rs.getString("path_comprovante"));
                return act;
            }, id);
            return Optional.ofNullable(activity);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void atualizarStatus(Atividade atividade) {
        String sql = "UPDATE tb_atividade SET status = ?, justificativa = ? WHERE id = ?";
        jdbcTemplate.update(sql, 
            atividade.getStatus(), 
            atividade.getJustificativa(), 
            atividade.getId()
        );
    }

    @Override
    public void deletar(Long id) {
        String sql = "DELETE FROM tb_atividade WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}