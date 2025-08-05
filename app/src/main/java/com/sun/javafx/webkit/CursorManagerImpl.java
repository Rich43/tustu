package com.sun.javafx.webkit;

import com.sun.webkit.CursorManager;
import com.sun.webkit.graphics.WCGraphicsManager;
import com.sun.webkit.graphics.WCImage;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/CursorManagerImpl.class */
public final class CursorManagerImpl extends CursorManager<Cursor> {
    private final Map<String, Cursor> map = new HashMap();
    private ResourceBundle bundle;

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.webkit.CursorManager
    public Cursor getCustomCursor(WCImage image, int hotspotX, int hotspotY) {
        return new ImageCursor(Image.impl_fromPlatformImage(WCGraphicsManager.getGraphicsManager().toPlatformImage(image)), hotspotX, hotspotY);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.webkit.CursorManager
    public Cursor getPredefinedCursor(int type) {
        switch (type) {
            case 0:
            default:
                return Cursor.DEFAULT;
            case 1:
                return Cursor.CROSSHAIR;
            case 2:
                return Cursor.HAND;
            case 3:
                return Cursor.MOVE;
            case 4:
                return Cursor.TEXT;
            case 5:
                return Cursor.WAIT;
            case 6:
                return getCustomCursor("help", Cursor.DEFAULT);
            case 7:
                return Cursor.E_RESIZE;
            case 8:
                return Cursor.N_RESIZE;
            case 9:
                return Cursor.NE_RESIZE;
            case 10:
                return Cursor.NW_RESIZE;
            case 11:
                return Cursor.S_RESIZE;
            case 12:
                return Cursor.SE_RESIZE;
            case 13:
                return Cursor.SW_RESIZE;
            case 14:
                return Cursor.W_RESIZE;
            case 15:
                return Cursor.V_RESIZE;
            case 16:
                return Cursor.H_RESIZE;
            case 17:
                return getCustomCursor("resize.nesw", Cursor.DEFAULT);
            case 18:
                return getCustomCursor("resize.nwse", Cursor.DEFAULT);
            case 19:
                return getCustomCursor("resize.column", Cursor.H_RESIZE);
            case 20:
                return getCustomCursor("resize.row", Cursor.V_RESIZE);
            case 21:
                return getCustomCursor("panning.middle", Cursor.DEFAULT);
            case 22:
                return getCustomCursor("panning.east", Cursor.DEFAULT);
            case 23:
                return getCustomCursor("panning.north", Cursor.DEFAULT);
            case 24:
                return getCustomCursor("panning.ne", Cursor.DEFAULT);
            case 25:
                return getCustomCursor("panning.nw", Cursor.DEFAULT);
            case 26:
                return getCustomCursor("panning.south", Cursor.DEFAULT);
            case 27:
                return getCustomCursor("panning.se", Cursor.DEFAULT);
            case 28:
                return getCustomCursor("panning.sw", Cursor.DEFAULT);
            case 29:
                return getCustomCursor("panning.west", Cursor.DEFAULT);
            case 30:
                return getCustomCursor("vertical.text", Cursor.DEFAULT);
            case 31:
                return getCustomCursor("cell", Cursor.DEFAULT);
            case 32:
                return getCustomCursor("context.menu", Cursor.DEFAULT);
            case 33:
                return getCustomCursor("no.drop", Cursor.DEFAULT);
            case 34:
                return getCustomCursor("not.allowed", Cursor.DEFAULT);
            case 35:
                return getCustomCursor("progress", Cursor.WAIT);
            case 36:
                return getCustomCursor("alias", Cursor.DEFAULT);
            case 37:
                return getCustomCursor("zoom.in", Cursor.DEFAULT);
            case 38:
                return getCustomCursor("zoom.out", Cursor.DEFAULT);
            case 39:
                return getCustomCursor("copy", Cursor.DEFAULT);
            case 40:
                return Cursor.NONE;
            case 41:
                return getCustomCursor("grab", Cursor.OPEN_HAND);
            case 42:
                return getCustomCursor("grabbing", Cursor.CLOSED_HAND);
        }
    }

    private Cursor getCustomCursor(String name, Cursor predefined) throws NumberFormatException {
        Cursor cursor = this.map.get(name);
        if (cursor == null) {
            try {
                if (this.bundle == null) {
                    this.bundle = ResourceBundle.getBundle("com.sun.javafx.webkit.Cursors", Locale.getDefault());
                }
                if (this.bundle != null) {
                    String resource = this.bundle.getString(name + ".file");
                    Image image = new Image(CursorManagerImpl.class.getResourceAsStream(resource));
                    String resource2 = this.bundle.getString(name + ".hotspotX");
                    int hotspotX = Integer.parseInt(resource2);
                    String resource3 = this.bundle.getString(name + ".hotspotY");
                    int hotspotY = Integer.parseInt(resource3);
                    cursor = new ImageCursor(image, hotspotX, hotspotY);
                }
            } catch (MissingResourceException e2) {
            }
            if (cursor == null) {
                cursor = predefined;
            }
            this.map.put(name, cursor);
        }
        return cursor;
    }
}
