package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.MediaError;
import com.sun.media.jfxmedia.MediaException;
import com.sun.media.jfxmedia.events.MediaErrorListener;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.logging.Logger;
import java.io.File;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.List;
import java.util.ListIterator;

/* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/MediaUtils.class */
public class MediaUtils {
    public static final int MAX_FILE_SIGNATURE_LENGTH = 22;
    static final String NATIVE_MEDIA_ERROR_FORMAT = "Internal media error: %d";
    static final String NATIVE_MEDIA_WARNING_FORMAT = "Internal media warning: %d";
    public static final String CONTENT_TYPE_AIFF = "audio/x-aiff";
    public static final String CONTENT_TYPE_MP3 = "audio/mp3";
    public static final String CONTENT_TYPE_MPA = "audio/mpeg";
    public static final String CONTENT_TYPE_WAV = "audio/x-wav";
    public static final String CONTENT_TYPE_JFX = "video/x-javafx";
    public static final String CONTENT_TYPE_FLV = "video/x-flv";
    public static final String CONTENT_TYPE_MP4 = "video/mp4";
    public static final String CONTENT_TYPE_M4A = "audio/x-m4a";
    public static final String CONTENT_TYPE_M4V = "video/x-m4v";
    public static final String CONTENT_TYPE_M3U8 = "application/vnd.apple.mpegurl";
    public static final String CONTENT_TYPE_M3U = "audio/mpegurl";
    private static final String FILE_TYPE_AIF = "aif";
    private static final String FILE_TYPE_AIFF = "aiff";
    private static final String FILE_TYPE_FLV = "flv";
    private static final String FILE_TYPE_FXM = "fxm";
    private static final String FILE_TYPE_MPA = "mp3";
    private static final String FILE_TYPE_WAV = "wav";
    private static final String FILE_TYPE_MP4 = "mp4";
    private static final String FILE_TYPE_M4A = "m4a";
    private static final String FILE_TYPE_M4V = "m4v";
    private static final String FILE_TYPE_M3U8 = "m3u8";
    private static final String FILE_TYPE_M3U = "m3u";

    public static String fileSignatureToContentType(byte[] buf, int size) throws MediaException {
        String contentType = Locator.DEFAULT_CONTENT_TYPE;
        if (size < 22) {
            throw new MediaException("Empty signature!");
        }
        if (buf.length < 22) {
            return contentType;
        }
        if ((buf[0] & 255) == 70 && (buf[1] & 255) == 76 && (buf[2] & 255) == 86) {
            contentType = CONTENT_TYPE_JFX;
        } else if ((((buf[0] & 255) << 24) | ((buf[1] & 255) << 16) | ((buf[2] & 255) << 8) | (buf[3] & 255)) == 1380533830 && (((buf[8] & 255) << 24) | ((buf[9] & 255) << 16) | ((buf[10] & 255) << 8) | (buf[11] & 255)) == 1463899717 && (((buf[12] & 255) << 24) | ((buf[13] & 255) << 16) | ((buf[14] & 255) << 8) | (buf[15] & 255)) == 1718449184) {
            if (((buf[20] & 255) == 1 && (buf[21] & 255) == 0) || ((buf[20] & 255) == 3 && (buf[21] & 255) == 0)) {
                contentType = CONTENT_TYPE_WAV;
            } else {
                throw new MediaException("Compressed WAVE is not supported!");
            }
        } else if ((((buf[0] & 255) << 24) | ((buf[1] & 255) << 16) | ((buf[2] & 255) << 8) | (buf[3] & 255)) == 1380533830 && (((buf[8] & 255) << 24) | ((buf[9] & 255) << 16) | ((buf[10] & 255) << 8) | (buf[11] & 255)) == 1463899717) {
            contentType = CONTENT_TYPE_WAV;
        } else if ((((buf[0] & 255) << 24) | ((buf[1] & 255) << 16) | ((buf[2] & 255) << 8) | (buf[3] & 255)) == 1179603533 && (((buf[8] & 255) << 24) | ((buf[9] & 255) << 16) | ((buf[10] & 255) << 8) | (buf[11] & 255)) == 1095321158 && (((buf[12] & 255) << 24) | ((buf[13] & 255) << 16) | ((buf[14] & 255) << 8) | (buf[15] & 255)) == 1129270605) {
            contentType = CONTENT_TYPE_AIFF;
        } else if ((buf[0] & 255) == 73 && (buf[1] & 255) == 68 && (buf[2] & 255) == 51) {
            contentType = CONTENT_TYPE_MPA;
        } else if ((buf[0] & 255) == 255 && (buf[1] & 224) == 224 && (buf[1] & 24) != 8 && (buf[1] & 6) != 0) {
            contentType = CONTENT_TYPE_MPA;
        } else if ((((buf[4] & 255) << 24) | ((buf[5] & 255) << 16) | ((buf[6] & 255) << 8) | (buf[7] & 255)) == 1718909296) {
            if ((buf[8] & 255) == 77 && (buf[9] & 255) == 52 && (buf[10] & 255) == 65 && (buf[11] & 255) == 32) {
                contentType = CONTENT_TYPE_M4A;
            } else if ((buf[8] & 255) == 77 && (buf[9] & 255) == 52 && (buf[10] & 255) == 86 && (buf[11] & 255) == 32) {
                contentType = CONTENT_TYPE_M4V;
            } else if ((buf[8] & 255) == 109 && (buf[9] & 255) == 112 && (buf[10] & 255) == 52 && (buf[11] & 255) == 50) {
                contentType = CONTENT_TYPE_MP4;
            } else if ((buf[8] & 255) == 105 && (buf[9] & 255) == 115 && (buf[10] & 255) == 111 && (buf[11] & 255) == 109) {
                contentType = CONTENT_TYPE_MP4;
            } else if ((buf[8] & 255) == 77 && (buf[9] & 255) == 80 && (buf[10] & 255) == 52 && (buf[11] & 255) == 32) {
                contentType = CONTENT_TYPE_MP4;
            }
        } else if ((buf[0] & 255) == 35 && (buf[1] & 255) == 69 && (buf[2] & 255) == 88 && (buf[3] & 255) == 84 && (buf[4] & 255) == 77 && (buf[5] & 255) == 51 && (buf[6] & 255) == 85) {
            contentType = CONTENT_TYPE_M3U8;
        } else {
            throw new MediaException("Unrecognized file signature!");
        }
        return contentType;
    }

    public static String filenameToContentType(URI uri) {
        int dotIndex;
        String fileName = getFilenameFromURI(uri);
        if (fileName != null && (dotIndex = fileName.lastIndexOf(".")) != -1) {
            String extension = fileName.toLowerCase().substring(dotIndex + 1);
            switch (extension) {
                case "aif":
                case "aiff":
                    return CONTENT_TYPE_AIFF;
                case "flv":
                case "fxm":
                    return CONTENT_TYPE_JFX;
                case "mp3":
                    return CONTENT_TYPE_MPA;
                case "wav":
                    return CONTENT_TYPE_WAV;
                case "mp4":
                    return CONTENT_TYPE_MP4;
                case "m4a":
                    return CONTENT_TYPE_M4A;
                case "m4v":
                    return CONTENT_TYPE_M4V;
                case "m3u8":
                    return CONTENT_TYPE_M3U8;
                case "m3u":
                    return CONTENT_TYPE_M3U;
                default:
                    return Locator.DEFAULT_CONTENT_TYPE;
            }
        }
        return Locator.DEFAULT_CONTENT_TYPE;
    }

    public static String getFilenameFromURI(URI uri) {
        if (uri.getScheme() == null) {
            return null;
        }
        String scheme = uri.getScheme().toLowerCase();
        if ("jar".equals(scheme)) {
            String[] jarURI = uri.toASCIIString().split("!/");
            if (jarURI.length != 2) {
                return null;
            }
            File entry = new File(jarURI[1]);
            String fileName = entry.getName();
            if (!fileName.isEmpty()) {
                return fileName;
            }
            return null;
        }
        return uri.getPath();
    }

    public static void warning(Object source, String message) {
        if ((source != null) & (message != null)) {
            Logger.logMsg(3, source.getClass().getName() + ": " + message);
        }
    }

    public static void error(Object source, int errCode, String message, Throwable cause) {
        StackTraceElement[] stackTrace;
        if (cause != null && (stackTrace = cause.getStackTrace()) != null && stackTrace.length > 0) {
            StackTraceElement trace = stackTrace[0];
            Logger.logMsg(4, trace.getClassName(), trace.getMethodName(), "( " + trace.getLineNumber() + ") " + message);
        }
        List<WeakReference<MediaErrorListener>> listeners = NativeMediaManager.getDefaultInstance().getMediaErrorListeners();
        if (!listeners.isEmpty()) {
            ListIterator<WeakReference<MediaErrorListener>> it = listeners.listIterator();
            while (it.hasNext()) {
                MediaErrorListener l2 = it.next().get();
                if (l2 != null) {
                    l2.onError(source, errCode, message);
                } else {
                    it.remove();
                }
            }
            return;
        }
        MediaException e2 = cause instanceof MediaException ? (MediaException) cause : new MediaException(message, cause);
        throw e2;
    }

    public static void nativeWarning(Object source, int warningCode, String warningMessage) {
        String message = String.format(NATIVE_MEDIA_WARNING_FORMAT, Integer.valueOf(warningCode));
        if (warningMessage != null) {
            message = message + ": " + warningMessage;
        }
        Logger.logMsg(3, message);
    }

    public static void nativeError(Object source, MediaError error) {
        Logger.logMsg(4, error.description());
        List<WeakReference<MediaErrorListener>> listeners = NativeMediaManager.getDefaultInstance().getMediaErrorListeners();
        if (!listeners.isEmpty()) {
            ListIterator<WeakReference<MediaErrorListener>> it = listeners.listIterator();
            while (it.hasNext()) {
                MediaErrorListener l2 = it.next().get();
                if (l2 != null) {
                    l2.onError(source, error.code(), error.description());
                } else {
                    it.remove();
                }
            }
            return;
        }
        throw new MediaException(error.description(), null, error);
    }
}
