package javax.sql;

import java.util.EventListener;

/* loaded from: rt.jar:javax/sql/StatementEventListener.class */
public interface StatementEventListener extends EventListener {
    void statementClosed(StatementEvent statementEvent);

    void statementErrorOccurred(StatementEvent statementEvent);
}
