package jdk.jfr.events;

import jdk.jfr.Category;
import jdk.jfr.DataAmount;
import jdk.jfr.Description;
import jdk.jfr.Label;
import jdk.jfr.Name;

@Category({"Java Application"})
@Label("Socket Write")
@Name("jdk.SocketWrite")
@Description("Writing data to a socket")
/* loaded from: jfr.jar:jdk/jfr/events/SocketWriteEvent.class */
public final class SocketWriteEvent extends AbstractJDKEvent {
    public static final ThreadLocal<SocketWriteEvent> EVENT = new ThreadLocal<SocketWriteEvent>() { // from class: jdk.jfr.events.SocketWriteEvent.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public SocketWriteEvent initialValue() {
            return new SocketWriteEvent();
        }
    };

    @Label("Remote Host")
    public String host;

    @Label("Remote Address")
    public String address;

    @Label("Remote Port")
    public int port;

    @DataAmount
    @Label("Bytes Written")
    @Description("Number of bytes written to the socket")
    public long bytesWritten;

    public void reset() {
        this.host = null;
        this.address = null;
        this.port = 0;
        this.bytesWritten = 0L;
    }
}
