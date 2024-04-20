package top.funsite.spring.action;

import org.junit.jupiter.api.Test;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * @author Butcher
 */
public class ThymeleafTests {

    @Test
    public void testClassLoader() {
        // 创建 TemplateEngine 对象
        TemplateEngine templateEngine = new TemplateEngine();

        // 创建 TemplateResolver 对象
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");

        // 设置 TemplateResolver 到 TemplateEngine
        templateEngine.setTemplateResolver(templateResolver);

        // 创建 Context 对象
        Context context = new Context();
        context.setVariable("username", "John Doe");

        // 加载模板
        String templateName = "test.html";

        // 处理模板
        String processedHtml = templateEngine.process("test.html", context);

        // 输出处理后的 HTML
        System.out.println(processedHtml);
    }
}
