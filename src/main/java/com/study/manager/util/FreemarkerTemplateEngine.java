package com.study.manager.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FreemarkerTemplateEngine {

    private String templateName;

    public FreemarkerTemplateEngine(String templateName) {
        this.templateName = templateName;
    }

    public Template getTemplate() throws IOException {
        Configuration cfg = new Configuration();
        cfg.setClassForTemplateLoading(FreemarkerTemplateEngine.class, "/templates");
        cfg.setIncompatibleImprovements(new Version(2, 3, 20));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setLocale(Locale.US);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        Template template = cfg.getTemplate(templateName);
        return template;
    }


}
