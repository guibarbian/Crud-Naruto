package com.db.crud_naruto.infra.security;

import com.db.crud_naruto.DTO.auth.AuthenticationDTO;
import com.db.crud_naruto.DTO.auth.AuthenticationResponse;
import com.db.crud_naruto.DTO.auth.RegisterDTO;
import com.db.crud_naruto.enums.Role;
import com.db.crud_naruto.model.Usuario;
import com.db.crud_naruto.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterDTO dto) {
        var usuario = Usuario.builder()
                .nome(dto.getNome())
                .senha(passwordEncoder.encode(dto.getSenha()))
                .role(Role.USER).build();

        usuarioRepository.save(usuario);

        var jwtToken = tokenService.generateToken(usuario);
        return AuthenticationResponse.builder()
                .token(jwtToken).build();
    }

    public AuthenticationResponse login(AuthenticationDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getNome(),
                        dto.getSenha()
                )
        );

        var person = usuarioRepository.findByNome(dto.getNome())
                .orElseThrow();
        var jwtToken = tokenService.generateToken(person);
        return AuthenticationResponse.builder()
                .token(jwtToken).build();
    }

}
