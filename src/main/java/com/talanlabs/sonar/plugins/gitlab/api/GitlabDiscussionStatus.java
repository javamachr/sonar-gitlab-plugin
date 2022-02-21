/*
 * SonarQube :: GitLab Plugin
 * Copyright (C) 2016-2022 Talanlabs
 * gabriel.allaigre@gmail.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
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
