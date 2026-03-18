package med.voll.web_application.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import med.voll.web_application.domain.RegraDeNegocioException;
import med.voll.web_application.domain.usuario.DadosAlterarSenha;
import med.voll.web_application.domain.usuario.Usuario;
import med.voll.web_application.domain.usuario.UsuarioService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Controller
public class LoginController {

    private static final String FORMULARIO_ALTERAR_SENHA = "auth/formulario-alteracao-senha";

    private final UsuarioService service;

    public LoginController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping("/login")
    public String loginView(){
        return "auth/login";
    }

    @GetMapping("/logout")
    public String logout(){
        return "auth/logout";
    }

    @GetMapping("/alterar-senha")
    public String alterarSenha(){
        return "auth/formulario-alteracao-senha";
    }

    @PostMapping("/alterar-senha")
    public String cadastrarNovaSenha(@Valid @ModelAttribute("dados") DadosAlterarSenha dados,
                            BindingResult result,
                            Model model,
                            @AuthenticationPrincipal Usuario logado,
                            HttpServletRequest request) {

        if (result.hasErrors()) {
            model.addAttribute("dados", dados);
            return FORMULARIO_ALTERAR_SENHA;
        }

        try {
            service.alterarSenha(dados, logado);

            // Força o logout do usuário (PARA SABER MAIS)
            SecurityContextHolder.clearContext(); // Limpa o contexto de segurança
            // Invalida a sessão atual, deslogando o usuário
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate(); // Invalida a sessão
            }
            return "redirect:login?alteracaoSucesso";
        } catch (RegraDeNegocioException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("dados", dados);
            return FORMULARIO_ALTERAR_SENHA;
        }
    }

}
