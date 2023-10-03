package med.voll.api.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.usuarios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //System.out.println("Este es el inicio dl filtro");
        var authHeader=request.getHeader("Authorization"); //.replace("Bearer","");
        /*
        if(token==null || token =="") {
            throw new RuntimeException("El token enviado no es valido");
        }
        */
        System.out.println("authHeader:::"+authHeader);
        if(authHeader !=null) {
            //System.out.println("Validamos que el token no e null");
            var token = authHeader.replace("Bearer ", "");
           System.out.println(token);
           // System.out.println(tokenService.getSubject(token));//Este usuario tienen session
            var subject =tokenService.getSubject(token);
            System.out.println("USUARIO:::"+subject);
            if(subject!=null){
                //
                var usuario=usuarioRepository.findByLogin(subject);
                var authentication=new UsernamePasswordAuthenticationToken(usuario,null,usuario.getAuthorities());//forzamos inicio de session
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
