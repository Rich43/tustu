package java.awt.peer;

/* loaded from: rt.jar:java/awt/peer/TrayIconPeer.class */
public interface TrayIconPeer {
    void dispose();

    void setToolTip(String str);

    void updateImage();

    void displayMessage(String str, String str2, String str3);

    void showPopupMenu(int i2, int i3);
}
