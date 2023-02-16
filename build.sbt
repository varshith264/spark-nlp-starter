import com.typesafe.sbt.packager.archetypes.JavaAppPackaging

enablePlugins(JavaServerAppPackaging)
enablePlugins(JavaAppPackaging)

val scalaTestVersion = "3.2.14"

name := "spark-nlp-starter"

version := "4.3.0"

scalaVersion := "2.12.15"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

licenses := Seq("Apache-2.0" -> url("https://opensource.org/licenses/Apache-2.0"))

val sparkVer = "3.3.1"
val sparkNLP = "4.3.0"

libraryDependencies ++= {
  Seq(
    "org.apache.spark" %% "spark-core" % sparkVer % Provided,
    "org.apache.spark" %% "spark-mllib" % sparkVer % Provided,
    "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
    "org.json" % "json" % "20220320",
    "com.johnsnowlabs.nlp" %% "spark-nlp" % sparkNLP)
}

/** Disables tests in assembly */
assembly / test := {}

assembly / assemblyMergeStrategy := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x if x.startsWith("NativeLibrary") => MergeStrategy.last
  case x if x.startsWith("aws") => MergeStrategy.last
  case _ => MergeStrategy.last
}

