package sun.swing;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.PrintGraphics;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.print.PrinterGraphics;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.BreakIterator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.event.TreeModelEvent;
import javax.swing.table.TableColumnModel;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import sun.awt.AppContext;
import sun.awt.SunToolkit;
import sun.font.FontDesignMetrics;
import sun.font.FontUtilities;
import sun.java2d.SunGraphicsEnvironment;
import sun.print.ProxyPrintGraphics;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:sun/swing/SwingUtilities2.class */
public class SwingUtilities2 {
    public static final Object LAF_STATE_KEY;
    public static final Object MENU_SELECTION_MANAGER_LISTENER_KEY;
    private static LSBCacheEntry[] fontCache;
    private static final int CACHE_SIZE = 6;
    private static int nextIndex;
    private static LSBCacheEntry searchKey;
    private static final int MIN_CHAR_INDEX = 87;
    private static final int MAX_CHAR_INDEX = 88;
    public static final FontRenderContext DEFAULT_FRC;
    public static final Object AA_TEXT_PROPERTY_KEY;
    public static final String IMPLIED_CR = "CR";
    private static final StringBuilder SKIP_CLICK_COUNT;
    public static final Object COMPONENT_UI_PROPERTY_KEY;
    public static final StringUIClientPropertyKey BASICMENUITEMUI_MAX_TEXT_OFFSET;
    private static Field inputEvent_CanAccessSystemClipboard_Field;
    private static final String UntrustedClipboardAccess = "UNTRUSTED_CLIPBOARD_ACCESS_KEY";
    private static final int CHAR_BUFFER_SIZE = 100;
    private static final Object charsBufferLock;
    private static char[] charsBuffer;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: rt.jar:sun/swing/SwingUtilities2$RepaintListener.class */
    public interface RepaintListener {
        void repaintPerformed(JComponent jComponent, int i2, int i3, int i4, int i5);
    }

    /* loaded from: rt.jar:sun/swing/SwingUtilities2$Section.class */
    public enum Section {
        LEADING,
        MIDDLE,
        TRAILING
    }

    static {
        $assertionsDisabled = !SwingUtilities2.class.desiredAssertionStatus();
        LAF_STATE_KEY = new StringBuffer("LookAndFeel State");
        MENU_SELECTION_MANAGER_LISTENER_KEY = new StringBuffer("MenuSelectionManager listener key");
        DEFAULT_FRC = new FontRenderContext((AffineTransform) null, false, false);
        AA_TEXT_PROPERTY_KEY = new StringBuffer("AATextInfoPropertyKey");
        SKIP_CLICK_COUNT = new StringBuilder("skipClickCount");
        COMPONENT_UI_PROPERTY_KEY = new StringBuffer("ComponentUIPropertyKey");
        BASICMENUITEMUI_MAX_TEXT_OFFSET = new StringUIClientPropertyKey("maxTextOffset");
        inputEvent_CanAccessSystemClipboard_Field = null;
        charsBufferLock = new Object();
        charsBuffer = new char[100];
        fontCache = new LSBCacheEntry[6];
    }

    /* loaded from: rt.jar:sun/swing/SwingUtilities2$AATextInfo.class */
    public static class AATextInfo {
        Object aaHint;
        Integer lcdContrastHint;
        FontRenderContext frc;

        private static AATextInfo getAATextInfoFromMap(Map map) {
            Object obj = map.get(RenderingHints.KEY_TEXT_ANTIALIASING);
            Object obj2 = map.get(RenderingHints.KEY_TEXT_LCD_CONTRAST);
            if (obj == null || obj == RenderingHints.VALUE_TEXT_ANTIALIAS_OFF || obj == RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT) {
                return null;
            }
            return new AATextInfo(obj, (Integer) obj2);
        }

        public static AATextInfo getAATextInfo(boolean z2) {
            SunToolkit.setAAFontSettingsCondition(z2);
            Object desktopProperty = Toolkit.getDefaultToolkit().getDesktopProperty(SunToolkit.DESKTOPFONTHINTS);
            if (desktopProperty instanceof Map) {
                return getAATextInfoFromMap((Map) desktopProperty);
            }
            return null;
        }

        public AATextInfo(Object obj, Integer num) {
            if (obj == null) {
                throw new InternalError("null not allowed here");
            }
            if (obj == RenderingHints.VALUE_TEXT_ANTIALIAS_OFF || obj == RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT) {
                throw new InternalError("AA must be on");
            }
            this.aaHint = obj;
            this.lcdContrastHint = num;
            this.frc = new FontRenderContext((AffineTransform) null, obj, RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT);
        }
    }

    private static int syncCharsBuffer(String str) {
        int length = str.length();
        if (charsBuffer == null || charsBuffer.length < length) {
            charsBuffer = str.toCharArray();
        } else {
            str.getChars(0, length, charsBuffer, 0);
        }
        return length;
    }

    public static final boolean isComplexLayout(char[] cArr, int i2, int i3) {
        return FontUtilities.isComplexText(cArr, i2, i3);
    }

    public static AATextInfo drawTextAntialiased(JComponent jComponent) {
        if (jComponent != null) {
            return (AATextInfo) jComponent.getClientProperty(AA_TEXT_PROPERTY_KEY);
        }
        return null;
    }

    public static int getLeftSideBearing(JComponent jComponent, FontMetrics fontMetrics, String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        return getLeftSideBearing(jComponent, fontMetrics, str.charAt(0));
    }

    public static int getLeftSideBearing(JComponent jComponent, FontMetrics fontMetrics, char c2) {
        int leftSideBearing;
        if (c2 < 'X' && c2 >= 'W') {
            FontRenderContext fontRenderContext = getFontRenderContext(jComponent, fontMetrics);
            Font font = fontMetrics.getFont();
            synchronized (SwingUtilities2.class) {
                LSBCacheEntry lSBCacheEntry = null;
                if (searchKey == null) {
                    searchKey = new LSBCacheEntry(fontRenderContext, font);
                } else {
                    searchKey.reset(fontRenderContext, font);
                }
                LSBCacheEntry[] lSBCacheEntryArr = fontCache;
                int length = lSBCacheEntryArr.length;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        break;
                    }
                    LSBCacheEntry lSBCacheEntry2 = lSBCacheEntryArr[i2];
                    if (!searchKey.equals(lSBCacheEntry2)) {
                        i2++;
                    } else {
                        lSBCacheEntry = lSBCacheEntry2;
                        break;
                    }
                }
                if (lSBCacheEntry == null) {
                    lSBCacheEntry = searchKey;
                    fontCache[nextIndex] = searchKey;
                    searchKey = null;
                    nextIndex = (nextIndex + 1) % 6;
                }
                leftSideBearing = lSBCacheEntry.getLeftSideBearing(c2);
            }
            return leftSideBearing;
        }
        return 0;
    }

    public static FontMetrics getFontMetrics(JComponent jComponent, Graphics graphics) {
        return getFontMetrics(jComponent, graphics, graphics.getFont());
    }

    public static FontMetrics getFontMetrics(JComponent jComponent, Graphics graphics, Font font) {
        if (jComponent != null) {
            return jComponent.getFontMetrics(font);
        }
        return Toolkit.getDefaultToolkit().getFontMetrics(font);
    }

    public static int stringWidth(JComponent jComponent, FontMetrics fontMetrics, String str) {
        if (str == null || str.equals("")) {
            return 0;
        }
        boolean zIsComplexLayout = (jComponent == null || jComponent.getClientProperty(TextAttribute.NUMERIC_SHAPING) == null) ? false : true;
        if (zIsComplexLayout) {
            synchronized (charsBufferLock) {
                zIsComplexLayout = isComplexLayout(charsBuffer, 0, syncCharsBuffer(str));
            }
        }
        if (zIsComplexLayout) {
            return (int) createTextLayout(jComponent, str, fontMetrics.getFont(), fontMetrics.getFontRenderContext()).getAdvance();
        }
        return fontMetrics.stringWidth(str);
    }

    public static String clipStringIfNecessary(JComponent jComponent, FontMetrics fontMetrics, String str, int i2) {
        if (str == null || str.equals("")) {
            return "";
        }
        if (stringWidth(jComponent, fontMetrics, str) > i2) {
            return clipString(jComponent, fontMetrics, str, i2);
        }
        return str;
    }

    public static String clipString(JComponent jComponent, FontMetrics fontMetrics, String str, int i2) {
        boolean zIsComplexLayout;
        int iStringWidth = i2 - stringWidth(jComponent, fontMetrics, "...");
        if (iStringWidth <= 0) {
            return "...";
        }
        synchronized (charsBufferLock) {
            int iSyncCharsBuffer = syncCharsBuffer(str);
            zIsComplexLayout = isComplexLayout(charsBuffer, 0, iSyncCharsBuffer);
            if (!zIsComplexLayout) {
                int iCharWidth = 0;
                int i3 = 0;
                while (true) {
                    if (i3 >= iSyncCharsBuffer) {
                        break;
                    }
                    iCharWidth += fontMetrics.charWidth(charsBuffer[i3]);
                    if (iCharWidth <= iStringWidth) {
                        i3++;
                    } else {
                        str = str.substring(0, i3);
                        break;
                    }
                }
            }
        }
        if (zIsComplexLayout) {
            AttributedString attributedString = new AttributedString(str);
            if (jComponent != null) {
                attributedString.addAttribute(TextAttribute.NUMERIC_SHAPING, jComponent.getClientProperty(TextAttribute.NUMERIC_SHAPING));
            }
            str = str.substring(0, new LineBreakMeasurer(attributedString.getIterator(), BreakIterator.getCharacterInstance(), getFontRenderContext(jComponent, fontMetrics)).nextOffset(iStringWidth));
        }
        return str + "...";
    }

    public static void drawString(JComponent jComponent, Graphics graphics, String str, int i2, int i3) {
        Graphics2D graphics2D;
        if (str == null || str.length() <= 0) {
            return;
        }
        if (isPrinting(graphics) && (graphics2D = getGraphics2D(graphics)) != null) {
            String strTrimTrailingSpaces = trimTrailingSpaces(str);
            if (!strTrimTrailingSpaces.isEmpty()) {
                float width = (float) graphics2D.getFont().getStringBounds(strTrimTrailingSpaces, getFontRenderContext(jComponent)).getWidth();
                TextLayout textLayoutCreateTextLayout = createTextLayout(jComponent, str, graphics2D.getFont(), graphics2D.getFontRenderContext());
                if (stringWidth(jComponent, graphics2D.getFontMetrics(), strTrimTrailingSpaces) > width) {
                    textLayoutCreateTextLayout = textLayoutCreateTextLayout.getJustifiedLayout(width);
                }
                Color color = graphics2D.getColor();
                if (color instanceof PrintColorUIResource) {
                    graphics2D.setColor(((PrintColorUIResource) color).getPrintColor());
                }
                textLayoutCreateTextLayout.draw(graphics2D, i2, i3);
                graphics2D.setColor(color);
                return;
            }
            return;
        }
        if (graphics instanceof Graphics2D) {
            AATextInfo aATextInfoDrawTextAntialiased = drawTextAntialiased(jComponent);
            Graphics2D graphics2D2 = (Graphics2D) graphics;
            boolean zIsComplexLayout = (jComponent == null || jComponent.getClientProperty(TextAttribute.NUMERIC_SHAPING) == null) ? false : true;
            if (zIsComplexLayout) {
                synchronized (charsBufferLock) {
                    zIsComplexLayout = isComplexLayout(charsBuffer, 0, syncCharsBuffer(str));
                }
            }
            if (aATextInfoDrawTextAntialiased != null) {
                Object renderingHint = null;
                Object renderingHint2 = graphics2D2.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
                if (aATextInfoDrawTextAntialiased.aaHint != renderingHint2) {
                    graphics2D2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, aATextInfoDrawTextAntialiased.aaHint);
                } else {
                    renderingHint2 = null;
                }
                if (aATextInfoDrawTextAntialiased.lcdContrastHint != null) {
                    renderingHint = graphics2D2.getRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST);
                    if (aATextInfoDrawTextAntialiased.lcdContrastHint.equals(renderingHint)) {
                        renderingHint = null;
                    } else {
                        graphics2D2.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, aATextInfoDrawTextAntialiased.lcdContrastHint);
                    }
                }
                if (zIsComplexLayout) {
                    createTextLayout(jComponent, str, graphics2D2.getFont(), graphics2D2.getFontRenderContext()).draw(graphics2D2, i2, i3);
                } else {
                    graphics.drawString(str, i2, i3);
                }
                if (renderingHint2 != null) {
                    graphics2D2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, renderingHint2);
                }
                if (renderingHint != null) {
                    graphics2D2.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, renderingHint);
                    return;
                }
                return;
            }
            if (zIsComplexLayout) {
                createTextLayout(jComponent, str, graphics2D2.getFont(), graphics2D2.getFontRenderContext()).draw(graphics2D2, i2, i3);
                return;
            }
        }
        graphics.drawString(str, i2, i3);
    }

    public static void drawStringUnderlineCharAt(JComponent jComponent, Graphics graphics, String str, int i2, int i3, int i4) {
        if (str == null || str.length() <= 0) {
            return;
        }
        drawString(jComponent, graphics, str, i3, i4);
        int length = str.length();
        if (i2 >= 0 && i2 < length) {
            int iStringWidth = 0;
            int iCharWidth = 0;
            boolean zIsPrinting = isPrinting(graphics);
            boolean zIsComplexLayout = zIsPrinting;
            if (!zIsComplexLayout) {
                synchronized (charsBufferLock) {
                    syncCharsBuffer(str);
                    zIsComplexLayout = isComplexLayout(charsBuffer, 0, length);
                }
            }
            if (!zIsComplexLayout) {
                FontMetrics fontMetrics = graphics.getFontMetrics();
                iStringWidth = i3 + stringWidth(jComponent, fontMetrics, str.substring(0, i2));
                iCharWidth = fontMetrics.charWidth(str.charAt(i2));
            } else {
                Graphics2D graphics2D = getGraphics2D(graphics);
                if (graphics2D != null) {
                    TextLayout textLayoutCreateTextLayout = createTextLayout(jComponent, str, graphics2D.getFont(), graphics2D.getFontRenderContext());
                    if (zIsPrinting) {
                        float width = (float) graphics2D.getFont().getStringBounds(str, getFontRenderContext(jComponent)).getWidth();
                        if (stringWidth(jComponent, graphics2D.getFontMetrics(), str) > width) {
                            textLayoutCreateTextLayout = textLayoutCreateTextLayout.getJustifiedLayout(width);
                        }
                    }
                    Rectangle bounds = textLayoutCreateTextLayout.getVisualHighlightShape(TextHitInfo.leading(i2), TextHitInfo.trailing(i2)).getBounds();
                    iStringWidth = i3 + bounds.f12372x;
                    iCharWidth = bounds.width;
                }
            }
            graphics.fillRect(iStringWidth, i4 + 1, iCharWidth, 1);
        }
    }

    public static int loc2IndexFileList(JList jList, Point point) {
        int iLocationToIndex = jList.locationToIndex(point);
        if (iLocationToIndex != -1) {
            Object clientProperty = jList.getClientProperty("List.isFileList");
            if ((clientProperty instanceof Boolean) && ((Boolean) clientProperty).booleanValue() && !pointIsInActualBounds(jList, iLocationToIndex, point)) {
                iLocationToIndex = -1;
            }
        }
        return iLocationToIndex;
    }

    private static boolean pointIsInActualBounds(JList jList, int i2, Point point) {
        Component listCellRendererComponent = jList.getCellRenderer().getListCellRendererComponent(jList, jList.getModel().getElementAt(i2), i2, false, false);
        Dimension preferredSize = listCellRendererComponent.getPreferredSize();
        Rectangle cellBounds = jList.getCellBounds(i2, i2);
        if (!listCellRendererComponent.getComponentOrientation().isLeftToRight()) {
            cellBounds.f12372x += cellBounds.width - preferredSize.width;
        }
        cellBounds.width = preferredSize.width;
        return cellBounds.contains(point);
    }

    public static boolean pointOutsidePrefSize(JTable jTable, int i2, int i3, Point point) {
        if (jTable.convertColumnIndexToModel(i3) != 0 || i2 == -1) {
            return true;
        }
        Dimension preferredSize = jTable.getCellRenderer(i2, i3).getTableCellRendererComponent(jTable, jTable.getValueAt(i2, i3), false, false, i2, i3).getPreferredSize();
        Rectangle cellRect = jTable.getCellRect(i2, i3, false);
        cellRect.width = preferredSize.width;
        cellRect.height = preferredSize.height;
        if ($assertionsDisabled || (point.f12370x >= cellRect.f12372x && point.f12371y >= cellRect.f12373y)) {
            return point.f12370x > cellRect.f12372x + cellRect.width || point.f12371y > cellRect.f12373y + cellRect.height;
        }
        throw new AssertionError();
    }

    public static void setLeadAnchorWithoutSelection(ListSelectionModel listSelectionModel, int i2, int i3) {
        if (i3 == -1) {
            i3 = i2;
        }
        if (i2 == -1) {
            listSelectionModel.setAnchorSelectionIndex(-1);
            listSelectionModel.setLeadSelectionIndex(-1);
        } else {
            if (listSelectionModel.isSelectedIndex(i2)) {
                listSelectionModel.addSelectionInterval(i2, i2);
            } else {
                listSelectionModel.removeSelectionInterval(i2, i2);
            }
            listSelectionModel.setAnchorSelectionIndex(i3);
        }
    }

    public static boolean shouldIgnore(MouseEvent mouseEvent, JComponent jComponent) {
        return jComponent == null || !jComponent.isEnabled() || !SwingUtilities.isLeftMouseButton(mouseEvent) || mouseEvent.isConsumed();
    }

    public static void adjustFocus(JComponent jComponent) {
        if (!jComponent.hasFocus() && jComponent.isRequestFocusEnabled()) {
            jComponent.requestFocus();
        }
    }

    public static int drawChars(JComponent jComponent, Graphics graphics, char[] cArr, int i2, int i3, int i4, int i5) {
        Graphics2D graphics2D;
        if (i3 <= 0) {
            return i4;
        }
        int iCharsWidth = i4 + getFontMetrics(jComponent, graphics).charsWidth(cArr, i2, i3);
        if (isPrinting(graphics) && (graphics2D = getGraphics2D(graphics)) != null) {
            FontRenderContext fontRenderContext = graphics2D.getFontRenderContext();
            FontRenderContext fontRenderContext2 = getFontRenderContext(jComponent);
            if (fontRenderContext2 != null && !isFontRenderContextPrintCompatible(fontRenderContext, fontRenderContext2)) {
                String str = new String(cArr, i2, i3);
                TextLayout textLayout = new TextLayout(str, graphics2D.getFont(), fontRenderContext);
                String strTrimTrailingSpaces = trimTrailingSpaces(str);
                if (!strTrimTrailingSpaces.isEmpty()) {
                    float width = (float) graphics2D.getFont().getStringBounds(strTrimTrailingSpaces, fontRenderContext2).getWidth();
                    if (stringWidth(jComponent, graphics2D.getFontMetrics(), strTrimTrailingSpaces) > width) {
                        textLayout = textLayout.getJustifiedLayout(width);
                    }
                    Color color = graphics2D.getColor();
                    if (color instanceof PrintColorUIResource) {
                        graphics2D.setColor(((PrintColorUIResource) color).getPrintColor());
                    }
                    textLayout.draw(graphics2D, i4, i5);
                    graphics2D.setColor(color);
                }
                return iCharsWidth;
            }
        }
        AATextInfo aATextInfoDrawTextAntialiased = drawTextAntialiased(jComponent);
        if (aATextInfoDrawTextAntialiased != null && (graphics instanceof Graphics2D)) {
            Graphics2D graphics2D2 = (Graphics2D) graphics;
            Object renderingHint = null;
            Object renderingHint2 = graphics2D2.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
            if (aATextInfoDrawTextAntialiased.aaHint != null && aATextInfoDrawTextAntialiased.aaHint != renderingHint2) {
                graphics2D2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, aATextInfoDrawTextAntialiased.aaHint);
            } else {
                renderingHint2 = null;
            }
            if (aATextInfoDrawTextAntialiased.lcdContrastHint != null) {
                renderingHint = graphics2D2.getRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST);
                if (aATextInfoDrawTextAntialiased.lcdContrastHint.equals(renderingHint)) {
                    renderingHint = null;
                } else {
                    graphics2D2.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, aATextInfoDrawTextAntialiased.lcdContrastHint);
                }
            }
            graphics.drawChars(cArr, i2, i3, i4, i5);
            if (renderingHint2 != null) {
                graphics2D2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, renderingHint2);
            }
            if (renderingHint != null) {
                graphics2D2.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, renderingHint);
            }
        } else {
            graphics.drawChars(cArr, i2, i3, i4, i5);
        }
        return iCharsWidth;
    }

    public static float drawString(JComponent jComponent, Graphics graphics, AttributedCharacterIterator attributedCharacterIterator, int i2, int i3) {
        FontRenderContext fontRenderContext;
        TextLayout textLayout;
        float advance;
        boolean zIsPrinting = isPrinting(graphics);
        Color color = graphics.getColor();
        if (zIsPrinting && (color instanceof PrintColorUIResource)) {
            graphics.setColor(((PrintColorUIResource) color).getPrintColor());
        }
        Graphics2D graphics2D = getGraphics2D(graphics);
        if (graphics2D == null) {
            graphics.drawString(attributedCharacterIterator, i2, i3);
            advance = i2;
        } else {
            if (zIsPrinting) {
                fontRenderContext = getFontRenderContext(jComponent);
                if (fontRenderContext.isAntiAliased() || fontRenderContext.usesFractionalMetrics()) {
                    fontRenderContext = new FontRenderContext(fontRenderContext.getTransform(), false, false);
                }
            } else {
                FontRenderContext fRCProperty = getFRCProperty(jComponent);
                fontRenderContext = fRCProperty;
                if (fRCProperty == null) {
                    fontRenderContext = graphics2D.getFontRenderContext();
                }
            }
            if (zIsPrinting) {
                FontRenderContext fontRenderContext2 = graphics2D.getFontRenderContext();
                if (!isFontRenderContextPrintCompatible(fontRenderContext, fontRenderContext2)) {
                    textLayout = new TextLayout(attributedCharacterIterator, fontRenderContext2);
                    AttributedCharacterIterator trimmedTrailingSpacesIterator = getTrimmedTrailingSpacesIterator(attributedCharacterIterator);
                    if (trimmedTrailingSpacesIterator != null) {
                        textLayout = textLayout.getJustifiedLayout(new TextLayout(trimmedTrailingSpacesIterator, fontRenderContext).getAdvance());
                    }
                } else {
                    textLayout = new TextLayout(attributedCharacterIterator, fontRenderContext);
                }
            } else {
                textLayout = new TextLayout(attributedCharacterIterator, fontRenderContext);
            }
            textLayout.draw(graphics2D, i2, i3);
            advance = textLayout.getAdvance();
        }
        if (zIsPrinting) {
            graphics.setColor(color);
        }
        return advance;
    }

    public static void drawVLine(Graphics graphics, int i2, int i3, int i4) {
        if (i4 < i3) {
            i4 = i3;
            i3 = i4;
        }
        graphics.fillRect(i2, i3, 1, (i4 - i3) + 1);
    }

    public static void drawHLine(Graphics graphics, int i2, int i3, int i4) {
        if (i3 < i2) {
            i3 = i2;
            i2 = i3;
        }
        graphics.fillRect(i2, i4, (i3 - i2) + 1, 1);
    }

    public static void drawRect(Graphics graphics, int i2, int i3, int i4, int i5) {
        if (i4 < 0 || i5 < 0) {
            return;
        }
        if (i5 == 0 || i4 == 0) {
            graphics.fillRect(i2, i3, i4 + 1, i5 + 1);
            return;
        }
        graphics.fillRect(i2, i3, i4, 1);
        graphics.fillRect(i2 + i4, i3, 1, i5);
        graphics.fillRect(i2 + 1, i3 + i5, i4, 1);
        graphics.fillRect(i2, i3 + 1, 1, i5);
    }

    private static TextLayout createTextLayout(JComponent jComponent, String str, Font font, FontRenderContext fontRenderContext) {
        Object clientProperty = jComponent == null ? null : jComponent.getClientProperty(TextAttribute.NUMERIC_SHAPING);
        if (clientProperty == null) {
            return new TextLayout(str, font, fontRenderContext);
        }
        HashMap map = new HashMap();
        map.put(TextAttribute.FONT, font);
        map.put(TextAttribute.NUMERIC_SHAPING, clientProperty);
        return new TextLayout(str, map, fontRenderContext);
    }

    private static boolean isFontRenderContextPrintCompatible(FontRenderContext fontRenderContext, FontRenderContext fontRenderContext2) {
        if (fontRenderContext == fontRenderContext2) {
            return true;
        }
        if (fontRenderContext == null || fontRenderContext2 == null || fontRenderContext.getFractionalMetricsHint() != fontRenderContext2.getFractionalMetricsHint()) {
            return false;
        }
        if (!fontRenderContext.isTransformed() && !fontRenderContext2.isTransformed()) {
            return true;
        }
        double[] dArr = new double[4];
        double[] dArr2 = new double[4];
        fontRenderContext.getTransform().getMatrix(dArr);
        fontRenderContext2.getTransform().getMatrix(dArr2);
        return dArr[0] == dArr2[0] && dArr[1] == dArr2[1] && dArr[2] == dArr2[2] && dArr[3] == dArr2[3];
    }

    public static Graphics2D getGraphics2D(Graphics graphics) {
        if (graphics instanceof Graphics2D) {
            return (Graphics2D) graphics;
        }
        if (graphics instanceof ProxyPrintGraphics) {
            return (Graphics2D) ((ProxyPrintGraphics) graphics).getGraphics();
        }
        return null;
    }

    public static FontRenderContext getFontRenderContext(Component component) {
        if (!$assertionsDisabled && component == null) {
            throw new AssertionError();
        }
        if (component == null) {
            return DEFAULT_FRC;
        }
        return component.getFontMetrics(component.getFont()).getFontRenderContext();
    }

    private static FontRenderContext getFontRenderContext(Component component, FontMetrics fontMetrics) {
        if (!$assertionsDisabled && fontMetrics == null && component == null) {
            throw new AssertionError();
        }
        return fontMetrics != null ? fontMetrics.getFontRenderContext() : getFontRenderContext(component);
    }

    public static FontMetrics getFontMetrics(JComponent jComponent, Font font) {
        FontRenderContext fRCProperty = getFRCProperty(jComponent);
        if (fRCProperty == null) {
            fRCProperty = DEFAULT_FRC;
        }
        return FontDesignMetrics.getMetrics(font, fRCProperty);
    }

    private static FontRenderContext getFRCProperty(JComponent jComponent) {
        AATextInfo aATextInfo;
        if (jComponent != null && (aATextInfo = (AATextInfo) jComponent.getClientProperty(AA_TEXT_PROPERTY_KEY)) != null) {
            return aATextInfo.frc;
        }
        return null;
    }

    static boolean isPrinting(Graphics graphics) {
        return (graphics instanceof PrinterGraphics) || (graphics instanceof PrintGraphics);
    }

    private static String trimTrailingSpaces(String str) {
        int length = str.length() - 1;
        while (length >= 0 && Character.isWhitespace(str.charAt(length))) {
            length--;
        }
        return str.substring(0, length + 1);
    }

    private static AttributedCharacterIterator getTrimmedTrailingSpacesIterator(AttributedCharacterIterator attributedCharacterIterator) {
        char c2;
        int index = attributedCharacterIterator.getIndex();
        char cLast = attributedCharacterIterator.last();
        while (true) {
            c2 = cLast;
            if (c2 == 65535 || !Character.isWhitespace(c2)) {
                break;
            }
            cLast = attributedCharacterIterator.previous();
        }
        if (c2 != 65535) {
            int index2 = attributedCharacterIterator.getIndex();
            if (index2 == attributedCharacterIterator.getEndIndex() - 1) {
                attributedCharacterIterator.setIndex(index);
                return attributedCharacterIterator;
            }
            return new AttributedString(attributedCharacterIterator, attributedCharacterIterator.getBeginIndex(), index2 + 1).getIterator();
        }
        return null;
    }

    public static boolean useSelectedTextColor(Highlighter.Highlight highlight, JTextComponent jTextComponent) {
        Highlighter.HighlightPainter painter = highlight.getPainter();
        String name = painter.getClass().getName();
        if (name.indexOf("javax.swing.text.DefaultHighlighter") != 0 && name.indexOf("com.sun.java.swing.plaf.windows.WindowsTextUI") != 0) {
            return false;
        }
        try {
            DefaultHighlighter.DefaultHighlightPainter defaultHighlightPainter = (DefaultHighlighter.DefaultHighlightPainter) painter;
            if (defaultHighlightPainter.getColor() != null) {
                if (!defaultHighlightPainter.getColor().equals(jTextComponent.getSelectionColor())) {
                    return false;
                }
                return true;
            }
            return true;
        } catch (ClassCastException e2) {
            return false;
        }
    }

    /* loaded from: rt.jar:sun/swing/SwingUtilities2$LSBCacheEntry.class */
    private static class LSBCacheEntry {
        private static final byte UNSET = Byte.MAX_VALUE;
        private static final char[] oneChar;
        private byte[] lsbCache = new byte[1];
        private Font font;
        private FontRenderContext frc;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !SwingUtilities2.class.desiredAssertionStatus();
            oneChar = new char[1];
        }

        public LSBCacheEntry(FontRenderContext fontRenderContext, Font font) {
            reset(fontRenderContext, font);
        }

        public void reset(FontRenderContext fontRenderContext, Font font) {
            this.font = font;
            this.frc = fontRenderContext;
            for (int length = this.lsbCache.length - 1; length >= 0; length--) {
                this.lsbCache[length] = Byte.MAX_VALUE;
            }
        }

        public int getLeftSideBearing(char c2) {
            Object antiAliasingHint;
            int i2 = c2 - 'W';
            if (!$assertionsDisabled && (i2 < 0 || i2 >= 1)) {
                throw new AssertionError();
            }
            byte b2 = this.lsbCache[i2];
            if (b2 == Byte.MAX_VALUE) {
                oneChar[0] = c2;
                b2 = (byte) this.font.createGlyphVector(this.frc, oneChar).getGlyphPixelBounds(0, this.frc, 0.0f, 0.0f).f12372x;
                if (b2 < 0 && ((antiAliasingHint = this.frc.getAntiAliasingHint()) == RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB || antiAliasingHint == RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR)) {
                    b2 = (byte) (b2 + 1);
                }
                this.lsbCache[i2] = b2;
            }
            return b2;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof LSBCacheEntry)) {
                return false;
            }
            LSBCacheEntry lSBCacheEntry = (LSBCacheEntry) obj;
            return this.font.equals(lSBCacheEntry.font) && this.frc.equals(lSBCacheEntry.frc);
        }

        public int hashCode() {
            int iHashCode = 17;
            if (this.font != null) {
                iHashCode = (37 * 17) + this.font.hashCode();
            }
            if (this.frc != null) {
                iHashCode = (37 * iHashCode) + this.frc.hashCode();
            }
            return iHashCode;
        }
    }

    public static boolean canAccessSystemClipboard() {
        boolean zCanCurrentEventAccessSystemClipboard = false;
        if (!GraphicsEnvironment.isHeadless()) {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager == null) {
                zCanCurrentEventAccessSystemClipboard = true;
            } else {
                try {
                    securityManager.checkPermission(SecurityConstants.AWT.ACCESS_CLIPBOARD_PERMISSION);
                    zCanCurrentEventAccessSystemClipboard = true;
                } catch (SecurityException e2) {
                }
                if (zCanCurrentEventAccessSystemClipboard && !isTrustedContext()) {
                    zCanCurrentEventAccessSystemClipboard = canCurrentEventAccessSystemClipboard(true);
                }
            }
        }
        return zCanCurrentEventAccessSystemClipboard;
    }

    public static boolean canCurrentEventAccessSystemClipboard() {
        return isTrustedContext() || canCurrentEventAccessSystemClipboard(false);
    }

    public static boolean canEventAccessSystemClipboard(AWTEvent aWTEvent) {
        return isTrustedContext() || canEventAccessSystemClipboard(aWTEvent, false);
    }

    private static synchronized boolean inputEvent_canAccessSystemClipboard(InputEvent inputEvent) throws IllegalArgumentException {
        if (inputEvent_CanAccessSystemClipboard_Field == null) {
            inputEvent_CanAccessSystemClipboard_Field = (Field) AccessController.doPrivileged(new PrivilegedAction<Field>() { // from class: sun.swing.SwingUtilities2.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Field run2() {
                    try {
                        Field declaredField = InputEvent.class.getDeclaredField("canAccessSystemClipboard");
                        declaredField.setAccessible(true);
                        return declaredField;
                    } catch (NoSuchFieldException | SecurityException e2) {
                        return null;
                    }
                }
            });
        }
        if (inputEvent_CanAccessSystemClipboard_Field == null) {
            return false;
        }
        boolean z2 = false;
        try {
            z2 = inputEvent_CanAccessSystemClipboard_Field.getBoolean(inputEvent);
        } catch (IllegalAccessException e2) {
        }
        return z2;
    }

    private static boolean isAccessClipboardGesture(InputEvent inputEvent) {
        boolean z2 = false;
        if (inputEvent instanceof KeyEvent) {
            KeyEvent keyEvent = (KeyEvent) inputEvent;
            int keyCode = keyEvent.getKeyCode();
            int modifiers = keyEvent.getModifiers();
            switch (keyCode) {
                case 67:
                case 86:
                case 88:
                    z2 = modifiers == 2;
                    break;
                case 127:
                    z2 = modifiers == 1;
                    break;
                case 155:
                    z2 = modifiers == 2 || modifiers == 1;
                    break;
                case KeyEvent.VK_COPY /* 65485 */:
                case KeyEvent.VK_PASTE /* 65487 */:
                case KeyEvent.VK_CUT /* 65489 */:
                    z2 = true;
                    break;
            }
        }
        return z2;
    }

    private static boolean canEventAccessSystemClipboard(AWTEvent aWTEvent, boolean z2) {
        if (EventQueue.isDispatchThread()) {
            if (aWTEvent instanceof InputEvent) {
                if (!z2 || isAccessClipboardGesture((InputEvent) aWTEvent)) {
                    return inputEvent_canAccessSystemClipboard((InputEvent) aWTEvent);
                }
                return false;
            }
            return false;
        }
        return true;
    }

    public static void checkAccess(int i2) {
        if (System.getSecurityManager() != null && !Modifier.isPublic(i2)) {
            throw new SecurityException("Resource is not accessible");
        }
    }

    private static boolean canCurrentEventAccessSystemClipboard(boolean z2) {
        return canEventAccessSystemClipboard(EventQueue.getCurrentEvent(), z2);
    }

    private static boolean isTrustedContext() {
        return System.getSecurityManager() == null || AppContext.getAppContext().get(UntrustedClipboardAccess) == null;
    }

    public static String displayPropertiesToCSS(Font font, Color color) {
        StringBuffer stringBuffer = new StringBuffer("body {");
        if (font != null) {
            stringBuffer.append(" font-family: ");
            stringBuffer.append(font.getFamily());
            stringBuffer.append(" ; ");
            stringBuffer.append(" font-size: ");
            stringBuffer.append(font.getSize());
            stringBuffer.append("pt ;");
            if (font.isBold()) {
                stringBuffer.append(" font-weight: 700 ; ");
            }
            if (font.isItalic()) {
                stringBuffer.append(" font-style: italic ; ");
            }
        }
        if (color != null) {
            stringBuffer.append(" color: #");
            if (color.getRed() < 16) {
                stringBuffer.append('0');
            }
            stringBuffer.append(Integer.toHexString(color.getRed()));
            if (color.getGreen() < 16) {
                stringBuffer.append('0');
            }
            stringBuffer.append(Integer.toHexString(color.getGreen()));
            if (color.getBlue() < 16) {
                stringBuffer.append('0');
            }
            stringBuffer.append(Integer.toHexString(color.getBlue()));
            stringBuffer.append(" ; ");
        }
        stringBuffer.append(" }");
        return stringBuffer.toString();
    }

    public static Object makeIcon(final Class<?> cls, final Class<?> cls2, final String str) {
        return new UIDefaults.LazyValue() { // from class: sun.swing.SwingUtilities2.2
            @Override // javax.swing.UIDefaults.LazyValue
            public Object createValue(UIDefaults uIDefaults) {
                byte[] bArr = (byte[]) AccessController.doPrivileged(new PrivilegedAction<byte[]>() { // from class: sun.swing.SwingUtilities2.2.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public byte[] run2() {
                        try {
                            InputStream resourceAsStream = null;
                            for (Class superclass = cls; superclass != null; superclass = superclass.getSuperclass()) {
                                resourceAsStream = superclass.getResourceAsStream(str);
                                if (resourceAsStream != null || superclass == cls2) {
                                    break;
                                }
                            }
                            if (resourceAsStream == null) {
                                return null;
                            }
                            BufferedInputStream bufferedInputStream = new BufferedInputStream(resourceAsStream);
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                            byte[] bArr2 = new byte[1024];
                            while (true) {
                                int i2 = bufferedInputStream.read(bArr2);
                                if (i2 > 0) {
                                    byteArrayOutputStream.write(bArr2, 0, i2);
                                } else {
                                    bufferedInputStream.close();
                                    byteArrayOutputStream.flush();
                                    return byteArrayOutputStream.toByteArray();
                                }
                            }
                        } catch (IOException e2) {
                            System.err.println(e2.toString());
                            return null;
                        }
                    }
                });
                if (bArr == null) {
                    return null;
                }
                if (bArr.length == 0) {
                    System.err.println("warning: " + str + " is zero-length");
                    return null;
                }
                return new ImageIconUIResource(bArr);
            }
        };
    }

    public static boolean isLocalDisplay() {
        boolean zIsDisplayLocal;
        GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        if (localGraphicsEnvironment instanceof SunGraphicsEnvironment) {
            zIsDisplayLocal = ((SunGraphicsEnvironment) localGraphicsEnvironment).isDisplayLocal();
        } else {
            zIsDisplayLocal = true;
        }
        return zIsDisplayLocal;
    }

    public static int getUIDefaultsInt(Object obj) {
        return getUIDefaultsInt(obj, 0);
    }

    public static int getUIDefaultsInt(Object obj, Locale locale) {
        return getUIDefaultsInt(obj, locale, 0);
    }

    public static int getUIDefaultsInt(Object obj, int i2) {
        return getUIDefaultsInt(obj, null, i2);
    }

    public static int getUIDefaultsInt(Object obj, Locale locale, int i2) {
        Object obj2 = UIManager.get(obj, locale);
        if (obj2 instanceof Integer) {
            return ((Integer) obj2).intValue();
        }
        if (obj2 instanceof String) {
            try {
                return Integer.parseInt((String) obj2);
            } catch (NumberFormatException e2) {
            }
        }
        return i2;
    }

    public static Component compositeRequestFocus(Component component) {
        Component componentAfter;
        Component defaultComponent;
        if (component instanceof Container) {
            Container container = (Container) component;
            if (container.isFocusCycleRoot() && (defaultComponent = container.getFocusTraversalPolicy().getDefaultComponent(container)) != null) {
                defaultComponent.requestFocus();
                return defaultComponent;
            }
            Container focusCycleRootAncestor = container.getFocusCycleRootAncestor();
            if (focusCycleRootAncestor != null && (componentAfter = focusCycleRootAncestor.getFocusTraversalPolicy().getComponentAfter(focusCycleRootAncestor, container)) != null && SwingUtilities.isDescendingFrom(componentAfter, container)) {
                componentAfter.requestFocus();
                return componentAfter;
            }
        }
        if (component.isFocusable()) {
            component.requestFocus();
            return component;
        }
        return null;
    }

    public static boolean tabbedPaneChangeFocusTo(Component component) {
        if (component != null) {
            if (component.isFocusTraversable()) {
                compositeRequestFocus(component);
                return true;
            }
            if ((component instanceof JComponent) && ((JComponent) component).requestDefaultFocus()) {
                return true;
            }
            return false;
        }
        return false;
    }

    public static <V> Future<V> submit(Callable<V> callable) {
        if (callable == null) {
            throw new NullPointerException();
        }
        FutureTask futureTask = new FutureTask(callable);
        execute(futureTask);
        return futureTask;
    }

    public static <V> Future<V> submit(Runnable runnable, V v2) {
        if (runnable == null) {
            throw new NullPointerException();
        }
        FutureTask futureTask = new FutureTask(runnable, v2);
        execute(futureTask);
        return futureTask;
    }

    private static void execute(Runnable runnable) {
        SwingUtilities.invokeLater(runnable);
    }

    public static void setSkipClickCount(Component component, int i2) {
        if ((component instanceof JTextComponent) && (((JTextComponent) component).getCaret() instanceof DefaultCaret)) {
            ((JTextComponent) component).putClientProperty(SKIP_CLICK_COUNT, Integer.valueOf(i2));
        }
    }

    public static int getAdjustedClickCount(JTextComponent jTextComponent, MouseEvent mouseEvent) {
        int clickCount = mouseEvent.getClickCount();
        if (clickCount == 1) {
            jTextComponent.putClientProperty(SKIP_CLICK_COUNT, null);
        } else {
            Integer num = (Integer) jTextComponent.getClientProperty(SKIP_CLICK_COUNT);
            if (num != null) {
                return clickCount - num.intValue();
            }
        }
        return clickCount;
    }

    private static Section liesIn(Rectangle rectangle, Point point, boolean z2, boolean z3, boolean z4) {
        int i2;
        int i3;
        int i4;
        boolean z5;
        if (z2) {
            i2 = rectangle.f12372x;
            i3 = point.f12370x;
            i4 = rectangle.width;
            z5 = z3;
        } else {
            i2 = rectangle.f12373y;
            i3 = point.f12371y;
            i4 = rectangle.height;
            z5 = true;
        }
        if (z4) {
            int i5 = i4 >= 30 ? 10 : i4 / 3;
            if (i3 < i2 + i5) {
                return z5 ? Section.LEADING : Section.TRAILING;
            }
            if (i3 >= (i2 + i4) - i5) {
                return z5 ? Section.TRAILING : Section.LEADING;
            }
            return Section.MIDDLE;
        }
        int i6 = i2 + (i4 / 2);
        return z5 ? i3 >= i6 ? Section.TRAILING : Section.LEADING : i3 < i6 ? Section.TRAILING : Section.LEADING;
    }

    public static Section liesInHorizontal(Rectangle rectangle, Point point, boolean z2, boolean z3) {
        return liesIn(rectangle, point, true, z2, z3);
    }

    public static Section liesInVertical(Rectangle rectangle, Point point, boolean z2) {
        return liesIn(rectangle, point, false, false, z2);
    }

    public static int convertColumnIndexToModel(TableColumnModel tableColumnModel, int i2) {
        if (i2 < 0) {
            return i2;
        }
        return tableColumnModel.getColumn(i2).getModelIndex();
    }

    public static int convertColumnIndexToView(TableColumnModel tableColumnModel, int i2) {
        if (i2 < 0) {
            return i2;
        }
        for (int i3 = 0; i3 < tableColumnModel.getColumnCount(); i3++) {
            if (tableColumnModel.getColumn(i3).getModelIndex() == i2) {
                return i3;
            }
        }
        return -1;
    }

    public static int setAltGraphMask(int i2) {
        return i2 | 8192;
    }

    public static int getSystemMnemonicKeyMask() {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        if (defaultToolkit instanceof SunToolkit) {
            return ((SunToolkit) defaultToolkit).getFocusAcceleratorKeyMask();
        }
        return 8;
    }

    public static TreePath getTreePath(TreeModelEvent treeModelEvent, TreeModel treeModel) {
        Object root;
        TreePath treePath = treeModelEvent.getTreePath();
        if (treePath == null && treeModel != null && (root = treeModel.getRoot()) != null) {
            treePath = new TreePath(root);
        }
        return treePath;
    }
}
