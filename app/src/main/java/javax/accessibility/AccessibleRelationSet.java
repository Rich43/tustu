package javax.accessibility;

import java.util.Vector;

/* loaded from: rt.jar:javax/accessibility/AccessibleRelationSet.class */
public class AccessibleRelationSet {
    protected Vector<AccessibleRelation> relations;

    public AccessibleRelationSet() {
        this.relations = null;
        this.relations = null;
    }

    public AccessibleRelationSet(AccessibleRelation[] accessibleRelationArr) {
        this.relations = null;
        if (accessibleRelationArr.length != 0) {
            this.relations = new Vector<>(accessibleRelationArr.length);
            for (AccessibleRelation accessibleRelation : accessibleRelationArr) {
                add(accessibleRelation);
            }
        }
    }

    public boolean add(AccessibleRelation accessibleRelation) {
        if (this.relations == null) {
            this.relations = new Vector<>();
        }
        AccessibleRelation accessibleRelation2 = get(accessibleRelation.getKey());
        if (accessibleRelation2 == null) {
            this.relations.addElement(accessibleRelation);
            return true;
        }
        Object[] target = accessibleRelation2.getTarget();
        Object[] target2 = accessibleRelation.getTarget();
        int length = target.length + target2.length;
        Object[] objArr = new Object[length];
        for (int i2 = 0; i2 < target.length; i2++) {
            objArr[i2] = target[i2];
        }
        int length2 = target.length;
        int i3 = 0;
        while (length2 < length) {
            objArr[length2] = target2[i3];
            length2++;
            i3++;
        }
        accessibleRelation2.setTarget(objArr);
        return true;
    }

    public void addAll(AccessibleRelation[] accessibleRelationArr) {
        if (accessibleRelationArr.length != 0) {
            if (this.relations == null) {
                this.relations = new Vector<>(accessibleRelationArr.length);
            }
            for (AccessibleRelation accessibleRelation : accessibleRelationArr) {
                add(accessibleRelation);
            }
        }
    }

    public boolean remove(AccessibleRelation accessibleRelation) {
        if (this.relations == null) {
            return false;
        }
        return this.relations.removeElement(accessibleRelation);
    }

    public void clear() {
        if (this.relations != null) {
            this.relations.removeAllElements();
        }
    }

    public int size() {
        if (this.relations == null) {
            return 0;
        }
        return this.relations.size();
    }

    public boolean contains(String str) {
        return get(str) != null;
    }

    public AccessibleRelation get(String str) {
        if (this.relations == null) {
            return null;
        }
        int size = this.relations.size();
        for (int i2 = 0; i2 < size; i2++) {
            AccessibleRelation accessibleRelationElementAt = this.relations.elementAt(i2);
            if (accessibleRelationElementAt != null && accessibleRelationElementAt.getKey().equals(str)) {
                return accessibleRelationElementAt;
            }
        }
        return null;
    }

    public AccessibleRelation[] toArray() {
        if (this.relations == null) {
            return new AccessibleRelation[0];
        }
        AccessibleRelation[] accessibleRelationArr = new AccessibleRelation[this.relations.size()];
        for (int i2 = 0; i2 < accessibleRelationArr.length; i2++) {
            accessibleRelationArr[i2] = this.relations.elementAt(i2);
        }
        return accessibleRelationArr;
    }

    public String toString() {
        String displayString = "";
        if (this.relations != null && this.relations.size() > 0) {
            displayString = this.relations.elementAt(0).toDisplayString();
            for (int i2 = 1; i2 < this.relations.size(); i2++) {
                displayString = displayString + "," + this.relations.elementAt(i2).toDisplayString();
            }
        }
        return displayString;
    }
}
