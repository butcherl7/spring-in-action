package top.funsite.spring.action.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class JacksonUtils {

    /**
     * ObjectMapper，默认配置了：
     * <ul>
     *     <li>Register JavaTimeModule</li>
     *     <ol>
     *         <li>{@link LocalDate} &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;格式: yyyy-MM-dd</li>
     *         <li>{@link LocalDateTime} 格式: yyyy-MM-dd HH:mm:ss</li>
     *     </ol>
     * <li>{@link java.util.Date} 格式: yyyy-MM-dd HH:mm:ss</li>
     * </ul>
     */
    public static final ObjectMapper DEFAULT_OBJECT_MAPPER = new ObjectMapper();

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    static {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        // LocalDate LocalTime 和 LocalDateTime 的格式。
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ISO_LOCAL_TIME));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ISO_LOCAL_TIME));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ISO_LOCAL_DATE));
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DATE_TIME_FORMATTER));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DATE_TIME_FORMATTER));

        DEFAULT_OBJECT_MAPPER.registerModule(javaTimeModule);
        DEFAULT_OBJECT_MAPPER.setDateFormat(new SimpleDateFormat(DATE_TIME_PATTERN));
    }

    private JacksonUtils() {
    }
}
