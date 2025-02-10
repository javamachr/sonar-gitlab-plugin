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
import com.talanlabs.sonar.plugins.gitlab.models.QualityGate;
import com.talanlabs.sonar.plugins.gitlab.models.StatusNotificationsMode;
import org.jetbrains.annotations.NotNull;
import org.sonar.api.batch.postjob.PostJob;
import org.sonar.api.batch.postjob.PostJobContext;
import org.sonar.api.batch.postjob.PostJobDescriptor;
import org.sonar.api.utils.MessageException;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.File;
import java.util.List;

/**
 * Compute comments to be added on the commit on preview or issue mode only
 */
public class CommitPublishPostJob implements PostJob {

    private static final Logger LOG = Loggers.get(CommitPublishPostJob.class);
    private static final String SONAR_PROJECT_BASE_DIR = "sonar.projectBaseDir";
    private static final String SONAR_WORKING_DIRECTORY = "sonar.working.directory";

    private final GitLabPluginConfiguration gitLabPluginConfiguration;
    private final SonarFacade sonarFacade;
    private final CommitFacade commitFacade;
    private final ReporterBuilder reporterBuilder;

    public CommitPublishPostJob(GitLabPluginConfiguration gitLabPluginConfiguration, SonarFacade sonarFacade, CommitFacade commitFacade, ReporterBuilder reporterBuilder) {
        this.gitLabPluginConfiguration = gitLabPluginConfiguration;
        this.sonarFacade = sonarFacade;
        this.commitFacade = commitFacade;
        this.reporterBuilder = reporterBuilder;
    }

    @Override
    public void describe(PostJobDescriptor descriptor) {
        descriptor.name("GitLab Commit Issue Publisher")
                .requireProperty(GitLabPlugin.GITLAB_URL, GitLabPlugin.GITLAB_USER_TOKEN, GitLabPlugin.GITLAB_PROJECT_ID, GitLabPlugin.GITLAB_COMMIT_SHA, GitLabPlugin.GITLAB_REF_NAME,
                        SONAR_PROJECT_BASE_DIR, SONAR_WORKING_DIRECTORY);
    }

    @Override
    public void execute(@NotNull PostJobContext context) {
        LOG.info("Will execute CommitPublishPostJob of GitlabPlugin.");
        try {
            if (!gitLabPluginConfiguration.isEnabled()) {
                LOG.info("GitLab plugin is disabled");
                return;
            } else {
                LOG.info("GitLab plugin is enabled");
            }
            File baseDir = fileFromProperty(context, SONAR_PROJECT_BASE_DIR);
            if (baseDir == null) {
                throw MessageException.of("SonarQube failed because sonar.projectBaseDir is null");
            }
            File workDir = fileFromProperty(context, SONAR_WORKING_DIRECTORY);
            if (workDir == null) {
                throw MessageException.of("SonarQube failed because sonar.working.directory is null");
            }
            sonarFacade.init(baseDir, workDir);
            commitFacade.init(baseDir);

            if (StatusNotificationsMode.COMMIT_STATUS.equals(gitLabPluginConfiguration.statusNotificationsMode())) {
                commitFacade.createOrUpdateSonarQubeStatus(gitLabPluginConfiguration.buildInitState().getMeaning(), "SonarQube analysis in progress");
            }
            QualityGate qualityGate;
            List<Issue> issues;
            qualityGate = sonarFacade.loadQualityGate();
            issues = sonarFacade.getNewIssues();

            Reporter report = reporterBuilder.build(qualityGate, issues);
            notification(report);

            if(gitLabPluginConfiguration.failOnQualityGate() && QualityGate.Status.ERROR.equals(qualityGate.getStatus()))
            {
                throw MessageException.of("Quality Gate failed. Exiting scan with failure.");
            }

        } catch (MessageException e) {
            StatusNotificationsMode i = gitLabPluginConfiguration.statusNotificationsMode();
            if (i == StatusNotificationsMode.COMMIT_STATUS) {
                commitFacade.createOrUpdateSonarQubeStatus(MessageHelper.FAILED_GITLAB_STATUS, MessageHelper.sonarQubeFailed(e.getMessage()));
            }

            throw e;
        } catch (Exception e) {
            StatusNotificationsMode i = gitLabPluginConfiguration.statusNotificationsMode();
            if (i == StatusNotificationsMode.COMMIT_STATUS) {
                commitFacade.createOrUpdateSonarQubeStatus(MessageHelper.FAILED_GITLAB_STATUS, MessageHelper.sonarQubeFailed(e.getMessage()));
            }

            throw MessageException.of(MessageHelper.sonarQubeFailed(e.getMessage()), e);
        }
    }

    private File fileFromProperty(PostJobContext context, String property) {
        String value = context.config().get(property).orElse(null);
        return value != null ? new File(value) : null;
    }

    private void notification(Reporter report) {
        String status = report.getStatus();
        String statusDescription = report.getStatusDescription();
        String message = String.format("Report status=%s, desc=%s", status, statusDescription);

        switch (gitLabPluginConfiguration.statusNotificationsMode()) {
            case COMMIT_STATUS:
                notificationCommitStatus(status, statusDescription, message);
                break;
            case EXIT_CODE:
                notificationCommitStatus(status, message);
                break;
            case NOTHING:
                LOG.info(message);
                break;
        }
    }

    private void notificationCommitStatus(String status, String statusDescription, String message) {
        LOG.info(message);
        commitFacade.createOrUpdateSonarQubeStatus(status, statusDescription);
    }

    private void notificationCommitStatus(String status, String message) {
        if (MessageHelper.FAILED_GITLAB_STATUS.equals(status)) {
            throw MessageException.of(message);
        } else {
            LOG.info(message);
        }
    }
}
