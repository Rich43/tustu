package sun.net.www.protocol.http.ntlm;

import java.io.IOException;
import java.util.Base64;

/* loaded from: rt.jar:sun/net/www/protocol/http/ntlm/NTLMAuthSequence.class */
public class NTLMAuthSequence {
    private String username;
    private String password;
    private String ntdomain;
    private long crdHandle;
    private long ctxHandle;
    static final /* synthetic */ boolean $assertionsDisabled;
    Status status = new Status();
    private int state = 0;

    private static native void initFirst(Class<Status> cls);

    private native long getCredentialsHandle(String str, String str2, String str3);

    private native byte[] getNextToken(long j2, byte[] bArr, Status status);

    static {
        $assertionsDisabled = !NTLMAuthSequence.class.desiredAssertionStatus();
        initFirst(Status.class);
    }

    /* loaded from: rt.jar:sun/net/www/protocol/http/ntlm/NTLMAuthSequence$Status.class */
    class Status {
        boolean sequenceComplete;

        Status() {
        }
    }

    NTLMAuthSequence(String str, String str2, String str3) throws IOException {
        this.username = str;
        this.password = str2;
        this.ntdomain = str3;
        this.crdHandle = getCredentialsHandle(str, str3, str2);
        if (this.crdHandle == 0) {
            throw new IOException("could not get credentials handle");
        }
    }

    public String getAuthHeader(String str) throws IOException {
        byte[] bArrDecode = null;
        if (!$assertionsDisabled && this.status.sequenceComplete) {
            throw new AssertionError();
        }
        if (str != null) {
            bArrDecode = Base64.getDecoder().decode(str);
        }
        byte[] nextToken = getNextToken(this.crdHandle, bArrDecode, this.status);
        if (nextToken == null) {
            throw new IOException("Internal authentication error");
        }
        return Base64.getEncoder().encodeToString(nextToken);
    }

    public boolean isComplete() {
        return this.status.sequenceComplete;
    }
}
