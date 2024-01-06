package top.funsite.spring.action.log.event;

import org.springframework.context.ApplicationEvent;
import top.funsite.spring.action.entity.LogEntity;

import java.io.Serial;

public class LogEvent extends ApplicationEvent {

    @Serial
    private static final long serialVersionUID = -816434959136079123L;

    public LogEvent(LogEntity logEntity) {
        super(logEntity);
    }
}
