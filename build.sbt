name := "kisearch-crawler"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "net.sourceforge.htmlunit" % "htmlunit" % "2.14",
  "edu.uci.ics" % "crawler4j" % "3.5",
  "net.vz.mongodb.jackson" %% "play-mongo-jackson-mapper" % "1.1.0"
)

javacOptions ++= Seq("-source", "1.7", "-target", "1.7")

scalacOptions += "-target:jvm-1.7"

play.Project.playJavaSettings
