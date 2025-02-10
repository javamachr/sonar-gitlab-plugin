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
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateModelException;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.sonar.api.batch.rule.Severity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class IssueCountTemplateMethodModelExTest {

    private IssueCountTemplateMethodModelEx issueCountTemplateMethodModelEx;

    private Object count(List<Object> arguments) {
        try {
            return issueCountTemplateMethodModelEx.exec(arguments);
        } catch (TemplateModelException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSuccessEmpty() {
        issueCountTemplateMethodModelEx = new IssueCountTemplateMethodModelEx(Collections.emptyList());

        Assertions.assertThat(count(Collections.emptyList())).isEqualTo(0);
        Assertions.assertThat(count(Collections.singletonList(new SimpleScalar("MAJOR")))).isEqualTo(0);
        Assertions.assertThat(count(Collections.singletonList(TemplateBooleanModel.FALSE))).isEqualTo(0);
        Assertions.assertThat(count(Arrays.asList(new SimpleScalar("MAJOR"), TemplateBooleanModel.FALSE))).isEqualTo(0);
    }

    @Test
    public void testSuccess() {
        List<ReportIssue> reportIssues = new ArrayList<>();
        reportIssues.add(ReportIssue.newBuilder().issue(Utils.newIssue("foo:src/Foo.php", null, 1, Severity.BLOCKER, true, "msg1")).revision("123").url("url").file("file").ruleLink("ruleLink").reportedOnDiff(true).build());
        reportIssues.add(ReportIssue.newBuilder().issue(Utils.newIssue("foo:src/Foo.php", null, 1, Severity.BLOCKER, true, "msg1")).revision("123").url("url").file("file").ruleLink("ruleLink").reportedOnDiff( false).build());
        reportIssues.add(ReportIssue.newBuilder().issue(Utils.newIssue("foo:src/Foo.php", null, 1, Severity.BLOCKER, true, "msg1")).revision("123").url("url").file("file").ruleLink("ruleLink").reportedOnDiff(false).build());

        reportIssues.add(ReportIssue.newBuilder().issue(Utils.newIssue("foo:src/Foo.php", null, 1, Severity.MAJOR, true, "msg1")).revision("123").url("url").file("file").ruleLink("ruleLink").reportedOnDiff(false).build());
        reportIssues.add(ReportIssue.newBuilder().issue(Utils.newIssue("foo:src/Foo.php", null, 1, Severity.MAJOR, true, "msg1")).revision("123").url("url").file("file").ruleLink("ruleLink").reportedOnDiff(true).build());

        issueCountTemplateMethodModelEx = new IssueCountTemplateMethodModelEx(reportIssues);

        Assertions.assertThat(count(Collections.emptyList())).isEqualTo(5);
        Assertions.assertThat(count(Collections.singletonList(new SimpleScalar("MAJOR")))).isEqualTo(2);
        Assertions.assertThat(count(Collections.singletonList(TemplateBooleanModel.FALSE))).isEqualTo(3);
        Assertions.assertThat(count(Arrays.asList(new SimpleScalar("MAJOR"), TemplateBooleanModel.FALSE))).isEqualTo(1);
    }

    @Test
    public void testFailed() {
        List<ReportIssue> reportIssues = new ArrayList<>();
        reportIssues.add(ReportIssue.newBuilder().issue(Utils.newIssue("foo:src/Foo.php", null, 1, Severity.BLOCKER, true, "msg1")).revision("123").url("url").file("file").ruleLink("ruleLink").reportedOnDiff(true).build());
        reportIssues.add(ReportIssue.newBuilder().issue(Utils.newIssue("foo:src/Foo.php", null, 1, Severity.BLOCKER, true, "msg1")).revision("123").url("url").file("file").ruleLink("ruleLink").reportedOnDiff(false).build());
        reportIssues.add(ReportIssue.newBuilder().issue(Utils.newIssue("foo:src/Foo.php", null, 1, Severity.BLOCKER, true, "msg1")).revision("123").url("url").file("file").ruleLink("ruleLink").reportedOnDiff(false).build());

        reportIssues.add(ReportIssue.newBuilder().issue(Utils.newIssue("foo:src/Foo.php", null, 1, Severity.MAJOR, true, "msg1")).revision("123").url("url").file("file").ruleLink("ruleLink").reportedOnDiff(false).build());
        reportIssues.add(ReportIssue.newBuilder().issue(Utils.newIssue("foo:src/Foo.php", null, 1, Severity.MAJOR, true, "msg1")).revision("123").url("url").file("file").ruleLink("ruleLink").reportedOnDiff(true).build());

        issueCountTemplateMethodModelEx = new IssueCountTemplateMethodModelEx(reportIssues);

        Assertions.assertThatThrownBy(() -> count(Collections.singletonList(null))).hasCauseInstanceOf(TemplateModelException.class);
        Assertions.assertThatThrownBy(() -> count(Collections.singletonList(new SimpleScalar("TOTO")))).hasCauseInstanceOf(TemplateModelException.class);
        Assertions.assertThatThrownBy(() -> count(Arrays.asList(TemplateBooleanModel.FALSE, new SimpleScalar("MAJOR")))).hasCauseInstanceOf(TemplateModelException.class);
        Assertions.assertThatThrownBy(() -> count(Arrays.asList(new SimpleScalar("TOTO"), TemplateBooleanModel.FALSE))).hasCauseInstanceOf(TemplateModelException.class);
        Assertions.assertThatThrownBy(() -> count(Arrays.asList(new SimpleScalar("TOTO"), TemplateBooleanModel.FALSE, TemplateBooleanModel.FALSE))).hasCauseInstanceOf(TemplateModelException.class);
    }
}
