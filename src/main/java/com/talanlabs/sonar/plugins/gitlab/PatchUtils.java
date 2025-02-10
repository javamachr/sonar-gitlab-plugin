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

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatchUtils {

    // http://en.wikipedia.org/wiki/Diff_utility#Unified_format
    private static final Pattern PATCH_PATTERN = Pattern.compile("@@\\p{Space}-[0-9]+(?:,[0-9]+)?\\p{Space}\\+([0-9]+)(?:,[0-9]+)?\\p{Space}@@.*");

    private PatchUtils() {
        // Nothing
    }

    public static Set<IGitLabApiWrapper.Line> getPositionsFromPatch(String patch) {
        Set<IGitLabApiWrapper.Line> positions = new HashSet<>();

        int currentLine = -1;
        for (String line : patch.split("[\\r\\n]+")) {
            if (line.startsWith("@")) {
                Matcher matcher = PATCH_PATTERN.matcher(line);
                if (!matcher.matches()) {
                    throw new IllegalStateException("Unable to parse line:\n\t" + line + "\nFull patch: \n\t" + patch);
                }
                currentLine = Integer.parseInt(matcher.group(1));
            } else if (line.startsWith("+")) {
                positions.add(new IGitLabApiWrapper.Line(currentLine, line.replaceFirst("\\+", "")));
                currentLine++;
            } else if (line.startsWith(" ")) {
                // Can't comment line if not addition or deletion due to following bug
                // https://gitlab.com/gitlab-org/gitlab-ce/issues/26606
                currentLine++;
            }
        }

        return positions;
    }
}
