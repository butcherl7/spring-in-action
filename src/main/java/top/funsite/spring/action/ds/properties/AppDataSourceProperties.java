package top.funsite.spring.action.ds.properties;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import top.funsite.spring.action.ds.DsName;

import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.datasource")
public class AppDataSourceProperties {

    /**
     * 默认数据源名称。
     */
    private DsName defaultDsName;

    /**
     * 数据源名称对应 DataSource 实例的映射。
     */
    private Map<DsName, HikariDataSource> targetDataSources;
}
