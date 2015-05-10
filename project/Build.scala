import sbt._
import Keys._
import Tests._
import scala.util.Properties

/**
 * This is a simple sbt setup generating Slick code from the given
 * database before compiling the projects code.
 */
object myBuild extends Build {
	import Repositories._
	import SlickConfig._
	
  lazy val mainProject = Project(
    id="scaldi-slick",
    base=file("."),
    settings = Project.defaultSettings ++ Seq(
      scalaVersion := "2.11.2",
      libraryDependencies ++= List(
		  "com.typesafe.slick" %% "slick" % "3.0.0-RC3",
		  "com.typesafe.slick" %% "slick-codegen" % "3.0.0-RC3",
		  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
		  "org.slf4j" % "slf4j-nop" % "1.6.4",
		  "com.h2database" % "h2" % "1.3.170",
		  "com.h2database" % "h2" % "1.4.181",
		  "com.h2database" % "h2" % "1.4.181",
		  "org.specs2" %% "specs2" % "2.4.2" % "test",
		  "org.postgresql" % "postgresql" % "9.3-1102-jdbc41",
		  "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test",
		  "org.slf4j" % "jcl-over-slf4j" % "1.7.7",
		  "org.slf4j" % "slf4j-api" % "1.7.7",
		  "com.typesafe.scala-logging" %% "scala-logging" % "3.0.0",
		  "ch.qos.logback" % "logback-classic" % "1.1.2",
		  "ch.qos.logback" % "logback-core" % "1.1.2",
		  "io.spray" %% "spray-routing" % "1.3.1" % "compile" withSources(),
		  "io.spray" %% "spray-http" % "1.3.1" % "compile" withSources(),
		  "io.spray" %% "spray-httpx" % "1.3.1" % "compile" withSources(),
		  "io.spray" %% "spray-can" % "1.3.1" % "compile" withSources(),
		  "io.spray" %% "spray-io" % "1.3.1" % "compile" withSources(),
		  "io.spray" %% "spray-caching" % "1.3.1" % "compile" withSources(),
		  "io.spray" %% "spray-client" % "1.3.1" % "compile" withSources(),
		  "io.spray" %% "spray-util" % "1.3.1" % "compile" withSources(),
		  "io.spray" %% "spray-testkit" % "1.3.1" % "test" withSources(),
		  "org.scaldi" %% "scaldi-akka" % "0.4",
		  "com.typesafe.akka" %% "akka-actor" % "2.3.6",
		  "com.typesafe.akka" %% "akka-slf4j" % "2.3.6",
		  "org.json4s" %% "json4s-jackson" % "3.2.10",
		  "com.novus"  %% "salat-core" % "1.9.9",
		  "com.novus"  %% "salat-util" % "1.9.9",
		  "org.mindrot" % "jbcrypt" % "0.3m",
		  "com.github.nscala-time" %% "nscala-time" % "1.4.0",
		  "commons-validator" % "commons-validator" % "1.4.0",
		  "org.apache.commons" % "commons-email" % "1.3.3",
		  "org.apache.httpcomponents" % "httpclient" % "4.3.5",
		  "org.scala-lang.modules" %% "scala-xml" % "1.0.2",
		  "org.postgresql" % "postgresql" % "9.3-1102-jdbc41",
		  "org.yaml" % "snakeyaml" % "1.5"
      ),
      slick <<= slickCodeGenTask, // register manual sbt command
      sourceGenerators in Compile <+= slickCodeGenTask // register automatic code generation on every compile, remove for only manual use
    )
  ) 
  }
  object Repositories {
  val reps = Seq(
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
    "Sonatype OSS" at "https://oss.sonatype.org/content/repositories/releases/",
    "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
    "Spray repo" at "http://repo.spray.cc")
}

 object SlickConfig {
  val B2B_SQL_URL = Properties.envOrElse("B2B_SQL_URL", "jdbc:postgresql://localhost:5432/satya")
  val B2B_SQL_USER = Properties.envOrElse("B2B_SQL_USER", "postgres")
  val B2B_SQL_PASS = Properties.envOrElse("B2B_SQL_PASS", "abiram06")

  val slick = TaskKey[Seq[File]]("gen-tables")
  val slickCodeGenTask = (sourceManaged, dependencyClasspath in Compile, runner in Compile, streams) map { (dir, cp, r, s) =>
    val outputDir = (dir / "scaldi-slick").getPath // place generated files in sbt's managed sources folder
    val jdbcDriver = "org.postgresql.Driver"
    val slickDriver = "slick.driver.PostgresDriver"
    val pkg = "demo"
    toError(r.run("slick.codegen.SourceCodeGenerator", cp.files,
      Array(slickDriver, jdbcDriver, B2B_SQL_URL, outputDir, pkg, B2B_SQL_USER, B2B_SQL_PASS), s.log))
    val fname = outputDir + "/demo/Tables.scala"
    Seq(file(fname))
  }
}