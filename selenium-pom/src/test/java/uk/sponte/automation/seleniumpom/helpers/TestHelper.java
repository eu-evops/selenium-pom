package uk.sponte.automation.seleniumpom.helpers;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by swozniak on 03/04/15.
 */
public class TestHelper {
    public String getTestPageAsBase64(String testPagePath) {
        String resourcePath = String.format("uk/sponte/automation/seleniumpom/%s", testPagePath);

        Configuration freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_22);
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
        input.put("jquery", getResourceAsString("uk/sponte/automation/seleniumpom/jquery-2.1.3.js"));
        input.put("jqueryui", getResourceAsString("uk/sponte/automation/seleniumpom/jquery-ui.js"));

        try {
            template.process(input, stringWriter);
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "file://" + this.getClass().getResource(String.format("../%s", testPagePath)).getFile();
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
