package med.voll.web_application.domain.usuario;

import jakarta.persistence.*;
import med.voll.web_application.domain.perfil.Perfil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name="usuarios")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String senha;

    private Boolean senhaAlterada;

    @Enumerated(EnumType.STRING)
    private Perfil perfil;

    private String token;
    private LocalDateTime expiracaoToken;

    public Usuario(String nome, String email, String senha, Perfil perfil) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.perfil = perfil;
        this.senhaAlterada = false;
    }

    public Usuario() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + perfil.name()));
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public Long getId(){
        return id;
    }

    public String getNome(){
        return nome;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public void alterarSenha(String novaSenhaEncoded) {
        this.senha = novaSenhaEncoded;
    }

    public Boolean getSenhaAlterada() {
        return senhaAlterada;
    }

    public void setSenhaAlterada(Boolean senhaAlterada) {
        this.senhaAlterada = senhaAlterada;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getExpiracaoToken() {
        return expiracaoToken;
    }

    public void setExpiracaoToken(LocalDateTime localDateTime) {
        this.expiracaoToken = localDateTime;
    }
}