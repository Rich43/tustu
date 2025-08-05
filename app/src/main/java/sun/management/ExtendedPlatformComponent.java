package sun.management;

import java.lang.management.PlatformManagedObject;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

/* loaded from: rt.jar:sun/management/ExtendedPlatformComponent.class */
public final class ExtendedPlatformComponent {
    private ExtendedPlatformComponent() {
    }

    public static List<? extends PlatformManagedObject> getMXBeans() {
        PlatformManagedObject flightRecorderBean = getFlightRecorderBean();
        if (flightRecorderBean != null) {
            return Collections.singletonList(flightRecorderBean);
        }
        return Collections.emptyList();
    }

    public static <T extends PlatformManagedObject> T getMXBean(Class<T> cls) {
        if ("jdk.management.jfr.FlightRecorderMXBean".equals(cls.getName())) {
            return (T) getFlightRecorderBean();
        }
        return null;
    }

    private static PlatformManagedObject getFlightRecorderBean() {
        PlatformManagedObject platformManagedObject = null;
        try {
            platformManagedObject = (PlatformManagedObject) Class.forName("jdk.management.jfr.internal.FlightRecorderMXBeanProvider").getDeclaredMethod("getFlightRecorderMXBean", new Class[0]).invoke(null, new Object[0]);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e2) {
        }
        return platformManagedObject;
    }
}
