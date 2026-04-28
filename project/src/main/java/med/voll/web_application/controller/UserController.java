package med.voll.web_application.controller;

import jakarta.validation.Valid;
import med.voll.web_application.domain.RegraDeNegocioException;
import med.voll.web_application.domain.pacientes.DadosCadastroPaciente;
import med.voll.web_application.domain.pacientes.PacienteService;
import med.voll.web_application.domain.usuario.DadosCriarContaPaciente;
import med.voll.web_application.domain.usuario.Usuario;
import med.voll.web_application.domain.usuario.UsuarioService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    private static final String FORMULARIO_CRIAR_CONTA = "auth/formulario-criar-conta";

    private final PacienteService pacienteService;
    private final UsuarioService usuarioService;

    UserController(PacienteService pacienteService, UsuarioService usuarioService) {
        this.pacienteService = pacienteService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/criar-conta")
    public String criarContaForm(Model model) {
       model.addAttribute("dados", new DadosCriarContaPaciente(null, "", "", "", "", ""));

        return FORMULARIO_CRIAR_CONTA;
    }

    @PostMapping("/criar-conta")
    public String criarConta(@Valid @ModelAttribute("dados") DadosCriarContaPaciente dados, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("dados", dados);
            return FORMULARIO_CRIAR_CONTA;
        }

        try {
            pacienteService.criarConta(dados);
            return "redirect:/login?sucesso";
        } catch (RegraDeNegocioException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("dados", dados);
            return FORMULARIO_CRIAR_CONTA;
        }
    }

    @GetMapping("/ativar-conta")
    public String ativarConta(@RequestParam String token) {

        usuarioService.ativarConta(token);
        return "redirect:/login?ativacaoSucesso";
    }

}
