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

import java.util.List;

public class GitlabDiscussionStatus {


    private String id;

    @JsonProperty("individual_note")
    private boolean individualNote;

    private List<GitlabNote> notes;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isIndividualNote() {
		return individualNote;
	}

	public void setIndividualNote(boolean individualNote) {
		this.individualNote = individualNote;
	}

	public List<GitlabNote> getNotes() {
		return notes;
	}

	public void setNotes(List<GitlabNote> notes) {
		this.notes = notes;
	}

}
