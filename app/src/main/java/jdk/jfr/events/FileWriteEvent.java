package jdk.jfr.events;

import jdk.jfr.Category;
import jdk.jfr.DataAmount;
import jdk.jfr.Description;
import jdk.jfr.Label;
import jdk.jfr.Name;

@Category({"Java Application"})
@Label("File Write")
@Name("jdk.FileWrite")
@Description("Writing data to a file")
/* loaded from: jfr.jar:jdk/jfr/events/FileWriteEvent.class */
public final class FileWriteEvent extends AbstractJDKEvent {
    public static final ThreadLocal<FileWriteEvent> EVENT = new ThreadLocal<FileWriteEvent>() { // from class: jdk.jfr.events.FileWriteEvent.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public FileWriteEvent initialValue() {
            return new FileWriteEvent();
        }
    };

    @Label("Path")
    @Description("Full path of the file")
    public String path;

    @DataAmount
    @Label("Bytes Written")
    @Description("Number of bytes written to the file")
    public long bytesWritten;

    public void reset() {
        this.path = null;
        this.bytesWritten = 0L;
    }
}
