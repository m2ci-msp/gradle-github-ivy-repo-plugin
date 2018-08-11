package org.m2ci.msp.github

import org.gradle.api.Plugin
import org.gradle.api.Project

class GithubIvyRepoPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.repositories.metaClass.github { user ->
            delegate.ivy {
                name "github/$user"
                url "https://github.com/$user"
            }
        }
    }
}
