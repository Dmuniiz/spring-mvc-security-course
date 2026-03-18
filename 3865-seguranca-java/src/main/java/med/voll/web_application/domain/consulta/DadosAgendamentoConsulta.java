package med.voll.web_application.domain.consulta;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import med.voll.web_application.domain.medico.Especialidade;

import java.time.LocalDateTime;

public record DadosAgendamentoConsulta(

        Long id,
        Long idMedico,

        @NotNull(message = "cpf was not accepted")
        String paciente,

        @NotNull
        @Future
        LocalDateTime data,

        Especialidade especialidade) {
}
