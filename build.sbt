lazy val defaultSettings =
  Project.defaultSettings ++
  Compiler.defaultSettings ++
  Publish.defaultSettings ++
  Tests.defaultSettings ++
  Formatting.defaultSettings ++
  Console.defaultSettings ++
  Docs.defaultSettings

lazy val squants = crossProject
  .crossType(CrossType.Full)
  .in(file("."))
  .settings(defaultSettings: _*)
  .jvmSettings(
    osgiSettings,
    libraryDependencies ~= (_.map(_.withDottyCompat()).filterNot(_.name contains "scala-library")),
    scalacOptions := Seq("-language:Scala2"),
    scalaVersion := dottyLatestNightlyBuild.get
  )
  .jsSettings(
    parallelExecution in Test := false,
    excludeFilter in Test := "*Serializer.scala" || "*SerializerSpec.scala"
  )

lazy val root = project.in(file("."))
  .settings(defaultSettings: _*)
  .settings(
    name := "squants",
    publish := {},
    publishLocal := {},
    publishArtifact := false
  )
  .aggregate(squantsJVM, squantsJS)

lazy val squantsJVM = squants.jvm.enablePlugins(SbtOsgi)
lazy val squantsJS = squants.js
