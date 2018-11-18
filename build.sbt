name := "ScalaJavaInterop"

version := "1.0"

scalaVersion := "2.12.2"
/*
libraryDependencies ++= Seq(
  // "org.scalaz"    %% "scalaz-core"       % "7.1.0",
  // "org.scalatest" %% "scalatest"         % "2.2.4",
  "commons-lang"  %  "commons-lang"      % "2.6",
  "jline"         %  "jline"             % "0.9.94",
  "org.jmock"     %  "jmock"             % "2.5.1"  % "test",
  "org.joda"      %  "joda-convert"      % "1.2",
  "joda-time"     %  "joda-time"         % "2.1",
  "com.novocode"  %  "junit-interface"   % "0.10"   % "test",
  "log4j"         %  "log4j"             % "1.2.17",
  "org.mockito"   %  "mockito-core"      % "1.9.0"  % "test",
  "org.mongodb"   %  "mongo-java-driver" % "2.7.2",
  "org.fitnesse"  % "fitnesse"           % "20180127",
  "org.slf4j"     %  "slf4j-api"         % "1.7.5",
  "org.slf4j"     %  "slf4j-simple"      % "1.7.5",
  "co.fs2"        %% "fs2-core"          % "0.9.7",
  "co.fs2"        %% "fs2-io"            % "0.9.7"
)
*/

libraryDependencies ++= Seq(
  "org.joda"      %  "joda-convert"      % "1.2",
  "joda-time"     %  "joda-time"         % "2.1",
  "com.github.nscala-time" %% "nscala-time" % "2.20.0",
  "org.mongodb"   %  "mongo-java-driver" % "2.7.2",
  "org.typelevel" %% "cats-effect"       % "0.5",
  "co.fs2"        %% "fs2-core"          % "0.9.7",
  "co.fs2"        %% "fs2-io"            % "0.9.7",
  "com.lihaoyi"   %% "ammonite"          % "1.0.3" % "test" cross CrossVersion.full,
  "commons-lang"  %  "commons-lang"      % "2.6",
  "com.typesafe.akka" %% "akka-actor"    % "2.5.11",
  "com.typesafe.akka" %% "akka-testkit"  % "2.5.11" % Test,
  "org.fitnesse"  % "fitnesse"           % "20180127",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0",
  "ch.qos.logback" % "logback-classic"   % "1.2.3",
  "org.scalactic" %% "scalactic"         % "3.0.5",
  "org.scalatest" %% "scalatest"         % "3.0.5" % "test"
)

libraryDependencies += "com.lihaoyi"   %% "ammonite"          % "1.0.3" % "test" cross CrossVersion.full

// sbt ScalaJavaInterop/test:run
sourceGenerators in Test += Def.task {
  val file = (sourceManaged in Test).value / "amm.scala"
  IO.write(file, """object amm extends App { ammonite.Main.main(args) }""")
  Seq(file)
}.taskValue
