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

import org.scalatest.{ Finders, FreeSpec, Matchers }
import org.slf4j.{ ILoggerFactory, Logger }
import org.slf4j.spi.LoggerFactoryBinder

class JavaVersionTest extends FreeSpec with Matchers {
  "test" in {
    JavaVersion.toVersion("1.4.1-beta") should be(JavaVersion(4))
    JavaVersion.toVersion("1.6.0_30") should be(JavaVersion(6))
    JavaVersion.toVersion("1.6.0_30") should be(JavaVersion(6))
    JavaVersion.toVersion("1.7.0_10") should be(JavaVersion(7))
    JavaVersion.toVersion("1.8.0_05") should be(JavaVersion(8))
    JavaVersion.toVersion("1.9.0_25-b15") should be(JavaVersion(9))
    JavaVersion.toVersion("9-ea+19") should be(JavaVersion(9))
  }
}
