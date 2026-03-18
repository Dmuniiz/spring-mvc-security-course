package med.voll.web_application.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DadosCriarContaPaciente(Long id,
                                      @NotBlank
                                      String nome,

                                      @NotBlank
                                      @Email
                                      String email,
                                      @NotBlank
                                      String telefone,

                                      @NotBlank
                                      @Pattern(regexp = "^\\d{11}$", message = "CPF must contain 11 digits")
                                      String cpf,

                                      @NotBlank
                                      String password) { }
