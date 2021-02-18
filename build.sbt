lazy val projectName = "RaphaelIcons"

name := projectName

lazy val projectVersion = "1.0.7"
lazy val mimaVersion    = "1.0.1"

// ---- dependencies ----

lazy val deps = new {
  val gen = new {
    val batik     = "1.13"
  }
  val test = new {
    val swingPlus = "0.5.0"
    val submin    = "0.2.5"
  }
}

// ---- base settings ----

def basicJavaOpts = Seq("-source", "1.8")

// sonatype plugin requires that these are in global
ThisBuild / version      := projectVersion
ThisBuild / organization := "de.sciss"

lazy val commonSettings = Seq(
//  version            := projectVersion,
//  organization       := "de.sciss",
  description        := "Icon set designed by Dmitry Baranovskiy",
  homepage           := Some(url(s"https://git.iem.at/sciss/$projectName")),
  licenses           := Seq("LGPL v2.1+" -> url("http://www.gnu.org/licenses/lgpl-2.1.txt")),
  scalaVersion       := "2.13.4",
  crossScalaVersions := Seq("3.0.0-RC1", "2.13.4", "2.12.13"),
  javacOptions                   := basicJavaOpts ++ Seq("-target", "1.8", "-encoding", "UTF-8"),
  javacOptions in (Compile, doc) := basicJavaOpts,
  // retrieveManaged := true,
  scalacOptions  ++= Seq(
    // "-Xelide-below", "INFO", // elide debug logging!
    "-deprecation", "-unchecked", "-feature", "-Xlint", "-Xsource:2.13"
  ),
  scalacOptions in (Compile, compile) ++=
    (if (!isDotty.value && scala.util.Properties.isJavaAtLeast("9")) Seq("-release", "8") else Nil), // JDK >8 may break API
) ++ publishSettings

// ---- sub-projects ----

lazy val root = project.withId("root").in(file("."))
  .aggregate(core, gen)
  .settings(commonSettings)
  .settings(
    packagedArtifacts := Map.empty
  )

lazy val java2DGenerator = TaskKey[Seq[File]]("java2d-generate", "Generate Icon Java2D source code")

lazy val core = project.withId("raphael-icons").in(file("core"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "de.sciss" %% "swingplus" % deps.test.swingPlus % Test,
      "de.sciss" %  "submin"    % deps.test.submin    % Test
    ),
    mimaPreviousArtifacts := Set("de.sciss" %% "raphael-icons" % mimaVersion),
    sourceGenerators in Compile += (java2DGenerator in Compile).taskValue,
    java2DGenerator in Compile := {
      val spec = (resourceDirectory   in Compile in gen).value
      val src  = (sourceManaged       in Compile).value
      val cp   = (dependencyClasspath in Runtime in gen).value
      val st   = streams.value
      runJava2DGenerator(spec, src, cp.files, st.log)
    }
  )

lazy val publishSettings = Seq(
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
  developers := List(
    Developer(
      id    = "sciss",
      name  = "Hanns Holger Rutz",
      email = "contact@sciss.de",
      url   = url("https://www.sciss.de"),
    ),
    Developer(
      id    = "DmitryBaranovskiy",
      name  = "Dmitry Baranovskiy",
      email = "dmitry@baranovskiy.com",
      url   = url("http://dmitry.baranovskiy.com/"),
    ),
  ),
  scmInfo := {
    val h = "git.iem.at"
    val a = s"sciss/$projectName"
    Some(ScmInfo(url(s"https://$h/$a"), s"scm:git@$h:$a.git"))
  },
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
    val fOpt  = ForkOptions(javaHome = Option.empty[File], outputStrategy = Some(outs), bootJars = cp.toVector,
      workingDirectory = Option.empty[File], runJVMOptions = Vector.empty[String], connectInput = false, envVars = Map.empty[String, String])
    val res: Int = Fork.scala(config = fOpt, arguments = mainClass :: Nil)
    if (res != 0) {
      sys.error(s"Java2D class file generator failed with exit code $res")
    }
  } finally {
    os.close()
  }
  val sources = outFile :: Nil
  sources
}

lazy val gen = project.withId("generate").in(file("generate"))
  .settings(commonSettings)
  .settings(
    crossScalaVersions := Seq("2.13.4"),
    packagedArtifacts := Map.empty,    // don't send this to Sonatype
    libraryDependencies ++= Seq(
      "org.apache.xmlgraphics"  % "batik-parser"      % deps.gen.batik,
      "org.scala-lang"          %  "scala-compiler"   % "2.13.4", // scalaVersion.value, // needed for Fork.scala -- note: sbt cross broken; we have to use explicit version here
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

