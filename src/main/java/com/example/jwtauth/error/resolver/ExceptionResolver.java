package com.example.jwtauth.error.resolver;

import com.example.jwtauth.error.NotValidTokenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class ExceptionResolver extends AbstractHandlerExceptionResolver {
    public void handleNotValidTokenException(HttpServletRequest request,
                                             HttpServletResponse response,
                                             NotValidTokenException ex) {
        try {
            doResolveException(request, response, null, ex);
        } catch (Exception handlerException) {
            logger.warn("", handlerException);
        }
    }

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request,
                                              HttpServletResponse response,
                                              Object handler,
                                              Exception ex) {
        if (ex instanceof NotValidTokenException) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
            try {
                response.getWriter().write("Это не аксес токен");
            } catch (IOException e) {
                logger.error("", e);
            }
            return new ModelAndView();
        }
        return null;
    }
}
