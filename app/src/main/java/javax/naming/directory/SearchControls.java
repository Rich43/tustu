package javax.naming.directory;

import java.io.Serializable;

/* loaded from: rt.jar:javax/naming/directory/SearchControls.class */
public class SearchControls implements Serializable {
    public static final int OBJECT_SCOPE = 0;
    public static final int ONELEVEL_SCOPE = 1;
    public static final int SUBTREE_SCOPE = 2;
    private int searchScope;
    private int timeLimit;
    private boolean derefLink;
    private boolean returnObj;
    private long countLimit;
    private String[] attributesToReturn;
    private static final long serialVersionUID = -2480540967773454797L;

    public SearchControls() {
        this.searchScope = 1;
        this.timeLimit = 0;
        this.countLimit = 0L;
        this.derefLink = false;
        this.returnObj = false;
        this.attributesToReturn = null;
    }

    public SearchControls(int i2, long j2, int i3, String[] strArr, boolean z2, boolean z3) {
        this.searchScope = i2;
        this.timeLimit = i3;
        this.derefLink = z3;
        this.returnObj = z2;
        this.countLimit = j2;
        this.attributesToReturn = strArr;
    }

    public int getSearchScope() {
        return this.searchScope;
    }

    public int getTimeLimit() {
        return this.timeLimit;
    }

    public boolean getDerefLinkFlag() {
        return this.derefLink;
    }

    public boolean getReturningObjFlag() {
        return this.returnObj;
    }

    public long getCountLimit() {
        return this.countLimit;
    }

    public String[] getReturningAttributes() {
        return this.attributesToReturn;
    }

    public void setSearchScope(int i2) {
        this.searchScope = i2;
    }

    public void setTimeLimit(int i2) {
        this.timeLimit = i2;
    }

    public void setDerefLinkFlag(boolean z2) {
        this.derefLink = z2;
    }

    public void setReturningObjFlag(boolean z2) {
        this.returnObj = z2;
    }

    public void setCountLimit(long j2) {
        this.countLimit = j2;
    }

    public void setReturningAttributes(String[] strArr) {
        this.attributesToReturn = strArr;
    }
}
