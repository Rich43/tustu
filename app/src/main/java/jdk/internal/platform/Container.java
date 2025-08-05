package jdk.internal.platform;

/* loaded from: rt.jar:jdk/internal/platform/Container.class */
public class Container {
    private Container() {
    }

    public static Metrics metrics() {
        return Metrics.systemMetrics();
    }
}
