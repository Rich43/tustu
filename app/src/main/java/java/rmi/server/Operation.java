package java.rmi.server;

@Deprecated
/* loaded from: rt.jar:java/rmi/server/Operation.class */
public class Operation {
    private String operation;

    @Deprecated
    public Operation(String str) {
        this.operation = str;
    }

    @Deprecated
    public String getOperation() {
        return this.operation;
    }

    @Deprecated
    public String toString() {
        return this.operation;
    }
}
