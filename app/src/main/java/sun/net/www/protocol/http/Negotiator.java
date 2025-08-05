package sun.net.www.protocol.http;

import java.io.IOException;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:sun/net/www/protocol/http/Negotiator.class */
public abstract class Negotiator {
    public abstract byte[] firstToken() throws IOException;

    public abstract byte[] nextToken(byte[] bArr) throws IOException;

    static Negotiator getNegotiator(HttpCallerInfo httpCallerInfo) {
        try {
            try {
                return (Negotiator) Class.forName("sun.net.www.protocol.http.spnego.NegotiatorImpl", true, null).getConstructor(HttpCallerInfo.class).newInstance(httpCallerInfo);
            } catch (ReflectiveOperationException e2) {
                finest(e2);
                Throwable cause = e2.getCause();
                if (cause != null && (cause instanceof Exception)) {
                    finest((Exception) cause);
                    return null;
                }
                return null;
            }
        } catch (ClassNotFoundException e3) {
            finest(e3);
            return null;
        } catch (ReflectiveOperationException e4) {
            throw new AssertionError(e4);
        }
    }

    private static void finest(Exception exc) {
        PlatformLogger httpLogger = HttpURLConnection.getHttpLogger();
        if (httpLogger.isLoggable(PlatformLogger.Level.FINEST)) {
            httpLogger.finest("NegotiateAuthentication: " + ((Object) exc));
        }
    }
}
