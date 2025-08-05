package com.sun.javafx.webkit;

import com.sun.glass.ui.Clipboard;
import com.sun.media.jfxmedia.MetadataParser;
import com.sun.webkit.UIClient;
import com.sun.webkit.WebPage;
import com.sun.webkit.graphics.WCImage;
import com.sun.webkit.graphics.WCRectangle;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.PromptData;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javax.imageio.ImageIO;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/UIClientImpl.class */
public final class UIClientImpl implements UIClient {
    private final Accessor accessor;
    private FileChooser chooser;
    private static final Map<String, FileExtensionInfo> fileExtensionMap = new HashMap();
    private static String[] chooseFiles = null;
    private ClipboardContent content;
    private static final DataFormat DF_DRAG_IMAGE;
    private static final DataFormat DF_DRAG_IMAGE_OFFSET;

    static {
        FileExtensionInfo.add("video", "Video Files", "*.webm", "*.mp4", "*.ogg");
        FileExtensionInfo.add("audio", "Audio Files", "*.mp3", "*.aac", "*.wav");
        FileExtensionInfo.add("text", "Text Files", "*.txt", "*.csv", "*.text", "*.ttf", "*.sdf", "*.srt", "*.htm", "*.html");
        FileExtensionInfo.add(MetadataParser.IMAGE_TAG_NAME, "Image Files", "*.png", "*.jpg", "*.gif", "*.bmp", "*.jpeg");
        DF_DRAG_IMAGE = getDataFormat(Clipboard.DRAG_IMAGE);
        DF_DRAG_IMAGE_OFFSET = getDataFormat(Clipboard.DRAG_IMAGE_OFFSET);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/webkit/UIClientImpl$FileExtensionInfo.class */
    private static class FileExtensionInfo {
        private String description;
        private List<String> extensions;

        private FileExtensionInfo() {
        }

        static void add(String type, String description, String... extensions) {
            FileExtensionInfo info = new FileExtensionInfo();
            info.description = description;
            info.extensions = Arrays.asList(extensions);
            UIClientImpl.fileExtensionMap.put(type, info);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public FileChooser.ExtensionFilter getExtensionFilter(String type) {
            String extensionType = "*." + type;
            String desc = this.description + " ";
            if (type.equals("*")) {
                return new FileChooser.ExtensionFilter(desc + ((String) this.extensions.stream().collect(Collectors.joining(", ", "(", ")"))), this.extensions);
            }
            if (this.extensions.contains(extensionType)) {
                return new FileChooser.ExtensionFilter(desc + "(" + extensionType + ")", extensionType);
            }
            return null;
        }
    }

    public UIClientImpl(Accessor accessor) {
        this.accessor = accessor;
    }

    private WebEngine getWebEngine() {
        return this.accessor.getEngine();
    }

    private AccessControlContext getAccessContext() {
        return this.accessor.getPage().getAccessControlContext();
    }

    @Override // com.sun.webkit.UIClient
    public WebPage createPage(boolean menu, boolean status, boolean toolbar, boolean resizable) {
        WebEngine w2 = getWebEngine();
        if (w2 != null && w2.getCreatePopupHandler() != null) {
            PopupFeatures pf = new PopupFeatures(menu, status, toolbar, resizable);
            WebEngine popup = (WebEngine) AccessController.doPrivileged(() -> {
                return w2.getCreatePopupHandler().call(pf);
            }, getAccessContext());
            return Accessor.getPageFor(popup);
        }
        return null;
    }

    private void dispatchWebEvent(EventHandler handler, WebEvent ev) {
        AccessController.doPrivileged(() -> {
            handler.handle(ev);
            return null;
        }, getAccessContext());
    }

    private void notifyVisibilityChanged(boolean visible) {
        WebEngine w2 = getWebEngine();
        if (w2 != null && w2.getOnVisibilityChanged() != null) {
            dispatchWebEvent(w2.getOnVisibilityChanged(), new WebEvent(w2, WebEvent.VISIBILITY_CHANGED, Boolean.valueOf(visible)));
        }
    }

    @Override // com.sun.webkit.UIClient
    public void closePage() {
        notifyVisibilityChanged(false);
    }

    @Override // com.sun.webkit.UIClient
    public void showView() {
        notifyVisibilityChanged(true);
    }

    @Override // com.sun.webkit.UIClient
    public WCRectangle getViewBounds() {
        Window win;
        WebView view = this.accessor.getView();
        if (view != null && view.getScene() != null && (win = view.getScene().getWindow()) != null) {
            return new WCRectangle((float) win.getX(), (float) win.getY(), (float) win.getWidth(), (float) win.getHeight());
        }
        return null;
    }

    @Override // com.sun.webkit.UIClient
    public void setViewBounds(WCRectangle r2) {
        WebEngine w2 = getWebEngine();
        if (w2 != null && w2.getOnResized() != null) {
            dispatchWebEvent(w2.getOnResized(), new WebEvent(w2, WebEvent.RESIZED, new Rectangle2D(r2.getX(), r2.getY(), r2.getWidth(), r2.getHeight())));
        }
    }

    @Override // com.sun.webkit.UIClient
    public void setStatusbarText(String text) {
        WebEngine w2 = getWebEngine();
        if (w2 != null && w2.getOnStatusChanged() != null) {
            dispatchWebEvent(w2.getOnStatusChanged(), new WebEvent(w2, WebEvent.STATUS_CHANGED, text));
        }
    }

    @Override // com.sun.webkit.UIClient
    public void alert(String text) {
        WebEngine w2 = getWebEngine();
        if (w2 != null && w2.getOnAlert() != null) {
            dispatchWebEvent(w2.getOnAlert(), new WebEvent(w2, WebEvent.ALERT, text));
        }
    }

    @Override // com.sun.webkit.UIClient
    public boolean confirm(String text) {
        WebEngine w2 = getWebEngine();
        if (w2 != null && w2.getConfirmHandler() != null) {
            return ((Boolean) AccessController.doPrivileged(() -> {
                return w2.getConfirmHandler().call(text);
            }, getAccessContext())).booleanValue();
        }
        return false;
    }

    @Override // com.sun.webkit.UIClient
    public String prompt(String text, String defaultValue) {
        WebEngine w2 = getWebEngine();
        if (w2 != null && w2.getPromptHandler() != null) {
            PromptData data = new PromptData(text, defaultValue);
            return (String) AccessController.doPrivileged(() -> {
                return w2.getPromptHandler().call(data);
            }, getAccessContext());
        }
        return "";
    }

    @Override // com.sun.webkit.UIClient
    public boolean canRunBeforeUnloadConfirmPanel() {
        return false;
    }

    @Override // com.sun.webkit.UIClient
    public boolean runBeforeUnloadConfirmPanel(String message) {
        return false;
    }

    static void test_setChooseFiles(String[] files) {
        chooseFiles = files;
    }

    @Override // com.sun.webkit.UIClient
    public String[] chooseFile(String initialFileName, boolean multiple, String mimeFilters) {
        File dir;
        if (chooseFiles != null) {
            return chooseFiles;
        }
        Window win = null;
        WebView view = this.accessor.getView();
        if (view != null && view.getScene() != null) {
            win = view.getScene().getWindow();
        }
        if (this.chooser == null) {
            this.chooser = new FileChooser();
        }
        this.chooser.getExtensionFilters().clear();
        if (mimeFilters != null && !mimeFilters.isEmpty()) {
            addMimeFilters(this.chooser, mimeFilters);
        }
        this.chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
        if (initialFileName != null) {
            File file = new File(initialFileName);
            while (true) {
                dir = file;
                if (dir == null || dir.isDirectory()) {
                    break;
                }
                file = dir.getParentFile();
            }
            this.chooser.setInitialDirectory(dir);
        }
        if (multiple) {
            List<File> files = this.chooser.showOpenMultipleDialog(win);
            if (files != null) {
                int n2 = files.size();
                String[] result = new String[n2];
                for (int i2 = 0; i2 < n2; i2++) {
                    result[i2] = files.get(i2).getAbsolutePath();
                }
                return result;
            }
            return null;
        }
        File f2 = this.chooser.showOpenDialog(win);
        if (f2 != null) {
            return new String[]{f2.getAbsolutePath()};
        }
        return null;
    }

    private void addSpecificFilters(FileChooser chooser, String mimeString) {
        FileChooser.ExtensionFilter extFilter;
        if (mimeString.contains("/")) {
            String[] splittedMime = mimeString.split("/");
            String mainType = splittedMime[0];
            String subType = splittedMime[1];
            FileExtensionInfo extensionValue = fileExtensionMap.get(mainType);
            if (extensionValue != null && (extFilter = extensionValue.getExtensionFilter(subType)) != null) {
                chooser.getExtensionFilters().addAll(extFilter);
            }
        }
    }

    private void addMimeFilters(FileChooser chooser, String mimeFilters) {
        if (mimeFilters.contains(",")) {
            String[] types = mimeFilters.split(",");
            for (String mimeType : types) {
                addSpecificFilters(chooser, mimeType);
            }
            return;
        }
        addSpecificFilters(chooser, mimeFilters);
    }

    @Override // com.sun.webkit.UIClient
    public void print() {
    }

    private static DataFormat getDataFormat(String mimeType) {
        DataFormat dataFormat;
        synchronized (DataFormat.class) {
            DataFormat ret = DataFormat.lookupMimeType(mimeType);
            if (ret == null) {
                ret = new DataFormat(mimeType);
            }
            dataFormat = ret;
        }
        return dataFormat;
    }

    @Override // com.sun.webkit.UIClient
    public void startDrag(WCImage image, int imageOffsetX, int imageOffsetY, int eventPosX, int eventPosY, String[] mimeTypes, Object[] values, boolean isImageSource) {
        this.content = new ClipboardContent();
        for (int i2 = 0; i2 < mimeTypes.length; i2++) {
            if (values[i2] != null) {
                try {
                    this.content.put(getDataFormat(mimeTypes[i2]), Clipboard.IE_URL_SHORTCUT_FILENAME.equals(mimeTypes[i2]) ? ByteBuffer.wrap(((String) values[i2]).getBytes("UTF-16LE")) : values[i2]);
                } catch (UnsupportedEncodingException e2) {
                }
            }
        }
        if (image != null && !image.isNull()) {
            ByteBuffer dragImageOffset = ByteBuffer.allocate(8);
            dragImageOffset.rewind();
            dragImageOffset.putInt(imageOffsetX);
            dragImageOffset.putInt(imageOffsetY);
            this.content.put(DF_DRAG_IMAGE_OFFSET, dragImageOffset);
            int w2 = image.getWidth();
            int h2 = image.getHeight();
            ByteBuffer pixels = image.getPixelBuffer();
            ByteBuffer dragImage = ByteBuffer.allocate(8 + (w2 * h2 * 4));
            dragImage.putInt(w2);
            dragImage.putInt(h2);
            dragImage.put(pixels);
            this.content.put(DF_DRAG_IMAGE, dragImage);
            if (isImageSource) {
                String fileExtension = image.getFileExtension();
                try {
                    File temp = File.createTempFile("jfx", "." + fileExtension);
                    temp.deleteOnExit();
                    ImageIO.write(image.toBufferedImage(), fileExtension, temp);
                    this.content.put(DataFormat.FILES, Arrays.asList(temp));
                } catch (IOException | SecurityException e3) {
                }
            }
        }
    }

    @Override // com.sun.webkit.UIClient
    public void confirmStartDrag() {
        WebView view = this.accessor.getView();
        if (view != null && this.content != null) {
            Dragboard db = view.startDragAndDrop(TransferMode.ANY);
            db.setContent(this.content);
        }
        this.content = null;
    }

    @Override // com.sun.webkit.UIClient
    public boolean isDragConfirmed() {
        return (this.accessor.getView() == null || this.content == null) ? false : true;
    }
}
