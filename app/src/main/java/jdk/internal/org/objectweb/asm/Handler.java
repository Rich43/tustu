package jdk.internal.org.objectweb.asm;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/Handler.class */
class Handler {
    Label start;
    Label end;
    Label handler;
    String desc;
    int type;
    Handler next;

    Handler() {
    }

    static Handler remove(Handler handler, Label label, Label label2) {
        if (handler == null) {
            return null;
        }
        handler.next = remove(handler.next, label, label2);
        int i2 = handler.start.position;
        int i3 = handler.end.position;
        int i4 = label.position;
        int i5 = label2 == null ? Integer.MAX_VALUE : label2.position;
        if (i4 < i3 && i5 > i2) {
            if (i4 <= i2) {
                if (i5 >= i3) {
                    handler = handler.next;
                } else {
                    handler.start = label2;
                }
            } else if (i5 >= i3) {
                handler.end = label;
            } else {
                Handler handler2 = new Handler();
                handler2.start = label2;
                handler2.end = handler.end;
                handler2.handler = handler.handler;
                handler2.desc = handler.desc;
                handler2.type = handler.type;
                handler2.next = handler.next;
                handler.end = label;
                handler.next = handler2;
            }
        }
        return handler;
    }
}
