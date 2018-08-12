package org.m2ci.msp.github

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
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

    @Test
    void testResolution() {
        def projectDir = File.createTempDir()
        new File(projectDir, 'build.gradle').withWriter { buildScript ->
            buildScript << """|plugins {
                              |    id 'org.m2ci.msp.github-ivy-repo'
                              |}
                              |
                              |repositories {
                              |    github 'psibre'
                              |}
                              |
                              |dependencies {
                              |    'default' group: 'de.dfki.mary', name: 'cmu-awb-time-data', version: '0.1'
                              |}
                              |
                              |task testResolution(type: Copy) {
                              |    from configurations.'default'
                              |    into buildDir
                              |    doLast {
                              |        def flacFile = file("\$buildDir/cmu-awb-time-data-0.1-flac.flac")
                              |        def yamlZipFile = file("\$buildDir/cmu-awb-time-data-0.1-yaml.zip")
                              |        assert flacFile.exists()
                              |        assert yamlZipFile.exists()
                              |        ant.checksum file: flacFile, algorithm: 'SHA-1', property: 'flacFileSha1'
                              |        ant.checksum file: yamlZipFile, algorithm: 'SHA-1', property: 'yamlZipFileSha1'
                              |        assert ant.properties.flacFileSha1 == '4d41b934c5c3beb3dc764a564624731d0059cdb4'
                              |        assert ant.properties.yamlZipFileSha1 == '291c160ac05e50de83e27255abf510843e30390a'
                              |    }
                              |}
                              |""".stripMargin()
        }
        def gradle = GradleRunner.create().withProjectDir(projectDir).withPluginClasspath()
        assert gradle.build()
        def result = gradle.withArguments('--gradle-user-home', "$projectDir/.gradleHome", 'testResolution').build()
        println result.output
        assert result.task(':testResolution').outcome == TaskOutcome.SUCCESS
    }
}
