package top.funsite.spring.action;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Slf4j
@EnableAsync
@SpringBootApplication
public class SpringInActionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringInActionApplication.class, args);
    }

    /**
     * 自定义线程池
     */
    @Bean("taskExecutor")
    public Executor taskExecutor() {
        // 返回 Java 虚拟机可用的处理器数。
        int processors = Runtime.getRuntime().availableProcessors();

        log.info("Available Processors: {}.", processors);

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程池大小
        executor.setCorePoolSize(1);
        // 最大线程数
        executor.setMaxPoolSize(processors);
        // 配置队列容量，默认值为 Integer.MAX_VALUE
        executor.setQueueCapacity(99999);
        // 活跃时间
        executor.setKeepAliveSeconds(60);
        // 线程名称前缀
        executor.setThreadNamePrefix("TaskExecutor-");
        // 设置此执行程序应该在关闭时阻止的最大秒数，以便在容器的其余部分继续关闭之前等待剩余的任务完成执行
        // executor.setAwaitTerminationSeconds(60);
        // 等待所有的任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }

}
