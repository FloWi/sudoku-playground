import com.typesafe.sbt.packager.universal.UniversalKeys
import play.sbt.PlayImport._
import play.sbt.PlayScala
import sbt.Keys._
import sbt._

object SudokuBuild extends Build with UniversalKeys {

  val appName = "sudoku-playground"

  lazy val root =
    project.in(file("."))
      .settings(commonSettings:_*)
      .aggregate(server)

  /**
    * Define the server project as a Play application.
    */
  lazy val server =
    project.in(file("server"))
      .settings(serverSettings:_*)
      .enablePlugins(PlayScala)

  /**
    * The settings for the server module
    */
  lazy val serverSettings = commonSettings ++ Seq(
    name := s"$appName-server",
    libraryDependencies ++= Dependencies.serverDeps
  )
  /**
    * The setting that will be applied to all sub projects
    */
  lazy val commonSettings = Seq(
    organization := "de.flwi",
    version := "1.0-SNAPSHOT",
    name := appName,
    scalaVersion := "2.11.7"
  )

  /**
    * A simple container object for the dependencies.
    */
  object Dependencies {

    lazy val serverDeps = Seq(
      jdbc,
      cache,
      ws,
      specs2 % Test
    )
  }
}

