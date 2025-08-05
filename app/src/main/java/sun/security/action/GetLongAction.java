package sun.security.action;

import java.security.PrivilegedAction;

/* loaded from: rt.jar:sun/security/action/GetLongAction.class */
public class GetLongAction implements PrivilegedAction<Long> {
    private String theProp;
    private long defaultVal;
    private boolean defaultSet;

    public GetLongAction(String str) {
        this.defaultSet = false;
        this.theProp = str;
    }

    public GetLongAction(String str, long j2) {
        this.defaultSet = false;
        this.theProp = str;
        this.defaultVal = j2;
        this.defaultSet = true;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.security.PrivilegedAction
    public Long run() {
        Long l2 = Long.getLong(this.theProp);
        if (l2 == null && this.defaultSet) {
            return new Long(this.defaultVal);
        }
        return l2;
    }
}
