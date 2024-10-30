package org.jullaene.walkmong_back.common.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.common.exception.ErrorType;
import org.jullaene.walkmong_back.common.exception.ResponseMessage;
import org.jullaene.walkmong_back.common.utils.JwtTokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        String token = jwtTokenUtil.extract(request);
        try {
            if (!Objects.isNull(token)) {
                verifyTokenAndSetAuthentication(token);
            }
            filterChain.doFilter(request, response);
        } catch (ServletException e ) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            ResponseMessage responseMessage = new ResponseMessage(ErrorType.INTERNAL_SERVER.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            response.getWriter().write(convertObjectToJson(responseMessage));
        } catch (JwtException e){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            ResponseMessage responseMessage = new ResponseMessage(e.getMessage(), HttpStatus.UNAUTHORIZED.value(), e.getMessage());
            response.getWriter().write(convertObjectToJson(responseMessage));
        }
    }

    private void verifyTokenAndSetAuthentication(String token){
        Authentication authentication = jwtTokenUtil.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
