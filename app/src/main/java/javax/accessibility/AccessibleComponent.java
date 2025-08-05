package javax.accessibility;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusListener;

/* loaded from: rt.jar:javax/accessibility/AccessibleComponent.class */
public interface AccessibleComponent {
    Color getBackground();

    void setBackground(Color color);

    Color getForeground();

    void setForeground(Color color);

    Cursor getCursor();

    void setCursor(Cursor cursor);

    Font getFont();

    void setFont(Font font);

    FontMetrics getFontMetrics(Font font);

    boolean isEnabled();

    void setEnabled(boolean z2);

    boolean isVisible();

    void setVisible(boolean z2);

    boolean isShowing();

    boolean contains(Point point);

    Point getLocationOnScreen();

    Point getLocation();

    void setLocation(Point point);

    Rectangle getBounds();

    void setBounds(Rectangle rectangle);

    Dimension getSize();

    void setSize(Dimension dimension);

    Accessible getAccessibleAt(Point point);

    boolean isFocusTraversable();

    void requestFocus();

    void addFocusListener(FocusListener focusListener);

    void removeFocusListener(FocusListener focusListener);
}
