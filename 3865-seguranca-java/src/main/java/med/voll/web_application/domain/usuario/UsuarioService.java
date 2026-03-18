package med.voll.web_application.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import med.voll.web_application.domain.RegraDeNegocioException;
import med.voll.web_application.domain.perfil.Perfil;
import med.voll.web_application.domain.usuario.email.EmailService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private PasswordEncoder encoder;
    private final EmailService emailService;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder encoder, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.encoder = encoder;
        this.emailService = emailService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }

    public Long saveUser(String nome, String email, Perfil perfil) {
        String primeiroAcessoSenha = UUID.randomUUID().toString().substring(0, 8);
        String passwordEncoded = encoder.encode(primeiroAcessoSenha);
        Usuario usuario = usuarioRepository.save(new Usuario(nome, email, true, passwordEncoded, perfil));

        usuarioRepository.save(usuario);

        emailService.sendEmailRandomPassword(usuario, primeiroAcessoSenha);

        return usuario.getId();
    }

    public void deleteUser(Long userId) {
        usuarioRepository.deleteById(userId);
    }

    public void alterarSenha(DadosAlterarSenha dados, Usuario logado) {
        if (!encoder.matches(dados.senhaAtual(), logado.getPassword())) {
            throw new RegraDeNegocioException("Senha atual incorreta");
        }

        if (!dados.novaSenha().equals(dados.novaSenhaConfirmacao())) {
            throw new RegraDeNegocioException("Nova senha e confirmação de senha não coincidem");
        }

        String novaSenhaEncoded = encoder.encode(dados.novaSenha());
        logado.alterarSenha(novaSenhaEncoded);
        logado.setSenhaAlterada(true);
        usuarioRepository.save(logado);
    }

    public void enviarToken(String email){
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email).orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado com email: " + email));

        String token = UUID.randomUUID().toString();
        usuario.setToken(token);
        usuario.setExpiracaoToken(LocalDateTime.now().plusHours(15));

        usuarioRepository.save(usuario);

        emailService.sendEmailChangePassword(usuario);
    }

    public void recuperarConta(String codigo, DadosRecuperacaoConta dados) {

        Usuario usuario = usuarioRepository.findByTokenIgnoreCase(codigo).orElseThrow(() -> new RegraDeNegocioException("Token inválido"));

        if (usuario.getExpiracaoToken() == null || usuario.getExpiracaoToken().isBefore(LocalDateTime.now())) {
            throw new RegraDeNegocioException("Link expirado");
        }

        if (!dados.novaSenha().equals(dados.novaSenhaConfirmacao())) {
            throw new RegraDeNegocioException("Nova senha e confirmação de senha não coincidem");
        }

        String novaSenhaEncoded = encoder.encode(dados.novaSenha());
        usuario.alterarSenha(novaSenhaEncoded);
        usuario.setSenhaAlterada(true);
        usuario.setToken(null);
        usuario.setExpiracaoToken(null);

        usuarioRepository.save(usuario);
    }

    public Long criarContaPaciente(String nome,String email, String password, Perfil perfil) {
        String passwordEncoded = encoder.encode(password);
        Usuario usuario = usuarioRepository.save(new Usuario(nome, email, passwordEncoded, perfil));

        String token = UUID.randomUUID().toString();
        usuario.setToken(token);
        usuario.setExpiracaoToken(LocalDateTime.now().plusHours(15));

        usuarioRepository.save(usuario);
        emailService.sendEmailActiveAccount(usuario);

        return usuario.getId();
    }

    public void ativarConta(String token) {
        var usuario = usuarioRepository.findByTokenIgnoreCase(token).orElseThrow(() -> new RegraDeNegocioException("Token inválido"));

        if (usuario.getExpiracaoToken() == null || usuario.getExpiracaoToken().isBefore(LocalDateTime.now())) {
            throw new RegraDeNegocioException("Link expirado");
        }

        usuario.setEnabled(true);
        usuario.setToken(null);
        usuario.setExpiracaoToken(null);

        usuarioRepository.save(usuario);
    }
}
