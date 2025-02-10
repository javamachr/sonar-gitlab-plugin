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
import org.mockito.Mockito;
import org.sonar.api.batch.fs.InputComponent;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.Severity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class Utils {

    private Utils() {
        super();
    }

    public static InputComponent newMockedInputComponent(String key) {
        InputComponent inputComponent = Mockito.mock(InputComponent.class);
        Mockito.when(inputComponent.key()).thenReturn(key);
        Mockito.when(inputComponent.isFile()).thenReturn(false);
        return inputComponent;
    }

    public static InputFile newMockedInputFile(File file) {
        InputFile inputFile = Mockito.mock(InputFile.class);
        Mockito.when(inputFile.key()).thenReturn(file.getPath());
        Mockito.when(inputFile.isFile()).thenReturn(true);
        Mockito.when(inputFile.uri()).thenReturn(file.toURI());
        return inputFile;
    }

    public static Issue newIssue(String componentKey, Severity severity, boolean isNew, String message) {
        return newIssue(componentKey, null, null, severity, isNew, message, "rule");
    }

    public static Issue newIssue(String componentKey, File file, Integer line, Severity severity, boolean isNew, String message) {
        return newIssue(componentKey, file, line, severity, isNew, message, "rule");
    }

    public static Issue newIssue(String componentKey, File file, Integer line, Severity severity, boolean isNew, String message, String rule) {
        return newIssue(null, componentKey, file, line, severity, isNew, message, rule);
    }

    public static Issue newIssue(String key, String componentKey, File file, Integer line, Severity severity, boolean isNew, String message, String rule) {
        return Issue.newBuilder().key(key).componentKey(componentKey).file(file).line(line).severity(severity).newIssue(isNew).message(message).ruleKey("repo:" + rule).build();
    }

    public static void createFile(File root, String path, String filename, String value) throws IOException {
        File dir = new File(root, path);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IOException("Failed to create directories " + dir.toString());
            }
        }
        File file = new File(dir, filename);
        if (!file.createNewFile()) {
            throw new IOException("Failed to create file " + file.toString());
        }
        try (BufferedWriter bw = Files.newBufferedWriter(file.toPath(), Charset.defaultCharset())) {
            bw.write(value);
        }
    }
}
