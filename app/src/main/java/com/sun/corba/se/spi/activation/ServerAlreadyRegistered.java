package com.sun.corba.se.spi.activation;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/ServerAlreadyRegistered.class */
public final class ServerAlreadyRegistered extends UserException {
    public int serverId;

    public ServerAlreadyRegistered() {
        super(ServerAlreadyRegisteredHelper.id());
        this.serverId = 0;
    }

    public ServerAlreadyRegistered(int i2) {
        super(ServerAlreadyRegisteredHelper.id());
        this.serverId = 0;
        this.serverId = i2;
    }

    public ServerAlreadyRegistered(String str, int i2) {
        super(ServerAlreadyRegisteredHelper.id() + Constants.INDENT + str);
        this.serverId = 0;
        this.serverId = i2;
    }
}
