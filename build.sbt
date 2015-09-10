import com.typesafe.sbt.SbtAspectj._
import com.typesafe.sbt.SbtAspectj.AspectjKeys

val kamonVersion = "0.5.1"

lazy val root = (project in file("."))
  .settings(
    name := "kamon-spray-example",
    version := "1.0",
    scalaVersion := "2.11.7",
    resolvers += "spray repo" at "http://repo.spray.io",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" % "akka-actor_2.11" % "2.3.12",
      "io.spray" %% "spray-can" % "1.3.3",
      "io.spray" %% "spray-routing" % "1.3.3",
      "io.kamon" %% "kamon-core" % kamonVersion,
      "io.kamon" %% "kamon-system-metrics" % kamonVersion,
      "io.kamon" %% "kamon-scala" % kamonVersion,
      "io.kamon" %% "kamon-akka" % kamonVersion,
      "io.kamon" %% "kamon-spray" % kamonVersion,
      "io.kamon" %% "kamon-datadog" % kamonVersion,
      "io.kamon" %% "kamon-log-reporter" % kamonVersion
    ),
    aspectjSettings,
    javaOptions in run <++= AspectjKeys.weaverOptions in Aspectj,
    fork in run := true
  )
