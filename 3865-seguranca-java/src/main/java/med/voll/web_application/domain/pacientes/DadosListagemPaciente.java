package med.voll.web_application.domain.pacientes;

import med.voll.web_application.domain.medico.Especialidade;

public record DadosListagemPaciente(String nome, String email, String cpf) {

    public DadosListagemPaciente(Paciente paciente) {
        this(paciente.getNome(), paciente.getEmail(), paciente.getSenhaCPF());
    }
}
