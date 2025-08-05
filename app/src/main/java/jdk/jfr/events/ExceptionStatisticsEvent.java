package jdk.jfr.events;

import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Label;
import jdk.jfr.Name;
import jdk.jfr.StackTrace;

@Category({"Java Application", "Statistics"})
@Label("Exception Statistics")
@StackTrace(false)
@Name("jdk.ExceptionStatistics")
@Description("Number of objects derived from java.lang.Throwable that have been created")
/* loaded from: jfr.jar:jdk/jfr/events/ExceptionStatisticsEvent.class */
public final class ExceptionStatisticsEvent extends AbstractJDKEvent {

    @Label("Exceptions Created")
    public long throwables;
}
