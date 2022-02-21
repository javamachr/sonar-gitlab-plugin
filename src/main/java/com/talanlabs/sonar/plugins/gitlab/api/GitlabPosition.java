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

public class GitlabPosition {
  

    @JsonProperty("base_sha")
    private String baseSha;

    @JsonProperty("start_sha")
    private String startSha;

    @JsonProperty("head_sha")
    private String headSha;

    @JsonProperty("position_type")
    private String positionType;

    @JsonProperty("new_path")
    private String newPath;

    @JsonProperty("new_line")
    private Integer newLine;

    @JsonProperty("old_path")
    private String oldPath;

    @JsonProperty("old_line")
    private Integer oldLine;

    private Integer width;
    private Integer height;
    private Integer x;
    private Integer y;

    public String getBaseSha() {
        return baseSha;
    }

    public void setBaseSha(String baseSha) {
        this.baseSha = baseSha;
    }

    public String getStartSha() {
        return startSha;
    }

    public void setStartSha(String startSha) {
        this.startSha = startSha;
    }

    public String getHeadSha() {
        return headSha;
    }

    public void setHeadSha(String headSha) {
        this.headSha = headSha;
    }

    public String getPositionType() {
        return positionType;
    }

    public void setPositionType(String positionType) {
        this.positionType = positionType;
    }

    public String getNewPath() {
        return newPath;
    }

    public void setNewPath(String newPath) {
        this.newPath = newPath;
    }

    public Integer getNewLine() {
        return newLine;
    }

    public void setNewLine(Integer newLine) {
        this.newLine = newLine;
    }

    public String getOldPath() {
        return oldPath;
    }

    public void setOldPath(String oldPath) {
        this.oldPath = oldPath;
    }

    public Integer getOldLine() {
        return oldLine;
    }

    public void setOldLine(Integer oldLine) {
        this.oldLine = oldLine;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }
}

