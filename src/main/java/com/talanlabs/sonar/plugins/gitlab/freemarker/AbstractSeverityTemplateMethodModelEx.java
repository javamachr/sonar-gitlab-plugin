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

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;
import org.sonar.api.batch.rule.Severity;

import java.util.List;

public abstract class AbstractSeverityTemplateMethodModelEx implements TemplateMethodModelEx {

    AbstractSeverityTemplateMethodModelEx() {
        super();
    }

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        if (arguments.size() == 1) {
            return execOneArg(arguments.get(0));
        }
        throw new TemplateModelException("Failed call accept 1 Severity arg (INFO,MINOR,MAJOR,CRITICAL,BLOCKER)");
    }

    protected abstract Object exec(Severity severity);

    private Object execOneArg(Object arg) throws TemplateModelException {
        if (arg instanceof TemplateScalarModel) {
            String name = ((TemplateScalarModel) arg).getAsString();
            try {
                Severity severity = Severity.valueOf(name);
                return exec(severity);
            } catch (IllegalArgumentException e) {
                throw new TemplateModelException("Failed call 1 Severity arg (INFO,MINOR,MAJOR,CRITICAL,BLOCKER)", e);
            }
        }
        throw new TemplateModelException("Failed call accept 1 Severity arg (INFO,MINOR,MAJOR,CRITICAL,BLOCKER)");
    }
}
