name := "proofbox"
organization := "com.github.rootmos"
version := "0.0.1"

val theScalaVersion = "2.11.8"

lazy val macros = (project in file("macros")).settings(
  scalaVersion := theScalaVersion,
  libraryDependencies += "org.scala-lang" % "scala-reflect" % theScalaVersion 
)

lazy val core = (project in file("core"))
  .dependsOn(macros)
  .settings( 
    scalaVersion := theScalaVersion,
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "2.2.6" % "test",
      "com.chuusai" %% "shapeless" % "2.3.2" % "test"
    )
  )
