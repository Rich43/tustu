package org.icepdf.ri.viewer;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JWindow;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/viewer/SplashWindow.class */
final class SplashWindow extends JWindow {
    private static final Logger logger = Logger.getLogger(SplashWindow.class.toString());
    private Image splashImage;
    private MediaTracker mediaTracker;

    public SplashWindow(Image image) {
        this.splashImage = image;
    }

    public void splash() throws HeadlessException {
        this.mediaTracker = new MediaTracker(this);
        setSize(this.splashImage.getWidth(null), this.splashImage.getHeight(null));
        this.mediaTracker.addImage(this.splashImage, 0);
        try {
            this.mediaTracker.waitForID(0);
        } catch (InterruptedException ex) {
            logger.log(Level.FINE, "Failed to track splash image load.", (Throwable) ex);
        }
        setSize(this.splashImage.getWidth(null), this.splashImage.getHeight(null));
        center();
        setVisible(true);
    }

    private void center() throws HeadlessException {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle frame = getBounds();
        setLocation((screen.width - frame.width) / 2, (screen.height - frame.height) / 2);
    }

    @Override // java.awt.Window, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        if (this.splashImage != null) {
            graphics.drawImage(this.splashImage, 0, 0, this);
        }
    }
}
