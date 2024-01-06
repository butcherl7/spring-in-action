package top.funsite.spring.action.log.event;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.funsite.spring.action.entity.LogEntity;

@Slf4j
@Service
public class LogListener implements ApplicationListener<LogEvent> {

    @Resource
    private JdbcClient jdbcClient;

    @Override
    @Async("taskExecutor")
    public void onApplicationEvent(LogEvent event) {
        if (event.getSource() instanceof LogEntity logEntity) {
            int insert = jdbcClient.sql("""
                            insert into request_log (name, method_name, request_ip, request_uri, http_method, token,
                                                     headers, request_payload, response_result, request_time, response_time,
                                                     error, error_message, created_by)
                            values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                                                            """)
                    .param(logEntity.getName())
                    .param(logEntity.getMethodName())
                    .param(logEntity.getRequestIp())
                    .param(logEntity.getRequestURI())
                    .param(logEntity.getHttpMethod())
                    .param(logEntity.getToken())
                    .param(logEntity.getHeaders())
                    .param(logEntity.getRequestPayload())
                    .param(logEntity.getResponseResult())
                    .param(logEntity.getRequestTime())
                    .param(logEntity.getResponseTime())
                    .param(logEntity.getError())
                    .param(logEntity.getErrorMessage())
                    .param(logEntity.getCreatedBy())
                    .update();
            log.info("insert log {}.", insert);
        }
    }
}
