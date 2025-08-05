package javax.print.attribute.standard;

import javax.print.attribute.Attribute;
import javax.print.attribute.IntegerSyntax;
import javax.print.attribute.PrintServiceAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/QueuedJobCount.class */
public final class QueuedJobCount extends IntegerSyntax implements PrintServiceAttribute {
    private static final long serialVersionUID = 7499723077864047742L;

    public QueuedJobCount(int i2) {
        super(i2, 0, Integer.MAX_VALUE);
    }

    @Override // javax.print.attribute.IntegerSyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof QueuedJobCount);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return QueuedJobCount.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "queued-job-count";
    }
}
