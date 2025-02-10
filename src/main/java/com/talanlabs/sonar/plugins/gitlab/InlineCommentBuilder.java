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
package com.talanlabs.sonar.plugins.gitlab;

import com.talanlabs.sonar.plugins.gitlab.models.ReportIssue;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InlineCommentBuilder extends AbstractCommentBuilder {

    private final Integer lineNumber;
    private final String author;

    public InlineCommentBuilder(GitLabPluginConfiguration gitLabPluginConfiguration, String revision, String author, Integer lineNumber, List<ReportIssue> reportIssues,
                                MarkDownUtils markDownUtils) {
        super(gitLabPluginConfiguration, revision, reportIssues, markDownUtils, "inline", gitLabPluginConfiguration.inlineTemplate());

        this.lineNumber = lineNumber;
        this.author = author;
    }

    @Override
    protected String buildDefaultComment() {
        String msg = reportIssues.stream()
                .map(reportIssue -> markDownUtils.printIssue(reportIssue.getIssue().getSeverity(), reportIssue.getIssue().getMessage(), reportIssue.getRuleLink(), null, null))
                .map(reportIssue -> reportIssues.size() > 1 ? "* " + reportIssue : reportIssue)
                .collect(Collectors.joining("\n"));
        if (gitLabPluginConfiguration.pingUser() && author != null) {
            if (reportIssues.size() > 1) {
                msg += "\n\n";
            }
            msg += " @" + author;
        }
        return msg;
    }

    @Override
    protected Map<String, Object> createContext() {
        Map<String, Object> root = super.createContext();
        root.put("lineNumber", lineNumber);
        root.put("author", author);
        return root;
    }
}
