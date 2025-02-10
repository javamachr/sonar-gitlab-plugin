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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QualityGateConditionsTemplateMethodModelEx extends AbstractQualityGateConditionsTemplateMethodModelEx {

    public QualityGateConditionsTemplateMethodModelEx(List<QualityGate.Condition> conditions) {
        super(conditions);
    }

    @Override
    protected Object exec(Stream<QualityGate.Condition> stream) {
        return stream.map(this::convertCondition).collect(Collectors.toList());
    }

    private Map<String, Object> convertCondition(QualityGate.Condition condition) {
        Map<String, Object> root = new HashMap<>();
        root.put("status", condition.getStatus());
        root.put("actual", condition.getActual());
        root.put("warning", condition.getWarning());
        root.put("error", condition.getError());
        root.put("metricKey", condition.getMetricKey());
        root.put("metricName", condition.getMetricName());
        root.put("symbol", condition.getSymbol());
        return root;
    }
}
