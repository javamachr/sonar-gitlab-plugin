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

import com.talanlabs.sonar.plugins.gitlab.models.Issue;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.rule.Severity;

public class IssueComparatorTest {

    private IssueComparator issueComparator;

    @Before
    public void before() {
        issueComparator = new IssueComparator();
    }

    @Test
    public void testNull() {
        Assertions.assertThat(issueComparator.compare(null, null)).isEqualTo(0);
        Assertions.assertThat(issueComparator.compare(Issue.newBuilder().build(), null)).isEqualTo(-1);
        Assertions.assertThat(issueComparator.compare(null, Issue.newBuilder().build())).isEqualTo(1);
    }

    @Test
    public void testSeverity() {
        Assertions.assertThat(issueComparator.compare(Issue.newBuilder().severity(Severity.BLOCKER).componentKey("toto").line(1).build(), Issue.newBuilder().severity(Severity.BLOCKER).componentKey( "toto").line(1).build())).isEqualTo(0);
        Assertions.assertThat(issueComparator.compare(Issue.newBuilder().severity(Severity.MAJOR).componentKey( "toto").line(1).build(), Issue.newBuilder().severity(Severity.BLOCKER).componentKey( "toto").line(1).build())).isEqualTo(1);
        Assertions.assertThat(issueComparator.compare(Issue.newBuilder().severity(Severity.MAJOR).componentKey( "toto").line(1).build(), Issue.newBuilder().severity(Severity.MINOR).componentKey( "toto").line(1).build())).isEqualTo(-1);
    }

    @Test
    public void testComponentKey() {
        Assertions.assertThat(issueComparator.compare(Issue.newBuilder().severity(Severity.BLOCKER).componentKey( "a").line(1).build(), Issue.newBuilder().severity(Severity.BLOCKER).componentKey( "b").line( 1).build())).isEqualTo(-1);
        Assertions.assertThat(issueComparator.compare(Issue.newBuilder().severity(Severity.BLOCKER).componentKey( "b").line(1).build(), Issue.newBuilder().severity(Severity.BLOCKER).componentKey( "a").line( 1).build())).isEqualTo(1);
    }

    @Test
    public void testSimple() {
        Assertions.assertThat(issueComparator.compare(Issue.newBuilder().severity(Severity.BLOCKER).componentKey( "a").line(null).build(), Issue.newBuilder().severity(Severity.BLOCKER).componentKey( "a").line( null).build())).isEqualTo(0);
        Assertions.assertThat(issueComparator.compare(Issue.newBuilder().severity(Severity.BLOCKER).componentKey( "a").line(1).build(), Issue.newBuilder().severity(Severity.BLOCKER).componentKey( "a").line( 2).build())).isEqualTo(-1);
        Assertions.assertThat(issueComparator.compare(Issue.newBuilder().severity(Severity.BLOCKER).componentKey( "a").line(1).build(), Issue.newBuilder().severity(Severity.BLOCKER).componentKey( "a").line( null).build())).isEqualTo(1);
        Assertions.assertThat(issueComparator.compare(Issue.newBuilder().severity(Severity.BLOCKER).componentKey( "a").line(10).build(), Issue.newBuilder().severity(Severity.BLOCKER).componentKey( "a").line( 1).build())).isEqualTo(1);
        Assertions.assertThat(issueComparator.compare(Issue.newBuilder().severity(Severity.BLOCKER).componentKey( "a").line(null).build(), Issue.newBuilder().severity(Severity.BLOCKER).componentKey( "a").line( 1).build())).isEqualTo(-1);
    }
}
