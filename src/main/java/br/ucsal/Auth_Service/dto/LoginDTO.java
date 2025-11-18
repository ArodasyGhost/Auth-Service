package br.ucsal.Auth_Service.dto;

import lombok.Data;

@Data
public class LoginDTO {
    private String email;
    private String senha;
}
