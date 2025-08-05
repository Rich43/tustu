package java.text;

import java.text.Format;

/* loaded from: rt.jar:java/text/DontCareFieldPosition.class */
class DontCareFieldPosition extends FieldPosition {
    static final FieldPosition INSTANCE = new DontCareFieldPosition();
    private final Format.FieldDelegate noDelegate;

    private DontCareFieldPosition() {
        super(0);
        this.noDelegate = new Format.FieldDelegate() { // from class: java.text.DontCareFieldPosition.1
            @Override // java.text.Format.FieldDelegate
            public void formatted(Format.Field field, Object obj, int i2, int i3, StringBuffer stringBuffer) {
            }

            @Override // java.text.Format.FieldDelegate
            public void formatted(int i2, Format.Field field, Object obj, int i3, int i4, StringBuffer stringBuffer) {
            }
        };
    }

    @Override // java.text.FieldPosition
    Format.FieldDelegate getFieldDelegate() {
        return this.noDelegate;
    }
}
