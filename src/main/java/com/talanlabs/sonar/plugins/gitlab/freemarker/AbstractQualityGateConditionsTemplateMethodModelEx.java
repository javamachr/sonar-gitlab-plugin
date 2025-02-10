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

import com.talanlabs.sonar.plugins.gitlab.models.QualityGate;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public abstract class AbstractQualityGateConditionsTemplateMethodModelEx implements TemplateMethodModelEx {

    private final List<QualityGate.Condition> conditions;

    AbstractQualityGateConditionsTemplateMethodModelEx(List<QualityGate.Condition> conditions) {
        super();

        this.conditions = Collections.unmodifiableList(new ArrayList<>(conditions));
    }

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        if (arguments.isEmpty()) {
            return execEmptyArg();
        } else if (arguments.size() == 1) {
            return execOneArg(arguments.get(0));
        }
        throw new TemplateModelException("Failed call accept 0 or 1 args");
    }

    protected abstract Object exec(Stream<QualityGate.Condition> stream);

    private Object execEmptyArg() {
        return exec(conditions.stream());
    }

    private Object execOneArg(Object arg) throws TemplateModelException {
        if (arg instanceof TemplateScalarModel) {
            String name = ((TemplateScalarModel) arg).getAsString();
            try {
                QualityGate.Status status = QualityGate.Status.valueOf(name);
                return exec(conditions.stream().filter(c -> isStatusEquals(c, status)));
            } catch (IllegalArgumentException e) {
                throw new TemplateModelException("Failed call 1 Status arg (OK,WARN,ERROR)", e);
            }
        }
        throw new TemplateModelException("Failed call accept Status");
    }

    private boolean isStatusEquals(QualityGate.Condition condition, QualityGate.Status status) {
        return status == condition.getStatus();
    }
}
