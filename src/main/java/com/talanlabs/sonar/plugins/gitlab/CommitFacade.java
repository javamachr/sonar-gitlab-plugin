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

import com.google.common.annotations.VisibleForTesting;
import com.talanlabs.sonar.plugins.gitlab.models.JsonMode;
import org.sonar.api.scan.filesystem.PathResolver;
import org.sonar.api.scanner.ScannerSide;
import org.sonar.api.utils.MessageException;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

/**
 * Facade for all WS interaction with GitLab.
 */
@ScannerSide
public class CommitFacade {

    private static final Logger LOG = Loggers.get(CommitFacade.class);

    private static final String CODECLIMATE_JSON_NAME = "gl-code-quality-report.json";
    private static final String SAST_JSON_NAME = "gl-sast-report.json";

    private final GitLabPluginConfiguration gitLabPluginConfiguration;
    private final String ruleUrlPrefix;
    private File gitBaseDir;

    private IGitLabApiWrapper gitLabWrapper;

    public CommitFacade(GitLabPluginConfiguration gitLabPluginConfiguration) {
        this.gitLabPluginConfiguration = gitLabPluginConfiguration;

        this.ruleUrlPrefix = gitLabPluginConfiguration.baseUrl();

        if (GitLabPlugin.V4_API_VERSION.equals(gitLabPluginConfiguration.apiVersion())) {
            this.gitLabWrapper = new GitLabApiV4Wrapper(gitLabPluginConfiguration);
        }
    }

    @VisibleForTesting
    void setGitLabWrapper(IGitLabApiWrapper gitLabWrapper) {
        this.gitLabWrapper = gitLabWrapper;
    }

    public static String encodeForUrl(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Encoding not supported", e);
        }
    }

    public void init(File projectBaseDir) {
        initGitBaseDir(projectBaseDir);

        gitLabWrapper.init();
    }

    void initGitBaseDir(File projectBaseDir) {
        File detectedGitBaseDir = findGitBaseDir(projectBaseDir);
        if (detectedGitBaseDir == null) {
            LOG.debug("Unable to find Git root directory. Is " + projectBaseDir + " part of a Git repository?");
            setGitBaseDir(projectBaseDir);
        } else {
            setGitBaseDir(detectedGitBaseDir);
        }
    }

    private File findGitBaseDir(@Nullable File baseDir) {
        if (baseDir == null) {
            return null;
        }
        if (new File(baseDir, ".git").exists()) {
            this.gitBaseDir = baseDir;
            return baseDir;
        }
        return findGitBaseDir(baseDir.getParentFile());
    }

    void setGitBaseDir(File gitBaseDir) {
        this.gitBaseDir = gitBaseDir;
    }

    public boolean hasSameCommitCommentsForFile(String revision, File file, Integer lineNumber, String body) {
        String path = getPath(file);
        return gitLabWrapper.hasSameCommitCommentsForFile(revision, path, lineNumber, body);
    }

    /**
     * Author Email is access only for admin gitlab user but search work for all users
     */
    public String getUsernameForRevision(String revision) {
        return gitLabWrapper.getUsernameForRevision(revision);
    }

    public void createOrUpdateSonarQubeStatus(String status, String statusDescription) {
        gitLabWrapper.createOrUpdateSonarQubeStatus(status, statusDescription);
    }

    public boolean hasFile(File file) {
        String path = getPath(file);
        return gitLabWrapper.hasFile(path);
    }

    public String getRevisionForLine(File file, int lineNumber) {
        String path = getPath(file);
        return gitLabWrapper.getRevisionForLine(file, path, lineNumber);
    }

    @CheckForNull
    public String getGitLabUrl(@Nullable String revision, @Nullable File file, @Nullable Integer issueLine) {
        if (file != null) {
            String path = getPath(file);
            return gitLabWrapper.getGitLabUrl(revision, path, issueLine);
        }
        return null;
    }

    @CheckForNull
    public String getSrc(@Nullable File file) {
        if (file != null) {
            return getPath(file);
        }
        return null;
    }

    public void createOrUpdateReviewComment(String revision, File file, Integer line, String body) {
        String fullPath = getPath(file);
        gitLabWrapper.createOrUpdateReviewComment(revision, fullPath, line, body);
    }

    String getPath(File file) {
        String prefix = gitLabPluginConfiguration.prefixDirectory() != null ? gitLabPluginConfiguration.prefixDirectory() : "";
        return prefix + new PathResolver().relativePath(gitBaseDir, file);
    }

    public void addGlobalComment(String comment) {
        gitLabWrapper.addGlobalComment(comment);
    }

    public String getRuleLink(String ruleKey) {
        return ruleUrlPrefix + "coding_rules#rule_key=" + encodeForUrl(ruleKey);
    }

    public void writeJsonFile(String json) {
        String name = null;
        if (gitLabPluginConfiguration.jsonMode().equals(JsonMode.CODECLIMATE)) {
            name = CODECLIMATE_JSON_NAME;
        } else if (gitLabPluginConfiguration.jsonMode().equals(JsonMode.SAST)) {
            name = SAST_JSON_NAME;
        }
        if (name != null) {
            File file = new File(gitBaseDir, name);
            try {
                Files.write(Paths.get(file.getAbsolutePath()), json.getBytes(), StandardOpenOption.CREATE);
            } catch (IOException e) {
                throw MessageException.of("Failed to write file " + file, e);
            }
        }
    }
}
