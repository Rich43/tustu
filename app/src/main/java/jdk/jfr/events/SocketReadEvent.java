package jdk.jfr.events;

import jdk.jfr.Category;
import jdk.jfr.DataAmount;
import jdk.jfr.Description;
import jdk.jfr.Label;
import jdk.jfr.Name;
import jdk.jfr.Timespan;

@Category({"Java Application"})
@Label("Socket Read")
@Name("jdk.SocketRead")
@Description("Reading data from a socket")
/* loaded from: jfr.jar:jdk/jfr/events/SocketReadEvent.class */
public final class SocketReadEvent extends AbstractJDKEvent {
    public static final ThreadLocal<SocketReadEvent> EVENT = new ThreadLocal<SocketReadEvent>() { // from class: jdk.jfr.events.SocketReadEvent.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public SocketReadEvent initialValue() {
            return new SocketReadEvent();
        }
    };

    @Label("Remote Host")
    public String host;

    @Label("Remote Address")
    public String address;

    @Label("Remote Port")
    public int port;

    @Label("Timeout Value")
    @Timespan(Timespan.MILLISECONDS)
    public long timeout;

    @DataAmount
    @Label("Bytes Read")
    @Description("Number of bytes read from the socket")
    public long bytesRead;

    @Label("End of Stream")
    @Description("If end of stream was reached")
    public boolean endOfStream;

    public void reset() {
        this.host = null;
        this.address = null;
        this.port = 0;
        this.timeout = 0L;
        this.bytesRead = 0L;
        this.endOfStream = false;
    }
}
