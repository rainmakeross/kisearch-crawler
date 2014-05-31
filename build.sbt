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

play.Project.playJavaSettings
