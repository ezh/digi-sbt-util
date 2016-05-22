//
// Copyright (c) 2016 Alexey Aksenov ezh@ezh.msk.ru
//
// Licensed under the BSD license.
//
// You may obtain a copy of the License at
//
//     http://www.opensource.org/licenses/BSD-3-Clause
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

// DEVELOPMENT CONFIGURATION


name := "digi-sbt-util"

description := "Utilities for SBT plugin"

licenses := Seq("BSD 3-Clause License" -> url("http://www.opensource.org/licenses/BSD-3-Clause"))

organization := "org.digimead"

organizationHomepage := Some(url("http://digimead.org"))

homepage := Some(url("https://github.com/ezh/digi-sbt-slf4j"))

version <<= (baseDirectory) { (b) => scala.io.Source.fromFile(b / "version").mkString.trim }

crossScalaVersions := Seq("2.10.6")

scalaVersion := "2.10.6"

scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked", "-Xcheckinit", "-feature")

javacOptions ++= Seq("-Xlint:unchecked", "-Xlint:deprecation")

//
// Custom local options
//

resolvers += "digimead-maven" at "http://storage.googleapis.com/maven.repository.digimead.org/"

libraryDependencies ++= Seq(
  "org.ow2.asm" % "asm" % "5.1",
  "org.ow2.asm" % "asm-commons" % "5.1",
  "org.slf4j" % "slf4j-api" % "1.7.21",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test")

//
// Testing
//

parallelExecution in Test := false

testGrouping in Test <<= (definedTests in Test) map { tests =>
  tests map { test =>
    new Tests.Group(
      name = test.name,
      tests = Seq(test),
      runPolicy = Tests.SubProcess(ForkOptions(runJVMOptions = Seq.empty[String])))
  }
}

//logLevel := Level.Debug
