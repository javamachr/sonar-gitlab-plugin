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
package com.talanlabs.sonar.plugins.gitlab.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.talanlabs.gitlab.api.v4.models.users.GitLabUser;

import java.util.Date;

public class GitlabNote {

    public static final String URL = "/notes";

    private Integer id;
    private String body;
    private String attachment;
    private GitLabUser author;
    private boolean system;
    private boolean upvote;
    private boolean downvote;

    @JsonProperty("created_at")
    private Date createdAt;

    private GitlabPosition position;

    @JsonProperty("noteable_id")
    private Integer noteableId;

    @JsonProperty("noteableType")
    private String noteable_type;

    private boolean resolvable;
    private boolean resolved;

    @JsonProperty("resolved_by")
    private GitLabUser resolvedBy;

    @JsonProperty("noteable_iid")
    private Integer noteableIid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public GitLabUser getAuthor() {
        return author;
    }

    public void setAuthor(GitLabUser author) {
        this.author = author;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public boolean isSystem() {
        return system;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }

    public boolean isUpvote() {
        return upvote;
    }

    public void setUpvote(boolean upvote) {
        this.upvote = upvote;
    }

    public boolean isDownvote() {
        return downvote;
    }

    public void setDownvote(boolean downvote) {
        this.downvote = downvote;
    }

    public GitlabPosition getPosition() {
        return position;
    }

    public void setPosition(GitlabPosition position) {
        this.position = position;
    }

    public Integer getNoteableId() {
        return noteableId;
    }

    public void setNoteableId(Integer noteableId) {
        this.noteableId = noteableId;
    }

    public String getNoteable_type() {
        return noteable_type;
    }

    public void setNoteable_type(String noteable_type) {
        this.noteable_type = noteable_type;
    }

    public boolean isResolvable() {
        return resolvable;
    }

    public void setResolvable(boolean resolvable) {
        this.resolvable = resolvable;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public GitLabUser getResolvedBy() {
        return resolvedBy;
    }

    public void setResolvedBy(GitLabUser resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    public Integer getNoteableIid() {
        return noteableIid;
    }

    public void setNoteableIid(Integer noteableIid) {
        this.noteableIid = noteableIid;
    }
}
