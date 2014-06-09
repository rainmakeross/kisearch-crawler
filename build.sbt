name := "kisearch-crawler"

version := "1.0-SNAPSHOT"

resolvers += "neo4j-releases" at "http://m2.neo4j.org/releases"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "net.sourceforge.htmlunit" % "htmlunit" % "2.14",
  "edu.uci.ics" % "crawler4j" % "3.5",
  "net.vz.mongodb.jackson" %% "play-mongo-jackson-mapper" % "1.1.0",
  "org.neo4j" % "neo4j-rest-graphdb" % "2.0.1",
  "org.neo4j" % "neo4j" % "2.0.1",
  "org.graphstream" % "gs-algo" % "1.1.2"
)

javacOptions ++= Seq("-source", "1.7", "-target", "1.7")

scalacOptions += "-target:jvm-1.7"

play.Project.playJavaSettings
