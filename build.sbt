name := "scalatra-microservice-seed"

organization := "com.mintbeans"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.5"

scalacOptions ++= Seq("-feature", "-unchecked", "-deprecation", "-encoding", "utf8")

resolvers ++= Seq(
    "Sonatype Snapshots"  at "https://oss.sonatype.org/content/repositories/snapshots/",
    "Sonatype Releases"   at "http://oss.sonatype.org/content/repositories/releases",
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= {
    val configVersion     = "1.2.1"
    val scalatraVersion   = "2.3.0"
    val jettyVersion      = "9.3.0.M1"
    val json4sVersion     = "3.2.9"
    val casbahVersion     = "2.8.0"
    val embedMongoVersion = "0.2.2"
    val macwireVersion    = "0.8.0"
    val slf4jVersion      = "1.7.7"
    val scalaMockVersion  = "3.2.1"
    Seq(
        "com.typesafe"              %   "config"                      % configVersion,
        "org.scalatra"              %%  "scalatra"                    % scalatraVersion,
        "org.scalatra"              %%  "scalatra-json"               % scalatraVersion,
        "org.scalatra"              %%  "scalatra-scalatest"          % scalatraVersion % "test",
        "org.json4s"                %%  "json4s-jackson"              % json4sVersion,
        "org.eclipse.jetty"         %   "jetty-server"                % jettyVersion,
        "org.eclipse.jetty"         %   "jetty-webapp"                % jettyVersion,
        "org.mongodb"               %%  "casbah-core"                 % casbahVersion,
        "com.github.simplyscala"    %%  "scalatest-embedmongo"        % embedMongoVersion % "test",
        "com.softwaremill.macwire"  %%  "macros"                      % macwireVersion,
        "com.softwaremill.macwire"  %%  "runtime"                     % macwireVersion,
        "org.slf4j"                 %   "slf4j-api"                   % slf4jVersion,
        "org.slf4j"                 %   "slf4j-simple"                % slf4jVersion,
        "org.scalamock"             %%  "scalamock-scalatest-support" % scalaMockVersion % "test"
    )
}

mainClass := Some("com.mintbeans.geo.LocationProvider")

initialCommands in console := """
    import collection.JavaConversions._
"""

Revolver.settings
