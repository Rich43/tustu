package sun.security.timestamp;

import java.io.IOException;

/* loaded from: rt.jar:sun/security/timestamp/Timestamper.class */
public interface Timestamper {
    TSResponse generateTimestamp(TSRequest tSRequest) throws IOException;
}
