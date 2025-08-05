package sun.security.action;

import java.security.PrivilegedAction;

/* loaded from: rt.jar:sun/security/action/GetIntegerAction.class */
public class GetIntegerAction implements PrivilegedAction<Integer> {
    private String theProp;
    private int defaultVal;
    private boolean defaultSet;

    public GetIntegerAction(String str) {
        this.defaultSet = false;
        this.theProp = str;
    }

    public GetIntegerAction(String str, int i2) {
        this.defaultSet = false;
        this.theProp = str;
        this.defaultVal = i2;
        this.defaultSet = true;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.security.PrivilegedAction
    public Integer run() {
        Integer integer = Integer.getInteger(this.theProp);
        if (integer == null && this.defaultSet) {
            return new Integer(this.defaultVal);
        }
        return integer;
    }
}
