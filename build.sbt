name := """play-cloudwickone-slick-template"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  cache,
  ws,
  "com.typesafe.play" %% "play-slick" % "1.1.1", // Slick 3.1
  "com.typesafe.play" %% "play-slick-evolutions" % "1.1.1",
  "org.postgresql" % "postgresql" % "9.4-1204-jdbc4",
  // web jars
  "org.webjars" %% "webjars-play" % "2.4.0-2",
  "org.webjars" % "bootstrap" % "3.3.4",
  "com.adrianhurt" %% "play-bootstrap" % "1.0-P24-B3-SNAPSHOT" exclude("org.webjars", "jquery"),
  "org.webjars" % "jquery" % "2.2.0",
  "org.webjars" % "font-awesome" % "4.5.0",
  "org.webjars" % "bootstrap-datepicker" % "1.4.0",
  specs2 % Test
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
