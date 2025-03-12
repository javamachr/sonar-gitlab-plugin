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

import com.talanlabs.sonar.plugins.gitlab.models.JsonMode;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommitFacadeTest {

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    @Test
    public void testInitGitBaseDirNotFound() throws Exception {
        CommitFacade facade = new CommitFacade(mock(GitLabPluginConfiguration.class));
        File projectBaseDir = temp.newFolder();
        facade.initGitBaseDir(projectBaseDir);
        assertThat(facade.getPath(new File(projectBaseDir, "src/main/java/Foo.java"))).isEqualTo("src/main/java/Foo.java");
    }

    @Test
    public void testInitGitBaseDir() throws Exception {
        CommitFacade facade = new CommitFacade(mock(GitLabPluginConfiguration.class));
        File gitBaseDir = temp.newFolder();
        Files.createDirectory(gitBaseDir.toPath().resolve(".git"));
        File projectBaseDir = new File(gitBaseDir, "myProject");
        facade.initGitBaseDir(projectBaseDir);
        assertThat(facade.getPath(new File(projectBaseDir, "src/main/java/Foo.java"))).isEqualTo("myProject/src/main/java/Foo.java");
    }

    @Test
    public void testGetPath() throws IOException {
        GitLabPluginConfiguration gitLabPluginConfiguration = mock(GitLabPluginConfiguration.class);
        when(gitLabPluginConfiguration.commitSHA()).thenReturn(Collections.singletonList("1"));
        when(gitLabPluginConfiguration.refName()).thenReturn("master");

        CommitFacade facade = new CommitFacade(gitLabPluginConfiguration);

        File gitBasedir = temp.newFolder();
        facade.setGitBaseDir(gitBasedir);

        Assertions.assertThat(facade.getPath(new File(gitBasedir, "src/main/Foo.java"))).isEqualTo("src/main/Foo.java");

        when(gitLabPluginConfiguration.prefixDirectory()).thenReturn("toto/");

        Assertions.assertThat(facade.getPath(new File(gitBasedir, "src/main/Foo.java"))).isEqualTo("toto/src/main/Foo.java");
    }

    @Test
    public void testWriteCodeClimateJson() throws IOException {
        GitLabPluginConfiguration gitLabPluginConfiguration = mock(GitLabPluginConfiguration.class);
        when(gitLabPluginConfiguration.jsonMode()).thenReturn(JsonMode.CODECLIMATE);
        CommitFacade facade = new CommitFacade(gitLabPluginConfiguration);
        File projectBaseDir = temp.newFolder();
        facade.initGitBaseDir(projectBaseDir);

        facade.writeJsonFile("[{\"tool\":\"sonarqube\",\"fingerprint\":\"null\",\"message\":\"Issue\",\"file\":\"file\",\"line\":\"0\",\"priority\":\"INFO\",\"solution\":\"http://myserver\"}]");

        File file = new File(projectBaseDir, "gl-code-quality-report.json");
        Assertions.assertThat(file).exists().hasContent("[{\"tool\":\"sonarqube\",\"fingerprint\":\"null\",\"message\":\"Issue\",\"file\":\"file\",\"line\":\"0\",\"priority\":\"INFO\",\"solution\":\"http://myserver\"}]");
    }

    @Test
    public void testWriteSastJson() throws IOException {
        GitLabPluginConfiguration gitLabPluginConfiguration = mock(GitLabPluginConfiguration.class);
        when(gitLabPluginConfiguration.jsonMode()).thenReturn(JsonMode.SAST);
        CommitFacade facade = new CommitFacade(gitLabPluginConfiguration);
        File projectBaseDir = temp.newFolder();
        facade.initGitBaseDir(projectBaseDir);

        facade.writeJsonFile("[{\"tool\":\"sonarqube\",\"fingerprint\":\"null\",\"message\":\"Issue\",\"file\":\"file\",\"line\":\"0\",\"priority\":\"INFO\",\"solution\":\"http://myserver\"}]");

        File file = new File(projectBaseDir, "gl-sast-report.json");
        Assertions.assertThat(file).exists().hasContent("[{\"tool\":\"sonarqube\",\"fingerprint\":\"null\",\"message\":\"Issue\",\"file\":\"file\",\"line\":\"0\",\"priority\":\"INFO\",\"solution\":\"http://myserver\"}]");
    }

    @Test
    public void testWriteNoneJson() throws IOException {
        GitLabPluginConfiguration gitLabPluginConfiguration = mock(GitLabPluginConfiguration.class);
        when(gitLabPluginConfiguration.jsonMode()).thenReturn(JsonMode.NONE);
        CommitFacade facade = new CommitFacade(gitLabPluginConfiguration);
        File projectBaseDir = temp.newFolder();
        facade.initGitBaseDir(projectBaseDir);

        facade.writeJsonFile("[{\"tool\":\"sonarqube\",\"fingerprint\":\"null\",\"message\":\"Issue\",\"file\":\"file\",\"line\":\"0\",\"priority\":\"INFO\",\"solution\":\"http://myserver\"}]");

        File file = new File(projectBaseDir, "gl-code-quality-report.json");
        Assertions.assertThat(projectBaseDir.listFiles((p) -> p.getPath().endsWith(".json"))).isEmpty();
    }

    @Test
    public void testUsernameForRevision() {
        GitLabPluginConfiguration gitLabPluginConfiguration = mock(GitLabPluginConfiguration.class);
        CommitFacade facade = new CommitFacade(gitLabPluginConfiguration);
        IGitLabApiWrapper gitLabApiWrapper = mock(IGitLabApiWrapper.class);
        facade.setGitLabWrapper(gitLabApiWrapper);
        facade.getUsernameForRevision("123");

        Mockito.verify(gitLabApiWrapper).getUsernameForRevision("123");
    }

    @Test
    public void testCreateOrUpdateSonarQubeStatus() {
        GitLabPluginConfiguration gitLabPluginConfiguration = mock(GitLabPluginConfiguration.class);
        CommitFacade facade = new CommitFacade(gitLabPluginConfiguration);
        IGitLabApiWrapper gitLabApiWrapper = mock(IGitLabApiWrapper.class);
        facade.setGitLabWrapper(gitLabApiWrapper);
        facade.createOrUpdateSonarQubeStatus("ok", "hello");

        Mockito.verify(gitLabApiWrapper).createOrUpdateSonarQubeStatus("ok", "hello");
    }

    @Test
    public void testGetGitLabUrl() throws IOException {
        GitLabPluginConfiguration gitLabPluginConfiguration = mock(GitLabPluginConfiguration.class);
        when(gitLabPluginConfiguration.commitSHA()).thenReturn(Collections.singletonList("1"));
        when(gitLabPluginConfiguration.refName()).thenReturn("master");

        CommitFacade facade = new CommitFacade(gitLabPluginConfiguration);
        IGitLabApiWrapper gitLabApiWrapper = mock(IGitLabApiWrapper.class);

        File gitBasedir = temp.newFolder();
        facade.setGitBaseDir(gitBasedir);
        facade.setGitLabWrapper(gitLabApiWrapper);

        Assertions.assertThat(facade.getGitLabUrl("123", null, null)).isNull();

        when(gitLabApiWrapper.getGitLabUrl("123", "src/main/Foo.java", 1)).thenReturn("response");
        Assertions.assertThat(facade.getGitLabUrl("123", new File(gitBasedir, "src/main/Foo.java"), 1)).isEqualTo("response");
    }

    @Test
    public void testGetSrc() throws IOException {
        GitLabPluginConfiguration gitLabPluginConfiguration = mock(GitLabPluginConfiguration.class);
        when(gitLabPluginConfiguration.commitSHA()).thenReturn(Collections.singletonList("1"));
        when(gitLabPluginConfiguration.refName()).thenReturn("master");

        CommitFacade facade = new CommitFacade(gitLabPluginConfiguration);

        File gitBasedir = temp.newFolder();
        facade.setGitBaseDir(gitBasedir);

        Assertions.assertThat(facade.getSrc(null)).isNull();

        Assertions.assertThat(facade.getSrc(new File(gitBasedir, "src/main/Foo.java"))).isEqualTo("src/main/Foo.java");

        when(gitLabPluginConfiguration.prefixDirectory()).thenReturn("toto/");

        Assertions.assertThat(facade.getSrc(new File(gitBasedir, "src/main/Foo.java"))).isEqualTo("toto/src/main/Foo.java");
    }

    @Test
    public void testCreateOrUpdateReviewComment() throws IOException {
        GitLabPluginConfiguration gitLabPluginConfiguration = mock(GitLabPluginConfiguration.class);
        when(gitLabPluginConfiguration.commitSHA()).thenReturn(Collections.singletonList("1"));
        when(gitLabPluginConfiguration.refName()).thenReturn("master");

        CommitFacade facade = new CommitFacade(gitLabPluginConfiguration);
        IGitLabApiWrapper gitLabApiWrapper = mock(IGitLabApiWrapper.class);

        File gitBasedir = temp.newFolder();
        facade.setGitBaseDir(gitBasedir);
        facade.setGitLabWrapper(gitLabApiWrapper);

        facade.createOrUpdateReviewComment("123", new File(gitBasedir, "src/main/Foo.java"), 5, "toto");

        Mockito.verify(gitLabApiWrapper).createOrUpdateReviewComment("123", "src/main/Foo.java", 5, "toto");
    }

    @Test
    public void testAddGlobalComment() {
        GitLabPluginConfiguration gitLabPluginConfiguration = mock(GitLabPluginConfiguration.class);
        CommitFacade facade = new CommitFacade(gitLabPluginConfiguration);
        IGitLabApiWrapper gitLabApiWrapper = mock(IGitLabApiWrapper.class);
        facade.setGitLabWrapper(gitLabApiWrapper);
        facade.addGlobalComment("hello");

        Mockito.verify(gitLabApiWrapper).addGlobalComment("hello");
    }

    @Test
    public void testGetRuleLink() {
        GitLabPluginConfiguration gitLabPluginConfiguration = mock(GitLabPluginConfiguration.class);
        when(gitLabPluginConfiguration.baseUrl()).thenReturn("http://test/");

        CommitFacade facade = new CommitFacade(gitLabPluginConfiguration);

        Assertions.assertThat(facade.getRuleLink("hello")).isEqualTo("http://test/coding_rules#rule_key=hello");

    }
}