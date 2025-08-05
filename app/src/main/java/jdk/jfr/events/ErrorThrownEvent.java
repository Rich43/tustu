package jdk.jfr.events;

import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Label;
import jdk.jfr.Name;

@Category({"Java Application"})
@Label("Java Error")
@Name("jdk.JavaErrorThrow")
@Description("An object derived from java.lang.Error has been created. OutOfMemoryErrors are ignored")
/* loaded from: jfr.jar:jdk/jfr/events/ErrorThrownEvent.class */
public final class ErrorThrownEvent extends AbstractJDKEvent {

    @Label("Message")
    public String message;

    @Label("Class")
    public Class<?> thrownClass;
}
