DROP TABLE IF EXISTS tb_atividade;

CREATE TABLE tb_atividade (
    id BIGSERIAL PRIMARY KEY,
    codigo_acesso VARCHAR(6) NOT NULL,
    ra_aluno VARCHAR(20) NOT NULL,
    titulo VARCHAR(100) NOT NULL,
    carga_horaria_minutos INT NOT NULL,
    data_realizacao DATE NOT NULL,
    descricao TEXT NOT NULL,
    status VARCHAR(30) NOT NULL,
    path_comprovante VARCHAR(255)
);

ALTER TABLE tb_atividade ADD COLUMN IF NOT EXISTS justificativa VARCHAR(255);