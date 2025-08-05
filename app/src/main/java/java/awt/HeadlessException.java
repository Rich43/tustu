package java.awt;

/* loaded from: rt.jar:java/awt/HeadlessException.class */
public class HeadlessException extends UnsupportedOperationException {
    private static final long serialVersionUID = 167183644944358563L;

    public HeadlessException() {
    }

    public HeadlessException(String str) {
        super(str);
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        String message = super.getMessage();
        String headlessMessage = GraphicsEnvironment.getHeadlessMessage();
        if (message == null) {
            return headlessMessage;
        }
        if (headlessMessage == null) {
            return message;
        }
        return message + headlessMessage;
    }
}
