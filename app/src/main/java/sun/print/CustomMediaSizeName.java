package sun.print;

import java.util.ArrayList;
import javax.print.attribute.EnumSyntax;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;

/* loaded from: rt.jar:sun/print/CustomMediaSizeName.class */
class CustomMediaSizeName extends MediaSizeName {
    private static ArrayList customStringTable = new ArrayList();
    private static ArrayList customEnumTable = new ArrayList();
    private String choiceName;
    private MediaSizeName mediaName;
    private static final long serialVersionUID = 7412807582228043717L;

    private CustomMediaSizeName(int i2) {
        super(i2);
    }

    private static synchronized int nextValue(String str) {
        customStringTable.add(str);
        return customStringTable.size() - 1;
    }

    public CustomMediaSizeName(String str) {
        super(nextValue(str));
        customEnumTable.add(this);
        this.choiceName = null;
        this.mediaName = null;
    }

    public CustomMediaSizeName(String str, String str2, float f2, float f3) {
        super(nextValue(str));
        this.choiceName = str2;
        customEnumTable.add(this);
        this.mediaName = null;
        try {
            this.mediaName = MediaSize.findMedia(f2, f3, 25400);
        } catch (IllegalArgumentException e2) {
        }
        if (this.mediaName != null) {
            MediaSize mediaSizeForName = MediaSize.getMediaSizeForName(this.mediaName);
            if (mediaSizeForName == null) {
                this.mediaName = null;
                return;
            }
            float x2 = mediaSizeForName.getX(25400);
            float y2 = mediaSizeForName.getY(25400);
            float fAbs = Math.abs(x2 - f2);
            float fAbs2 = Math.abs(y2 - f3);
            if (fAbs > 0.1d || fAbs2 > 0.1d) {
                this.mediaName = null;
            }
        }
    }

    public String getChoiceName() {
        return this.choiceName;
    }

    public MediaSizeName getStandardMedia() {
        return this.mediaName;
    }

    public static MediaSizeName findMedia(Media[] mediaArr, float f2, float f3, int i2) {
        if (f2 <= 0.0f || f3 <= 0.0f || i2 < 1) {
            throw new IllegalArgumentException("args must be +ve values");
        }
        if (mediaArr == null || mediaArr.length == 0) {
            throw new IllegalArgumentException("args must have valid array of media");
        }
        int i3 = 0;
        MediaSizeName[] mediaSizeNameArr = new MediaSizeName[mediaArr.length];
        for (int i4 = 0; i4 < mediaArr.length; i4++) {
            if (mediaArr[i4] instanceof MediaSizeName) {
                int i5 = i3;
                i3++;
                mediaSizeNameArr[i5] = (MediaSizeName) mediaArr[i4];
            }
        }
        if (i3 == 0) {
            return null;
        }
        int i6 = 0;
        double d2 = (f2 * f2) + (f3 * f3);
        int i7 = 0;
        while (true) {
            if (i7 >= i3) {
                break;
            }
            MediaSize mediaSizeForName = MediaSize.getMediaSizeForName(mediaSizeNameArr[i7]);
            if (mediaSizeForName != null) {
                float[] size = mediaSizeForName.getSize(i2);
                if (f2 == size[0] && f3 == size[1]) {
                    i6 = i7;
                    break;
                }
                float f4 = f2 - size[0];
                float f5 = f3 - size[1];
                double d3 = (f4 * f4) + (f5 * f5);
                if (d3 < d2) {
                    d2 = d3;
                    i6 = i7;
                }
            }
            i7++;
        }
        return mediaSizeNameArr[i6];
    }

    public Media[] getSuperEnumTable() {
        return (Media[]) super.getEnumValueTable();
    }

    @Override // javax.print.attribute.standard.MediaSizeName, javax.print.attribute.EnumSyntax
    protected String[] getStringTable() {
        return (String[]) customStringTable.toArray(new String[customStringTable.size()]);
    }

    @Override // javax.print.attribute.standard.MediaSizeName, javax.print.attribute.EnumSyntax
    protected EnumSyntax[] getEnumValueTable() {
        return (MediaSizeName[]) customEnumTable.toArray(new MediaSizeName[customEnumTable.size()]);
    }
}
