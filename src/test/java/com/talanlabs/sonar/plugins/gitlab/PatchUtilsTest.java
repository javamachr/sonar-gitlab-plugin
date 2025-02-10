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

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class PatchUtilsTest {

    @Test
    public void testEmpty() {
        Assertions.assertThat(PatchUtils.getPositionsFromPatch("12313")).isEmpty();
    }

    @Test
    public void testWrong() {
        Assertions.assertThatThrownBy(() -> PatchUtils.getPositionsFromPatch("@ wrong")).isInstanceOf(IllegalStateException.class).hasMessage("Unable to parse line:\n" +
                "\t@ wrong\n" +
                "Full patch: \n" +
                "\t@ wrong");
    }

    @Test
    public void testCorrectUnixEOL() {
        Assertions.assertThat(PatchUtils.getPositionsFromPatch("@@ -78,6 +78,27 @@\n" +
                "\t\t\t\t\"src/styles.scss\",\n" +
                "                \"src/cordova-styles.scss\"\n" +
                "              ]\n" +
                "            },\n" +
                "+           \"prod-cordova\": {\n" +
                "+             \"optimization\": true,\n" +
                "+             \"outputHashing\": \"all\",\n" +
                "              \"sourceMap\": false,\n" +
                "              \"extractCss\": true,\n" +
                "              \"namedChunks\": false,\n" +
                "              \"aot\": true,\n" +
                "              \"extractLicenses\": true,\n" +
                "              \"vendorChunk\": false,\n" +
                "              \"buildOptimizer\": true,\n" +
                "              \"fileReplacements\": [\n" +
                "                {\n" +
                "                  \"replace\": \"src/environments/environment.ts\",\n" +
                "                  \"with\": \"src/environments/environment.prod-cordova.ts\"\n" +
                "                }\n" +
                "              ],\n" +
                "              \"styles\": [\n" +
                "                \"src/styles.scss\",\n" +
                "                \"src/cordova-styles.scss\"\n" +
                "              ]\n" +
                "            }\n" +
                "          }\n" +
                "        },")).isNotEmpty().hasSize(3).containsExactlyInAnyOrder(
                new IGitLabApiWrapper.Line(83, "             \"outputHashing\": \"all\","),
                new IGitLabApiWrapper.Line(82, "             \"optimization\": true,"),
                new IGitLabApiWrapper.Line(81, "           \"prod-cordova\": {")
        );
    }


    @Test
    public void testCorrectMacEOL() {
        Assertions.assertThat(PatchUtils.getPositionsFromPatch("@@ -78,6 +78,27 @@\r" +
                "\t\t\t\t\"src/styles.scss\",\r" +
                "                \"src/cordova-styles.scss\"\r" +
                "              ]\r" +
                "            },\r" +
                "+           \"prod-cordova\": {\r" +
                "+             \"optimization\": true,\r" +
                "+             \"outputHashing\": \"all\",\r" +
                "              \"sourceMap\": false,\r" +
                "              \"extractCss\": true,\r" +
                "              \"namedChunks\": false,\r" +
                "              \"aot\": true,\r" +
                "              \"extractLicenses\": true,\r" +
                "              \"vendorChunk\": false,\r" +
                "              \"buildOptimizer\": true,\r" +
                "              \"fileReplacements\": [\r" +
                "                {\r" +
                "                  \"replace\": \"src/environments/environment.ts\",\r" +
                "                  \"with\": \"src/environments/environment.prod-cordova.ts\"\r" +
                "                }\r" +
                "              ],\r" +
                "              \"styles\": [\r" +
                "                \"src/styles.scss\",\r" +
                "                \"src/cordova-styles.scss\"\r" +
                "              ]\r" +
                "            }\r" +
                "          }\r" +
                "        },")).isNotEmpty().hasSize(3).containsExactlyInAnyOrder(
                new IGitLabApiWrapper.Line(83, "             \"outputHashing\": \"all\","),
                new IGitLabApiWrapper.Line(82, "             \"optimization\": true,"),
                new IGitLabApiWrapper.Line(81, "           \"prod-cordova\": {")
        );
    }

    @Test
    public void testCorrectWindowsEOL() {
        Assertions.assertThat(PatchUtils.getPositionsFromPatch("@@ -78,6 +78,27 @@\n" +
                "\t\t\t\t\"src/styles.scss\",\r\n" +
                "                \"src/cordova-styles.scss\"\r\n" +
                "              ]\r\n" +
                "            },\r\n" +
                "+           \"prod-cordova\": {\r\n" +
                "+             \"optimization\": true,\r\n" +
                "+             \"outputHashing\": \"all\",\r\n" +
                "              \"sourceMap\": false,\r\n" +
                "              \"extractCss\": true,\r\n" +
                "              \"namedChunks\": false,\r\n" +
                "              \"aot\": true,\r\n" +
                "              \"extractLicenses\": true,\r\n" +
                "              \"vendorChunk\": false,\r\n" +
                "              \"buildOptimizer\": true,\r\n" +
                "              \"fileReplacements\": [\r\n" +
                "                {\r\n" +
                "                  \"replace\": \"src/environments/environment.ts\",\r\n" +
                "                  \"with\": \"src/environments/environment.prod-cordova.ts\"\r\n" +
                "                }\r\n" +
                "              ],\r\n" +
                "              \"styles\": [\rn" +
                "                \"src/styles.scss\",\r\n" +
                "                \"src/cordova-styles.scss\"\r\n" +
                "              ]\r\n" +
                "            }\r\n" +
                "          }\r\n" +
                "        },")).isNotEmpty().hasSize(3).containsExactlyInAnyOrder(
                new IGitLabApiWrapper.Line(83, "             \"outputHashing\": \"all\","),
                new IGitLabApiWrapper.Line(82, "             \"optimization\": true,"),
                new IGitLabApiWrapper.Line(81, "           \"prod-cordova\": {")
        );
    }
}
