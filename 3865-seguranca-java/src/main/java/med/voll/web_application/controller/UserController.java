package med.voll.web_application.controller;

import jakarta.validation.Valid;
import med.voll.web_application.domain.RegraDeNegocioException;
import med.voll.web_application.domain.pacientes.DadosCadastroPaciente;
import med.voll.web_application.domain.pacientes.PacienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private static final String FORMULARIO_CRIAR_CONTA = "auth/formulario-criar-conta";

    private final PacienteService pacienteService;

    UserController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping("/criar-conta")
    public String criarContaForm(Model model) {
       model.addAttribute("dados", new DadosCadastroPaciente(null, "", "", "", ""));

        return FORMULARIO_CRIAR_CONTA;
    }

    @PostMapping
    public String criarConta(@Valid @ModelAttribute("dados") DadosCadastroPaciente dados, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("dados", dados);
            return FORMULARIO_CRIAR_CONTA;
        }

        try {
            pacienteService.cadastrar(dados);
            return "redirect:/login?sucesso";
        } catch (RegraDeNegocioException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("dados", dados);
            return FORMULARIO_CRIAR_CONTA;
        }
    }

}
