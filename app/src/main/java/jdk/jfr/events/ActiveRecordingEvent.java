package jdk.jfr.events;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import jdk.jfr.Category;
import jdk.jfr.DataAmount;
import jdk.jfr.Label;
import jdk.jfr.Name;
import jdk.jfr.StackTrace;
import jdk.jfr.Timespan;
import jdk.jfr.Timestamp;

@Category({"Flight Recorder"})
@Label("Flight Recording")
@StackTrace(false)
@Name("jdk.ActiveRecording")
/* loaded from: jfr.jar:jdk/jfr/events/ActiveRecordingEvent.class */
public final class ActiveRecordingEvent extends AbstractJDKEvent {

    @Label(Constants._ATT_ID)
    public long id;

    @Label("Name")
    public String name;

    @Label("Destination")
    public String destination;

    @Label("Max Age")
    @Timespan(Timespan.MILLISECONDS)
    public long maxAge;

    @DataAmount
    @Label("Max Size")
    public long maxSize;

    @Label("Start Time")
    @Timestamp(Timestamp.MILLISECONDS_SINCE_EPOCH)
    public long recordingStart;

    @Label("Recording Duration")
    @Timespan(Timespan.MILLISECONDS)
    public long recordingDuration;
}
