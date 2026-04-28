package med.voll.web_application.domain.pacientes;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.web_application.domain.RegraDeNegocioException;
import med.voll.web_application.domain.perfil.Perfil;
import med.voll.web_application.domain.usuario.DadosCriarContaPaciente;
import med.voll.web_application.domain.usuario.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PacienteService {

    private final PacienteRepository repository;
    private final UsuarioService usuarioService;

    public PacienteService(PacienteRepository repository, UsuarioService usuarioService) {
        this.repository = repository;
        this.usuarioService = usuarioService;
    }

    @Transactional
    public void cadastrar(DadosCadastroPaciente dados) {
        if (repository.isJaCadastrado(dados.email(), dados.cpf(), dados.id())) {
            throw new RegraDeNegocioException("E-mail ou CPF já cadastrado para outro Paciente!");
        }

        if (dados.id() == null) {
            Long userId = usuarioService.saveUser(dados.nome(), dados.email(), Perfil.PACIENTE);
            repository.save(new Paciente(userId, dados));
        } else {
            var paciente = repository.findById(dados.id()).orElseThrow();
            paciente.atualizarDados(paciente.getId(), dados);
        }
    }

    public Page<DadosListagemPaciente> listar(Pageable paginacao) {
        return repository.findAll(paginacao).map(DadosListagemPaciente::new);
    }

    public DadosCadastroPaciente carregarPorId(Long id) {
        Paciente paciente = repository.findById(id).orElseThrow();
        return new DadosCadastroPaciente(paciente.getId(), paciente.getNome(), paciente.getEmail(), paciente.getSenhaCPF(), paciente.getTelefone());
    }

    @Transactional
    public void excluir(Long userId) {
        repository.deleteById(userId);
        usuarioService.deleteUser(userId);
    }

    @Transactional
    public void criarConta(DadosCriarContaPaciente dados) {
        if (repository.isJaCadastrado(dados.email(), dados.cpf(), null)) {
            throw new RegraDeNegocioException("E-mail ou CPF já cadastrado para outro Paciente!");
        }

        Long userId = usuarioService.criarContaPaciente(dados.nome(), dados.email(), dados.password(), Perfil.PACIENTE);
        repository.save(new Paciente(userId, dados));
    }
}
