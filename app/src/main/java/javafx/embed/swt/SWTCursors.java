package javafx.embed.swt;

import com.sun.javafx.cursor.CursorFrame;
import com.sun.javafx.cursor.ImageCursorFrame;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;

/* loaded from: jfxswt.jar:javafx/embed/swt/SWTCursors.class */
class SWTCursors {
    SWTCursors() {
    }

    private static Cursor createCustomCursor(ImageCursorFrame cursorFrame) {
        return null;
    }

    static Cursor embedCursorToCursor(CursorFrame cursorFrame) {
        int id = 0;
        switch (cursorFrame.getCursorType()) {
            case DEFAULT:
                id = 0;
                break;
            case CROSSHAIR:
                id = 2;
                break;
            case TEXT:
                id = 19;
                break;
            case WAIT:
                id = 1;
                break;
            case SW_RESIZE:
                id = 16;
                break;
            case SE_RESIZE:
                id = 15;
                break;
            case NW_RESIZE:
                id = 17;
                break;
            case NE_RESIZE:
                id = 14;
                break;
            case N_RESIZE:
                id = 10;
                break;
            case S_RESIZE:
                id = 11;
                break;
            case W_RESIZE:
                id = 13;
                break;
            case E_RESIZE:
                id = 12;
                break;
            case OPEN_HAND:
            case CLOSED_HAND:
            case HAND:
                id = 21;
                break;
            case MOVE:
                id = 5;
                break;
            case H_RESIZE:
                id = 9;
                break;
            case V_RESIZE:
                id = 7;
                break;
            case NONE:
                return null;
        }
        Display display = Display.getCurrent();
        if (display != null) {
            return display.getSystemCursor(id);
        }
        return null;
    }
}
