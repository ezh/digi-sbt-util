/**
 * digi-sbt-slf4 - Bridge between SBT plugin and SLF4J.
 *
 * Copyright (c) 2016 Alexey Aksenov ezh@ezh.msk.ru
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License.
 * http://directory.fsf.org/wiki/License:BSD_3Clause
 */

package org.digimead.sbt.util;

import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

public class StaticLoggerBinder implements LoggerFactoryBinder {
	/**
	 * The unique instance of this class.
	 */
	private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();

	/**
	 * Return the singleton of this class.
	 *
	 * @return the StaticLoggerBinder singleton
	 */
	public static final StaticLoggerBinder getSingleton() {
		return SINGLETON;
	}

	@Override
	public ILoggerFactory getLoggerFactory() {
		return org.digimead.sbt.util.SLF4JBridge$.MODULE$.inner().context().value();
	}

	@Override
	public String getLoggerFactoryClassStr() {
		return org.digimead.sbt.util.SLF4JBridge$.MODULE$.inner().context().value().getClass().getName();
	}

}
