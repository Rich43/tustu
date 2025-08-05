package sun.management;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import net.lingala.zip4j.util.InternalZipConstants;
import sun.management.counter.Counter;
import sun.management.counter.Units;
import sun.management.counter.perf.PerfInstrumentation;
import sun.misc.Perf;

/* loaded from: rt.jar:sun/management/ConnectorAddressLink.class */
public class ConnectorAddressLink {
    private static final String CONNECTOR_ADDRESS_COUNTER = "sun.management.JMXConnectorServer.address";
    private static final String REMOTE_CONNECTOR_COUNTER_PREFIX = "sun.management.JMXConnectorServer.";
    private static AtomicInteger counter = new AtomicInteger();

    public static void export(String str) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("address not specified");
        }
        Perf.getPerf().createString(CONNECTOR_ADDRESS_COUNTER, 1, Units.STRING.intValue(), str);
    }

    public static String importFrom(int i2) throws IOException {
        try {
            Iterator<Counter> it = new PerfInstrumentation(Perf.getPerf().attach(i2, InternalZipConstants.READ_MODE)).findByPattern(CONNECTOR_ADDRESS_COUNTER).iterator();
            if (it.hasNext()) {
                return (String) it.next().getValue();
            }
            return null;
        } catch (IllegalArgumentException e2) {
            throw new IOException(e2.getMessage());
        }
    }

    public static void exportRemote(Map<String, String> map) {
        int andIncrement = counter.getAndIncrement();
        Perf perf = Perf.getPerf();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            perf.createString(REMOTE_CONNECTOR_COUNTER_PREFIX + andIncrement + "." + entry.getKey(), 1, Units.STRING.intValue(), entry.getValue());
        }
    }

    public static Map<String, String> importRemoteFrom(int i2) throws IOException {
        try {
            List<Counter> allCounters = new PerfInstrumentation(Perf.getPerf().attach(i2, InternalZipConstants.READ_MODE)).getAllCounters();
            HashMap map = new HashMap();
            for (Counter counter2 : allCounters) {
                String name = counter2.getName();
                if (name.startsWith(REMOTE_CONNECTOR_COUNTER_PREFIX) && !name.equals(CONNECTOR_ADDRESS_COUNTER)) {
                    map.put(name, counter2.getValue().toString());
                }
            }
            return map;
        } catch (IllegalArgumentException e2) {
            throw new IOException(e2.getMessage());
        }
    }
}
