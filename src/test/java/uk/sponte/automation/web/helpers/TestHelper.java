package uk.sponte.automation.web.helpers;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by swozniak on 03/04/15.
 */
public class TestHelper {
    public String getTestPageAsBase64() {
        String resourcePath = "uk/sponte/automation/web/test.page.html";

        Configuration freemarkerConfiguration = new Configuration();
        freemarkerConfiguration.setClassForTemplateLoading(this.getClass(), "/");
        Template template;
        try {
            template = freemarkerConfiguration.getTemplate(resourcePath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        StringWriter stringWriter = new StringWriter();
        Map<String, Object> input = new HashMap<String, Object>();
        input.put("jquery", getResourceAsString("uk/sponte/automation/web/jquery-2.1.3.js"));
        input.put("jqueryui", getResourceAsString("uk/sponte/automation/web/jquery-ui.js"));

        try {
            template.process(input, stringWriter);
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] encodedBytes = Base64.getEncoder().encode(stringWriter.toString().getBytes());
        String base64String = new String(encodedBytes);

        return "file://" + this.getClass().getResource("../test.page.html").getFile();
//        return String.format("data:%s;%s;base64,%s", "text/html", "utf-8", base64String);
    }

    public String getResourceAsString(String resourcePath) {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(
                        this.getClass().getClassLoader().getResourceAsStream(resourcePath)
                )
        );

        StringBuilder result = new StringBuilder();
        String line;

        try {
            while((line = bufferedReader.readLine())!=null) {
                result.append(line);
                result.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }
}
