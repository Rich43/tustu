package javafx.scene.input;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.scene.image.Image;

/* loaded from: jfxrt.jar:javafx/scene/input/ClipboardContent.class */
public class ClipboardContent extends HashMap<DataFormat, Object> {
    public final boolean hasString() {
        return containsKey(DataFormat.PLAIN_TEXT);
    }

    public final boolean putString(String s2) {
        if (s2 == null) {
            remove(DataFormat.PLAIN_TEXT);
            return true;
        }
        put(DataFormat.PLAIN_TEXT, s2);
        return true;
    }

    public final String getString() {
        return (String) get(DataFormat.PLAIN_TEXT);
    }

    public final boolean hasUrl() {
        return containsKey(DataFormat.URL);
    }

    public final boolean putUrl(String url) {
        if (url == null) {
            remove(DataFormat.URL);
            return true;
        }
        put(DataFormat.URL, url);
        return true;
    }

    public final String getUrl() {
        return (String) get(DataFormat.URL);
    }

    public final boolean hasHtml() {
        return containsKey(DataFormat.HTML);
    }

    public final boolean putHtml(String html) {
        if (html == null) {
            remove(DataFormat.HTML);
            return true;
        }
        put(DataFormat.HTML, html);
        return true;
    }

    public final String getHtml() {
        return (String) get(DataFormat.HTML);
    }

    public final boolean hasRtf() {
        return containsKey(DataFormat.RTF);
    }

    public final boolean putRtf(String rtf) {
        if (rtf == null) {
            remove(DataFormat.RTF);
            return true;
        }
        put(DataFormat.RTF, rtf);
        return true;
    }

    public final String getRtf() {
        return (String) get(DataFormat.RTF);
    }

    public final boolean hasImage() {
        return containsKey(DataFormat.IMAGE);
    }

    public final boolean putImage(Image i2) {
        if (i2 == null) {
            remove(DataFormat.IMAGE);
            return true;
        }
        put(DataFormat.IMAGE, i2);
        return true;
    }

    public final Image getImage() {
        return (Image) get(DataFormat.IMAGE);
    }

    public final boolean hasFiles() {
        return containsKey(DataFormat.FILES);
    }

    public final boolean putFiles(List<File> files) {
        if (files == null) {
            remove(DataFormat.FILES);
            return true;
        }
        put(DataFormat.FILES, files);
        return true;
    }

    public final boolean putFilesByPath(List<String> filePaths) {
        List<File> files = new ArrayList<>(filePaths.size());
        for (String path : filePaths) {
            files.add(new File(path));
        }
        return putFiles(files);
    }

    public final List<File> getFiles() {
        return (List) get(DataFormat.FILES);
    }
}
