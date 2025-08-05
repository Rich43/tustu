package sun.java2d.cmm;

import java.awt.color.ProfileDataException;
import java.util.Iterator;
import java.util.Vector;

/* loaded from: rt.jar:sun/java2d/cmm/ProfileDeferralMgr.class */
public class ProfileDeferralMgr {
    public static boolean deferring = true;
    private static Vector<ProfileActivator> aVector;

    public static void registerDeferral(ProfileActivator profileActivator) {
        if (!deferring) {
            return;
        }
        if (aVector == null) {
            aVector = new Vector<>(3, 3);
        }
        aVector.addElement(profileActivator);
    }

    public static void unregisterDeferral(ProfileActivator profileActivator) {
        if (!deferring || aVector == null) {
            return;
        }
        aVector.removeElement(profileActivator);
    }

    public static void activateProfiles() {
        deferring = false;
        if (aVector == null) {
            return;
        }
        aVector.size();
        Iterator<ProfileActivator> it = aVector.iterator();
        while (it.hasNext()) {
            try {
                it.next().activate();
            } catch (ProfileDataException e2) {
            }
        }
        aVector.removeAllElements();
        aVector = null;
    }
}
