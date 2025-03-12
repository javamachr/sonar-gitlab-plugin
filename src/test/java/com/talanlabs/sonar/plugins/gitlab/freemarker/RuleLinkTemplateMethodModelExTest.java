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
package com.talanlabs.sonar.plugins.gitlab.freemarker;

import com.talanlabs.sonar.plugins.gitlab.GitLabPlugin;
import com.talanlabs.sonar.plugins.gitlab.GitLabPluginConfiguration;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateModelException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.CoreProperties;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.config.PropertyDefinitions;
import org.sonar.api.config.internal.MapSettings;
import org.sonar.api.utils.System2;

import java.util.Collections;
import java.util.List;

public class RuleLinkTemplateMethodModelExTest {

    private RuleLinkTemplateMethodModelEx ruleLinkTemplateMethodModelEx;

    @Before
    public void setUp() {
        MapSettings settings = new MapSettings(new PropertyDefinitions(System2.INSTANCE, PropertyDefinition.builder(CoreProperties.SERVER_BASE_URL).name("Server base URL")
                .description("HTTP URL of this SonarQube server, such as <i>http://yourhost.yourdomain/sonar</i>. This value is used i.e. to create links in emails.")
                .category(CoreProperties.CATEGORY_GENERAL).defaultValue("http://localhost:9000").build()).addComponents(GitLabPlugin.definitions()));

        settings.setProperty(CoreProperties.SERVER_BASE_URL, "http://myserver");

        GitLabPluginConfiguration gitLabPluginConfiguration = new GitLabPluginConfiguration(settings.asConfig(),new System2());

        ruleLinkTemplateMethodModelEx = new RuleLinkTemplateMethodModelEx(gitLabPluginConfiguration);
    }

    private String ruleLink(List<Object> arguments) {
        try {
            return (String) ruleLinkTemplateMethodModelEx.exec(arguments);
        } catch (TemplateModelException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSuccess() {
        Assertions.assertThat(ruleLink(Collections.singletonList(new SimpleScalar("http://rien")))).isEqualTo("http://myserver/coding_rules#rule_key=http%3A%2F%2Frien");
    }

    @Test
    public void testFailed() {
        Assertions.assertThatThrownBy(() -> ruleLink(Collections.emptyList())).hasCauseInstanceOf(TemplateModelException.class);
        Assertions.assertThatThrownBy(() -> ruleLink(Collections.singletonList(null))).hasCauseInstanceOf(TemplateModelException.class);
    }
}
