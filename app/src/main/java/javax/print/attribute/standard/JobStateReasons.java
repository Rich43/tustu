package javax.print.attribute.standard;

import java.util.Collection;
import java.util.HashSet;
import javax.print.attribute.Attribute;
import javax.print.attribute.PrintJobAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/JobStateReasons.class */
public final class JobStateReasons extends HashSet<JobStateReason> implements PrintJobAttribute {
    private static final long serialVersionUID = 8849088261264331812L;

    public JobStateReasons() {
    }

    public JobStateReasons(int i2) {
        super(i2);
    }

    public JobStateReasons(int i2, float f2) {
        super(i2, f2);
    }

    public JobStateReasons(Collection<JobStateReason> collection) {
        super(collection);
    }

    @Override // java.util.HashSet, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(JobStateReason jobStateReason) {
        if (jobStateReason == null) {
            throw new NullPointerException();
        }
        return super.add((JobStateReasons) jobStateReason);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return JobStateReasons.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "job-state-reasons";
    }
}
