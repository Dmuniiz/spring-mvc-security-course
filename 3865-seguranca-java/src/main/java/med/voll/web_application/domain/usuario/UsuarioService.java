package med.voll.web_application.domain.usuario;

import org.apache.catalina.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private PasswordEncoder encoder;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }

    public Long saveUser(String nome, String email, String senha) {
        String passwordEncoded = encoder.encode(senha);
        Usuario usuario = usuarioRepository.save(new Usuario(nome, email, passwordEncoded));
        usuarioRepository.save(usuario);

        return usuario.getId();
    }

    public void deleteUser(Long userId) {
        usuarioRepository.deleteById(userId);
    }
}
