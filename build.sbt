import sbt.Project.projectToRef
import sbt.project

lazy val clients = Seq(client)
lazy val scalaV = "2.11.7"

lazy val server = (project in file("server")).settings(
  scalaVersion := scalaV,
  routesImport += "config.Routes._",
  scalaJSProjects := clients,
  pipelineStages := Seq(scalaJSProd, gzip),
  resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
  libraryDependencies ++= Seq(
    filters,
    jdbc,
    evolutions,
    "com.typesafe.play" %% "anorm" % "2.5.0",
    "com.vmunier" %% "play-scalajs-scripts" % "0.3.0",
    "com.typesafe.slick" %% "slick" % "3.0.2",
    "com.typesafe.play" %% "play-slick" % "1.0.1",
    "com.lihaoyi" %% "upickle" % "0.3.4",
    "org.webjars" %% "webjars-play" % "2.4.0",
    "org.webjars" % "bootstrap" % "3.3.5",
    "org.webjars" % "jquery" % "2.1.4",
    "org.webjars" % "font-awesome" % "4.4.0"
  )
).enablePlugins(PlayScala).
  aggregate(clients.map(projectToRef): _*).
  dependsOn(sharedJvm)

lazy val client = (project in file("client")).settings(
  scalaVersion := scalaV,
  persistLauncher := true,
  persistLauncher in Test := false,
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.8.1",
    "com.lihaoyi" %%% "scalatags" % "0.5.2",
    "com.lihaoyi" %%% "scalarx" % "0.2.8",
    "be.doeraene" %%% "scalajs-jquery" % "0.8.0",
    "com.lihaoyi" %%% "upickle" % "0.3.4"
  )
).enablePlugins(ScalaJSPlugin, ScalaJSPlay).
  dependsOn(sharedJs)

lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared")).
  settings(scalaVersion := scalaV).
  jsConfigure(_ enablePlugins ScalaJSPlay)

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

// loads the jvm project at sbt startup
onLoad in Global := (Command.process("project server", _: State)) compose (onLoad in Global).value

sourcePositionMappers := Nil