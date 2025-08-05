package com.sun.corba.se.spi.activation;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/ServerNotRegistered.class */
public final class ServerNotRegistered extends UserException {
    public int serverId;

    public ServerNotRegistered() {
        super(ServerNotRegisteredHelper.id());
        this.serverId = 0;
    }

    public ServerNotRegistered(int i2) {
        super(ServerNotRegisteredHelper.id());
        this.serverId = 0;
        this.serverId = i2;
    }

    public ServerNotRegistered(String str, int i2) {
        super(ServerNotRegisteredHelper.id() + Constants.INDENT + str);
        this.serverId = 0;
        this.serverId = i2;
    }
}
