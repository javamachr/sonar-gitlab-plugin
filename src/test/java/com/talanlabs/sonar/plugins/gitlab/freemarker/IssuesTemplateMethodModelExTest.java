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

import com.talanlabs.sonar.plugins.gitlab.Utils;
import com.talanlabs.sonar.plugins.gitlab.models.ReportIssue;
import com.talanlabs.sonar.plugins.gitlab.models.Rule;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateModelException;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.sonar.api.batch.rule.Severity;

import java.util.*;

public class IssuesTemplateMethodModelExTest {

    private IssuesTemplateMethodModelEx issuesTemplateMethodModelEx;

    private List<Map<String, Object>> list(List<Object> arguments) {
        try {
            return (List<Map<String, Object>>) issuesTemplateMethodModelEx.exec(arguments);
        } catch (TemplateModelException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSuccessEmpty() {
        issuesTemplateMethodModelEx = new IssuesTemplateMethodModelEx(Collections.emptyList());

        Assertions.assertThat(list(Collections.emptyList())).isEmpty();
        Assertions.assertThat(list(Collections.singletonList(new SimpleScalar("MAJOR")))).isEmpty();
        Assertions.assertThat(list(Collections.singletonList(TemplateBooleanModel.FALSE))).isEmpty();
        Assertions.assertThat(list(Arrays.asList(new SimpleScalar("MAJOR"), TemplateBooleanModel.FALSE))).isEmpty();
    }

    @Test
    public void testSuccess() {
        List<ReportIssue> reportIssues = new ArrayList<>();
        reportIssues.add(ReportIssue.newBuilder().issue(Utils.newIssue("foo:src/Foo.php", null, 1, Severity.BLOCKER, true, "msg1")).revision("123").url("url").file("file").ruleLink("ruleLink").reportedOnDiff(true).rule(Rule.newBuilder().key("toto").name("Toto").build()).build());
        reportIssues.add(ReportIssue.newBuilder().issue(Utils.newIssue("foo:src/Foo.php", null, 1, Severity.BLOCKER, true, "msg1")).revision("123").url("url").file("file").ruleLink("ruleLink").reportedOnDiff(false).build());
        reportIssues.add(ReportIssue.newBuilder().issue(Utils.newIssue("foo:src/Foo.php", null, 1, Severity.BLOCKER, true, "msg1")).revision("123").url("url").file("file").ruleLink("ruleLink").reportedOnDiff(false).build());

        reportIssues.add(ReportIssue.newBuilder().issue(Utils.newIssue("foo:src/Foo.php", null, 1, Severity.MAJOR, true, "msg1")).revision("123").url("url").file("file").ruleLink("ruleLink").reportedOnDiff(false).build());
        reportIssues.add(ReportIssue.newBuilder().issue(Utils.newIssue("foo:src/Foo.php", null, 1, Severity.MAJOR, true, "msg1")).revision("123").url("url").file("file").ruleLink("ruleLink").reportedOnDiff(true).build());

        issuesTemplateMethodModelEx = new IssuesTemplateMethodModelEx(reportIssues);

        Assertions.assertThat(list(Collections.emptyList())).hasSize(5);
        Assertions.assertThat(list(Collections.singletonList(new SimpleScalar("MAJOR")))).hasSize(2);
        Assertions.assertThat(list(Collections.singletonList(TemplateBooleanModel.FALSE))).hasSize(3);
        Assertions.assertThat(list(Arrays.asList(new SimpleScalar("MAJOR"), TemplateBooleanModel.FALSE))).hasSize(1);
    }

    @Test
    public void testFailed() {
        List<ReportIssue> reportIssues = new ArrayList<>();
        reportIssues.add(ReportIssue.newBuilder().issue(Utils.newIssue("foo:src/Foo.php", null, 1, Severity.BLOCKER, true, "msg1")).revision("123").url("url").file("file").ruleLink("ruleLink").reportedOnDiff(true).build());
        reportIssues.add(ReportIssue.newBuilder().issue(Utils.newIssue("foo:src/Foo.php", null, 1, Severity.BLOCKER, true, "msg1")).revision("123").url("url").file("file").ruleLink("ruleLink").reportedOnDiff(false).build());
        reportIssues.add(ReportIssue.newBuilder().issue(Utils.newIssue("foo:src/Foo.php", null, 1, Severity.BLOCKER, true, "msg1")).revision("123").url("url").file("file").ruleLink("ruleLink").reportedOnDiff(false).build());

        reportIssues.add(ReportIssue.newBuilder().issue(Utils.newIssue("foo:src/Foo.php", null, 1, Severity.MAJOR, true, "msg1")).revision("123").url("url").file("file").ruleLink("ruleLink").reportedOnDiff(false).build());
        reportIssues.add(ReportIssue.newBuilder().issue(Utils.newIssue("foo:src/Foo.php", null, 1, Severity.MAJOR, true, "msg1")).revision("123").url("url").file("file").ruleLink("ruleLink").reportedOnDiff(true).build());

        issuesTemplateMethodModelEx = new IssuesTemplateMethodModelEx(reportIssues);

        Assertions.assertThatThrownBy(() -> list(Collections.singletonList(null))).hasCauseInstanceOf(TemplateModelException.class);
        Assertions.assertThatThrownBy(() -> list(Collections.singletonList(new SimpleScalar("TOTO")))).hasCauseInstanceOf(TemplateModelException.class);
        Assertions.assertThatThrownBy(() -> list(Arrays.asList(TemplateBooleanModel.FALSE, new SimpleScalar("MAJOR")))).hasCauseInstanceOf(TemplateModelException.class);
        Assertions.assertThatThrownBy(() -> list(Arrays.asList(new SimpleScalar("TOTO"), TemplateBooleanModel.FALSE))).hasCauseInstanceOf(TemplateModelException.class);
        Assertions.assertThatThrownBy(() -> list(Arrays.asList(new SimpleScalar("TOTO"), TemplateBooleanModel.FALSE, TemplateBooleanModel.FALSE))).hasCauseInstanceOf(TemplateModelException.class);
    }
}
