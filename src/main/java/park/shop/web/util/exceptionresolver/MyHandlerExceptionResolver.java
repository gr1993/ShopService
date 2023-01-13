package park.shop.web.util.exceptionresolver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.info("500에러 처리");

        ResponseBody responseBody = ((HandlerMethod)handler).getMethodAnnotation(ResponseBody.class);
        //log.info("responseBody={}",  responseBody);

        //Rest 요청이 아닌 경우 HTML로 리턴
        if (responseBody == null) {
            request.setAttribute("message", ex.getMessage());

            return new ModelAndView("error/500");
        } else {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("message", ex.getMessage());

            try {
                String result = objectMapper.writeValueAsString(errorResult);

                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().write(result);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return new ModelAndView();
        }
    }
}
