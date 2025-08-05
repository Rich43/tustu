package javax.obex;

import java.io.IOException;

/* loaded from: bluecove-2.1.1.jar:javax/obex/HeaderSet.class */
public interface HeaderSet {
    public static final int COUNT = 192;
    public static final int NAME = 1;
    public static final int TYPE = 66;
    public static final int LENGTH = 195;
    public static final int TIME_ISO_8601 = 68;
    public static final int TIME_4_BYTE = 196;
    public static final int DESCRIPTION = 5;
    public static final int TARGET = 70;
    public static final int HTTP = 71;
    public static final int WHO = 74;
    public static final int OBJECT_CLASS = 79;
    public static final int APPLICATION_PARAMETER = 76;

    void setHeader(int i2, Object obj);

    Object getHeader(int i2) throws IOException;

    int[] getHeaderList() throws IOException;

    void createAuthenticationChallenge(String str, boolean z2, boolean z3);

    int getResponseCode() throws IOException;
}
