[![Build Status](https://travis-ci.org/psibre/gradle-github-ivy-repo-plugin.svg?branch=master)](https://travis-ci.org/psibre/gradle-github-ivy-repo-plugin)

Gradle GitHub Ivy Repo Plugin
=============================

Easily resolve GitHub release assets as dependencies in Gradle

Usage
-----

See [here](https://plugins.gradle.org/plugin/org.m2ci.msp.github-ivy-repo) how to apply this plugin.

GitHub release assets can be resolved and cached as dependencies by adding an Ivy repository with a custom [pattern layout].
This plugin adds a new `github(USER)` method to a Gradle project's `repositories` block so that this can work with minimal overhead.

### Example

If GitHub user **foo** has a repository **bar** with a [release] tagged **v1.0**, and that release has assets (e.g., `bar-1.0.zip`) that include an [Ivy descriptor] (i.e., `ivy-1.0.xml`), then those assets can be resolved from the URLs
- https://github.com/foo/bar/releases/download/v1.0/ivy-1.0.xml
- https://github.com/foo/bar/releases/download/v1.0/bar-1.0.zip
- etc.

In your Gradle build script, you can apply this plugin and then add a repository like this:
```groovy
repositories {
    github('foo')
}
```
Then, you can declare a dependency on **bar** (for some configuration, e.g., `data`) like this:
```groovy
dependencies {
    data group: 'io.github.foo', name: 'bar', version: '1.0'
}
```
The `group` must match the `organisation` in the Ivy descriptor.

[pattern layout]: https://docs.gradle.org/4.9/userguide/repository_types.html#sec:defining_custom_pattern_layout_for_an_ivy_repository
[release]: https://help.github.com/articles/about-releases/
[Ivy descriptor]: http://ant.apache.org/ivy/history/2.5.0-rc1/ivyfile.html
