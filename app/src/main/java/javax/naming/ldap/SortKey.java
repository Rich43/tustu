package javax.naming.ldap;

/* loaded from: rt.jar:javax/naming/ldap/SortKey.class */
public class SortKey {
    private String attrID;
    private boolean reverseOrder;
    private String matchingRuleID;

    public SortKey(String str) {
        this.reverseOrder = false;
        this.matchingRuleID = null;
        this.attrID = str;
    }

    public SortKey(String str, boolean z2, String str2) {
        this.reverseOrder = false;
        this.matchingRuleID = null;
        this.attrID = str;
        this.reverseOrder = !z2;
        this.matchingRuleID = str2;
    }

    public String getAttributeID() {
        return this.attrID;
    }

    public boolean isAscending() {
        return !this.reverseOrder;
    }

    public String getMatchingRuleID() {
        return this.matchingRuleID;
    }
}
