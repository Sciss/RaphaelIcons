name            := "SVGPathToJava2D"

organization    := "de.sciss"

version         := "0.1.0-SNAPSHOT"

scalaVersion    := "2.10.3"

retrieveManaged := true

libraryDependencies ++= Seq(
  "org.apache.xmlgraphics" % "batik-parser" % "1.7",
  "org.scala-lang" % "scala-swing" % scalaVersion.value
)
