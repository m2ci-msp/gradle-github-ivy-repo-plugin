package org.m2ci.msp.github

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin

class GithubIvyRepoPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.pluginManager.apply BasePlugin

        project.repositories.metaClass.github { user ->
            delegate.ivy {
                name "github/$user"
                url "https://github.com/$user"
                layout 'pattern', {
                    artifact '[module]/releases/download/v[revision]/[artifact]-[revision](-[classifier]).[ext]'
                    ivy '[module]/releases/download/v[revision]/ivy(-[revision]).xml'
                }
            }
        }
    }
}
