package com.sun.corba.se.spi.logging;

import java.util.logging.Logger;

/* loaded from: rt.jar:com/sun/corba/se/spi/logging/LogWrapperFactory.class */
public interface LogWrapperFactory {
    LogWrapperBase create(Logger logger);
}
