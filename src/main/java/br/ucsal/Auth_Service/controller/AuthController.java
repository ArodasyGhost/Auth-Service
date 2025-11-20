package br.ucsal.Auth_Service.controller;

import br.ucsal.Auth_Service.dto.LoginDTO;
import br.ucsal.Auth_Service.model.entity.Usuario;
import br.ucsal.Auth_Service.model.repository.UsuarioRepository;
import br.ucsal.Auth_Service.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    @PostMapping("/register")
    public Usuario register(@RequestBody Usuario usuario) {

        // Se já existe email igual → retorna erro
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // Criptografa a senha
        String hashedPassword = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(hashedPassword);

        return usuarioRepository.save(usuario);
    }

    // --------------------------
    // LOGIN
    // --------------------------
    @PostMapping("/login")
    public String login(@RequestBody LoginDTO login) {

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        login.getEmail(),
                        login.getSenha()
                )
        );

        // Se autenticado → gerar token JWT
        return jwtUtil.gerarToken(login.getEmail());
    }
}
