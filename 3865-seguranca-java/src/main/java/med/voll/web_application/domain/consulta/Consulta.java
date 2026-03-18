package med.voll.web_application.domain.consulta;

import jakarta.persistence.*;
import med.voll.web_application.domain.medico.Medico;
import med.voll.web_application.domain.pacientes.Paciente;

import java.time.LocalDateTime;

@Entity
@Table(name = "consultas")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id")
    private Medico medico;

    private LocalDateTime data;

    @Deprecated
    public Consulta(){}

    public Consulta(Medico medico, Paciente paciente, DadosAgendamentoConsulta dados) {
        cadastrarConsulta(medico, paciente, dados);
    }

    public void cadastrarConsulta(Medico medico, Paciente paciente, DadosAgendamentoConsulta dados) {
        this.medico = medico;
        this.paciente = paciente;
        this.data = dados.data();
    }

    public Long getId() {
        return id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public LocalDateTime getData() {
        return data;
    }

}
