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

    @Test
    void testRepository() {
        def projectDir = File.createTempDir()
        new File(projectDir, 'build.gradle').withWriter { buildScript ->
            buildScript << """|plugins {
                              |    id 'org.m2ci.msp.github-ivy-repo'
                              |}
                              |
                              |repositories {
                              |    github 'fnord'
                              |}
                              |
                              |task testRepository {
                              |    doLast {
                              |        assert repositories.find { it.name == 'github/fnord' }.url as String == 'https://github.com/fnord'
                              |        assert repositories.find { it.url as String == 'https://github.com/fnord' }.name == 'github/fnord'
                              |    }
                              |}
                              |""".stripMargin()
        }
        def gradle = GradleRunner.create().withProjectDir(projectDir).withPluginClasspath()
        assert gradle.build()
        assert gradle.withArguments('testRepository').build()
    }
}
