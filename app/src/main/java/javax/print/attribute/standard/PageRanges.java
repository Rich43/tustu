package javax.print.attribute.standard;

import javax.print.attribute.Attribute;
import javax.print.attribute.DocAttribute;
import javax.print.attribute.PrintJobAttribute;
import javax.print.attribute.PrintRequestAttribute;
import javax.print.attribute.SetOfIntegerSyntax;

/* loaded from: rt.jar:javax/print/attribute/standard/PageRanges.class */
public final class PageRanges extends SetOfIntegerSyntax implements DocAttribute, PrintRequestAttribute, PrintJobAttribute {
    private static final long serialVersionUID = 8639895197656148392L;

    public PageRanges(int[][] iArr) {
        super(iArr);
        if (iArr == null) {
            throw new NullPointerException("members is null");
        }
        myPageRanges();
    }

    public PageRanges(String str) {
        super(str);
        if (str == null) {
            throw new NullPointerException("members is null");
        }
        myPageRanges();
    }

    private void myPageRanges() {
        int[][] members = getMembers();
        if (members.length == 0) {
            throw new IllegalArgumentException("members is zero-length");
        }
        for (int[] iArr : members) {
            if (iArr[0] < 1) {
                throw new IllegalArgumentException("Page value < 1 specified");
            }
        }
    }

    public PageRanges(int i2) {
        super(i2);
        if (i2 < 1) {
            throw new IllegalArgumentException("Page value < 1 specified");
        }
    }

    public PageRanges(int i2, int i3) {
        super(i2, i3);
        if (i2 > i3) {
            throw new IllegalArgumentException("Null range specified");
        }
        if (i2 < 1) {
            throw new IllegalArgumentException("Page value < 1 specified");
        }
    }

    @Override // javax.print.attribute.SetOfIntegerSyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof PageRanges);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return PageRanges.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "page-ranges";
    }
}
