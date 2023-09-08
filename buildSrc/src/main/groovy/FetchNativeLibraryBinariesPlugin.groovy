import groovy.json.JsonSlurper
import org.gradle.api.Plugin
import org.gradle.api.Project

public class FetchNativeLibraryBinariesPlugin implements Plugin<Project> {
	private final GITHUB_REPO = 'MrDOS/nrjavaserial'
	private final DEFAULT_BRANCH = 'update-to-gradle-8'
	private final BUILD_WORKFLOW_NAME = 'Build'
	private final ARTIFACT_PREFIX = 'nrjavaserial-'

	private slurper = new JsonSlurper()

	@Override
	public void apply(Project project) {
		project.task('fetchNativeLibraryBinaries') {
			doLast {
				println findClosestNativeArtifact().archive_download_url
			}
		}
	}

	private Object findClosestNativeArtifact() {
		def artifact = findArtifactForHead()
		if (artifact != null) {
			return artifact
		}

		artifact = findArtifactForCurrentBranch()
		if (artifact != null) {
			return artifact
		}

		artifact = findArtifactForLastRelease()
		if (artifact != null) {
			return artifact
		}

		return findArtifactForDefaultBranch()
	}

	private Object findArtifactForHead() {
		// TODO
		return null
	}

	private Object findArtifactForCurrentBranch() {
		// TODO
		return null
	}

	private Object findArtifactForLastRelease() {
		// TODO
		return null
	}

	private Object findArtifactForDefaultBranch() {
		return findArtifactForBranch(DEFAULT_BRANCH)
	}

	private Object findArtifactForBranch(String branch) {
		return findArtifact("branch=${branch}")
	}

	private Object findArtifactForCommit(String commit) {
		return findArtifact("head_sha=${commit}")
	}

	private Object findArtifact(String criteria) {
		def recentActionsJSON = new URL("https://api.github.com/repos/${GITHUB_REPO}/actions/runs?status=success&${criteria}")
		def recentActions = slurper.parse(recentActionsJSON);

		def latestBuildRun = recentActions.workflow_runs.find {
			run -> run.name == BUILD_WORKFLOW_NAME
		}
		if (latestBuildRun == null) {
			return
		}

		def latestBuildArtifactsJSON = new URL(latestBuildRun.artifacts_url)
		def latestBuildArtifacts = slurper.parse(latestBuildArtifactsJSON)

		def latestCompleteBuildArtifact = latestBuildArtifacts.artifacts.find {
			artifact -> artifact.name.startsWith(ARTIFACT_PREFIX)
		}
	}
}
