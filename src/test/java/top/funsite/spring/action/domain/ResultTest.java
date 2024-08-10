package top.funsite.spring.action.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;
import top.funsite.spring.action.domain.r.IPagination;
import top.funsite.spring.action.domain.r.Pagination;
import top.funsite.spring.action.domain.r.Result;
import top.funsite.spring.action.util.JsonUtils;

import java.util.Collections;
import java.util.Map;

public class ResultTest {

    @Test
    public void testOkPagination() throws JsonProcessingException {
        Map<String, String> data = Collections.emptyMap();
        IPagination<String> pagination = Pagination.empty();

        var result1 = Result.ok(data);
        var result2 = Result.ok(pagination);

        ObjectWriter objectWriter = JsonUtils.OBJECT_MAPPER.writerWithDefaultPrettyPrinter();

        var json1 = objectWriter.writeValueAsString(result1);
        var json2 = objectWriter.writeValueAsString(result2);

        System.out.println(json1);
        System.out.println("*******");
        System.out.println(json2);
    }
}
