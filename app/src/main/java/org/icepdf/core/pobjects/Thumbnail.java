package org.icepdf.core.pobjects;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/Thumbnail.class */
public class Thumbnail extends Dictionary {
    public static final Name THUMB_KEY = new Name("Thumb");
    public static final Name WIDTH_KEY = new Name("Width");
    public static final Name HEIGHT_KEY = new Name("Height");
    private ImageStream thumbStream;
    private boolean initialized;
    private BufferedImage image;
    private Dimension dimension;

    public Thumbnail(Library library, HashMap entries) {
        super(library, entries);
        Object thumb = library.getObject(entries, THUMB_KEY);
        if (thumb != null) {
            if (thumb instanceof ImageStream) {
                this.thumbStream = (ImageStream) thumb;
            } else {
                this.thumbStream = new ImageStream(library, ((Stream) thumb).getEntries(), ((Stream) thumb).getRawBytes());
            }
            int width = library.getInt(this.thumbStream.entries, WIDTH_KEY);
            int height = library.getInt(this.thumbStream.entries, HEIGHT_KEY);
            this.dimension = new Dimension(width, height);
        }
    }

    @Override // org.icepdf.core.pobjects.Dictionary
    public void init() {
        Resources resource = new Resources(this.library, this.thumbStream.entries);
        this.image = this.thumbStream.getImage(null, resource);
        this.initialized = true;
    }

    public BufferedImage getImage() {
        if (!this.initialized) {
            init();
        }
        return this.image;
    }

    public Dimension getDimension() {
        return this.dimension;
    }
}
