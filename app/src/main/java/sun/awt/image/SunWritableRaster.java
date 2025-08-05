package sun.awt.image;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferUShort;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import sun.java2d.StateTrackable;
import sun.java2d.StateTrackableDelegate;
import sun.java2d.SurfaceData;

/* loaded from: rt.jar:sun/awt/image/SunWritableRaster.class */
public class SunWritableRaster extends WritableRaster {
    private static DataStealer stealer;
    private StateTrackableDelegate theTrackable;

    /* loaded from: rt.jar:sun/awt/image/SunWritableRaster$DataStealer.class */
    public interface DataStealer {
        byte[] getData(DataBufferByte dataBufferByte, int i2);

        short[] getData(DataBufferUShort dataBufferUShort, int i2);

        int[] getData(DataBufferInt dataBufferInt, int i2);

        StateTrackableDelegate getTrackable(DataBuffer dataBuffer);

        void setTrackable(DataBuffer dataBuffer, StateTrackableDelegate stateTrackableDelegate);
    }

    public static void setDataStealer(DataStealer dataStealer) {
        if (stealer != null) {
            throw new InternalError("Attempt to set DataStealer twice");
        }
        stealer = dataStealer;
    }

    public static byte[] stealData(DataBufferByte dataBufferByte, int i2) {
        return stealer.getData(dataBufferByte, i2);
    }

    public static short[] stealData(DataBufferUShort dataBufferUShort, int i2) {
        return stealer.getData(dataBufferUShort, i2);
    }

    public static int[] stealData(DataBufferInt dataBufferInt, int i2) {
        return stealer.getData(dataBufferInt, i2);
    }

    public static StateTrackableDelegate stealTrackable(DataBuffer dataBuffer) {
        return stealer.getTrackable(dataBuffer);
    }

    public static void setTrackable(DataBuffer dataBuffer, StateTrackableDelegate stateTrackableDelegate) {
        stealer.setTrackable(dataBuffer, stateTrackableDelegate);
    }

    public static void makeTrackable(DataBuffer dataBuffer) {
        stealer.setTrackable(dataBuffer, StateTrackableDelegate.createInstance(StateTrackable.State.STABLE));
    }

    public static void markDirty(DataBuffer dataBuffer) {
        stealer.getTrackable(dataBuffer).markDirty();
    }

    public static void markDirty(WritableRaster writableRaster) {
        if (writableRaster instanceof SunWritableRaster) {
            ((SunWritableRaster) writableRaster).markDirty();
        } else {
            markDirty(writableRaster.getDataBuffer());
        }
    }

    public static void markDirty(Image image) {
        SurfaceData.getPrimarySurfaceData(image).markDirty();
    }

    public SunWritableRaster(SampleModel sampleModel, Point point) {
        super(sampleModel, point);
        this.theTrackable = stealTrackable(this.dataBuffer);
    }

    public SunWritableRaster(SampleModel sampleModel, DataBuffer dataBuffer, Point point) {
        super(sampleModel, dataBuffer, point);
        this.theTrackable = stealTrackable(dataBuffer);
    }

    public SunWritableRaster(SampleModel sampleModel, DataBuffer dataBuffer, Rectangle rectangle, Point point, WritableRaster writableRaster) {
        super(sampleModel, dataBuffer, rectangle, point, writableRaster);
        this.theTrackable = stealTrackable(dataBuffer);
    }

    public final void markDirty() {
        this.theTrackable.markDirty();
    }
}
