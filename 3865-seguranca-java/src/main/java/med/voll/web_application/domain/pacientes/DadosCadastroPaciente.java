package med.voll.web_application.domain.pacientes;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.web_application.domain.medico.Especialidade;

public record DadosCadastroPaciente(
        Long id,
        @NotBlank
        String nome,

        @NotBlank
        @Email
        String email,

        @NotBlank
        String telefone,

        @NotBlank
        @Pattern(regexp = "^\\d{11}$", message = "CPF must contain 11 digits")
        String cpf
) {
}
