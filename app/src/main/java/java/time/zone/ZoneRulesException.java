package java.time.zone;

import java.time.DateTimeException;

/* loaded from: rt.jar:java/time/zone/ZoneRulesException.class */
public class ZoneRulesException extends DateTimeException {
    private static final long serialVersionUID = -1632418723876261839L;

    public ZoneRulesException(String str) {
        super(str);
    }

    public ZoneRulesException(String str, Throwable th) {
        super(str, th);
    }
}
