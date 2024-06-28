package top.funsite.spring.action.ds;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.funsite.spring.action.ds.properties.AppDataSourceProperties;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DatasourceConfig {

    @Resource
    private AppDataSourceProperties dsp;

    @Bean
    public DataSource dataSource() {
        var defaultDsName = dsp.getDefaultDsName();
        var configuredDataSources = dsp.getTargetDataSources();

        if (defaultDsName == null) {
            throw new IllegalArgumentException("default ds name can not be empty.");
        }
        if (configuredDataSources == null || configuredDataSources.isEmpty()) {
            throw new IllegalArgumentException("targetDataSources can not be empty.");
        }
        if (configuredDataSources.get(defaultDsName) == null) {
            throw new IllegalArgumentException("default datasource can not be null.");
        }

        Map<Object, Object> targetDataSources = new HashMap<>(16);
        for (var entry : configuredDataSources.entrySet()) {
            DsName key = entry.getKey();
            HikariDataSource value = entry.getValue();
            if (key == null || value == null) {
                throw new IllegalArgumentException("datasource " + key + " can not be null.");
            }
            targetDataSources.put(key, value);
        }

        var routingDataSource = new RoutingDataSource();
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(configuredDataSources.get(defaultDsName));
        return routingDataSource;
    }

}
