/*
 * SonarQube :: GitLab Plugin
 * Copyright (C) 2016-2025 Talanlabs
 * gabriel.allaigre@gmail.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Sonar Source-Available License Version 1, as published by SonarSource SA.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the Sonar Source-Available License for more details.
 *
 * You should have received a copy of the Sonar Source-Available License
 * along with this program; if not, see https://sonarsource.com/license/ssal/
 */
package com.talanlabs.sonar.plugins.gitlab.freemarker;

import com.talanlabs.sonar.plugins.gitlab.MarkDownUtils;
import freemarker.ext.util.WrapperTemplateModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import org.sonar.api.batch.rule.Severity;

import java.util.List;
import java.util.Map;

public class PrintTemplateMethodModelEx implements TemplateMethodModelEx {

    private final MarkDownUtils markDownUtils;

    public PrintTemplateMethodModelEx(MarkDownUtils markDownUtils) {
        super();

        this.markDownUtils = markDownUtils;
    }

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        if (arguments.size() == 1) {
            return execOneArg(arguments.get(0));
        }
        throw new TemplateModelException("Failed call accept 1 issue arg");
    }

    private String execOneArg(Object arg) throws TemplateModelException {
        if (arg instanceof WrapperTemplateModel && ((WrapperTemplateModel) arg).getWrappedObject() instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) ((WrapperTemplateModel) arg).getWrappedObject();
            return markDownUtils.printIssue((Severity) (map.get("severity")), (String) map.get("message"), (String) map.get("ruleLink"), (String) map.get("url"), (String) map.get("componentKey"));
        }
        throw new TemplateModelException("Failed call accept 1 issue arg");
    }
}
