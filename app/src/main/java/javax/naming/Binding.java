package javax.naming;

import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:javax/naming/Binding.class */
public class Binding extends NameClassPair {
    private Object boundObj;
    private static final long serialVersionUID = 8839217842691845890L;

    public Binding(String str, Object obj) {
        super(str, null);
        this.boundObj = obj;
    }

    public Binding(String str, Object obj, boolean z2) {
        super(str, null, z2);
        this.boundObj = obj;
    }

    public Binding(String str, String str2, Object obj) {
        super(str, str2);
        this.boundObj = obj;
    }

    public Binding(String str, String str2, Object obj, boolean z2) {
        super(str, str2, z2);
        this.boundObj = obj;
    }

    @Override // javax.naming.NameClassPair
    public String getClassName() {
        String className = super.getClassName();
        if (className != null) {
            return className;
        }
        if (this.boundObj != null) {
            return this.boundObj.getClass().getName();
        }
        return null;
    }

    public Object getObject() {
        return this.boundObj;
    }

    public void setObject(Object obj) {
        this.boundObj = obj;
    }

    @Override // javax.naming.NameClassPair
    public String toString() {
        return super.toString() + CallSiteDescriptor.TOKEN_DELIMITER + getObject();
    }
}
