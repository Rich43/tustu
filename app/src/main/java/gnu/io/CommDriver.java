package gnu.io;

/* loaded from: RXTXcomm.jar:gnu/io/CommDriver.class */
public interface CommDriver {
    CommPort getCommPort(String str, int i2);

    void initialize();
}
