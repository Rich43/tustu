package jdk.management.jfr.internal;

import java.util.concurrent.Callable;
import jdk.jfr.internal.management.ManagementSupport;
import jdk.management.jfr.FlightRecorderMXBean;
import jdk.management.jfr.SettingDescriptorInfo;

/* loaded from: jfr.jar:jdk/management/jfr/internal/FlightRecorderMXBeanProvider.class */
public final class FlightRecorderMXBeanProvider {
    private static Callable<FlightRecorderMXBean> flightRecorderMXBeanFactory;
    private static volatile FlightRecorderMXBean flightRecorderMXBean;

    /* loaded from: jfr.jar:jdk/management/jfr/internal/FlightRecorderMXBeanProvider$SingleMBeanComponent.class */
    private static final class SingleMBeanComponent {
        private final String objectName;
        private final Class<FlightRecorderMXBean> mbeanInterface;

        public SingleMBeanComponent(String str, Class<FlightRecorderMXBean> cls) {
            this.objectName = str;
            this.mbeanInterface = cls;
        }
    }

    public static FlightRecorderMXBean getFlightRecorderMXBean() {
        FlightRecorderMXBean flightRecorderMXBean2 = flightRecorderMXBean;
        if (flightRecorderMXBean2 == null) {
            SettingDescriptorInfo.from(null);
            synchronized (flightRecorderMXBeanFactory) {
                flightRecorderMXBean2 = flightRecorderMXBean;
                if (flightRecorderMXBean2 != null) {
                    return flightRecorderMXBean2;
                }
                try {
                    FlightRecorderMXBean flightRecorderMXBeanCall = flightRecorderMXBeanFactory.call();
                    flightRecorderMXBean = flightRecorderMXBeanCall;
                    flightRecorderMXBean2 = flightRecorderMXBeanCall;
                } catch (Exception e2) {
                    ManagementSupport.logError("Could not create Flight Recorder instance for MBeanServer. " + e2.getMessage());
                }
            }
        }
        return flightRecorderMXBean2;
    }

    public static void setFlightRecorderMXBeanFactory(Callable<FlightRecorderMXBean> callable) {
        flightRecorderMXBeanFactory = callable;
    }
}
