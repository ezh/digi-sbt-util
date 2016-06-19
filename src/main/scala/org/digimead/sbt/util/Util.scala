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

import scala.language.implicitConversions

class Util {
  def applyWithClassLoader[T](classLoader: ClassLoader, clazz: Class[_], args: AnyRef*): T = {
    val thread = Thread.currentThread
    val oldLoader = thread.getContextClassLoader
    try {
      thread.setContextClassLoader(classLoader)
      val fclassloaded = classLoader.loadClass(clazz.getName())
      val floaded = fclassloaded.newInstance()
      fclassloaded.getDeclaredMethods().find { method ⇒ method.getName == "apply" && method.getParameterTypes.size == args.size }.
        map { _.invoke(floaded, args: _*).asInstanceOf[T] }.getOrElse {
          throw new IllegalArgumentException(s"Unable to find method apply with ${args.size} argument(s)")
        }
    } finally {
      thread.setContextClassLoader(oldLoader)
    }
  }
}

object Util {
  implicit def Util2implementation(u: Util.type): Util = u.inner
  /** Util implementation. */
  lazy val inner = new Util()
}
