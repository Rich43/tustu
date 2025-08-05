package com.sun.security.sasl.digest;

import javax.security.sasl.SaslException;

/* loaded from: rt.jar:com/sun/security/sasl/digest/SecurityCtx.class */
interface SecurityCtx {
    byte[] wrap(byte[] bArr, int i2, int i3) throws SaslException;

    byte[] unwrap(byte[] bArr, int i2, int i3) throws SaslException;
}
