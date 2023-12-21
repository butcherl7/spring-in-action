package top.funsite.spring.action.shiro.filter;

import org.springframework.http.HttpStatus;
import top.funsite.spring.action.domin.HttpErrorEntity;

import javax.servlet.http.HttpServletRequest;

public class PermissionDeniedFilter extends PassThruFilter {

    @Override
    protected HttpStatus getDeniedHttpStatus() {
        return HttpStatus.FORBIDDEN;
    }

    @Override
    protected Object getDeniedEntity(HttpServletRequest request) {
        return HttpErrorEntity.create(HttpStatus.FORBIDDEN, "Permission denied", request.getRequestURI());
    }
}
