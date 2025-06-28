package com.db.crud_naruto.infra.security;

import com.db.crud_naruto.DTO.auth.AuthenticationDTO;
import com.db.crud_naruto.DTO.auth.AuthenticationResponse;
import com.db.crud_naruto.DTO.auth.RegisterDTO;
import com.db.crud_naruto.enums.Role;
import com.db.crud_naruto.exceptions.BadRequestException;
import com.db.crud_naruto.model.Usuario;
import com.db.crud_naruto.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        if(usuarioRepository.findByNome(dto.nome()).isPresent()){
            throw new BadRequestException("Nome de usuário já está em uso");
        }

        var usuario = Usuario.builder()
                .nome(dto.nome())
                .senha(passwordEncoder.encode(dto.senha()))
                .role(Role.USER).build();

        usuarioRepository.save(usuario);

        var jwtToken = tokenService.generateToken(usuario);
        return AuthenticationResponse.builder()
                .token(jwtToken).build();
    }

    public AuthenticationResponse login(AuthenticationDTO dto) {
        var person = usuarioRepository.findByNome(dto.nome())
                .orElseThrow(() -> {
                    return new UsernameNotFoundException("Não existe usuário com esse nome");
                });

        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.nome(),
                            dto.senha()
                    )
            );

            var jwtToken = tokenService.generateToken(person);
            return AuthenticationResponse.builder()
                    .token(jwtToken).build();
        } catch (AuthenticationException e){
            throw new BadRequestException("Senha incorreta");
        }
    }

}
