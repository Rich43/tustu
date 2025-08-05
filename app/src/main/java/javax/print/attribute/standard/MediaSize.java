package javax.print.attribute.standard;

import com.sun.media.sound.DLSModulator;
import com.sun.org.apache.xpath.internal.compiler.OpCodes;
import java.util.HashMap;
import java.util.Vector;
import javax.print.attribute.Attribute;
import javax.print.attribute.Size2DSyntax;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.nntp.NNTPReply;
import sun.nio.fs.WindowsConstants;

/* loaded from: rt.jar:javax/print/attribute/standard/MediaSize.class */
public class MediaSize extends Size2DSyntax implements Attribute {
    private static final long serialVersionUID = -1967958664615414771L;
    private MediaSizeName mediaName;
    private static HashMap mediaMap = new HashMap(100, 10.0f);
    private static Vector sizeVector = new Vector(100, 10);

    static {
        MediaSize mediaSize = ISO.A4;
        MediaSize mediaSize2 = JIS.B5;
        MediaSize mediaSize3 = NA.LETTER;
        MediaSize mediaSize4 = Engineering.f12789C;
        MediaSize mediaSize5 = Other.EXECUTIVE;
    }

    public MediaSize(float f2, float f3, int i2) {
        super(f2, f3, i2);
        if (f2 > f3) {
            throw new IllegalArgumentException("X dimension > Y dimension");
        }
        sizeVector.add(this);
    }

    public MediaSize(int i2, int i3, int i4) {
        super(i2, i3, i4);
        if (i2 > i3) {
            throw new IllegalArgumentException("X dimension > Y dimension");
        }
        sizeVector.add(this);
    }

    public MediaSize(float f2, float f3, int i2, MediaSizeName mediaSizeName) {
        super(f2, f3, i2);
        if (f2 > f3) {
            throw new IllegalArgumentException("X dimension > Y dimension");
        }
        if (mediaSizeName != null && mediaMap.get(mediaSizeName) == null) {
            this.mediaName = mediaSizeName;
            mediaMap.put(this.mediaName, this);
        }
        sizeVector.add(this);
    }

    public MediaSize(int i2, int i3, int i4, MediaSizeName mediaSizeName) {
        super(i2, i3, i4);
        if (i2 > i3) {
            throw new IllegalArgumentException("X dimension > Y dimension");
        }
        if (mediaSizeName != null && mediaMap.get(mediaSizeName) == null) {
            this.mediaName = mediaSizeName;
            mediaMap.put(this.mediaName, this);
        }
        sizeVector.add(this);
    }

    public MediaSizeName getMediaSizeName() {
        return this.mediaName;
    }

    public static MediaSize getMediaSizeForName(MediaSizeName mediaSizeName) {
        return (MediaSize) mediaMap.get(mediaSizeName);
    }

    public static MediaSizeName findMedia(float f2, float f3, int i2) {
        MediaSize mediaSize = ISO.A4;
        if (f2 <= 0.0f || f3 <= 0.0f || i2 < 1) {
            throw new IllegalArgumentException("args must be +ve values");
        }
        double d2 = (f2 * f2) + (f3 * f3);
        int i3 = 0;
        while (true) {
            if (i3 >= sizeVector.size()) {
                break;
            }
            MediaSize mediaSize2 = (MediaSize) sizeVector.elementAt(i3);
            float[] size = mediaSize2.getSize(i2);
            if (f2 == size[0] && f3 == size[1]) {
                mediaSize = mediaSize2;
                break;
            }
            float f4 = f2 - size[0];
            float f5 = f3 - size[1];
            double d3 = (f4 * f4) + (f5 * f5);
            if (d3 < d2) {
                d2 = d3;
                mediaSize = mediaSize2;
            }
            i3++;
        }
        return mediaSize.getMediaSizeName();
    }

    @Override // javax.print.attribute.Size2DSyntax
    public boolean equals(Object obj) {
        return super.equals(obj) && (obj instanceof MediaSize);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return MediaSize.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "media-size";
    }

    /* loaded from: rt.jar:javax/print/attribute/standard/MediaSize$ISO.class */
    public static final class ISO {
        public static final MediaSize A0 = new MediaSize(841, 1189, 1000, MediaSizeName.ISO_A0);
        public static final MediaSize A1 = new MediaSize(594, 841, 1000, MediaSizeName.ISO_A1);
        public static final MediaSize A2 = new MediaSize(NNTPReply.NO_CURRENT_ARTICLE_SELECTED, 594, 1000, MediaSizeName.ISO_A2);
        public static final MediaSize A3 = new MediaSize(297, NNTPReply.NO_CURRENT_ARTICLE_SELECTED, 1000, MediaSizeName.ISO_A3);
        public static final MediaSize A4 = new MediaSize(210, 297, 1000, MediaSizeName.ISO_A4);
        public static final MediaSize A5 = new MediaSize(148, 210, 1000, MediaSizeName.ISO_A5);
        public static final MediaSize A6 = new MediaSize(105, 148, 1000, MediaSizeName.ISO_A6);
        public static final MediaSize A7 = new MediaSize(74, 105, 1000, MediaSizeName.ISO_A7);
        public static final MediaSize A8 = new MediaSize(52, 74, 1000, MediaSizeName.ISO_A8);
        public static final MediaSize A9 = new MediaSize(37, 52, 1000, MediaSizeName.ISO_A9);
        public static final MediaSize A10 = new MediaSize(26, 37, 1000, MediaSizeName.ISO_A10);
        public static final MediaSize B0 = new MediaSize(1000, 1414, 1000, MediaSizeName.ISO_B0);
        public static final MediaSize B1 = new MediaSize(707, 1000, 1000, MediaSizeName.ISO_B1);
        public static final MediaSize B2 = new MediaSize(500, 707, 1000, MediaSizeName.ISO_B2);
        public static final MediaSize B3 = new MediaSize(353, 500, 1000, MediaSizeName.ISO_B3);
        public static final MediaSize B4 = new MediaSize(250, 353, 1000, MediaSizeName.ISO_B4);
        public static final MediaSize B5 = new MediaSize(176, 250, 1000, MediaSizeName.ISO_B5);
        public static final MediaSize B6 = new MediaSize(125, 176, 1000, MediaSizeName.ISO_B6);
        public static final MediaSize B7 = new MediaSize(88, 125, 1000, MediaSizeName.ISO_B7);
        public static final MediaSize B8 = new MediaSize(62, 88, 1000, MediaSizeName.ISO_B8);
        public static final MediaSize B9 = new MediaSize(44, 62, 1000, MediaSizeName.ISO_B9);
        public static final MediaSize B10 = new MediaSize(31, 44, 1000, MediaSizeName.ISO_B10);
        public static final MediaSize C3 = new MediaSize(324, 458, 1000, MediaSizeName.ISO_C3);
        public static final MediaSize C4 = new MediaSize(229, 324, 1000, MediaSizeName.ISO_C4);
        public static final MediaSize C5 = new MediaSize(162, 229, 1000, MediaSizeName.ISO_C5);
        public static final MediaSize C6 = new MediaSize(114, 162, 1000, MediaSizeName.ISO_C6);
        public static final MediaSize DESIGNATED_LONG = new MediaSize(110, 220, 1000, MediaSizeName.ISO_DESIGNATED_LONG);

        private ISO() {
        }
    }

    /* loaded from: rt.jar:javax/print/attribute/standard/MediaSize$JIS.class */
    public static final class JIS {
        public static final MediaSize B0 = new MediaSize(OpCodes.NODETYPE_COMMENT, 1456, 1000, MediaSizeName.JIS_B0);
        public static final MediaSize B1 = new MediaSize(728, OpCodes.NODETYPE_COMMENT, 1000, MediaSizeName.JIS_B1);
        public static final MediaSize B2 = new MediaSize(515, 728, 1000, MediaSizeName.JIS_B2);
        public static final MediaSize B3 = new MediaSize(364, 515, 1000, MediaSizeName.JIS_B3);
        public static final MediaSize B4 = new MediaSize(257, 364, 1000, MediaSizeName.JIS_B4);
        public static final MediaSize B5 = new MediaSize(182, 257, 1000, MediaSizeName.JIS_B5);
        public static final MediaSize B6 = new MediaSize(128, 182, 1000, MediaSizeName.JIS_B6);
        public static final MediaSize B7 = new MediaSize(91, 128, 1000, MediaSizeName.JIS_B7);
        public static final MediaSize B8 = new MediaSize(64, 91, 1000, MediaSizeName.JIS_B8);
        public static final MediaSize B9 = new MediaSize(45, 64, 1000, MediaSizeName.JIS_B9);
        public static final MediaSize B10 = new MediaSize(32, 45, 1000, MediaSizeName.JIS_B10);
        public static final MediaSize CHOU_1 = new MediaSize(142, FTPReply.NEED_ACCOUNT, 1000);
        public static final MediaSize CHOU_2 = new MediaSize(119, DLSModulator.CONN_DST_VIB_STARTDELAY, 1000);
        public static final MediaSize CHOU_3 = new MediaSize(120, 235, 1000);
        public static final MediaSize CHOU_4 = new MediaSize(90, 205, 1000);
        public static final MediaSize CHOU_30 = new MediaSize(92, 235, 1000);
        public static final MediaSize CHOU_40 = new MediaSize(90, 225, 1000);
        public static final MediaSize KAKU_0 = new MediaSize(287, 382, 1000);
        public static final MediaSize KAKU_1 = new MediaSize(270, 382, 1000);
        public static final MediaSize KAKU_2 = new MediaSize(240, FTPReply.NEED_ACCOUNT, 1000);
        public static final MediaSize KAKU_3 = new MediaSize(216, DLSModulator.CONN_DST_VIB_STARTDELAY, 1000);
        public static final MediaSize KAKU_4 = new MediaSize(197, WindowsConstants.ERROR_DIRECTORY, 1000);
        public static final MediaSize KAKU_5 = new MediaSize(190, 240, 1000);
        public static final MediaSize KAKU_6 = new MediaSize(162, 229, 1000);
        public static final MediaSize KAKU_7 = new MediaSize(142, 205, 1000);
        public static final MediaSize KAKU_8 = new MediaSize(119, 197, 1000);
        public static final MediaSize KAKU_20 = new MediaSize(229, 324, 1000);
        public static final MediaSize KAKU_A4 = new MediaSize(228, 312, 1000);
        public static final MediaSize YOU_1 = new MediaSize(120, 176, 1000);
        public static final MediaSize YOU_2 = new MediaSize(114, 162, 1000);
        public static final MediaSize YOU_3 = new MediaSize(98, 148, 1000);
        public static final MediaSize YOU_4 = new MediaSize(105, 235, 1000);
        public static final MediaSize YOU_5 = new MediaSize(95, 217, 1000);
        public static final MediaSize YOU_6 = new MediaSize(98, 190, 1000);
        public static final MediaSize YOU_7 = new MediaSize(92, 165, 1000);

        private JIS() {
        }
    }

    /* loaded from: rt.jar:javax/print/attribute/standard/MediaSize$NA.class */
    public static final class NA {
        public static final MediaSize LETTER = new MediaSize(8.5f, 11.0f, 25400, MediaSizeName.NA_LETTER);
        public static final MediaSize LEGAL = new MediaSize(8.5f, 14.0f, 25400, MediaSizeName.NA_LEGAL);
        public static final MediaSize NA_5X7 = new MediaSize(5, 7, 25400, MediaSizeName.NA_5X7);
        public static final MediaSize NA_8X10 = new MediaSize(8, 10, 25400, MediaSizeName.NA_8X10);
        public static final MediaSize NA_NUMBER_9_ENVELOPE = new MediaSize(3.875f, 8.875f, 25400, MediaSizeName.NA_NUMBER_9_ENVELOPE);
        public static final MediaSize NA_NUMBER_10_ENVELOPE = new MediaSize(4.125f, 9.5f, 25400, MediaSizeName.NA_NUMBER_10_ENVELOPE);
        public static final MediaSize NA_NUMBER_11_ENVELOPE = new MediaSize(4.5f, 10.375f, 25400, MediaSizeName.NA_NUMBER_11_ENVELOPE);
        public static final MediaSize NA_NUMBER_12_ENVELOPE = new MediaSize(4.75f, 11.0f, 25400, MediaSizeName.NA_NUMBER_12_ENVELOPE);
        public static final MediaSize NA_NUMBER_14_ENVELOPE = new MediaSize(5.0f, 11.5f, 25400, MediaSizeName.NA_NUMBER_14_ENVELOPE);
        public static final MediaSize NA_6X9_ENVELOPE = new MediaSize(6.0f, 9.0f, 25400, MediaSizeName.NA_6X9_ENVELOPE);
        public static final MediaSize NA_7X9_ENVELOPE = new MediaSize(7.0f, 9.0f, 25400, MediaSizeName.NA_7X9_ENVELOPE);
        public static final MediaSize NA_9x11_ENVELOPE = new MediaSize(9.0f, 11.0f, 25400, MediaSizeName.NA_9X11_ENVELOPE);
        public static final MediaSize NA_9x12_ENVELOPE = new MediaSize(9.0f, 12.0f, 25400, MediaSizeName.NA_9X12_ENVELOPE);
        public static final MediaSize NA_10x13_ENVELOPE = new MediaSize(10.0f, 13.0f, 25400, MediaSizeName.NA_10X13_ENVELOPE);
        public static final MediaSize NA_10x14_ENVELOPE = new MediaSize(10.0f, 14.0f, 25400, MediaSizeName.NA_10X14_ENVELOPE);
        public static final MediaSize NA_10X15_ENVELOPE = new MediaSize(10.0f, 15.0f, 25400, MediaSizeName.NA_10X15_ENVELOPE);

        private NA() {
        }
    }

    /* loaded from: rt.jar:javax/print/attribute/standard/MediaSize$Engineering.class */
    public static final class Engineering {

        /* renamed from: A, reason: collision with root package name */
        public static final MediaSize f12787A = new MediaSize(8.5f, 11.0f, 25400, MediaSizeName.f12792A);

        /* renamed from: B, reason: collision with root package name */
        public static final MediaSize f12788B = new MediaSize(11.0f, 17.0f, 25400, MediaSizeName.f12793B);

        /* renamed from: C, reason: collision with root package name */
        public static final MediaSize f12789C = new MediaSize(17.0f, 22.0f, 25400, MediaSizeName.f12794C);

        /* renamed from: D, reason: collision with root package name */
        public static final MediaSize f12790D = new MediaSize(22.0f, 34.0f, 25400, MediaSizeName.f12795D);

        /* renamed from: E, reason: collision with root package name */
        public static final MediaSize f12791E = new MediaSize(34.0f, 44.0f, 25400, MediaSizeName.f12796E);

        private Engineering() {
        }
    }

    /* loaded from: rt.jar:javax/print/attribute/standard/MediaSize$Other.class */
    public static final class Other {
        public static final MediaSize EXECUTIVE = new MediaSize(7.25f, 10.5f, 25400, MediaSizeName.EXECUTIVE);
        public static final MediaSize LEDGER = new MediaSize(11.0f, 17.0f, 25400, MediaSizeName.LEDGER);
        public static final MediaSize TABLOID = new MediaSize(11.0f, 17.0f, 25400, MediaSizeName.TABLOID);
        public static final MediaSize INVOICE = new MediaSize(5.5f, 8.5f, 25400, MediaSizeName.INVOICE);
        public static final MediaSize FOLIO = new MediaSize(8.5f, 13.0f, 25400, MediaSizeName.FOLIO);
        public static final MediaSize QUARTO = new MediaSize(8.5f, 10.83f, 25400, MediaSizeName.QUARTO);
        public static final MediaSize ITALY_ENVELOPE = new MediaSize(110, 230, 1000, MediaSizeName.ITALY_ENVELOPE);
        public static final MediaSize MONARCH_ENVELOPE = new MediaSize(3.87f, 7.5f, 25400, MediaSizeName.MONARCH_ENVELOPE);
        public static final MediaSize PERSONAL_ENVELOPE = new MediaSize(3.625f, 6.5f, 25400, MediaSizeName.PERSONAL_ENVELOPE);
        public static final MediaSize JAPANESE_POSTCARD = new MediaSize(100, 148, 1000, MediaSizeName.JAPANESE_POSTCARD);
        public static final MediaSize JAPANESE_DOUBLE_POSTCARD = new MediaSize(148, 200, 1000, MediaSizeName.JAPANESE_DOUBLE_POSTCARD);

        private Other() {
        }
    }
}
