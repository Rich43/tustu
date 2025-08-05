package com.sun.media.sound;

import java.io.IOException;
import java.util.Arrays;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.VoiceStatus;

/* loaded from: rt.jar:com/sun/media/sound/SoftAbstractResampler.class */
public abstract class SoftAbstractResampler implements SoftResampler {
    public abstract int getPadding();

    public abstract void interpolate(float[] fArr, float[] fArr2, float f2, float[] fArr3, float f3, float[] fArr4, int[] iArr, int i2);

    /* loaded from: rt.jar:com/sun/media/sound/SoftAbstractResampler$ModelAbstractResamplerStream.class */
    private class ModelAbstractResamplerStream implements SoftResamplerStreamer {
        AudioFloatInputStream stream;
        int loopmode;
        float loopstart;
        float looplen;
        float target_pitch;
        boolean started;
        boolean eof;
        float[][] ibuffer;
        boolean ibuffer_order;
        float[] sbuffer;
        int pad;
        int pad2;
        boolean stream_eof = false;
        boolean loopdirection = true;
        float[] current_pitch = new float[1];
        int sector_pos = 0;
        int sector_size = 400;
        int sector_loopstart = -1;
        boolean markset = false;
        int marklimit = 0;
        int streampos = 0;
        int nrofchannels = 2;
        boolean noteOff_flag = false;
        float[] ix = new float[1];
        int[] ox = new int[1];
        float samplerateconv = 1.0f;
        float pitchcorrection = 0.0f;

        ModelAbstractResamplerStream() {
            this.ibuffer_order = true;
            this.pad = SoftAbstractResampler.this.getPadding();
            this.pad2 = SoftAbstractResampler.this.getPadding() * 2;
            this.ibuffer = new float[2][this.sector_size + this.pad2];
            this.ibuffer_order = true;
        }

        @Override // com.sun.media.sound.ModelOscillatorStream
        public void noteOn(MidiChannel midiChannel, VoiceStatus voiceStatus, int i2, int i3) {
        }

        @Override // com.sun.media.sound.ModelOscillatorStream
        public void noteOff(int i2) {
            this.noteOff_flag = true;
        }

        @Override // com.sun.media.sound.SoftResamplerStreamer
        public void open(ModelWavetable modelWavetable, float f2) throws IOException {
            this.eof = false;
            this.nrofchannels = modelWavetable.getChannels();
            if (this.ibuffer.length < this.nrofchannels) {
                this.ibuffer = new float[this.nrofchannels][this.sector_size + this.pad2];
            }
            this.stream = modelWavetable.openStream();
            this.streampos = 0;
            this.stream_eof = false;
            this.pitchcorrection = modelWavetable.getPitchcorrection();
            this.samplerateconv = this.stream.getFormat().getSampleRate() / f2;
            this.looplen = modelWavetable.getLoopLength();
            this.loopstart = modelWavetable.getLoopStart();
            this.sector_loopstart = (int) (this.loopstart / this.sector_size);
            this.sector_loopstart--;
            this.sector_pos = 0;
            if (this.sector_loopstart < 0) {
                this.sector_loopstart = 0;
            }
            this.started = false;
            this.loopmode = modelWavetable.getLoopType();
            if (this.loopmode != 0) {
                this.markset = false;
                this.marklimit = this.nrofchannels * ((int) (this.looplen + this.pad2 + 1.0f));
            } else {
                this.markset = true;
            }
            this.target_pitch = this.samplerateconv;
            this.current_pitch[0] = this.samplerateconv;
            this.ibuffer_order = true;
            this.loopdirection = true;
            this.noteOff_flag = false;
            for (int i2 = 0; i2 < this.nrofchannels; i2++) {
                Arrays.fill(this.ibuffer[i2], this.sector_size, this.sector_size + this.pad2, 0.0f);
            }
            this.ix[0] = this.pad;
            this.eof = false;
            this.ix[0] = this.sector_size + this.pad;
            this.sector_pos = -1;
            this.streampos = -this.sector_size;
            nextBuffer();
        }

        @Override // com.sun.media.sound.ModelOscillatorStream
        public void setPitch(float f2) {
            this.target_pitch = ((float) Math.exp((this.pitchcorrection + f2) * (Math.log(2.0d) / 1200.0d))) * this.samplerateconv;
            if (!this.started) {
                this.current_pitch[0] = this.target_pitch;
            }
        }

        public void nextBuffer() throws IOException {
            int i2;
            if (this.ix[0] < this.pad && this.markset) {
                this.stream.reset();
                float[] fArr = this.ix;
                fArr[0] = fArr[0] + (this.streampos - (this.sector_loopstart * this.sector_size));
                this.sector_pos = this.sector_loopstart;
                this.streampos = this.sector_pos * this.sector_size;
                float[] fArr2 = this.ix;
                fArr2[0] = fArr2[0] + this.sector_size;
                this.sector_pos--;
                this.streampos -= this.sector_size;
                this.stream_eof = false;
            }
            if (this.ix[0] >= this.sector_size + this.pad && this.stream_eof) {
                this.eof = true;
                return;
            }
            if (this.ix[0] >= (this.sector_size * 4) + this.pad) {
                int i3 = (int) (((this.ix[0] - (this.sector_size * 4)) + this.pad) / this.sector_size);
                float[] fArr3 = this.ix;
                fArr3[0] = fArr3[0] - (this.sector_size * i3);
                this.sector_pos += i3;
                this.streampos += this.sector_size * i3;
                this.stream.skip(this.sector_size * i3);
            }
            while (this.ix[0] >= this.sector_size + this.pad) {
                if (!this.markset && this.sector_pos + 1 == this.sector_loopstart) {
                    this.stream.mark(this.marklimit);
                    this.markset = true;
                }
                float[] fArr4 = this.ix;
                fArr4[0] = fArr4[0] - this.sector_size;
                this.sector_pos++;
                this.streampos += this.sector_size;
                for (int i4 = 0; i4 < this.nrofchannels; i4++) {
                    float[] fArr5 = this.ibuffer[i4];
                    for (int i5 = 0; i5 < this.pad2; i5++) {
                        fArr5[i5] = fArr5[i5 + this.sector_size];
                    }
                }
                if (this.nrofchannels == 1) {
                    i2 = this.stream.read(this.ibuffer[0], this.pad2, this.sector_size);
                } else {
                    int i6 = this.sector_size * this.nrofchannels;
                    if (this.sbuffer == null || this.sbuffer.length < i6) {
                        this.sbuffer = new float[i6];
                    }
                    int i7 = this.stream.read(this.sbuffer, 0, i6);
                    if (i7 == -1) {
                        i2 = -1;
                    } else {
                        i2 = i7 / this.nrofchannels;
                        for (int i8 = 0; i8 < this.nrofchannels; i8++) {
                            float[] fArr6 = this.ibuffer[i8];
                            int i9 = i8;
                            int i10 = this.nrofchannels;
                            int i11 = this.pad2;
                            int i12 = 0;
                            while (i12 < i2) {
                                fArr6[i11] = this.sbuffer[i9];
                                i12++;
                                i9 += i10;
                                i11++;
                            }
                        }
                    }
                }
                if (i2 == -1) {
                    this.stream_eof = true;
                    for (int i13 = 0; i13 < this.nrofchannels; i13++) {
                        Arrays.fill(this.ibuffer[i13], this.pad2, this.pad2 + this.sector_size, 0.0f);
                    }
                    return;
                }
                if (i2 != this.sector_size) {
                    for (int i14 = 0; i14 < this.nrofchannels; i14++) {
                        Arrays.fill(this.ibuffer[i14], this.pad2 + i2, this.pad2 + this.sector_size, 0.0f);
                    }
                }
                this.ibuffer_order = true;
            }
        }

        public void reverseBuffers() {
            this.ibuffer_order = !this.ibuffer_order;
            for (int i2 = 0; i2 < this.nrofchannels; i2++) {
                float[] fArr = this.ibuffer[i2];
                int length = fArr.length - 1;
                int length2 = fArr.length / 2;
                for (int i3 = 0; i3 < length2; i3++) {
                    float f2 = fArr[i3];
                    fArr[i3] = fArr[length - i3];
                    fArr[length - i3] = f2;
                }
            }
        }

        @Override // com.sun.media.sound.ModelOscillatorStream
        public int read(float[][] fArr, int i2, int i3) throws IOException {
            if (this.eof) {
                return -1;
            }
            if (this.noteOff_flag && (this.loopmode & 2) != 0 && this.loopdirection) {
                this.loopmode = 0;
            }
            float f2 = (this.target_pitch - this.current_pitch[0]) / i3;
            float[] fArr2 = this.current_pitch;
            this.started = true;
            int[] iArr = this.ox;
            iArr[0] = i2;
            int i4 = i3 + i2;
            float f3 = this.sector_size + this.pad;
            if (!this.loopdirection) {
                f3 = this.pad;
            }
            while (iArr[0] != i4) {
                nextBuffer();
                if (!this.loopdirection) {
                    if (this.streampos < this.loopstart + this.pad) {
                        f3 = (this.loopstart - this.streampos) + this.pad2;
                        if (this.ix[0] <= f3) {
                            if ((this.loopmode & 4) != 0) {
                                this.loopdirection = true;
                                f3 = this.sector_size + this.pad;
                            } else {
                                float[] fArr3 = this.ix;
                                fArr3[0] = fArr3[0] + this.looplen;
                                f3 = this.pad;
                            }
                        }
                    }
                    if (this.ibuffer_order != this.loopdirection) {
                        reverseBuffers();
                    }
                    this.ix[0] = (this.sector_size + this.pad2) - this.ix[0];
                    float f4 = ((this.sector_size + this.pad2) - f3) + 1.0f;
                    float f5 = this.ix[0];
                    int i5 = iArr[0];
                    float f6 = fArr2[0];
                    for (int i6 = 0; i6 < this.nrofchannels; i6++) {
                        if (fArr[i6] != null) {
                            this.ix[0] = f5;
                            iArr[0] = i5;
                            fArr2[0] = f6;
                            SoftAbstractResampler.this.interpolate(this.ibuffer[i6], this.ix, f4, fArr2, f2, fArr[i6], iArr, i4);
                        }
                    }
                    this.ix[0] = (this.sector_size + this.pad2) - this.ix[0];
                    f3 = (this.sector_size + this.pad2) - (f4 - 1.0f);
                    if (this.eof) {
                        fArr2[0] = this.target_pitch;
                        return iArr[0] - i2;
                    }
                } else {
                    if (this.loopmode != 0 && this.streampos + this.sector_size > this.looplen + this.loopstart + this.pad) {
                        f3 = ((this.loopstart + this.looplen) - this.streampos) + this.pad2;
                        if (this.ix[0] >= f3) {
                            if ((this.loopmode & 4) != 0 || (this.loopmode & 8) != 0) {
                                this.loopdirection = false;
                                f3 = this.pad;
                            } else {
                                f3 = this.sector_size + this.pad;
                                float[] fArr4 = this.ix;
                                fArr4[0] = fArr4[0] - this.looplen;
                            }
                        }
                    }
                    if (this.ibuffer_order != this.loopdirection) {
                        reverseBuffers();
                    }
                    float f7 = this.ix[0];
                    int i7 = iArr[0];
                    float f8 = fArr2[0];
                    for (int i8 = 0; i8 < this.nrofchannels; i8++) {
                        if (fArr[i8] != null) {
                            this.ix[0] = f7;
                            iArr[0] = i7;
                            fArr2[0] = f8;
                            SoftAbstractResampler.this.interpolate(this.ibuffer[i8], this.ix, f3, fArr2, f2, fArr[i8], iArr, i4);
                        }
                    }
                    if (this.eof) {
                        fArr2[0] = this.target_pitch;
                        return iArr[0] - i2;
                    }
                }
            }
            fArr2[0] = this.target_pitch;
            return i3;
        }

        @Override // com.sun.media.sound.ModelOscillatorStream
        public void close() throws IOException {
            this.stream.close();
        }
    }

    @Override // com.sun.media.sound.SoftResampler
    public final SoftResamplerStreamer openStreamer() {
        return new ModelAbstractResamplerStream();
    }
}
