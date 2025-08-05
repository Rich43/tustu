package sun.misc;

import java.io.PrintStream;

/* compiled from: RegexpPool.java */
/* loaded from: rt.jar:sun/misc/RegexpNode.class */
class RegexpNode {

    /* renamed from: c, reason: collision with root package name */
    char f13578c;
    RegexpNode firstchild;
    RegexpNode nextsibling;
    int depth;
    boolean exact;
    Object result;
    String re;

    RegexpNode() {
        this.re = null;
        this.f13578c = '#';
        this.depth = 0;
    }

    RegexpNode(char c2, int i2) {
        this.re = null;
        this.f13578c = c2;
        this.depth = i2;
    }

    RegexpNode add(char c2) {
        RegexpNode regexpNode;
        RegexpNode regexpNode2 = this.firstchild;
        if (regexpNode2 == null) {
            regexpNode = new RegexpNode(c2, this.depth + 1);
        } else {
            while (regexpNode2 != null) {
                if (regexpNode2.f13578c == c2) {
                    return regexpNode2;
                }
                regexpNode2 = regexpNode2.nextsibling;
            }
            regexpNode = new RegexpNode(c2, this.depth + 1);
            regexpNode.nextsibling = this.firstchild;
        }
        this.firstchild = regexpNode;
        return regexpNode;
    }

    RegexpNode find(char c2) {
        RegexpNode regexpNode = this.firstchild;
        while (true) {
            RegexpNode regexpNode2 = regexpNode;
            if (regexpNode2 != null) {
                if (regexpNode2.f13578c != c2) {
                    regexpNode = regexpNode2.nextsibling;
                } else {
                    return regexpNode2;
                }
            } else {
                return null;
            }
        }
    }

    void print(PrintStream printStream) {
        if (this.nextsibling != null) {
            RegexpNode regexpNode = this;
            printStream.print("(");
            while (regexpNode != null) {
                printStream.write(regexpNode.f13578c);
                if (regexpNode.firstchild != null) {
                    regexpNode.firstchild.print(printStream);
                }
                regexpNode = regexpNode.nextsibling;
                printStream.write(regexpNode != null ? 124 : 41);
            }
            return;
        }
        printStream.write(this.f13578c);
        if (this.firstchild != null) {
            this.firstchild.print(printStream);
        }
    }
}
