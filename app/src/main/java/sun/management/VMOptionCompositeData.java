package sun.management;

import com.sun.management.VMOption;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;

/* loaded from: rt.jar:sun/management/VMOptionCompositeData.class */
public class VMOptionCompositeData extends LazyCompositeData {
    private final VMOption option;
    private static final CompositeType vmOptionCompositeType;
    private static final String NAME = "name";
    private static final String VALUE = "value";
    private static final String WRITEABLE = "writeable";
    private static final String ORIGIN = "origin";
    private static final String[] vmOptionItemNames;
    private static final long serialVersionUID = -2395573975093578470L;

    private VMOptionCompositeData(VMOption vMOption) {
        this.option = vMOption;
    }

    public VMOption getVMOption() {
        return this.option;
    }

    public static CompositeData toCompositeData(VMOption vMOption) {
        return new VMOptionCompositeData(vMOption).getCompositeData();
    }

    @Override // sun.management.LazyCompositeData
    protected CompositeData getCompositeData() {
        try {
            return new CompositeDataSupport(vmOptionCompositeType, vmOptionItemNames, new Object[]{this.option.getName(), this.option.getValue(), new Boolean(this.option.isWriteable()), this.option.getOrigin().toString()});
        } catch (OpenDataException e2) {
            throw new AssertionError(e2);
        }
    }

    static {
        try {
            vmOptionCompositeType = (CompositeType) MappedMXBeanType.toOpenType(VMOption.class);
            vmOptionItemNames = new String[]{"name", "value", WRITEABLE, ORIGIN};
        } catch (OpenDataException e2) {
            throw new AssertionError(e2);
        }
    }

    static CompositeType getVMOptionCompositeType() {
        return vmOptionCompositeType;
    }

    public static String getName(CompositeData compositeData) {
        return getString(compositeData, "name");
    }

    public static String getValue(CompositeData compositeData) {
        return getString(compositeData, "value");
    }

    public static VMOption.Origin getOrigin(CompositeData compositeData) {
        return (VMOption.Origin) Enum.valueOf(VMOption.Origin.class, getString(compositeData, ORIGIN));
    }

    public static boolean isWriteable(CompositeData compositeData) {
        return getBoolean(compositeData, WRITEABLE);
    }

    public static void validateCompositeData(CompositeData compositeData) {
        if (compositeData == null) {
            throw new NullPointerException("Null CompositeData");
        }
        if (!isTypeMatched(vmOptionCompositeType, compositeData.getCompositeType())) {
            throw new IllegalArgumentException("Unexpected composite type for VMOption");
        }
    }
}
