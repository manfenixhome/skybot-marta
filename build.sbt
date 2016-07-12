name := """marta"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.webjars.bower" % "compass-mixins" % "1.0.2",
  "org.webjars.bower" % "bootstrap-sass" % "3.3.6",
  "org.mongodb" %% "casbah" % "2.7.4",
  "com.gilt" % "jerkson_2.11" % "0.6.6",
  "org.postgresql" % "postgresql" % "9.4-1204-jdbc4",
  "com.typesafe.play" %% "anorm" % "2.5.0",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
