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

class SLF4JBridgeTest extends FreeSpec with Matchers {
  "test" in {
    intercept[ClassNotFoundException] { Class.forName("org.slf4j.impl.StaticLoggerBinder") }

    implicit val classLoader = new MyLoader(new ClassLoaderDebug(getClass.getClassLoader()))
    val (a, b, c) =
      SLF4JBridge.withLogFactory(SLF4JBridgeTest.Factory) {
        Util.applyWithClassLoader[(Integer, String, ClassLoader)](classLoader, classOf[SLF4JBridgeTest.Function])
      }

    a should be(123)
    b should be("org.digimead.sbt.util.SLF4JBridgeTest$Factory$")
    c.getClass.getName should be("org.digimead.sbt.util.SLF4JBridgeTest$MyLoader")
    intercept[ClassNotFoundException] { Class.forName("org.slf4j.impl.StaticLoggerBinder") }
  }

  class MyLoader(parent: ClassLoader) extends ClassLoader(parent) with SLF4JBridge.Loader {
    @throws(classOf[ClassNotFoundException])
    override def loadClass(name: String, resolve: Boolean): Class[_] = {
      if (name == SLF4JBinderTargetName) {
        try super.loadClass(SLF4JBinderTargetName, resolve) catch {
          case e: ClassNotFoundException ⇒
            val bytecode = loadSLF4JBinder(name)
            defineClass(name, bytecode, 0, bytecode.length)
        }
      } else if (name == classOf[SLF4JBridgeTest.Function].getName()) {
        val clazz = super.loadClass(name, resolve)
        val is = clazz.getResourceAsStream('/' + clazz.getName().replace('.', '/') + ".class")
        val bytes = Stream.continually(is.read).takeWhile(_ != -1).map(_.toByte).toArray
        super.defineClass(name, bytes, 0, bytes.length)
      } else
        super.loadClass(name, resolve)
    }
  }
}

object SLF4JBridgeTest {
  class Function {
    def apply() = {
      val binderClass = Class.forName("org.slf4j.impl.StaticLoggerBinder")
      val binder = binderClass.getMethod("getSingleton").invoke(null).asInstanceOf[LoggerFactoryBinder]
      (123, binder.getLoggerFactory().getClass.getName, getClass.getClassLoader)
    }
  }
  object Factory extends ILoggerFactory {
    def getLogger(name: String): Logger = null
  }
}
