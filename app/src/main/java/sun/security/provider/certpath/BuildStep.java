package sun.security.provider.certpath;

import java.security.cert.X509Certificate;

/* loaded from: rt.jar:sun/security/provider/certpath/BuildStep.class */
public class BuildStep {
    private Vertex vertex;
    private X509Certificate cert;
    private Throwable throwable;
    private int result;
    public static final int POSSIBLE = 1;
    public static final int BACK = 2;
    public static final int FOLLOW = 3;
    public static final int FAIL = 4;
    public static final int SUCCEED = 5;

    public BuildStep(Vertex vertex, int i2) {
        this.vertex = vertex;
        if (this.vertex != null) {
            this.cert = this.vertex.getCertificate();
            this.throwable = this.vertex.getThrowable();
        }
        this.result = i2;
    }

    public Vertex getVertex() {
        return this.vertex;
    }

    public X509Certificate getCertificate() {
        return this.cert;
    }

    public String getIssuerName() {
        return getIssuerName(null);
    }

    public String getIssuerName(String str) {
        return this.cert == null ? str : this.cert.getIssuerX500Principal().toString();
    }

    public String getSubjectName() {
        return getSubjectName(null);
    }

    public String getSubjectName(String str) {
        return this.cert == null ? str : this.cert.getSubjectX500Principal().toString();
    }

    public Throwable getThrowable() {
        return this.throwable;
    }

    public int getResult() {
        return this.result;
    }

    public String resultToString(int i2) {
        String str;
        switch (i2) {
            case 1:
                str = "Certificate to be tried.\n";
                break;
            case 2:
                str = "Certificate backed out since path does not satisfy build requirements.\n";
                break;
            case 3:
                str = "Certificate satisfies conditions.\n";
                break;
            case 4:
                str = "Certificate backed out since path does not satisfy conditions.\n";
                break;
            case 5:
                str = "Certificate satisfies conditions.\n";
                break;
            default:
                str = "Internal error: Invalid step result value.\n";
                break;
        }
        return str;
    }

    public String toString() {
        String strResultToString;
        switch (this.result) {
            case 1:
            case 3:
            case 5:
                strResultToString = resultToString(this.result);
                break;
            case 2:
            case 4:
                strResultToString = resultToString(this.result) + this.vertex.throwableToString();
                break;
            default:
                strResultToString = "Internal Error: Invalid step result\n";
                break;
        }
        return strResultToString;
    }

    public String verboseToString() {
        String strResultToString = resultToString(getResult());
        switch (this.result) {
            case 2:
            case 4:
                strResultToString = strResultToString + this.vertex.throwableToString();
                break;
            case 3:
            case 5:
                strResultToString = strResultToString + this.vertex.moreToString();
                break;
        }
        return strResultToString + "Certificate contains:\n" + this.vertex.certToString();
    }

    public String fullToString() {
        return resultToString(getResult()) + this.vertex.toString();
    }
}
