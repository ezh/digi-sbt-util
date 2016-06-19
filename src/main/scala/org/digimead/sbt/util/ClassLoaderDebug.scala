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

class ClassLoaderDebug(parent: ClassLoader) extends ClassLoader(parent) {
  @throws(classOf[ClassNotFoundException])
  override def loadClass(name: String, resolve: Boolean): Class[_] = {
    println("Load class " + name)
    super.loadClass(name, resolve)
  }
}
