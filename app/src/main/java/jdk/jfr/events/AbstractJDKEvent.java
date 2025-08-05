package jdk.jfr.events;

import jdk.jfr.Enabled;
import jdk.jfr.Event;
import jdk.jfr.Registered;
import jdk.jfr.StackTrace;

@Enabled(false)
@Registered(false)
@StackTrace(false)
/* loaded from: jfr.jar:jdk/jfr/events/AbstractJDKEvent.class */
abstract class AbstractJDKEvent extends Event {
    AbstractJDKEvent() {
    }
}
