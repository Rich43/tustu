package com.sun.webkit.graphics;

import com.sun.prism.paint.Color;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.net.telnet.TelnetCommand;

/* loaded from: jfxrt.jar:com/sun/webkit/graphics/RenderMediaControls.class */
final class RenderMediaControls {
    private static final int PLAY_BUTTON = 1;
    private static final int PAUSE_BUTTON = 2;
    private static final int DISABLED_PLAY_BUTTON = 3;
    private static final int MUTE_BUTTON = 4;
    private static final int UNMUTE_BUTTON = 5;
    private static final int DISABLED_MUTE_BUTTON = 6;
    private static final int TIME_SLIDER_TRACK = 9;
    private static final int TIME_SLIDER_THUMB = 10;
    private static final int VOLUME_CONTAINER = 11;
    private static final int VOLUME_TRACK = 12;
    private static final int VOLUME_THUMB = 13;
    private static final int TimeSliderTrackThickness = 3;
    private static final int VolumeTrackThickness = 1;
    private static final int SLIDER_TYPE_TIME = 0;
    private static final int SLIDER_TYPE_VOLUME = 1;
    private static final boolean log = false;
    private static final Color TimeSliderTrackUnbufferedColor = rgba(236, 135, 125);
    private static final Color TimeSliderTrackBufferedColor = rgba(TelnetCommand.GA, 26, 2);
    private static final Color VolumeTrackColor = rgba(208, 208, 208, 128);
    private static final Map<String, WCImage> controlImages = new HashMap();

    private static String getControlName(int control) {
        switch (control) {
            case 1:
                return "PLAY_BUTTON";
            case 2:
                return "PAUSE_BUTTON";
            case 3:
                return "DISABLED_PLAY_BUTTON";
            case 4:
                return "MUTE_BUTTON";
            case 5:
                return "UNMUTE_BUTTON";
            case 6:
                return "DISABLED_MUTE_BUTTON";
            case 7:
            case 8:
            default:
                return "{UNKNOWN CONTROL " + control + "}";
            case 9:
                return "TIME_SLIDER_TRACK";
            case 10:
                return "TIME_SLIDER_THUMB";
            case 11:
                return "VOLUME_CONTAINER";
            case 12:
                return "VOLUME_TRACK";
            case 13:
                return "VOLUME_THUMB";
        }
    }

    private RenderMediaControls() {
    }

    static void paintControl(WCGraphicsContext gc, int type, int x2, int y2, int w2, int h2) {
        switch (type) {
            case 1:
                paintControlImage("mediaPlay", gc, x2, y2, w2, h2);
                break;
            case 2:
                paintControlImage("mediaPause", gc, x2, y2, w2, h2);
                break;
            case 3:
                paintControlImage("mediaPlayDisabled", gc, x2, y2, w2, h2);
                break;
            case 4:
                paintControlImage("mediaMute", gc, x2, y2, w2, h2);
                break;
            case 5:
                paintControlImage("mediaUnmute", gc, x2, y2, w2, h2);
                break;
            case 6:
                paintControlImage("mediaMuteDisabled", gc, x2, y2, w2, h2);
                break;
            case 10:
                paintControlImage("mediaTimeThumb", gc, x2, y2, w2, h2);
                break;
            case 13:
                paintControlImage("mediaVolumeThumb", gc, x2, y2, w2, h2);
                break;
        }
    }

    static void paintTimeSliderTrack(WCGraphicsContext gc, float duration, float curTime, float[] bufferedPairs, int x2, int y2, int w2, int h2) {
        int y3 = y2 + ((h2 - 3) / 2);
        int thumbWidth = (fwkGetSliderThumbSize(0) >> 16) & 65535;
        int w3 = w2 - thumbWidth;
        int x3 = x2 + (thumbWidth / 2);
        if (duration >= 0.0f) {
            float timeToPixel = (1.0f / duration) * w3;
            float start = 0.0f;
            for (int i2 = 0; i2 < bufferedPairs.length; i2 += 2) {
                gc.fillRect(x3 + (timeToPixel * start), y3, timeToPixel * (bufferedPairs[i2] - start), 3, TimeSliderTrackUnbufferedColor);
                gc.fillRect(x3 + (timeToPixel * bufferedPairs[i2]), y3, timeToPixel * (bufferedPairs[i2 + 1] - bufferedPairs[i2]), 3, TimeSliderTrackBufferedColor);
                start = bufferedPairs[i2 + 1];
            }
            if (start < duration) {
                gc.fillRect(x3 + (timeToPixel * start), y3, timeToPixel * (duration - start), 3, TimeSliderTrackUnbufferedColor);
            }
        }
    }

    static void paintVolumeTrack(WCGraphicsContext gc, float curVolume, boolean muted, int x2, int y2, int w2, int h2) {
        int thumbWidth = fwkGetSliderThumbSize(0) & 65535;
        gc.fillRect(x2 + (((w2 + 1) - 1) / 2), y2 + (thumbWidth / 2), 1, h2 - thumbWidth, VolumeTrackColor);
    }

    private static int fwkGetSliderThumbSize(int type) {
        WCImage image = null;
        switch (type) {
            case 0:
                image = getControlImage("mediaTimeThumb");
                break;
            case 1:
                image = getControlImage("mediaVolumeThumb");
                break;
        }
        if (image != null) {
            return (image.getWidth() << 16) | image.getHeight();
        }
        return 0;
    }

    private static WCImage getControlImage(String resName) {
        WCImage image = controlImages.get(resName);
        if (image == null) {
            WCImageDecoder decoder = WCGraphicsManager.getGraphicsManager().getImageDecoder();
            decoder.loadFromResource(resName);
            WCImageFrame frame = decoder.getFrame(0);
            if (frame != null) {
                image = frame.getFrame();
                controlImages.put(resName, image);
            }
        }
        return image;
    }

    private static void paintControlImage(String resName, WCGraphicsContext gc, int x2, int y2, int w2, int h2) {
        WCImage image = getControlImage(resName);
        if (image != null) {
            int x3 = x2 + ((w2 - image.getWidth()) / 2);
            int w3 = image.getWidth();
            int y3 = y2 + ((h2 - image.getHeight()) / 2);
            int h3 = image.getHeight();
            gc.drawImage(image, x3, y3, w3, h3, 0.0f, 0.0f, image.getWidth(), image.getHeight());
        }
    }

    private static Color rgba(int r2, int g2, int b2, int a2) {
        return new Color((r2 & 255) / 255.0f, (g2 & 255) / 255.0f, (b2 & 255) / 255.0f, (a2 & 255) / 255.0f);
    }

    private static Color rgba(int r2, int g2, int b2) {
        return rgba(r2, g2, b2, 255);
    }

    private static void log(String s2) {
        System.out.println(s2);
        System.out.flush();
    }
}
