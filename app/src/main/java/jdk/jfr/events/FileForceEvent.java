package jdk.jfr.events;

import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Label;
import jdk.jfr.Name;

@Category({"Java Application"})
@Label("File Force")
@Name("jdk.FileForce")
@Description("Force updates to be written to file")
/* loaded from: jfr.jar:jdk/jfr/events/FileForceEvent.class */
public final class FileForceEvent extends AbstractJDKEvent {
    public static final ThreadLocal<FileForceEvent> EVENT = new ThreadLocal<FileForceEvent>() { // from class: jdk.jfr.events.FileForceEvent.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public FileForceEvent initialValue() {
            return new FileForceEvent();
        }
    };

    @Label("Path")
    @Description("Full path of the file")
    public String path;

    @Label("Update Metadata")
    @Description("Whether the file metadata is updated")
    public boolean metaData;

    public void reset() {
        this.path = null;
        this.metaData = false;
    }
}
