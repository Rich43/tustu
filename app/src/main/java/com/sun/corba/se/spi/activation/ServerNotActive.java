package com.sun.corba.se.spi.activation;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/ServerNotActive.class */
public final class ServerNotActive extends UserException {
    public int serverId;

    public ServerNotActive() {
        super(ServerNotActiveHelper.id());
        this.serverId = 0;
    }

    public ServerNotActive(int i2) {
        super(ServerNotActiveHelper.id());
        this.serverId = 0;
        this.serverId = i2;
    }

    public ServerNotActive(String str, int i2) {
        super(ServerNotActiveHelper.id() + Constants.INDENT + str);
        this.serverId = 0;
        this.serverId = i2;
    }
}
