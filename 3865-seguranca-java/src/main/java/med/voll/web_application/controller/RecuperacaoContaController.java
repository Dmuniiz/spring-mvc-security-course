package med.voll.web_application.controller;

import med.voll.web_application.domain.RegraDeNegocioException;
import med.voll.web_application.domain.usuario.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/esqueci-minha-senha")
public class RecuperacaoContaController {

    private static final String FORMULARIO_RECUPERAR_CONTA = "auth/formulario-recuperacao-senha";

    private final UsuarioService service;

    public RecuperacaoContaController(UsuarioService usuarioService) {
        this.service = usuarioService;
    }

    @GetMapping
    public String recuperacaoContaForm(){
        return FORMULARIO_RECUPERAR_CONTA;
    }

    @PostMapping
    public String enviarEmailRecuperacaoToken(String email, Model model){
        try{
            service.enviarToken(email);
            return "redirect:esqueci-minha-senha?verificado=true";
        }catch (RegraDeNegocioException e){
            model.addAttribute("erro", e.getMessage());
            return FORMULARIO_RECUPERAR_CONTA;
        }
    }


}
