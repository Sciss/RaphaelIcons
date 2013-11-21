name            in ThisBuild   := "SVGPathToJava2D"

organization    in ThisBuild := "de.sciss"

version         in ThisBuild := "0.1.0-SNAPSHOT"

scalaVersion    in ThisBuild := "2.10.3"

retrieveManaged in ThisBuild := true

// lazy val ugenGenerator = TaskKey[Seq[File]]("ugen-generate", "Generate UGen class files")

// ---- sub-projects ----

lazy val root = project.in(file("."))
  .aggregate(generate, core)
  .settings(
    packagedArtifacts := Map.empty
  )

lazy val generate = project.in(file("generate")).settings(
  description := "Icon source code generator",
  libraryDependencies ++= Seq(
    "org.apache.xmlgraphics" % "batik-parser" % "1.7"
  )
)

lazy val core = project.in(file("core")) /* .dependsOn(generate) */ .settings(
  description := "A collection of icons",
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-swing" % scalaVersion.value % "test"
  )
)
