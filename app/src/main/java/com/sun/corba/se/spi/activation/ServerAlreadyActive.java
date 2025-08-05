package com.sun.corba.se.spi.activation;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/ServerAlreadyActive.class */
public final class ServerAlreadyActive extends UserException {
    public int serverId;

    public ServerAlreadyActive() {
        super(ServerAlreadyActiveHelper.id());
        this.serverId = 0;
    }

    public ServerAlreadyActive(int i2) {
        super(ServerAlreadyActiveHelper.id());
        this.serverId = 0;
        this.serverId = i2;
    }

    public ServerAlreadyActive(String str, int i2) {
        super(ServerAlreadyActiveHelper.id() + Constants.INDENT + str);
        this.serverId = 0;
        this.serverId = i2;
    }
}
