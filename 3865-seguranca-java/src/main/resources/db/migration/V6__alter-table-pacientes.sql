ALTER TABLE consultas DROP paciente;

ALTER TABLE consultas
    ADD column paciente_id bigint not null;

alter table consultas ADD CONSTRAINT fk_consultas_paciente_id
FOREIGN KEY (paciente_id) REFERENCES pacientes(id);