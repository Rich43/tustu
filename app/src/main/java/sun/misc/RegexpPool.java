package sun.misc;

import java.io.PrintStream;

/* loaded from: rt.jar:sun/misc/RegexpPool.class */
public class RegexpPool {
    private static final int BIG = Integer.MAX_VALUE;
    private RegexpNode prefixMachine = new RegexpNode();
    private RegexpNode suffixMachine = new RegexpNode();
    private int lastDepth = Integer.MAX_VALUE;

    public void add(String str, Object obj) throws REException {
        add(str, obj, false);
    }

    public void replace(String str, Object obj) {
        try {
            add(str, obj, true);
        } catch (Exception e2) {
        }
    }

    public Object delete(String str) {
        Object obj = null;
        RegexpNode regexpNodeFind = this.prefixMachine;
        RegexpNode regexpNode = regexpNodeFind;
        int length = str.length() - 1;
        boolean z2 = true;
        if (!str.startsWith("*") || !str.endsWith("*")) {
            length++;
        }
        if (length <= 0) {
            return null;
        }
        int i2 = 0;
        while (regexpNodeFind != null) {
            if (regexpNodeFind.result != null && regexpNodeFind.depth < Integer.MAX_VALUE && (!regexpNodeFind.exact || i2 == length)) {
                regexpNode = regexpNodeFind;
            }
            if (i2 >= length) {
                break;
            }
            regexpNodeFind = regexpNodeFind.find(str.charAt(i2));
            i2++;
        }
        RegexpNode regexpNodeFind2 = this.suffixMachine;
        int i3 = length;
        while (true) {
            i3--;
            if (i3 < 0 || regexpNodeFind2 == null) {
                break;
            }
            if (regexpNodeFind2.result != null && regexpNodeFind2.depth < Integer.MAX_VALUE) {
                z2 = false;
                regexpNode = regexpNodeFind2;
            }
            regexpNodeFind2 = regexpNodeFind2.find(str.charAt(i3));
        }
        if (z2) {
            if (str.equals(regexpNode.re)) {
                obj = regexpNode.result;
                regexpNode.result = null;
            }
        } else if (str.equals(regexpNode.re)) {
            obj = regexpNode.result;
            regexpNode.result = null;
        }
        return obj;
    }

    public Object match(String str) {
        return matchAfter(str, Integer.MAX_VALUE);
    }

    public Object matchNext(String str) {
        return matchAfter(str, this.lastDepth);
    }

    private void add(String str, Object obj, boolean z2) throws REException {
        RegexpNode regexpNodeAdd;
        int length = str.length();
        if (str.charAt(0) == '*') {
            RegexpNode regexpNodeAdd2 = this.suffixMachine;
            while (true) {
                regexpNodeAdd = regexpNodeAdd2;
                if (length <= 1) {
                    break;
                }
                length--;
                regexpNodeAdd2 = regexpNodeAdd.add(str.charAt(length));
            }
        } else {
            boolean z3 = false;
            if (str.charAt(length - 1) == '*') {
                length--;
            } else {
                z3 = true;
            }
            regexpNodeAdd = this.prefixMachine;
            for (int i2 = 0; i2 < length; i2++) {
                regexpNodeAdd = regexpNodeAdd.add(str.charAt(i2));
            }
            regexpNodeAdd.exact = z3;
        }
        if (regexpNodeAdd.result != null && !z2) {
            throw new REException(str + " is a duplicate");
        }
        regexpNodeAdd.re = str;
        regexpNodeAdd.result = obj;
    }

    private Object matchAfter(String str, int i2) {
        RegexpNode regexpNodeFind = this.prefixMachine;
        RegexpNode regexpNode = regexpNodeFind;
        int i3 = 0;
        int i4 = 0;
        int length = str.length();
        if (length <= 0) {
            return null;
        }
        int i5 = 0;
        while (regexpNodeFind != null) {
            if (regexpNodeFind.result != null && regexpNodeFind.depth < i2 && (!regexpNodeFind.exact || i5 == length)) {
                this.lastDepth = regexpNodeFind.depth;
                regexpNode = regexpNodeFind;
                i3 = i5;
                i4 = length;
            }
            if (i5 >= length) {
                break;
            }
            regexpNodeFind = regexpNodeFind.find(str.charAt(i5));
            i5++;
        }
        RegexpNode regexpNodeFind2 = this.suffixMachine;
        int i6 = length;
        while (true) {
            i6--;
            if (i6 < 0 || regexpNodeFind2 == null) {
                break;
            }
            if (regexpNodeFind2.result != null && regexpNodeFind2.depth < i2) {
                this.lastDepth = regexpNodeFind2.depth;
                regexpNode = regexpNodeFind2;
                i3 = 0;
                i4 = i6 + 1;
            }
            regexpNodeFind2 = regexpNodeFind2.find(str.charAt(i6));
        }
        Object objFound = regexpNode.result;
        if (objFound != null && (objFound instanceof RegexpTarget)) {
            objFound = ((RegexpTarget) objFound).found(str.substring(i3, i4));
        }
        return objFound;
    }

    public void reset() {
        this.lastDepth = Integer.MAX_VALUE;
    }

    public void print(PrintStream printStream) {
        printStream.print("Regexp pool:\n");
        if (this.suffixMachine.firstchild != null) {
            printStream.print(" Suffix machine: ");
            this.suffixMachine.firstchild.print(printStream);
            printStream.print("\n");
        }
        if (this.prefixMachine.firstchild != null) {
            printStream.print(" Prefix machine: ");
            this.prefixMachine.firstchild.print(printStream);
            printStream.print("\n");
        }
    }
}
