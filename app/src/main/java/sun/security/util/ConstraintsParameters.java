package sun.security.util;

import java.security.Key;
import java.util.Date;
import java.util.Set;

/* loaded from: rt.jar:sun/security/util/ConstraintsParameters.class */
public interface ConstraintsParameters {
    boolean anchorIsJdkCA();

    Set<Key> getKeys();

    Date getDate();

    String getVariant();

    String extendedExceptionMsg();
}
