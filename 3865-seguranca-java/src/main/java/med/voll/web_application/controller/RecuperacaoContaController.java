package med.voll.web_application.controller;

import med.voll.web_application.domain.RegraDeNegocioException;
import med.voll.web_application.domain.usuario.DadosRecuperacaoConta;
import med.voll.web_application.domain.usuario.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RecuperacaoContaController {

    private static final String FORMULARIO_RECUPERAR_SENHA = "auth/formulario-recuperacao-senha";
    private static final String FORMULARIO_RECUPERAR_CONTA = "auth/formulario-recuperacao-conta";

    private final UsuarioService service;

    public RecuperacaoContaController(UsuarioService usuarioService) {
        this.service = usuarioService;
    }

    @GetMapping("/esqueci-minha-senha")
    public String esqueciMinhaSenhaForm(){
        return FORMULARIO_RECUPERAR_SENHA;
    }

    @PostMapping("/esqueci-minha-senha")
    public String enviarEmailRecuperacaoToken(String email, Model model){
        try{
            service.enviarToken(email);
            return "redirect:esqueci-minha-senha?verificar";
        }catch (RegraDeNegocioException e){
            model.addAttribute("erro", e.getMessage());
            return FORMULARIO_RECUPERAR_SENHA;
        }
    }

    @GetMapping("/recuperar-conta")
    public String alterarSenhaForm(@RequestParam(name = "codigo", required = false) String codigo, Model model){
        if(codigo != null)
            model.addAttribute("codigo", codigo);
        return FORMULARIO_RECUPERAR_CONTA;
    }

    @PostMapping("/recuperar-conta")
    public String alterarSenha(@RequestParam(name = "codigo", required = false) String codigo, Model model, DadosRecuperacaoConta dados){
        try {
            service.recuperarConta(codigo, dados);
            return "redirect:login?recuperacaoSucesso";
        }catch (RegraDeNegocioException e){
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("codigo", codigo);
            return FORMULARIO_RECUPERAR_CONTA;
        }
    }

}
