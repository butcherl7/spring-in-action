package top.funsite.spring.action.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static top.funsite.spring.action.util.DateUtils.DATE_TIME_FORMATTER;
import static top.funsite.spring.action.util.DateUtils.SIMPLE_DATE_FORMATTER;

public class JSONUtils {

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
        DEFAULT_OBJECT_MAPPER.setDateFormat(SIMPLE_DATE_FORMATTER);
    }

    /**
     * 该方法用于使用提供的输出流（使用 UTF8 编码）将任何 Java 值序列化为 JSON 输出。
     *
     * @param out   OutputStream
     * @param value Java Object
     */
    public static void writeValue(OutputStream out, Object value) throws IOException {
        DEFAULT_OBJECT_MAPPER.writeValue(out, value);
    }

    public static void writeValue(ServletResponse response, Object value) {
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            writeValue(outputStream, value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JSONUtils() {
    }
}
