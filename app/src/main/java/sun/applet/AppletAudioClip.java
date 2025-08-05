package sun.applet;

import com.sun.media.sound.JavaSoundAudioClip;
import java.applet.AudioClip;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/* loaded from: rt.jar:sun/applet/AppletAudioClip.class */
public class AppletAudioClip implements AudioClip {
    private URL url;
    private AudioClip audioClip;
    boolean DEBUG;

    public AppletAudioClip(URL url) {
        this.url = null;
        this.audioClip = null;
        this.DEBUG = false;
        this.url = url;
        try {
            createAppletAudioClip(url.openStream());
        } catch (IOException e2) {
            if (this.DEBUG) {
                System.err.println("IOException creating AppletAudioClip" + ((Object) e2));
            }
        }
    }

    public AppletAudioClip(URLConnection uRLConnection) {
        this.url = null;
        this.audioClip = null;
        this.DEBUG = false;
        try {
            createAppletAudioClip(uRLConnection.getInputStream());
        } catch (IOException e2) {
            if (this.DEBUG) {
                System.err.println("IOException creating AppletAudioClip" + ((Object) e2));
            }
        }
    }

    public AppletAudioClip(byte[] bArr) {
        this.url = null;
        this.audioClip = null;
        this.DEBUG = false;
        try {
            createAppletAudioClip(new ByteArrayInputStream(bArr));
        } catch (IOException e2) {
            if (this.DEBUG) {
                System.err.println("IOException creating AppletAudioClip " + ((Object) e2));
            }
        }
    }

    void createAppletAudioClip(InputStream inputStream) throws IOException {
        try {
            this.audioClip = new JavaSoundAudioClip(inputStream);
        } catch (Exception e2) {
            throw new IOException("Failed to construct the AudioClip: " + ((Object) e2));
        }
    }

    @Override // java.applet.AudioClip
    public synchronized void play() {
        if (this.audioClip != null) {
            this.audioClip.play();
        }
    }

    @Override // java.applet.AudioClip
    public synchronized void loop() {
        if (this.audioClip != null) {
            this.audioClip.loop();
        }
    }

    @Override // java.applet.AudioClip
    public synchronized void stop() {
        if (this.audioClip != null) {
            this.audioClip.stop();
        }
    }
}
