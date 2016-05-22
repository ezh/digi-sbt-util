/**
 * digi-sbt-slf4 - Bridge between SBT plugin and SLF4J.
 *
 * Copyright (c) 2016 Alexey Aksenov ezh@ezh.msk.ru
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License.
 * http://directory.fsf.org/wiki/License:BSD_3Clause
 */

package org.digimead.sbt.util

import java.io.IOException
import org.objectweb.asm.{ ClassReader, ClassWriter }
import org.objectweb.asm.commons.{ ClassRemapper, Remapper }
import org.slf4j.ILoggerFactory
import scala.language.implicitConversions
import scala.util.DynamicVariable

class SLF4JBridge {
  val context = new DynamicVariable[ILoggerFactory](null)

  def withLogFactory[T](factory: ILoggerFactory)(f: ⇒ T): T =
    context.withValue(factory) { f }
}

object SLF4JBridge {
  implicit def SLF4JBridge2implementation(l: SLF4JBridge.type): SLF4JBridge = inner
  val inner = new SLF4JBridge

  trait Loader {
    self: ClassLoader ⇒
    val SLF4JBinderTargetName = "org.slf4j.impl.StaticLoggerBinder"
    val SLF4JBinderASMTargetName = "org/slf4j/impl/StaticLoggerBinder"
    val SLF4JBinderSourceName = "org.digimead.sbt.util.StaticLoggerBinder"
    val SLF4JBinderASMSourceName = "org/digimead/sbt/util/StaticLoggerBinder"

    protected def loadSLF4JBinder(name: String): Array[Byte] = {
      val is = getClass.getResourceAsStream(s"/${SLF4JBinderASMSourceName}.class")
      val bytes = Stream.continually(is.read).takeWhile(_ != -1).map(_.toByte).toArray
      try rewriteDefaultPackageClassNames(bytes, SLF4JBinderASMSourceName, SLF4JBinderASMTargetName)
      catch { case e: IOException ⇒ throw new RuntimeException("Could not rewrite class " + name) }
    }
    protected def rewriteDefaultPackageClassNames(bytecode: Array[Byte], from: String, to: String): Array[Byte] = {
      val classReader = new ClassReader(bytecode)
      val classWriter = new ClassWriter(classReader, 0)
      val remapper = new DefaultPackageClassNameRemapper(from, to)
      classReader.accept(new ClassRemapper(classWriter, remapper), 0)
      classWriter.toByteArray()
    }

    class DefaultPackageClassNameRemapper(from: String, to: String) extends Remapper {
      override def map(typeName: String): String =
        if (typeName.startsWith(from)) to + typeName.drop(from.length()) else typeName
    }
  }
}
