package org.icepdf.core.pobjects;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/NameNode.class */
public class NameNode extends Dictionary {
    public static final Name KIDS_KEY = new Name("Kids");
    public static final Name NAMES_KEY = new Name("Names");
    public static final Name LIMITS_KEY = new Name("Limits");
    private static Object NOT_FOUND = new Object();
    private static Object NOT_FOUND_IS_LESSER = new Object();
    private static Object NOT_FOUND_IS_GREATER = new Object();
    private boolean namesAreDecrypted;
    private List<String> namesAndValues;
    private List kidsReferences;
    private Vector<NameNode> kidsNodes;
    private String lowerLimit;
    private String upperLimit;

    public NameNode(Library l2, HashMap h2) {
        super(l2, h2);
        Object o2 = this.library.getObject(this.entries, KIDS_KEY);
        if (o2 != null && (o2 instanceof List)) {
            this.kidsReferences = (List) o2;
            int sz = this.kidsReferences.size();
            if (sz > 0) {
                this.kidsNodes = new Vector<>(sz);
                this.kidsNodes.setSize(sz);
            }
        }
        this.namesAreDecrypted = false;
        Object o3 = this.library.getObject(this.entries, NAMES_KEY);
        if (o3 != null && (o3 instanceof List)) {
            this.namesAndValues = (List) o3;
        }
        Object o4 = this.library.getObject(this.entries, LIMITS_KEY);
        if (o4 != null && (o4 instanceof List)) {
            List limits = (List) o4;
            if (limits.size() >= 2) {
                this.lowerLimit = decryptIfText(limits.get(0));
                this.upperLimit = decryptIfText(limits.get(1));
            }
        }
    }

    public boolean isEmpty() {
        return this.kidsNodes.size() == 0;
    }

    public boolean hasLimits() {
        return this.library.getObject(this.entries, LIMITS_KEY) != null;
    }

    public List getNamesAndValues() {
        return this.namesAndValues;
    }

    public List getKidsReferences() {
        return this.kidsReferences;
    }

    public List getKidsNodes() {
        return this.kidsNodes;
    }

    public String getLowerLimit() {
        return this.lowerLimit;
    }

    public String getUpperLimit() {
        return this.upperLimit;
    }

    private void ensureNamesDecrypted() {
        if (this.namesAreDecrypted) {
            return;
        }
        this.namesAreDecrypted = true;
        for (int i2 = 0; i2 < this.namesAndValues.size(); i2 += 2) {
            this.namesAndValues.set(i2, decryptIfText(this.namesAndValues.get(i2)));
        }
    }

    private String decryptIfText(Object tmp) {
        if (tmp instanceof StringObject) {
            StringObject nameText = (StringObject) tmp;
            return nameText.getDecryptedLiteralString(this.library.securityManager);
        }
        if (tmp instanceof String) {
            return (String) tmp;
        }
        return null;
    }

    Object searchName(String name) {
        Object ret = search(name);
        if (ret == NOT_FOUND || ret == NOT_FOUND_IS_LESSER || ret == NOT_FOUND_IS_GREATER) {
            ret = null;
        }
        return ret;
    }

    private Object search(String name) {
        if (this.kidsReferences != null) {
            if (this.lowerLimit != null) {
                int cmp = this.lowerLimit.compareTo(name);
                if (cmp > 0) {
                    return NOT_FOUND_IS_LESSER;
                }
                if (cmp == 0) {
                    return getNode(0).search(name);
                }
            }
            if (this.upperLimit != null) {
                int cmp2 = this.upperLimit.compareTo(name);
                if (cmp2 < 0) {
                    return NOT_FOUND_IS_GREATER;
                }
                if (cmp2 == 0) {
                    return getNode(this.kidsReferences.size() - 1).search(name);
                }
            }
            return binarySearchKids(0, this.kidsReferences.size() - 1, name);
        }
        if (this.namesAndValues != null) {
            int numNamesAndValues = this.namesAndValues.size();
            if (this.lowerLimit != null) {
                int cmp3 = this.lowerLimit.compareTo(name);
                if (cmp3 > 0) {
                    return NOT_FOUND_IS_LESSER;
                }
                if (cmp3 == 0) {
                    ensureNamesDecrypted();
                    if (this.namesAndValues.get(0).equals(name)) {
                        Object ob = this.namesAndValues.get(1);
                        if (ob instanceof Reference) {
                            ob = this.library.getObject((Reference) ob);
                        }
                        return ob;
                    }
                }
            }
            if (this.upperLimit != null) {
                int cmp4 = this.upperLimit.compareTo(name);
                if (cmp4 < 0) {
                    return NOT_FOUND_IS_GREATER;
                }
                if (cmp4 == 0) {
                    ensureNamesDecrypted();
                    if (this.namesAndValues.get(numNamesAndValues - 2).equals(name)) {
                        Object ob2 = this.namesAndValues.get(numNamesAndValues - 1);
                        if (ob2 instanceof Reference) {
                            ob2 = this.library.getObject((Reference) ob2);
                        }
                        return ob2;
                    }
                }
            }
            ensureNamesDecrypted();
            Object ret = binarySearchNames(0, numNamesAndValues - 1, name);
            if (ret == NOT_FOUND || ret == NOT_FOUND_IS_LESSER || ret == NOT_FOUND_IS_GREATER) {
                ret = null;
            }
            return ret;
        }
        return null;
    }

    private Object binarySearchKids(int firstIndex, int lastIndex, String name) {
        Object r2;
        if (firstIndex > lastIndex) {
            return NOT_FOUND;
        }
        int pivot = firstIndex + ((lastIndex - firstIndex) / 2);
        Object ret = getNode(pivot).search(name);
        if (ret == NOT_FOUND_IS_LESSER) {
            return binarySearchKids(firstIndex, pivot - 1, name);
        }
        if (ret == NOT_FOUND_IS_GREATER) {
            return binarySearchKids(pivot + 1, lastIndex, name);
        }
        if (ret == NOT_FOUND) {
            int i2 = firstIndex;
            while (true) {
                if (i2 > lastIndex) {
                    break;
                }
                if (i2 == pivot || (r2 = getNode(i2).search(name)) == NOT_FOUND || r2 == NOT_FOUND_IS_LESSER || r2 == NOT_FOUND_IS_GREATER) {
                    i2++;
                } else {
                    ret = r2;
                    break;
                }
            }
        }
        return ret;
    }

    private Object binarySearchNames(int firstIndex, int lastIndex, String name) {
        if (firstIndex > lastIndex) {
            return NOT_FOUND;
        }
        int pivot = (firstIndex + ((lastIndex - firstIndex) / 2)) & (-2);
        int cmp = this.namesAndValues.get(pivot).compareTo(name);
        if (cmp == 0) {
            Object ob = this.namesAndValues.get(pivot + 1);
            if (ob instanceof Reference) {
                ob = this.library.getObject((Reference) ob);
            }
            return ob;
        }
        if (cmp > 0) {
            return binarySearchNames(firstIndex, pivot - 1, name);
        }
        if (cmp < 0) {
            return binarySearchNames(pivot + 2, lastIndex, name);
        }
        return NOT_FOUND;
    }

    public NameNode getNode(int index) {
        NameNode n2 = this.kidsNodes.get(index);
        if (n2 == null) {
            Reference r2 = (Reference) this.kidsReferences.get(index);
            HashMap nh = (HashMap) this.library.getObject(r2);
            n2 = new NameNode(this.library, nh);
            this.kidsNodes.set(index, n2);
        }
        return n2;
    }
}
