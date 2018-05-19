name := "untitled"

version := "0.1"

scalaVersion := "2.12.3"


unmanagedJars in Compile += Attributed.blank(file(System.getenv("JAVA_HOME") + "/jre/lib/ext/jfxrt.jar"))

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

libraryDependencies ++= Seq(
  "org.scalafx" %% "scalafx" % "8.0.102-R11",
  "org.scalafx" %% "scalafxml-core-sfx8" % "0.4",
  "com.typesafe.akka" %% "akka-actor" % "2.5.6",
  "com.typesafe.akka" %% "akka-remote" % "2.5.6",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.6",
  "com.github.nscala-time" %% "nscala-time" % "2.18.0"
)
fork := true
: