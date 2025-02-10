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

public class ReportIssue {

    private Issue issue;
    private Rule rule;
    private String revision;
    private String url;
    private String file;
    private String ruleLink;
    private boolean reportedOnDiff;

    private ReportIssue() {
        // Nothing
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public Issue getIssue() {
        return issue;
    }

    public String getRevision() {
        return revision;
    }

    public String getUrl() {
        return url;
    }

    public String getFile() {
        return file;
    }

    public String getRuleLink() {
        return ruleLink;
    }

    public boolean isReportedOnDiff() {
        return reportedOnDiff;
    }

    public Rule getRule() {
        return rule;
    }

    public static class Builder {

        private final ReportIssue reportIssue;

        private Builder() {
            this.reportIssue = new ReportIssue();
        }

        public Builder issue(Issue issue) {
            this.reportIssue.issue = issue;
            return this;
        }

        public Builder rule(Rule rule) {
            this.reportIssue.rule = rule;
            return this;
        }

        public Builder revision(String revision) {
            this.reportIssue.revision = revision;
            return this;
        }

        public Builder url(String url) {
            this.reportIssue.url = url;
            return this;
        }

        public Builder file(String file) {
            this.reportIssue.file = file;
            return this;
        }

        public Builder ruleLink(String ruleLink) {
            this.reportIssue.ruleLink = ruleLink;
            return this;
        }

        public Builder reportedOnDiff(boolean reportedOnDiff) {
            this.reportIssue.reportedOnDiff = reportedOnDiff;
            return this;
        }

        public ReportIssue build() {
            return reportIssue;
        }
    }
}
