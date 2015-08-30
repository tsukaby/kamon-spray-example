name := "kamon-spray-example"

version := "1.0"

scalaVersion := "2.11.7"

resolvers += "spray repo" at "http://repo.spray.io"

libraryDependencies ++= Seq(
  "com.typesafe.akka" % "akka-actor_2.11" % "2.3.12",
  "io.spray" %% "spray-can" % "1.3.3",
  "io.spray" %% "spray-routing" % "1.3.3",
  "io.kamon" %% "kamon-core" % "0.5.0",
  "io.kamon" %% "kamon-system-metrics" % "0.5.0",
  "io.kamon" %% "kamon-scala" % "0.5.0",
  "io.kamon" %% "kamon-akka" % "0.5.0",
  "io.kamon" %% "kamon-spray" % "0.5.0",
  "io.kamon" %% "kamon-datadog" % "0.5.0",
  "io.kamon" %% "kamon-log-reporter" % "0.5.0"
)

aspectjSettings

javaOptions in run <++= AspectjKeys.weaverOptions in Aspectj

fork in run := true
