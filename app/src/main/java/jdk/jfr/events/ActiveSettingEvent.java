package jdk.jfr.events;

import jdk.jfr.Category;
import jdk.jfr.Label;
import jdk.jfr.Name;
import jdk.jfr.StackTrace;

@Category({"Flight Recorder"})
@Label("Recording Setting")
@StackTrace(false)
@Name("jdk.ActiveSetting")
/* loaded from: jfr.jar:jdk/jfr/events/ActiveSettingEvent.class */
public final class ActiveSettingEvent extends AbstractJDKEvent {

    @Label("Event Id")
    public long id;

    @Label("Setting Name")
    public String name;

    @Label("Setting Value")
    public String value;
}
