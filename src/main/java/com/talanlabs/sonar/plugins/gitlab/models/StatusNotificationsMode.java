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
package com.talanlabs.sonar.plugins.gitlab.models;

public enum StatusNotificationsMode {

    COMMIT_STATUS("commit-status"), EXIT_CODE("exit-code"), NOTHING("nothing");

    private final String meaning;

    StatusNotificationsMode(String meaning) {
        this.meaning = meaning;
    }

    public static StatusNotificationsMode of(String meaning) {
        for (StatusNotificationsMode m : values()) {
            if (m.meaning.equals(meaning)) {
                return m;
            }
        }
        return null;
    }

    public String getMeaning() {
        return meaning;
    }
}
