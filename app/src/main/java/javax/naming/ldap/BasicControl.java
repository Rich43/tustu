package javax.naming.ldap;

/* loaded from: rt.jar:javax/naming/ldap/BasicControl.class */
public class BasicControl implements Control {
    protected String id;
    protected boolean criticality;
    protected byte[] value;
    private static final long serialVersionUID = -4233907508771791687L;

    public BasicControl(String str) {
        this.criticality = false;
        this.value = null;
        this.id = str;
    }

    public BasicControl(String str, boolean z2, byte[] bArr) {
        this.criticality = false;
        this.value = null;
        this.id = str;
        this.criticality = z2;
        this.value = bArr;
    }

    @Override // javax.naming.ldap.Control
    public String getID() {
        return this.id;
    }

    @Override // javax.naming.ldap.Control
    public boolean isCritical() {
        return this.criticality;
    }

    @Override // javax.naming.ldap.Control
    public byte[] getEncodedValue() {
        return this.value;
    }
}
