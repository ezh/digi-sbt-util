/**
 * digi-sbt-util - Utilities for SBT plugin.
 *
 * Copyright (c) 2016 Alexey Aksenov ezh@ezh.msk.ru
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License.
 * http://directory.fsf.org/wiki/License:BSD_3Clause
 */

package org.digimead.sbt.util

import java.util.regex.Pattern

/**
 * Container for JVM version.
 */
case class JavaVersion(versionNumber: Int)

/**
 * Java version checker
 */
object JavaVersion {
  def current(): JavaVersion = toVersion(System.getProperty("java.version"))
  def isJava5() = current().versionNumber == 5
  def isJava6() = current().versionNumber == 6
  def isJava7() = current().versionNumber == 7
  def isJava8() = current().versionNumber == 8
  def isJava9() = current().versionNumber == 9
  def isJava5Compatible() = isJava5() || isJava6Compatible()
  def isJava6Compatible() = isJava6() || isJava7Compatible()
  def isJava7Compatible() = isJava7() || isJava8Compatible()
  def isJava8Compatible() = isJava8() || isJava9Compatible()
  def isJava9Compatible() = isJava9()
  def toVersion(value: AnyRef): JavaVersion = value match {
    case version: JavaVersion ⇒
      version
    case name: String ⇒
      if (name.matches("\\d")) {
        val versionIdx = Integer.parseInt(name)
        if (versionIdx >= 0)
          JavaVersion(versionIdx)
        else
          throw new IllegalArgumentException(s"Could not determine java version from '${name}'.")
      } else {
        val matcher = Pattern.compile("1\\.(\\d)(\\D.*)?").matcher(name)
        if (matcher.matches()) {
          val versionIdx = Integer.parseInt(matcher.group(1))
          if (versionIdx >= 0)
            JavaVersion(versionIdx)
          else
            throw new IllegalArgumentException(s"Could not determine java version from '${name}'.")
        } else {
          // JEP 223: New Version-String Scheme
          val matcher = Pattern.compile("(\\d)(\\D.*)?").matcher(name)
          if (matcher.matches()) {
            val versionIdx = Integer.parseInt(matcher.group(1))
            if (versionIdx >= 0)
              JavaVersion(versionIdx)
            else
              throw new IllegalArgumentException(s"Could not determine java version from '${name}'.")
          } else
            throw new IllegalArgumentException(s"Could not determine java version from '${name}'.")
        }
      }
  }
}
