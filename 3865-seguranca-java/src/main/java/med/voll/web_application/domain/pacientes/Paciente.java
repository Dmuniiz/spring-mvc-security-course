package med.voll.web_application.domain.pacientes;

import jakarta.persistence.*;
import med.voll.web_application.domain.medico.DadosCadastroMedico;
import med.voll.web_application.domain.perfil.Perfil;

@Entity
@Table(name = "pacientes")
public class Paciente {

    @Id
    private Long id;

    private String nome;
    private String email;
    private String cpf;
    private String telefone;

    @Deprecated
    public Paciente() {
    }

    public Paciente(Long userID, DadosCadastroPaciente dados) {
        this.id = userID;
        atualizarDados(this.id, dados);
    }

    public void atualizarDados(Long id, DadosCadastroPaciente dados) {
        this.nome = dados.nome();
        this.email = dados.email();
        this.cpf = dados.cpf();
        this.telefone = dados.telefone();
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenhaCPF() {
        return cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public Long getId() {
        return id;
    }

}
