package jdk.jfr.events;

import jdk.jfr.Category;
import jdk.jfr.DataAmount;
import jdk.jfr.Description;
import jdk.jfr.Label;
import jdk.jfr.Name;

@Category({"Java Application"})
@Label("File Read")
@Name("jdk.FileRead")
@Description("Reading data from a file")
/* loaded from: jfr.jar:jdk/jfr/events/FileReadEvent.class */
public final class FileReadEvent extends AbstractJDKEvent {
    public static final ThreadLocal<FileReadEvent> EVENT = new ThreadLocal<FileReadEvent>() { // from class: jdk.jfr.events.FileReadEvent.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public FileReadEvent initialValue() {
            return new FileReadEvent();
        }
    };

    @Label("Path")
    @Description("Full path of the file")
    public String path;

    @DataAmount
    @Label("Bytes Read")
    @Description("Number of bytes read from the file (possibly 0)")
    public long bytesRead;

    @Label("End of File")
    @Description("If end of file was reached")
    public boolean endOfFile;

    public void reset() {
        this.path = null;
        this.endOfFile = false;
        this.bytesRead = 0L;
    }
}
