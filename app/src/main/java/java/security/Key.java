package java.security;

import java.io.Serializable;

/* loaded from: rt.jar:java/security/Key.class */
public interface Key extends Serializable {
    public static final long serialVersionUID = 6603384152749567654L;

    String getAlgorithm();

    String getFormat();

    byte[] getEncoded();
}
