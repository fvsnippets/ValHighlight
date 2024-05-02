import org.jetbrains.changelog.Changelog.OutputType.HTML
import org.jetbrains.changelog.markdownToHTML

plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.17.3"
    id("org.jetbrains.changelog") version "2.2.0"
}

val pluginGroup: String by project
val pluginVersion: String by project
val pluginRepositoryUrl: String by project
val pluginSinceBuild: String by project
val platformVersion: String by project
val platformType: String by project
val platformPlugins: String by project
val javaVersion: String by project

group = pluginGroup
version = pluginVersion

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

intellij {
    val pluginName: String by project
    this.pluginName = pluginName
    version.set(platformVersion)
    type.set(platformType)
    updateSinceUntilBuild.set(false)
    plugins.set(platformPlugins.split(',').map(String::trim).filter(String::isNotEmpty))
}

changelog {
    groups.set(emptyList())
    version.set(pluginVersion)
    repositoryUrl.set(pluginRepositoryUrl)
}

tasks {
      buildSearchableOptions {
          enabled = false
      }

    wrapper {
        val gradleVersion: String by project
        setGradleVersion(gradleVersion)
    }

    patchPluginXml {
        version.set(pluginVersion)
        sinceBuild.set(pluginSinceBuild)

        pluginDescription.set(
            markdownToHTML(
                "Highlights Lombok's val in Java classes just as if it were a standard keyword.  \n" +
                "&nbsp;  \n" +
                "_Notice: At least syntax highlighting needs to be enabled on editor._"
            )
        )

        changeNotes.set(provider {
            with(changelog) {
                renderItem(
                    getOrNull(pluginVersion) ?: getLatest(),
                    HTML
                )
            }
        })
    }
}
