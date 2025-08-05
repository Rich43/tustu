package java.beans;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AudioClip;
import java.awt.Image;
import java.awt.image.ImageProducer;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

/* compiled from: Beans.java */
/* loaded from: rt.jar:java/beans/BeansAppletContext.class */
class BeansAppletContext implements AppletContext {
    Applet target;
    Hashtable<URL, Object> imageCache = new Hashtable<>();

    BeansAppletContext(Applet applet) {
        this.target = applet;
    }

    @Override // java.applet.AppletContext
    public AudioClip getAudioClip(URL url) {
        try {
            return (AudioClip) url.getContent();
        } catch (Exception e2) {
            return null;
        }
    }

    @Override // java.applet.AppletContext
    public synchronized Image getImage(URL url) {
        Object obj = this.imageCache.get(url);
        if (obj != null) {
            return (Image) obj;
        }
        try {
            Object content = url.getContent();
            if (content == null) {
                return null;
            }
            if (content instanceof Image) {
                this.imageCache.put(url, content);
                return (Image) content;
            }
            Image imageCreateImage = this.target.createImage((ImageProducer) content);
            this.imageCache.put(url, imageCreateImage);
            return imageCreateImage;
        } catch (Exception e2) {
            return null;
        }
    }

    @Override // java.applet.AppletContext
    public Applet getApplet(String str) {
        return null;
    }

    @Override // java.applet.AppletContext
    public Enumeration<Applet> getApplets() {
        Vector vector = new Vector();
        vector.addElement(this.target);
        return vector.elements();
    }

    @Override // java.applet.AppletContext
    public void showDocument(URL url) {
    }

    @Override // java.applet.AppletContext
    public void showDocument(URL url, String str) {
    }

    @Override // java.applet.AppletContext
    public void showStatus(String str) {
    }

    @Override // java.applet.AppletContext
    public void setStream(String str, InputStream inputStream) throws IOException {
    }

    @Override // java.applet.AppletContext
    public InputStream getStream(String str) {
        return null;
    }

    @Override // java.applet.AppletContext
    public Iterator<String> getStreamKeys() {
        return null;
    }
}
