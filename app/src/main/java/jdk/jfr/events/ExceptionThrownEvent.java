package jdk.jfr.events;

import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Label;
import jdk.jfr.Name;

@Category({"Java Application"})
@Label("Java Exception")
@Name("jdk.JavaExceptionThrow")
@Description("An object derived from java.lang.Exception has been created")
/* loaded from: jfr.jar:jdk/jfr/events/ExceptionThrownEvent.class */
public final class ExceptionThrownEvent extends AbstractJDKEvent {

    @Label("Message")
    public String message;

    @Label("Class")
    public Class<?> thrownClass;
}
