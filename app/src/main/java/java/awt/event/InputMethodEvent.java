package java.awt.event;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.font.TextHitInfo;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.AttributedCharacterIterator;
import org.icepdf.core.util.PdfOps;
import sun.awt.AWTAccessor;
import sun.awt.SunToolkit;

/* loaded from: rt.jar:java/awt/event/InputMethodEvent.class */
public class InputMethodEvent extends AWTEvent {
    private static final long serialVersionUID = 4727190874778922661L;
    public static final int INPUT_METHOD_FIRST = 1100;
    public static final int INPUT_METHOD_TEXT_CHANGED = 1100;
    public static final int CARET_POSITION_CHANGED = 1101;
    public static final int INPUT_METHOD_LAST = 1101;
    long when;
    private transient AttributedCharacterIterator text;
    private transient int committedCharacterCount;
    private transient TextHitInfo caret;
    private transient TextHitInfo visiblePosition;

    public InputMethodEvent(Component component, int i2, long j2, AttributedCharacterIterator attributedCharacterIterator, int i3, TextHitInfo textHitInfo, TextHitInfo textHitInfo2) {
        super(component, i2);
        if (i2 < 1100 || i2 > 1101) {
            throw new IllegalArgumentException("id outside of valid range");
        }
        if (i2 == 1101 && attributedCharacterIterator != null) {
            throw new IllegalArgumentException("text must be null for CARET_POSITION_CHANGED");
        }
        this.when = j2;
        this.text = attributedCharacterIterator;
        int endIndex = attributedCharacterIterator != null ? attributedCharacterIterator.getEndIndex() - attributedCharacterIterator.getBeginIndex() : 0;
        if (i3 < 0 || i3 > endIndex) {
            throw new IllegalArgumentException("committedCharacterCount outside of valid range");
        }
        this.committedCharacterCount = i3;
        this.caret = textHitInfo;
        this.visiblePosition = textHitInfo2;
    }

    public InputMethodEvent(Component component, int i2, AttributedCharacterIterator attributedCharacterIterator, int i3, TextHitInfo textHitInfo, TextHitInfo textHitInfo2) {
        this(component, i2, getMostRecentEventTimeForSource(component), attributedCharacterIterator, i3, textHitInfo, textHitInfo2);
    }

    public InputMethodEvent(Component component, int i2, TextHitInfo textHitInfo, TextHitInfo textHitInfo2) {
        this(component, i2, getMostRecentEventTimeForSource(component), null, 0, textHitInfo, textHitInfo2);
    }

    public AttributedCharacterIterator getText() {
        return this.text;
    }

    public int getCommittedCharacterCount() {
        return this.committedCharacterCount;
    }

    public TextHitInfo getCaret() {
        return this.caret;
    }

    public TextHitInfo getVisiblePosition() {
        return this.visiblePosition;
    }

    @Override // java.awt.AWTEvent
    public void consume() {
        this.consumed = true;
    }

    @Override // java.awt.AWTEvent
    public boolean isConsumed() {
        return this.consumed;
    }

    public long getWhen() {
        return this.when;
    }

    @Override // java.awt.AWTEvent
    public String paramString() {
        String str;
        String string;
        String str2;
        String str3;
        switch (this.id) {
            case 1100:
                str = "INPUT_METHOD_TEXT_CHANGED";
                break;
            case 1101:
                str = "CARET_POSITION_CHANGED";
                break;
            default:
                str = "unknown type";
                break;
        }
        if (this.text == null) {
            string = "no text";
        } else {
            StringBuilder sb = new StringBuilder(PdfOps.DOUBLE_QUOTE__TOKEN);
            int i2 = this.committedCharacterCount;
            char cFirst = this.text.first();
            while (true) {
                char next = cFirst;
                int i3 = i2;
                i2--;
                if (i3 > 0) {
                    sb.append(next);
                    cFirst = this.text.next();
                } else {
                    sb.append("\" + \"");
                    while (next != 65535) {
                        sb.append(next);
                        next = this.text.next();
                    }
                    sb.append(PdfOps.DOUBLE_QUOTE__TOKEN);
                    string = sb.toString();
                }
            }
        }
        String str4 = this.committedCharacterCount + " characters committed";
        if (this.caret == null) {
            str2 = "no caret";
        } else {
            str2 = "caret: " + this.caret.toString();
        }
        if (this.visiblePosition == null) {
            str3 = "no visible position";
        } else {
            str3 = "visible position: " + this.visiblePosition.toString();
        }
        return str + ", " + string + ", " + str4 + ", " + str2 + ", " + str3;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.when == 0) {
            this.when = EventQueue.getMostRecentEventTime();
        }
    }

    private static long getMostRecentEventTimeForSource(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("null source");
        }
        return AWTAccessor.getEventQueueAccessor().getMostRecentEventTime(SunToolkit.getSystemEventQueueImplPP(SunToolkit.targetToAppContext(obj)));
    }
}
