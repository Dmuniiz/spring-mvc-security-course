ALTER TABLE consultas DROP medico_id;

ALTER TABLE consultas
ADD column medico_id bigint;

ALTER TABLE medicos
ALTER COLUMN id TYPE bigint;

alter table consultas ADD CONSTRAINT fk_consultas_medico_id
FOREIGN KEY (medico_id) REFERENCES medicos(id);