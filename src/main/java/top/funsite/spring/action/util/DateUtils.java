package top.funsite.spring.action.util;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    /**
     * {@value}
     */
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 格式化为 "yyyy-MM-dd HH:mm:ss"
     */
    public static final SimpleDateFormat SIMPLE_DATE_FORMATTER = new SimpleDateFormat(DATE_TIME_PATTERN);

    /**
     * 格式化为 "yyyy-MM-dd HH:mm:ss.S"
     */
    public static final SimpleDateFormat SIMPLE_MILLI_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

    /**
     * 格式化为 "yyyy-MM-dd HH:mm:ss"
     */
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    private DateUtils() {
    }
}
