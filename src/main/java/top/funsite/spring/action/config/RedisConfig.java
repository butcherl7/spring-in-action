package top.funsite.spring.action.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import static top.funsite.spring.action.util.JsonUtils.DEFAULT_OBJECT_MAPPER;

/**
 * Redis Config.
 *
 * @see <a href="https://blog.csdn.net/weixin_43701894/article/details/130057289">Java、Redis、Jackson序列化与反序列化</a>
 * @see <a href="https://www.jb51.net/article/280590.htm#_label3">Jackson2JsonRedisSerializer 和 GenericJackson2JsonRedisSerializer 区别</a>
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        ObjectMapper mapper = DEFAULT_OBJECT_MAPPER.copy();
        mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        final StringRedisSerializer keySerializer = new StringRedisSerializer();
        final GenericJackson2JsonRedisSerializer redisSerializer = new GenericJackson2JsonRedisSerializer(mapper);

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(keySerializer);
        template.setValueSerializer(redisSerializer);
        template.setHashKeySerializer(keySerializer);
        template.setHashValueSerializer(redisSerializer);
        return template;
    }

}
