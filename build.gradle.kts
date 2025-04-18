import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
  java
  application
  id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.product.affiliation"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val vertxVersion = "4.5.10"
val junitJupiterVersion = "5.9.1"
val jacksonVersion = "2.9.9"
val fulcrumVersion = "0.39.0"

val mainVerticleName = "com.product.affiliation.MainVerticle"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

application {
  mainClass.set(launcherClassName)
}

dependencies {
  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  implementation("io.vertx:vertx-mongo-client")
  implementation("io.vertx:vertx-web")
  implementation("io.vertx:vertx-web-client")
  implementation("javax.servlet:javax.servlet-api:4.0.1")
  implementation("org.testcontainers:testcontainers:1.16.2")
  implementation("org.testcontainers:mongodb:1.16.2")
  implementation("com.google.inject:guice:5.0.1")
  implementation("com.fasterxml.jackson.core:jackson-databind:${jacksonVersion}")
  implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:${jacksonVersion}")
  implementation("com.obsidiandynamics.fulcrum:fulcrum-worker:${fulcrumVersion}")
  implementation("com.obsidiandynamics.fulcrum:fulcrum-json:${fulcrumVersion}")
  implementation("com.obsidiandynamics.fulcrum:fulcrum-constraints:${fulcrumVersion}")
  implementation("io.vertx:vertx-hazelcast:4.5.13")

  testImplementation("io.vertx:vertx-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter-api:${junitJupiterVersion}")
  testImplementation("org.junit.jupiter:junit-jupiter-engine:${junitJupiterVersion}")
  testImplementation("org.mockito:mockito-junit-jupiter:4.0.0")
  testImplementation("org.testcontainers:junit-jupiter:1.16.2")
  testImplementation("org.testcontainers:mongodb:1.19.0")
}

java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main-Verticle" to mainVerticleName))
  }
  mergeServiceFiles()
}

tasks.withType<Test> {
  useJUnit()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

tasks.withType<JavaExec> {
  args = listOf("run", mainVerticleName, "--redeploy=$watchForChange", "--launcher-class=$launcherClassName", "--on-redeploy=$doOnChange")
}
