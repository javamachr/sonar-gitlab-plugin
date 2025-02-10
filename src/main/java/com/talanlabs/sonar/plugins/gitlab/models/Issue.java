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
package com.talanlabs.sonar.plugins.gitlab.models;

import org.sonar.api.batch.rule.Severity;

import java.io.File;

public class Issue {

    private String key;
    private String ruleKey;
    private String componentKey;
    private File file;
    private Integer line;
    private String message;
    private Severity severity;
    private boolean newIssue;

    private Issue() {
        // Nothing
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getKey() {
        return key;
    }

    public String getRuleKey() {
        return ruleKey;
    }

    public String getComponentKey() {
        return componentKey;
    }

    public File getFile() {
        return file;
    }

    public Integer getLine() {
        return line;
    }

    public String getMessage() {
        return message;
    }

    public Severity getSeverity() {
        return severity;
    }

    public boolean isNewIssue() {
        return newIssue;
    }

    public static class Builder {

        private final Issue issue;

        private Builder() {
            this.issue = new Issue();
        }

        public Builder key(String key) {
            issue.key = key;
            return this;
        }

        public Builder ruleKey(String ruleKey) {
            issue.ruleKey = ruleKey;
            return this;
        }

        public Builder componentKey(String componentKey) {
            issue.componentKey = componentKey;
            return this;
        }

        public Builder file(File file) {
            issue.file = file;
            return this;
        }

        public Builder line(Integer line) {
            issue.line = line;
            return this;
        }

        public Builder message(String message) {
            issue.message = message;
            return this;
        }

        public Builder severity(Severity severity) {
            issue.severity = severity;
            return this;
        }

        public Builder newIssue(boolean newIssue) {
            issue.newIssue = newIssue;
            return this;
        }

        public Issue build() {
            return issue;
        }
    }
}
