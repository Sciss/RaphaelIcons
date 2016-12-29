lazy val projectName = "RaphaelIcons"

name := projectName

lazy val projectVersion = "1.0.4"
lazy val mimaVersion    = "1.0.1"

// ---- test dependencies ----

lazy val swingPlusVersion = "0.2.2"
lazy val subminVersion    = "0.1.0"

// ---- base settings ----

def basicJavaOpts = Seq("-source", "1.6")

lazy val commonSettings = Seq(
  version            := projectVersion,
  organization       := "de.sciss",
  description        := "Icon set designed by Dmitry Baranovskiy",
  homepage           := Some(url(s"https://github.com/Sciss/$projectName")),
  licenses           := Seq("LGPL v2.1+" -> url("http://www.gnu.org/licenses/lgpl-2.1.txt")),
  scalaVersion       := "2.11.8",
  crossScalaVersions := Seq("2.12.1", "2.11.8", "2.10.6"),
  javacOptions                   := basicJavaOpts ++ Seq("-target", "1.6", "-encoding", "UTF-8"),
  javacOptions in (Compile, doc) := basicJavaOpts,
  // retrieveManaged := true,
  scalacOptions  ++= Seq(
    // "-Xelide-below", "INFO", // elide debug logging!
    "-deprecation", "-unchecked", "-feature", "-Xfuture", "-Xlint"
  )
)

// ---- sub-projects ----

lazy val root = Project(id = "root", base = file("."))
  .aggregate(core, gen)
  .settings(commonSettings)
  .settings(
    packagedArtifacts := Map.empty
  )

lazy val java2DGenerator = TaskKey[Seq[File]]("java2d-generate", "Generate Icon Java2D source code")

lazy val core = Project(id = "raphael-icons", base = file("core"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "de.sciss" %% "swingplus" % swingPlusVersion % "test",
      "de.sciss" %  "submin"    % subminVersion    % "test"
    ),
    mimaPreviousArtifacts := Set("de.sciss" %% "raphael-icons" % mimaVersion),
    sourceGenerators in Compile <+= (java2DGenerator in Compile),
    java2DGenerator in Compile <<=
      (resourceDirectory   in Compile in gen,
       sourceManaged       in Compile,
       dependencyClasspath in Runtime in gen,
       streams) map {
        (spec, src, cp, st) => runJava2DGenerator(spec, src, cp.files, st.log)
      },
    // ---- publishing ----
    // publishArtifact in (Compile, packageDoc) := false,
    // publishArtifact in (Compile, packageSrc) := false,
    publishArtifact in Test := false,
    publishMavenStyle := true,
    publishTo :=
      Some(if (isSnapshot.value)
        "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
      else
        "Sonatype Releases"  at "https://oss.sonatype.org/service/local/staging/deploy/maven2"
      ),
    pomIncludeRepository := { _ => false },
    pomExtra := { val n = projectName
<scm>
  <url>git@github.com:Sciss/{n}.git</url>
  <connection>scm:git:git@github.com:Sciss/{n}.git</connection>
</scm>
<developers>
  <developer>
    <id>sciss</id>
    <name>Hanns Holger Rutz</name>
    <url>http://www.sciss.de</url>
  </developer>
  <developer>
    <id>DmitryBaranovskiy</id>
    <name>Dmitry Baranovskiy</name>
    <url>http://dmitry.baranovskiy.com/</url>
  </developer>
</developers>
    }
  )


def runJava2DGenerator(specDir: File, outputDir: File, cp: Seq[File], log: Logger): Seq[File] = {
  val outDir2     = outputDir / /* "java" / */ "de" / "sciss" / "icons" / "raphael"
  outDir2.mkdirs()
  val outFile     = outDir2 / "Shapes.java"
  val mainClass   = "de.sciss.icons.raphael.Generate"
  val os          = new java.io.FileOutputStream(outFile)
  log.info("Generating Java2D source code...")
  try {
    val outs  = CustomOutput(os)  // Generate.scala writes the class source file to standard out
    val p     = new Fork.ForkScala(mainClass).fork(javaHome = None, jvmOptions = Nil, scalaJars = cp,
      arguments = Nil, workingDirectory = None,
      connectInput = false, outputStrategy = outs)
    val res = p.exitValue()
    if (res != 0) {
      sys.error("Java2D class file generator failed with exit code " + res)
    }
  } finally {
    os.close()
  }
  val sources = outFile :: Nil
  sources
}

lazy val gen = Project(id = "generate", base = file("generate"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "org.apache.xmlgraphics" % "batik-parser" % "1.7"
    ),
    initialCommands in console :=
      """import org.apache.batik.parser.{PathHandler, PathParser}
        |import de.sciss.icons.raphael._
        |def path(str: String, scala: Boolean = true): String = {
        |  val p = new PathParser
        |  val h = new Generate.Handler
        |  if (scala) h.eol = "\n"
        |  p.setPathHandler(h)
        |  p.parse(str)
        |  h.result()
        |}
        |""".stripMargin
  )

