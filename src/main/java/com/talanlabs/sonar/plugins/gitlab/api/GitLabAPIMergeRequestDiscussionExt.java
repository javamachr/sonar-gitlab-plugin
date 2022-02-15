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

import com.talanlabs.gitlab.api.Paged;
import com.talanlabs.gitlab.api.v4.GitLabAPI;
import com.talanlabs.gitlab.api.v4.Pagination;
import com.talanlabs.gitlab.api.v4.http.Query;
import com.talanlabs.gitlab.api.v4.utils.QueryHelper;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GitLabAPIMergeRequestDiscussionExt {

	private static final Logger LOG = Loggers.get(GitLabAPIMergeRequestDiscussionExt.class);

	private static final String BASE_URL = "/projects/%s/merge_requests/%d/discussions";

	private final GitLabAPI gitLabAPI;
	static List<GitlabDiscussionStatus> discussions;

	public GitLabAPIMergeRequestDiscussionExt(GitLabAPI gitLabAPI) {
		this.gitLabAPI = gitLabAPI;
	}

//
//    /**
//     * Gets a list of all discussions for a single merge request.
//     * <p>
//     * GET /projects/:id/merge_requests/:merge_request_iid/discussions
//     *
//     * @param projectId  (required) - The ID or URL-encoded path of the project
//     * @param iid        (required) - The IID of a merge request
//     * @param pagination (optional) - The ID of a discussion
//     * @return Paged object of {@link GitlabDiscussionStatus} instances
//     * @throws IOException
//     */
	public Paged<GitlabDiscussionStatus> getAllDiscussions(Serializable projectId, Integer iid, Pagination pagination)
			throws IOException {
		Query query = QueryHelper.getQuery(pagination);
		String tailUrl = String.format(BASE_URL + "%s", gitLabAPI.sanitize(projectId), iid, query.build());
		return gitLabAPI.retrieve().toPaged(tailUrl, GitlabDiscussionStatus[].class);
	}

	public Boolean hasDiscussion(Integer projectId, Integer mergeRequestIid, String fullPath, Integer lineNumber,
			String body, String baseSha, String headSha) throws IOException {

		if (discussions == null) {

			LOG.debug("gettting existing Merge Request discussions");

			Paged<GitlabDiscussionStatus> paged = getAllDiscussions(projectId, mergeRequestIid, null);

			do {
				if (paged.getResults() != null) {
					if (discussions == null) {
						discussions = new ArrayList<>();
					}
					discussions.addAll(paged.getResults());
				}
			} while ((paged = paged.nextPage()) != null);


			if (LOG.isDebugEnabled()) {
				LOG.debug("Existing count {}", discussions.size());
				discussions.forEach(item -> {
					LOG.debug("discussion {} note count {}", item.getId(), item.getNotes().size());
					item.getNotes().forEach(note -> {
						GitlabPosition position = note.getPosition();
						if (position != null) {
							LOG.debug("-File: {} {}", position.getNewPath(), position.getNewLine());
							LOG.debug("-bSha: {}, hSha {}", position.getBaseSha(), position.getHeadSha());
						}
						LOG.debug("-Note {}: {}",note.getBody().length(), note.getBody());
					});
				});

				LOG.debug("Current Issue Comment:");
				LOG.debug("Path {} {}", fullPath, lineNumber);
				LOG.debug("bSha: {}, hSha {}", baseSha, headSha);
				LOG.debug("Note {}: {}",body.length(), body);
			}
		}

		boolean isExist = discussions.stream().anyMatch(x ->
			x.getNotes().stream().anyMatch(n ->
				   	body.equals(n.getBody())
				   	&& n.getPosition() != null
					&& lineNumber.equals(n.getPosition().getNewLine())
					&& baseSha.equals(n.getPosition().getBaseSha())
					&& headSha.equals(n.getPosition().getHeadSha())
					&& fullPath.equals(n.getPosition().getNewPath())
				)
			);

		if(isExist)
			LOG.debug("-------------------Issue Comment already exist on MR----------------------");

		return isExist;
	}
}
