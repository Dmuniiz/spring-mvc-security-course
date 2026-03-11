package med.voll.web_application.domain.usuario;

import med.voll.web_application.domain.perfil.Perfil;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private PasswordEncoder encoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder encoder) {
        this.usuarioRepository = usuarioRepository;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }

    public Long saveUser(String nome, String email, String senha, Perfil perfil) {
        String passwordEncoded = encoder.encode(senha);
        Usuario usuario = usuarioRepository.save(new Usuario(nome, email, passwordEncoded, perfil));
        usuarioRepository.save(usuario);

        return usuario.getId();
    }

    public void deleteUser(Long userId) {
        usuarioRepository.deleteById(userId);
    }

    public void alterarSenha(DadosAlterarSenha dados, Usuario logado) {
        if (!encoder.matches(dados.senhaAtual(), logado.getPassword())) {
            throw new RuntimeException("Senha atual incorreta");
        }

        if (!dados.novaSenha().equals(dados.novaSenhaConfirmacao())) {
            throw new RuntimeException("Nova senha e confirmação de senha não coincidem");
        }

        String novaSenhaEncoded = encoder.encode(dados.novaSenha());
        logado.alterarSenha(novaSenhaEncoded);
        logado.setSenhaAlterada(true);
        usuarioRepository.save(logado);
    }

}
