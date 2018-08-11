package org.m2ci.msp.github

import org.gradle.testkit.runner.GradleRunner
import org.testng.annotations.Test

class GithubIvyRepoPluginTest {

    @Test
    void testPlugin() {
        def projectDir = File.createTempDir()
        new File(projectDir, 'build.gradle').withWriter { buildScript ->
            buildScript << """|plugins {
                              |    id 'org.m2ci.msp.github-ivy-repo'
                              |}
                              |
                              |task testPlugin {
                              |    doLast {
                              |        assert pluginManager.hasPlugin('org.m2ci.msp.github-ivy-repo')
                              |    }
                              |}
                              |""".stripMargin()
        }
        def gradle = GradleRunner.create().withProjectDir(projectDir)
        assert gradle.buildAndFail()
        assert gradle.withPluginClasspath().build()
        assert gradle.withPluginClasspath().withArguments('testPlugin').build()
    }
}
