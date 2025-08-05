package com.sun.media.sound;

import java.util.Random;
import javax.sound.midi.Patch;
import javax.sound.sampled.AudioFormat;
import org.apache.commons.net.tftp.TFTP;

/* loaded from: rt.jar:com/sun/media/sound/EmergencySoundbank.class */
public final class EmergencySoundbank {
    private static final String[] general_midi_instruments = {"Acoustic Grand Piano", "Bright Acoustic Piano", "Electric Grand Piano", "Honky-tonk Piano", "Electric Piano 1", "Electric Piano 2", "Harpsichord", "Clavi", "Celesta", "Glockenspiel", "Music Box", "Vibraphone", "Marimba", "Xylophone", "Tubular Bells", "Dulcimer", "Drawbar Organ", "Percussive Organ", "Rock Organ", "Church Organ", "Reed Organ", "Accordion", "Harmonica", "Tango Accordion", "Acoustic Guitar (nylon)", "Acoustic Guitar (steel)", "Electric Guitar (jazz)", "Electric Guitar (clean)", "Electric Guitar (muted)", "Overdriven Guitar", "Distortion Guitar", "Guitar harmonics", "Acoustic Bass", "Electric Bass (finger)", "Electric Bass (pick)", "Fretless Bass", "Slap Bass 1", "Slap Bass 2", "Synth Bass 1", "Synth Bass 2", "Violin", "Viola", "Cello", "Contrabass", "Tremolo Strings", "Pizzicato Strings", "Orchestral Harp", "Timpani", "String Ensemble 1", "String Ensemble 2", "SynthStrings 1", "SynthStrings 2", "Choir Aahs", "Voice Oohs", "Synth Voice", "Orchestra Hit", "Trumpet", "Trombone", "Tuba", "Muted Trumpet", "French Horn", "Brass Section", "SynthBrass 1", "SynthBrass 2", "Soprano Sax", "Alto Sax", "Tenor Sax", "Baritone Sax", "Oboe", "English Horn", "Bassoon", "Clarinet", "Piccolo", "Flute", "Recorder", "Pan Flute", "Blown Bottle", "Shakuhachi", "Whistle", "Ocarina", "Lead 1 (square)", "Lead 2 (sawtooth)", "Lead 3 (calliope)", "Lead 4 (chiff)", "Lead 5 (charang)", "Lead 6 (voice)", "Lead 7 (fifths)", "Lead 8 (bass + lead)", "Pad 1 (new age)", "Pad 2 (warm)", "Pad 3 (polysynth)", "Pad 4 (choir)", "Pad 5 (bowed)", "Pad 6 (metallic)", "Pad 7 (halo)", "Pad 8 (sweep)", "FX 1 (rain)", "FX 2 (soundtrack)", "FX 3 (crystal)", "FX 4 (atmosphere)", "FX 5 (brightness)", "FX 6 (goblins)", "FX 7 (echoes)", "FX 8 (sci-fi)", "Sitar", "Banjo", "Shamisen", "Koto", "Kalimba", "Bag pipe", "Fiddle", "Shanai", "Tinkle Bell", "Agogo", "Steel Drums", "Woodblock", "Taiko Drum", "Melodic Tom", "Synth Drum", "Reverse Cymbal", "Guitar Fret Noise", "Breath Noise", "Seashore", "Bird Tweet", "Telephone Ring", "Helicopter", "Applause", "Gunshot"};

    public static SF2Soundbank createSoundbank() throws Exception {
        SF2Soundbank sF2Soundbank = new SF2Soundbank();
        sF2Soundbank.setName("Emergency GM sound set");
        sF2Soundbank.setVendor("Generated");
        sF2Soundbank.setDescription("Emergency generated soundbank");
        SF2Layer sF2LayerNew_bass_drum = new_bass_drum(sF2Soundbank);
        SF2Layer sF2LayerNew_snare_drum = new_snare_drum(sF2Soundbank);
        SF2Layer sF2LayerNew_tom = new_tom(sF2Soundbank);
        SF2Layer sF2LayerNew_open_hihat = new_open_hihat(sF2Soundbank);
        SF2Layer sF2LayerNew_closed_hihat = new_closed_hihat(sF2Soundbank);
        SF2Layer sF2LayerNew_crash_cymbal = new_crash_cymbal(sF2Soundbank);
        SF2Layer sF2LayerNew_side_stick = new_side_stick(sF2Soundbank);
        SF2Layer[] sF2LayerArr = new SF2Layer[128];
        sF2LayerArr[35] = sF2LayerNew_bass_drum;
        sF2LayerArr[36] = sF2LayerNew_bass_drum;
        sF2LayerArr[38] = sF2LayerNew_snare_drum;
        sF2LayerArr[40] = sF2LayerNew_snare_drum;
        sF2LayerArr[41] = sF2LayerNew_tom;
        sF2LayerArr[43] = sF2LayerNew_tom;
        sF2LayerArr[45] = sF2LayerNew_tom;
        sF2LayerArr[47] = sF2LayerNew_tom;
        sF2LayerArr[48] = sF2LayerNew_tom;
        sF2LayerArr[50] = sF2LayerNew_tom;
        sF2LayerArr[42] = sF2LayerNew_closed_hihat;
        sF2LayerArr[44] = sF2LayerNew_closed_hihat;
        sF2LayerArr[46] = sF2LayerNew_open_hihat;
        sF2LayerArr[49] = sF2LayerNew_crash_cymbal;
        sF2LayerArr[51] = sF2LayerNew_crash_cymbal;
        sF2LayerArr[52] = sF2LayerNew_crash_cymbal;
        sF2LayerArr[55] = sF2LayerNew_crash_cymbal;
        sF2LayerArr[57] = sF2LayerNew_crash_cymbal;
        sF2LayerArr[59] = sF2LayerNew_crash_cymbal;
        sF2LayerArr[37] = sF2LayerNew_side_stick;
        sF2LayerArr[39] = sF2LayerNew_side_stick;
        sF2LayerArr[53] = sF2LayerNew_side_stick;
        sF2LayerArr[54] = sF2LayerNew_side_stick;
        sF2LayerArr[56] = sF2LayerNew_side_stick;
        sF2LayerArr[58] = sF2LayerNew_side_stick;
        sF2LayerArr[69] = sF2LayerNew_side_stick;
        sF2LayerArr[70] = sF2LayerNew_side_stick;
        sF2LayerArr[75] = sF2LayerNew_side_stick;
        sF2LayerArr[60] = sF2LayerNew_side_stick;
        sF2LayerArr[61] = sF2LayerNew_side_stick;
        sF2LayerArr[62] = sF2LayerNew_side_stick;
        sF2LayerArr[63] = sF2LayerNew_side_stick;
        sF2LayerArr[64] = sF2LayerNew_side_stick;
        sF2LayerArr[65] = sF2LayerNew_side_stick;
        sF2LayerArr[66] = sF2LayerNew_side_stick;
        sF2LayerArr[67] = sF2LayerNew_side_stick;
        sF2LayerArr[68] = sF2LayerNew_side_stick;
        sF2LayerArr[71] = sF2LayerNew_side_stick;
        sF2LayerArr[72] = sF2LayerNew_side_stick;
        sF2LayerArr[73] = sF2LayerNew_side_stick;
        sF2LayerArr[74] = sF2LayerNew_side_stick;
        sF2LayerArr[76] = sF2LayerNew_side_stick;
        sF2LayerArr[77] = sF2LayerNew_side_stick;
        sF2LayerArr[78] = sF2LayerNew_side_stick;
        sF2LayerArr[79] = sF2LayerNew_side_stick;
        sF2LayerArr[80] = sF2LayerNew_side_stick;
        sF2LayerArr[81] = sF2LayerNew_side_stick;
        SF2Instrument sF2Instrument = new SF2Instrument(sF2Soundbank);
        sF2Instrument.setName("Standard Kit");
        sF2Instrument.setPatch(new ModelPatch(0, 0, true));
        sF2Soundbank.addInstrument(sF2Instrument);
        for (int i2 = 0; i2 < sF2LayerArr.length; i2++) {
            if (sF2LayerArr[i2] != null) {
                SF2InstrumentRegion sF2InstrumentRegion = new SF2InstrumentRegion();
                sF2InstrumentRegion.setLayer(sF2LayerArr[i2]);
                sF2InstrumentRegion.putBytes(43, new byte[]{(byte) i2, (byte) i2});
                sF2Instrument.getRegions().add(sF2InstrumentRegion);
            }
        }
        SF2Layer sF2LayerNew_gpiano = new_gpiano(sF2Soundbank);
        SF2Layer sF2LayerNew_gpiano2 = new_gpiano2(sF2Soundbank);
        SF2Layer sF2LayerNew_piano_hammer = new_piano_hammer(sF2Soundbank);
        SF2Layer sF2LayerNew_piano1 = new_piano1(sF2Soundbank);
        SF2Layer sF2LayerNew_epiano1 = new_epiano1(sF2Soundbank);
        SF2Layer sF2LayerNew_epiano2 = new_epiano2(sF2Soundbank);
        SF2Layer sF2LayerNew_guitar1 = new_guitar1(sF2Soundbank);
        SF2Layer sF2LayerNew_guitar_pick = new_guitar_pick(sF2Soundbank);
        SF2Layer sF2LayerNew_guitar_dist = new_guitar_dist(sF2Soundbank);
        SF2Layer sF2LayerNew_bass1 = new_bass1(sF2Soundbank);
        SF2Layer sF2LayerNew_bass2 = new_bass2(sF2Soundbank);
        SF2Layer sF2LayerNew_synthbass = new_synthbass(sF2Soundbank);
        SF2Layer sF2LayerNew_string2 = new_string2(sF2Soundbank);
        SF2Layer sF2LayerNew_orchhit = new_orchhit(sF2Soundbank);
        SF2Layer sF2LayerNew_choir = new_choir(sF2Soundbank);
        SF2Layer sF2LayerNew_solostring = new_solostring(sF2Soundbank);
        SF2Layer sF2LayerNew_organ = new_organ(sF2Soundbank);
        SF2Layer sF2LayerNew_ch_organ = new_ch_organ(sF2Soundbank);
        SF2Layer sF2LayerNew_bell = new_bell(sF2Soundbank);
        SF2Layer sF2LayerNew_flute = new_flute(sF2Soundbank);
        SF2Layer sF2LayerNew_timpani = new_timpani(sF2Soundbank);
        SF2Layer sF2LayerNew_melodic_toms = new_melodic_toms(sF2Soundbank);
        SF2Layer sF2LayerNew_trumpet = new_trumpet(sF2Soundbank);
        SF2Layer sF2LayerNew_trombone = new_trombone(sF2Soundbank);
        SF2Layer sF2LayerNew_brass_section = new_brass_section(sF2Soundbank);
        SF2Layer sF2LayerNew_horn = new_horn(sF2Soundbank);
        SF2Layer sF2LayerNew_sax = new_sax(sF2Soundbank);
        SF2Layer sF2LayerNew_oboe = new_oboe(sF2Soundbank);
        SF2Layer sF2LayerNew_bassoon = new_bassoon(sF2Soundbank);
        SF2Layer sF2LayerNew_clarinet = new_clarinet(sF2Soundbank);
        SF2Layer sF2LayerNew_reverse_cymbal = new_reverse_cymbal(sF2Soundbank);
        newInstrument(sF2Soundbank, "Piano", new Patch(0, 0), sF2LayerNew_gpiano, sF2LayerNew_piano_hammer);
        newInstrument(sF2Soundbank, "Piano", new Patch(0, 1), sF2LayerNew_gpiano2, sF2LayerNew_piano_hammer);
        newInstrument(sF2Soundbank, "Piano", new Patch(0, 2), sF2LayerNew_piano1);
        SF2Instrument sF2InstrumentNewInstrument = newInstrument(sF2Soundbank, "Honky-tonk Piano", new Patch(0, 3), sF2LayerNew_piano1, sF2LayerNew_piano1);
        SF2InstrumentRegion sF2InstrumentRegion2 = sF2InstrumentNewInstrument.getRegions().get(0);
        sF2InstrumentRegion2.putInteger(8, 80);
        sF2InstrumentRegion2.putInteger(52, 30);
        sF2InstrumentNewInstrument.getRegions().get(1).putInteger(8, 30);
        newInstrument(sF2Soundbank, "Rhodes", new Patch(0, 4), sF2LayerNew_epiano2);
        newInstrument(sF2Soundbank, "Rhodes", new Patch(0, 5), sF2LayerNew_epiano2);
        newInstrument(sF2Soundbank, "Clavinet", new Patch(0, 6), sF2LayerNew_epiano1);
        newInstrument(sF2Soundbank, "Clavinet", new Patch(0, 7), sF2LayerNew_epiano1);
        newInstrument(sF2Soundbank, "Rhodes", new Patch(0, 8), sF2LayerNew_epiano2);
        newInstrument(sF2Soundbank, "Bell", new Patch(0, 9), sF2LayerNew_bell);
        newInstrument(sF2Soundbank, "Bell", new Patch(0, 10), sF2LayerNew_bell);
        newInstrument(sF2Soundbank, "Vibraphone", new Patch(0, 11), sF2LayerNew_bell);
        newInstrument(sF2Soundbank, "Marimba", new Patch(0, 12), sF2LayerNew_bell);
        newInstrument(sF2Soundbank, "Marimba", new Patch(0, 13), sF2LayerNew_bell);
        newInstrument(sF2Soundbank, "Bell", new Patch(0, 14), sF2LayerNew_bell);
        newInstrument(sF2Soundbank, "Rock Organ", new Patch(0, 15), sF2LayerNew_organ);
        newInstrument(sF2Soundbank, "Rock Organ", new Patch(0, 16), sF2LayerNew_organ);
        newInstrument(sF2Soundbank, "Perc Organ", new Patch(0, 17), sF2LayerNew_organ);
        newInstrument(sF2Soundbank, "Rock Organ", new Patch(0, 18), sF2LayerNew_organ);
        newInstrument(sF2Soundbank, "Church Organ", new Patch(0, 19), sF2LayerNew_ch_organ);
        newInstrument(sF2Soundbank, "Accordion", new Patch(0, 20), sF2LayerNew_organ);
        newInstrument(sF2Soundbank, "Accordion", new Patch(0, 21), sF2LayerNew_organ);
        newInstrument(sF2Soundbank, "Accordion", new Patch(0, 22), sF2LayerNew_organ);
        newInstrument(sF2Soundbank, "Accordion", new Patch(0, 23), sF2LayerNew_organ);
        newInstrument(sF2Soundbank, "Guitar", new Patch(0, 24), sF2LayerNew_guitar1, sF2LayerNew_guitar_pick);
        newInstrument(sF2Soundbank, "Guitar", new Patch(0, 25), sF2LayerNew_guitar1, sF2LayerNew_guitar_pick);
        newInstrument(sF2Soundbank, "Guitar", new Patch(0, 26), sF2LayerNew_guitar1, sF2LayerNew_guitar_pick);
        newInstrument(sF2Soundbank, "Guitar", new Patch(0, 27), sF2LayerNew_guitar1, sF2LayerNew_guitar_pick);
        newInstrument(sF2Soundbank, "Guitar", new Patch(0, 28), sF2LayerNew_guitar1, sF2LayerNew_guitar_pick);
        newInstrument(sF2Soundbank, "Distorted Guitar", new Patch(0, 29), sF2LayerNew_guitar_dist);
        newInstrument(sF2Soundbank, "Distorted Guitar", new Patch(0, 30), sF2LayerNew_guitar_dist);
        newInstrument(sF2Soundbank, "Guitar", new Patch(0, 31), sF2LayerNew_guitar1, sF2LayerNew_guitar_pick);
        newInstrument(sF2Soundbank, "Finger Bass", new Patch(0, 32), sF2LayerNew_bass1);
        newInstrument(sF2Soundbank, "Finger Bass", new Patch(0, 33), sF2LayerNew_bass1);
        newInstrument(sF2Soundbank, "Finger Bass", new Patch(0, 34), sF2LayerNew_bass1);
        newInstrument(sF2Soundbank, "Frettless Bass", new Patch(0, 35), sF2LayerNew_bass2);
        newInstrument(sF2Soundbank, "Frettless Bass", new Patch(0, 36), sF2LayerNew_bass2);
        newInstrument(sF2Soundbank, "Frettless Bass", new Patch(0, 37), sF2LayerNew_bass2);
        newInstrument(sF2Soundbank, "Synth Bass1", new Patch(0, 38), sF2LayerNew_synthbass);
        newInstrument(sF2Soundbank, "Synth Bass2", new Patch(0, 39), sF2LayerNew_synthbass);
        newInstrument(sF2Soundbank, "Solo String", new Patch(0, 40), sF2LayerNew_string2, sF2LayerNew_solostring);
        newInstrument(sF2Soundbank, "Solo String", new Patch(0, 41), sF2LayerNew_string2, sF2LayerNew_solostring);
        newInstrument(sF2Soundbank, "Solo String", new Patch(0, 42), sF2LayerNew_string2, sF2LayerNew_solostring);
        newInstrument(sF2Soundbank, "Solo String", new Patch(0, 43), sF2LayerNew_string2, sF2LayerNew_solostring);
        newInstrument(sF2Soundbank, "Solo String", new Patch(0, 44), sF2LayerNew_string2, sF2LayerNew_solostring);
        newInstrument(sF2Soundbank, "Def", new Patch(0, 45), sF2LayerNew_piano1);
        newInstrument(sF2Soundbank, "Harp", new Patch(0, 46), sF2LayerNew_bell);
        newInstrument(sF2Soundbank, "Timpani", new Patch(0, 47), sF2LayerNew_timpani);
        newInstrument(sF2Soundbank, "Strings", new Patch(0, 48), sF2LayerNew_string2);
        SF2InstrumentRegion sF2InstrumentRegion3 = newInstrument(sF2Soundbank, "Slow Strings", new Patch(0, 49), sF2LayerNew_string2).getRegions().get(0);
        sF2InstrumentRegion3.putInteger(34, 2500);
        sF2InstrumentRegion3.putInteger(38, 2000);
        newInstrument(sF2Soundbank, "Synth Strings", new Patch(0, 50), sF2LayerNew_string2);
        newInstrument(sF2Soundbank, "Synth Strings", new Patch(0, 51), sF2LayerNew_string2);
        newInstrument(sF2Soundbank, "Choir", new Patch(0, 52), sF2LayerNew_choir);
        newInstrument(sF2Soundbank, "Choir", new Patch(0, 53), sF2LayerNew_choir);
        newInstrument(sF2Soundbank, "Choir", new Patch(0, 54), sF2LayerNew_choir);
        SF2InstrumentRegion sF2InstrumentRegion4 = newInstrument(sF2Soundbank, "Orch Hit", new Patch(0, 55), sF2LayerNew_orchhit, sF2LayerNew_orchhit, sF2LayerNew_timpani).getRegions().get(0);
        sF2InstrumentRegion4.putInteger(51, -12);
        sF2InstrumentRegion4.putInteger(48, -100);
        newInstrument(sF2Soundbank, "Trumpet", new Patch(0, 56), sF2LayerNew_trumpet);
        newInstrument(sF2Soundbank, "Trombone", new Patch(0, 57), sF2LayerNew_trombone);
        newInstrument(sF2Soundbank, "Trombone", new Patch(0, 58), sF2LayerNew_trombone);
        newInstrument(sF2Soundbank, "Trumpet", new Patch(0, 59), sF2LayerNew_trumpet);
        newInstrument(sF2Soundbank, "Horn", new Patch(0, 60), sF2LayerNew_horn);
        newInstrument(sF2Soundbank, "Brass Section", new Patch(0, 61), sF2LayerNew_brass_section);
        newInstrument(sF2Soundbank, "Brass Section", new Patch(0, 62), sF2LayerNew_brass_section);
        newInstrument(sF2Soundbank, "Brass Section", new Patch(0, 63), sF2LayerNew_brass_section);
        newInstrument(sF2Soundbank, "Sax", new Patch(0, 64), sF2LayerNew_sax);
        newInstrument(sF2Soundbank, "Sax", new Patch(0, 65), sF2LayerNew_sax);
        newInstrument(sF2Soundbank, "Sax", new Patch(0, 66), sF2LayerNew_sax);
        newInstrument(sF2Soundbank, "Sax", new Patch(0, 67), sF2LayerNew_sax);
        newInstrument(sF2Soundbank, "Oboe", new Patch(0, 68), sF2LayerNew_oboe);
        newInstrument(sF2Soundbank, "Horn", new Patch(0, 69), sF2LayerNew_horn);
        newInstrument(sF2Soundbank, "Bassoon", new Patch(0, 70), sF2LayerNew_bassoon);
        newInstrument(sF2Soundbank, "Clarinet", new Patch(0, 71), sF2LayerNew_clarinet);
        newInstrument(sF2Soundbank, "Flute", new Patch(0, 72), sF2LayerNew_flute);
        newInstrument(sF2Soundbank, "Flute", new Patch(0, 73), sF2LayerNew_flute);
        newInstrument(sF2Soundbank, "Flute", new Patch(0, 74), sF2LayerNew_flute);
        newInstrument(sF2Soundbank, "Flute", new Patch(0, 75), sF2LayerNew_flute);
        newInstrument(sF2Soundbank, "Flute", new Patch(0, 76), sF2LayerNew_flute);
        newInstrument(sF2Soundbank, "Flute", new Patch(0, 77), sF2LayerNew_flute);
        newInstrument(sF2Soundbank, "Flute", new Patch(0, 78), sF2LayerNew_flute);
        newInstrument(sF2Soundbank, "Flute", new Patch(0, 79), sF2LayerNew_flute);
        newInstrument(sF2Soundbank, "Organ", new Patch(0, 80), sF2LayerNew_organ);
        newInstrument(sF2Soundbank, "Organ", new Patch(0, 81), sF2LayerNew_organ);
        newInstrument(sF2Soundbank, "Flute", new Patch(0, 82), sF2LayerNew_flute);
        newInstrument(sF2Soundbank, "Organ", new Patch(0, 83), sF2LayerNew_organ);
        newInstrument(sF2Soundbank, "Organ", new Patch(0, 84), sF2LayerNew_organ);
        newInstrument(sF2Soundbank, "Choir", new Patch(0, 85), sF2LayerNew_choir);
        newInstrument(sF2Soundbank, "Organ", new Patch(0, 86), sF2LayerNew_organ);
        newInstrument(sF2Soundbank, "Organ", new Patch(0, 87), sF2LayerNew_organ);
        newInstrument(sF2Soundbank, "Synth Strings", new Patch(0, 88), sF2LayerNew_string2);
        newInstrument(sF2Soundbank, "Organ", new Patch(0, 89), sF2LayerNew_organ);
        newInstrument(sF2Soundbank, "Def", new Patch(0, 90), sF2LayerNew_piano1);
        newInstrument(sF2Soundbank, "Choir", new Patch(0, 91), sF2LayerNew_choir);
        newInstrument(sF2Soundbank, "Organ", new Patch(0, 92), sF2LayerNew_organ);
        newInstrument(sF2Soundbank, "Organ", new Patch(0, 93), sF2LayerNew_organ);
        newInstrument(sF2Soundbank, "Organ", new Patch(0, 94), sF2LayerNew_organ);
        newInstrument(sF2Soundbank, "Organ", new Patch(0, 95), sF2LayerNew_organ);
        newInstrument(sF2Soundbank, "Organ", new Patch(0, 96), sF2LayerNew_organ);
        newInstrument(sF2Soundbank, "Organ", new Patch(0, 97), sF2LayerNew_organ);
        newInstrument(sF2Soundbank, "Bell", new Patch(0, 98), sF2LayerNew_bell);
        newInstrument(sF2Soundbank, "Organ", new Patch(0, 99), sF2LayerNew_organ);
        newInstrument(sF2Soundbank, "Organ", new Patch(0, 100), sF2LayerNew_organ);
        newInstrument(sF2Soundbank, "Organ", new Patch(0, 101), sF2LayerNew_organ);
        newInstrument(sF2Soundbank, "Def", new Patch(0, 102), sF2LayerNew_piano1);
        newInstrument(sF2Soundbank, "Synth Strings", new Patch(0, 103), sF2LayerNew_string2);
        newInstrument(sF2Soundbank, "Def", new Patch(0, 104), sF2LayerNew_piano1);
        newInstrument(sF2Soundbank, "Def", new Patch(0, 105), sF2LayerNew_piano1);
        newInstrument(sF2Soundbank, "Def", new Patch(0, 106), sF2LayerNew_piano1);
        newInstrument(sF2Soundbank, "Def", new Patch(0, 107), sF2LayerNew_piano1);
        newInstrument(sF2Soundbank, "Marimba", new Patch(0, 108), sF2LayerNew_bell);
        newInstrument(sF2Soundbank, "Sax", new Patch(0, 109), sF2LayerNew_sax);
        newInstrument(sF2Soundbank, "Solo String", new Patch(0, 110), sF2LayerNew_string2, sF2LayerNew_solostring);
        newInstrument(sF2Soundbank, "Oboe", new Patch(0, 111), sF2LayerNew_oboe);
        newInstrument(sF2Soundbank, "Bell", new Patch(0, 112), sF2LayerNew_bell);
        newInstrument(sF2Soundbank, "Melodic Toms", new Patch(0, 113), sF2LayerNew_melodic_toms);
        newInstrument(sF2Soundbank, "Marimba", new Patch(0, 114), sF2LayerNew_bell);
        newInstrument(sF2Soundbank, "Melodic Toms", new Patch(0, 115), sF2LayerNew_melodic_toms);
        newInstrument(sF2Soundbank, "Melodic Toms", new Patch(0, 116), sF2LayerNew_melodic_toms);
        newInstrument(sF2Soundbank, "Melodic Toms", new Patch(0, 117), sF2LayerNew_melodic_toms);
        newInstrument(sF2Soundbank, "Reverse Cymbal", new Patch(0, 118), sF2LayerNew_reverse_cymbal);
        newInstrument(sF2Soundbank, "Reverse Cymbal", new Patch(0, 119), sF2LayerNew_reverse_cymbal);
        newInstrument(sF2Soundbank, "Guitar", new Patch(0, 120), sF2LayerNew_guitar1);
        newInstrument(sF2Soundbank, "Def", new Patch(0, 121), sF2LayerNew_piano1);
        SF2InstrumentRegion sF2InstrumentRegion5 = newInstrument(sF2Soundbank, "Seashore/Reverse Cymbal", new Patch(0, 122), sF2LayerNew_reverse_cymbal).getRegions().get(0);
        sF2InstrumentRegion5.putInteger(37, 1000);
        sF2InstrumentRegion5.putInteger(36, 18500);
        sF2InstrumentRegion5.putInteger(38, 4500);
        sF2InstrumentRegion5.putInteger(8, -4500);
        SF2InstrumentRegion sF2InstrumentRegion6 = newInstrument(sF2Soundbank, "Bird/Flute", new Patch(0, 123), sF2LayerNew_flute).getRegions().get(0);
        sF2InstrumentRegion6.putInteger(51, 24);
        sF2InstrumentRegion6.putInteger(36, -3000);
        sF2InstrumentRegion6.putInteger(37, 1000);
        newInstrument(sF2Soundbank, "Def", new Patch(0, 124), sF2LayerNew_side_stick);
        SF2InstrumentRegion sF2InstrumentRegion7 = newInstrument(sF2Soundbank, "Seashore/Reverse Cymbal", new Patch(0, 125), sF2LayerNew_reverse_cymbal).getRegions().get(0);
        sF2InstrumentRegion7.putInteger(37, 1000);
        sF2InstrumentRegion7.putInteger(36, 18500);
        sF2InstrumentRegion7.putInteger(38, 4500);
        sF2InstrumentRegion7.putInteger(8, -4500);
        newInstrument(sF2Soundbank, "Applause/crash_cymbal", new Patch(0, 126), sF2LayerNew_crash_cymbal);
        newInstrument(sF2Soundbank, "Gunshot/side_stick", new Patch(0, 127), sF2LayerNew_side_stick);
        for (SF2Instrument sF2Instrument2 : sF2Soundbank.getInstruments()) {
            Patch patch = sF2Instrument2.getPatch();
            if (!(patch instanceof ModelPatch) || !((ModelPatch) patch).isPercussion()) {
                sF2Instrument2.setName(general_midi_instruments[patch.getProgram()]);
            }
        }
        return sF2Soundbank;
    }

    public static SF2Layer new_bell(SF2Soundbank sF2Soundbank) {
        Random random = new Random(102030201L);
        double[] dArr = new double[4096 * 8 * 2];
        double d2 = 8 * 25;
        double d3 = 0.2d;
        double dPow = Math.pow(1.0E-5d / 0.2d, 0.025d);
        for (int i2 = 0; i2 < 40; i2++) {
            complexGaussianDist(dArr, d2 * (i2 + 1) * (1.0d + (((random.nextDouble() * 2.0d) - 1.0d) * 0.01d)), 0.01d + ((0.05d - 0.01d) * (i2 / 40.0d)), d3);
            d3 *= dPow;
        }
        SF2Layer sF2LayerNewLayer = newLayer(sF2Soundbank, "EPiano", newSimpleFFTSample(sF2Soundbank, "EPiano", dArr, d2));
        SF2LayerRegion sF2LayerRegion = sF2LayerNewLayer.getRegions().get(0);
        sF2LayerRegion.putInteger(54, 1);
        sF2LayerRegion.putInteger(34, -12000);
        sF2LayerRegion.putInteger(38, 0);
        sF2LayerRegion.putInteger(36, 4000);
        sF2LayerRegion.putInteger(37, 1000);
        sF2LayerRegion.putInteger(26, 1200);
        sF2LayerRegion.putInteger(30, 12000);
        sF2LayerRegion.putInteger(11, -9000);
        sF2LayerRegion.putInteger(8, 16000);
        return sF2LayerNewLayer;
    }

    public static SF2Layer new_guitar1(SF2Soundbank sF2Soundbank) {
        double[] dArr = new double[4096 * 8 * 2];
        double d2 = 8 * 25;
        double d3 = 2.0d;
        double dPow = Math.pow(0.01d / 2.0d, 0.025d);
        double[] dArr2 = new double[40];
        for (int i2 = 0; i2 < 40; i2++) {
            dArr2[i2] = d3;
            d3 *= dPow;
        }
        dArr2[0] = 2.0d;
        dArr2[1] = 0.5d;
        dArr2[2] = 0.45d;
        dArr2[3] = 0.2d;
        dArr2[4] = 1.0d;
        dArr2[5] = 0.5d;
        dArr2[6] = 2.0d;
        dArr2[7] = 1.0d;
        dArr2[8] = 0.5d;
        dArr2[9] = 1.0d;
        dArr2[9] = 0.5d;
        dArr2[10] = 0.2d;
        dArr2[11] = 1.0d;
        dArr2[12] = 0.7d;
        dArr2[13] = 0.5d;
        dArr2[14] = 1.0d;
        for (int i3 = 0; i3 < 40; i3++) {
            complexGaussianDist(dArr, d2 * (i3 + 1), 0.01d + ((0.01d - 0.01d) * (i3 / 40.0d)), dArr2[i3]);
        }
        SF2Layer sF2LayerNewLayer = newLayer(sF2Soundbank, "Guitar", newSimpleFFTSample(sF2Soundbank, "Guitar", dArr, d2));
        SF2LayerRegion sF2LayerRegion = sF2LayerNewLayer.getRegions().get(0);
        sF2LayerRegion.putInteger(54, 1);
        sF2LayerRegion.putInteger(34, -12000);
        sF2LayerRegion.putInteger(38, 0);
        sF2LayerRegion.putInteger(36, 2400);
        sF2LayerRegion.putInteger(37, 1000);
        sF2LayerRegion.putInteger(26, -100);
        sF2LayerRegion.putInteger(30, 12000);
        sF2LayerRegion.putInteger(11, -6000);
        sF2LayerRegion.putInteger(8, 16000);
        sF2LayerRegion.putInteger(48, -20);
        return sF2LayerNewLayer;
    }

    public static SF2Layer new_guitar_dist(SF2Soundbank sF2Soundbank) {
        double[] dArr = new double[4096 * 8 * 2];
        double d2 = 8 * 25;
        double d3 = 2.0d;
        double dPow = Math.pow(0.01d / 2.0d, 0.025d);
        double[] dArr2 = new double[40];
        for (int i2 = 0; i2 < 40; i2++) {
            dArr2[i2] = d3;
            d3 *= dPow;
        }
        dArr2[0] = 5.0d;
        dArr2[1] = 2.0d;
        dArr2[2] = 0.45d;
        dArr2[3] = 0.2d;
        dArr2[4] = 1.0d;
        dArr2[5] = 0.5d;
        dArr2[6] = 2.0d;
        dArr2[7] = 1.0d;
        dArr2[8] = 0.5d;
        dArr2[9] = 1.0d;
        dArr2[9] = 0.5d;
        dArr2[10] = 0.2d;
        dArr2[11] = 1.0d;
        dArr2[12] = 0.7d;
        dArr2[13] = 0.5d;
        dArr2[14] = 1.0d;
        for (int i3 = 0; i3 < 40; i3++) {
            complexGaussianDist(dArr, d2 * (i3 + 1), 0.01d + ((0.01d - 0.01d) * (i3 / 40.0d)), dArr2[i3]);
        }
        SF2Layer sF2LayerNewLayer = newLayer(sF2Soundbank, "Distorted Guitar", newSimpleFFTSample_dist(sF2Soundbank, "Distorted Guitar", dArr, d2, 10000.0d));
        SF2LayerRegion sF2LayerRegion = sF2LayerNewLayer.getRegions().get(0);
        sF2LayerRegion.putInteger(54, 1);
        sF2LayerRegion.putInteger(34, -12000);
        sF2LayerRegion.putInteger(38, 0);
        sF2LayerRegion.putInteger(8, 8000);
        return sF2LayerNewLayer;
    }

    public static SF2Layer new_guitar_pick(SF2Soundbank sF2Soundbank) {
        int i2 = 4096 * 2;
        double[] dArr = new double[2 * i2];
        Random random = new Random(3049912L);
        for (int i3 = 0; i3 < dArr.length; i3 += 2) {
            dArr[i3] = 2.0d * (random.nextDouble() - 0.5d);
        }
        fft(dArr);
        for (int i4 = i2 / 2; i4 < dArr.length; i4++) {
            dArr[i4] = 0.0d;
        }
        for (int i5 = 0; i5 < 2048 * 2; i5++) {
            int i6 = i5;
            dArr[i6] = dArr[i6] * (Math.exp((-Math.abs((i5 - 23) / 2)) * 1.2d) + Math.exp((-Math.abs((i5 - 40) / 2)) * 0.9d));
        }
        randomPhase(dArr, new Random(3049912L));
        ifft(dArr);
        normalize(dArr, 0.8d);
        double[] dArrRealPart = realPart(dArr);
        double d2 = 1.0d;
        for (int i7 = 0; i7 < dArrRealPart.length; i7++) {
            int i8 = i7;
            dArrRealPart[i8] = dArrRealPart[i8] * d2;
            d2 *= 0.9994d;
        }
        fadeUp(dArrRealPart, 80);
        SF2Sample sF2SampleNewSimpleDrumSample = newSimpleDrumSample(sF2Soundbank, "Guitar Noise", dArrRealPart);
        SF2Layer sF2Layer = new SF2Layer(sF2Soundbank);
        sF2Layer.setName("Guitar Noise");
        sF2Layer.setGlobalZone(new SF2GlobalRegion());
        sF2Soundbank.addResource(sF2Layer);
        SF2LayerRegion sF2LayerRegion = new SF2LayerRegion();
        sF2LayerRegion.putInteger(38, 12000);
        sF2LayerRegion.setSample(sF2SampleNewSimpleDrumSample);
        sF2Layer.getRegions().add(sF2LayerRegion);
        return sF2Layer;
    }

    public static SF2Layer new_gpiano(SF2Soundbank sF2Soundbank) {
        double[] dArr = new double[4096 * 8 * 2];
        double d2 = 8 * 25;
        double d3 = 0.2d;
        double dPow = Math.pow(0.001d / 0.2d, 0.06666666666666667d);
        double[] dArr2 = new double[30];
        for (int i2 = 0; i2 < 30; i2++) {
            dArr2[i2] = d3;
            d3 *= dPow;
        }
        dArr2[0] = dArr2[0] * 2.0d;
        dArr2[4] = dArr2[4] * 2.0d;
        dArr2[12] = dArr2[12] * 0.9d;
        dArr2[13] = dArr2[13] * 0.7d;
        for (int i3 = 14; i3 < 30; i3++) {
            int i4 = i3;
            dArr2[i4] = dArr2[i4] * 0.5d;
        }
        for (int i5 = 0; i5 < 30; i5++) {
            double d4 = 0.2d;
            double d5 = dArr2[i5];
            if (i5 > 10) {
                d4 = 5.0d;
                d5 *= 10.0d;
            }
            int i6 = 0;
            if (i5 > 5) {
                i6 = (i5 - 5) * 7;
            }
            complexGaussianDist(dArr, (d2 * (i5 + 1)) + i6, d4, d5);
        }
        SF2Layer sF2LayerNewLayer = newLayer(sF2Soundbank, "Grand Piano", newSimpleFFTSample(sF2Soundbank, "Grand Piano", dArr, d2, 200));
        SF2LayerRegion sF2LayerRegion = sF2LayerNewLayer.getRegions().get(0);
        sF2LayerRegion.putInteger(54, 1);
        sF2LayerRegion.putInteger(34, -7000);
        sF2LayerRegion.putInteger(38, 0);
        sF2LayerRegion.putInteger(36, 4000);
        sF2LayerRegion.putInteger(37, 1000);
        sF2LayerRegion.putInteger(26, -6000);
        sF2LayerRegion.putInteger(30, 12000);
        sF2LayerRegion.putInteger(11, -5500);
        sF2LayerRegion.putInteger(8, 18000);
        return sF2LayerNewLayer;
    }

    public static SF2Layer new_gpiano2(SF2Soundbank sF2Soundbank) {
        double[] dArr = new double[4096 * 8 * 2];
        double d2 = 8 * 25;
        double d3 = 0.2d;
        double dPow = Math.pow(0.001d / 0.2d, 0.05d);
        double[] dArr2 = new double[30];
        for (int i2 = 0; i2 < 30; i2++) {
            dArr2[i2] = d3;
            d3 *= dPow;
        }
        dArr2[0] = dArr2[0] * 1.0d;
        dArr2[4] = dArr2[4] * 2.0d;
        dArr2[12] = dArr2[12] * 0.9d;
        dArr2[13] = dArr2[13] * 0.7d;
        for (int i3 = 14; i3 < 30; i3++) {
            int i4 = i3;
            dArr2[i4] = dArr2[i4] * 0.5d;
        }
        for (int i5 = 0; i5 < 30; i5++) {
            double d4 = 0.2d;
            double d5 = dArr2[i5];
            if (i5 > 10) {
                d4 = 5.0d;
                d5 *= 10.0d;
            }
            int i6 = 0;
            if (i5 > 5) {
                i6 = (i5 - 5) * 7;
            }
            complexGaussianDist(dArr, (d2 * (i5 + 1)) + i6, d4, d5);
        }
        SF2Layer sF2LayerNewLayer = newLayer(sF2Soundbank, "Grand Piano", newSimpleFFTSample(sF2Soundbank, "Grand Piano", dArr, d2, 200));
        SF2LayerRegion sF2LayerRegion = sF2LayerNewLayer.getRegions().get(0);
        sF2LayerRegion.putInteger(54, 1);
        sF2LayerRegion.putInteger(34, -7000);
        sF2LayerRegion.putInteger(38, 0);
        sF2LayerRegion.putInteger(36, 4000);
        sF2LayerRegion.putInteger(37, 1000);
        sF2LayerRegion.putInteger(26, -6000);
        sF2LayerRegion.putInteger(30, 12000);
        sF2LayerRegion.putInteger(11, -5500);
        sF2LayerRegion.putInteger(8, 18000);
        return sF2LayerNewLayer;
    }

    public static SF2Layer new_piano_hammer(SF2Soundbank sF2Soundbank) {
        int i2 = 4096 * 2;
        double[] dArr = new double[2 * i2];
        Random random = new Random(3049912L);
        for (int i3 = 0; i3 < dArr.length; i3 += 2) {
            dArr[i3] = 2.0d * (random.nextDouble() - 0.5d);
        }
        fft(dArr);
        for (int i4 = i2 / 2; i4 < dArr.length; i4++) {
            dArr[i4] = 0.0d;
        }
        for (int i5 = 0; i5 < 2048 * 2; i5++) {
            int i6 = i5;
            dArr[i6] = dArr[i6] * Math.exp((-Math.abs((i5 - 37) / 2)) * 0.05d);
        }
        randomPhase(dArr, new Random(3049912L));
        ifft(dArr);
        normalize(dArr, 0.6d);
        double[] dArrRealPart = realPart(dArr);
        double d2 = 1.0d;
        for (int i7 = 0; i7 < dArrRealPart.length; i7++) {
            int i8 = i7;
            dArrRealPart[i8] = dArrRealPart[i8] * d2;
            d2 *= 0.9997d;
        }
        fadeUp(dArrRealPart, 80);
        SF2Sample sF2SampleNewSimpleDrumSample = newSimpleDrumSample(sF2Soundbank, "Piano Hammer", dArrRealPart);
        SF2Layer sF2Layer = new SF2Layer(sF2Soundbank);
        sF2Layer.setName("Piano Hammer");
        sF2Layer.setGlobalZone(new SF2GlobalRegion());
        sF2Soundbank.addResource(sF2Layer);
        SF2LayerRegion sF2LayerRegion = new SF2LayerRegion();
        sF2LayerRegion.putInteger(38, 12000);
        sF2LayerRegion.setSample(sF2SampleNewSimpleDrumSample);
        sF2Layer.getRegions().add(sF2LayerRegion);
        return sF2Layer;
    }

    public static SF2Layer new_piano1(SF2Soundbank sF2Soundbank) {
        double[] dArr = new double[4096 * 8 * 2];
        double d2 = 8 * 25;
        double d3 = 0.2d;
        double dPow = Math.pow(1.0E-4d / 0.2d, 0.025d);
        double[] dArr2 = new double[30];
        for (int i2 = 0; i2 < 30; i2++) {
            dArr2[i2] = d3;
            d3 *= dPow;
        }
        dArr2[0] = dArr2[0] * 5.0d;
        dArr2[2] = dArr2[2] * 0.1d;
        dArr2[7] = dArr2[7] * 5.0d;
        for (int i3 = 0; i3 < 30; i3++) {
            double d4 = 0.2d;
            double d5 = dArr2[i3];
            if (i3 > 12) {
                d4 = 5.0d;
                d5 *= 10.0d;
            }
            int i4 = 0;
            if (i3 > 5) {
                i4 = (i3 - 5) * 7;
            }
            complexGaussianDist(dArr, (d2 * (i3 + 1)) + i4, d4, d5);
        }
        complexGaussianDist(dArr, d2 * 15.5d, 1.0d, 0.1d);
        complexGaussianDist(dArr, d2 * 17.5d, 1.0d, 0.01d);
        SF2Layer sF2LayerNewLayer = newLayer(sF2Soundbank, "EPiano", newSimpleFFTSample(sF2Soundbank, "EPiano", dArr, d2, 200));
        SF2LayerRegion sF2LayerRegion = sF2LayerNewLayer.getRegions().get(0);
        sF2LayerRegion.putInteger(54, 1);
        sF2LayerRegion.putInteger(34, -12000);
        sF2LayerRegion.putInteger(38, 0);
        sF2LayerRegion.putInteger(36, 4000);
        sF2LayerRegion.putInteger(37, 1000);
        sF2LayerRegion.putInteger(26, -1200);
        sF2LayerRegion.putInteger(30, 12000);
        sF2LayerRegion.putInteger(11, -5500);
        sF2LayerRegion.putInteger(8, 16000);
        return sF2LayerNewLayer;
    }

    public static SF2Layer new_epiano1(SF2Soundbank sF2Soundbank) {
        Random random = new Random(302030201L);
        double[] dArr = new double[4096 * 8 * 2];
        double d2 = 8 * 25;
        double d3 = 0.2d;
        double dPow = Math.pow(1.0E-4d / 0.2d, 0.025d);
        for (int i2 = 0; i2 < 40; i2++) {
            complexGaussianDist(dArr, d2 * (i2 + 1) * (1.0d + (((random.nextDouble() * 2.0d) - 1.0d) * 1.0E-4d)), 0.05d + ((0.05d - 0.05d) * (i2 / 40.0d)), d3);
            d3 *= dPow;
        }
        SF2Layer sF2LayerNewLayer = newLayer(sF2Soundbank, "EPiano", newSimpleFFTSample(sF2Soundbank, "EPiano", dArr, d2));
        SF2LayerRegion sF2LayerRegion = sF2LayerNewLayer.getRegions().get(0);
        sF2LayerRegion.putInteger(54, 1);
        sF2LayerRegion.putInteger(34, -12000);
        sF2LayerRegion.putInteger(38, 0);
        sF2LayerRegion.putInteger(36, 4000);
        sF2LayerRegion.putInteger(37, 1000);
        sF2LayerRegion.putInteger(26, 1200);
        sF2LayerRegion.putInteger(30, 12000);
        sF2LayerRegion.putInteger(11, -9000);
        sF2LayerRegion.putInteger(8, 16000);
        return sF2LayerNewLayer;
    }

    public static SF2Layer new_epiano2(SF2Soundbank sF2Soundbank) {
        Random random = new Random(302030201L);
        double[] dArr = new double[4096 * 8 * 2];
        double d2 = 8 * 25;
        double d3 = 0.2d;
        double dPow = Math.pow(1.0E-5d / 0.2d, 0.025d);
        for (int i2 = 0; i2 < 40; i2++) {
            complexGaussianDist(dArr, d2 * (i2 + 1) * (1.0d + (((random.nextDouble() * 2.0d) - 1.0d) * 1.0E-4d)), 0.01d + ((0.05d - 0.01d) * (i2 / 40.0d)), d3);
            d3 *= dPow;
        }
        SF2Layer sF2LayerNewLayer = newLayer(sF2Soundbank, "EPiano", newSimpleFFTSample(sF2Soundbank, "EPiano", dArr, d2));
        SF2LayerRegion sF2LayerRegion = sF2LayerNewLayer.getRegions().get(0);
        sF2LayerRegion.putInteger(54, 1);
        sF2LayerRegion.putInteger(34, -12000);
        sF2LayerRegion.putInteger(38, 0);
        sF2LayerRegion.putInteger(36, 8000);
        sF2LayerRegion.putInteger(37, 1000);
        sF2LayerRegion.putInteger(26, 2400);
        sF2LayerRegion.putInteger(30, 12000);
        sF2LayerRegion.putInteger(11, -9000);
        sF2LayerRegion.putInteger(8, 16000);
        sF2LayerRegion.putInteger(48, -100);
        return sF2LayerNewLayer;
    }

    public static SF2Layer new_bass1(SF2Soundbank sF2Soundbank) {
        double[] dArr = new double[4096 * 8 * 2];
        double d2 = 8 * 25;
        double d3 = 0.2d;
        double dPow = Math.pow(0.02d / 0.2d, 0.04d);
        double[] dArr2 = new double[25];
        for (int i2 = 0; i2 < 25; i2++) {
            dArr2[i2] = d3;
            d3 *= dPow;
        }
        dArr2[0] = dArr2[0] * 8.0d;
        dArr2[1] = dArr2[1] * 4.0d;
        dArr2[3] = dArr2[3] * 8.0d;
        dArr2[5] = dArr2[5] * 8.0d;
        for (int i3 = 0; i3 < 25; i3++) {
            complexGaussianDist(dArr, d2 * (i3 + 1), 0.05d + ((0.05d - 0.05d) * (i3 / 40.0d)), dArr2[i3]);
        }
        SF2Layer sF2LayerNewLayer = newLayer(sF2Soundbank, "Bass", newSimpleFFTSample(sF2Soundbank, "Bass", dArr, d2));
        SF2LayerRegion sF2LayerRegion = sF2LayerNewLayer.getRegions().get(0);
        sF2LayerRegion.putInteger(54, 1);
        sF2LayerRegion.putInteger(34, -12000);
        sF2LayerRegion.putInteger(38, 0);
        sF2LayerRegion.putInteger(36, 4000);
        sF2LayerRegion.putInteger(37, 1000);
        sF2LayerRegion.putInteger(26, -3000);
        sF2LayerRegion.putInteger(30, 12000);
        sF2LayerRegion.putInteger(11, -5000);
        sF2LayerRegion.putInteger(8, 11000);
        sF2LayerRegion.putInteger(48, -100);
        return sF2LayerNewLayer;
    }

    public static SF2Layer new_synthbass(SF2Soundbank sF2Soundbank) {
        double[] dArr = new double[4096 * 8 * 2];
        double d2 = 8 * 25;
        double d3 = 0.2d;
        double dPow = Math.pow(0.02d / 0.2d, 0.04d);
        double[] dArr2 = new double[25];
        for (int i2 = 0; i2 < 25; i2++) {
            dArr2[i2] = d3;
            d3 *= dPow;
        }
        dArr2[0] = dArr2[0] * 16.0d;
        dArr2[1] = dArr2[1] * 4.0d;
        dArr2[3] = dArr2[3] * 16.0d;
        dArr2[5] = dArr2[5] * 8.0d;
        for (int i3 = 0; i3 < 25; i3++) {
            complexGaussianDist(dArr, d2 * (i3 + 1), 0.05d + ((0.05d - 0.05d) * (i3 / 40.0d)), dArr2[i3]);
        }
        SF2Layer sF2LayerNewLayer = newLayer(sF2Soundbank, "Bass", newSimpleFFTSample(sF2Soundbank, "Bass", dArr, d2));
        SF2LayerRegion sF2LayerRegion = sF2LayerNewLayer.getRegions().get(0);
        sF2LayerRegion.putInteger(54, 1);
        sF2LayerRegion.putInteger(34, -12000);
        sF2LayerRegion.putInteger(38, 0);
        sF2LayerRegion.putInteger(36, 4000);
        sF2LayerRegion.putInteger(37, 1000);
        sF2LayerRegion.putInteger(26, -3000);
        sF2LayerRegion.putInteger(30, 12000);
        sF2LayerRegion.putInteger(11, -3000);
        sF2LayerRegion.putInteger(9, 100);
        sF2LayerRegion.putInteger(8, 8000);
        sF2LayerRegion.putInteger(48, -100);
        return sF2LayerNewLayer;
    }

    public static SF2Layer new_bass2(SF2Soundbank sF2Soundbank) {
        double[] dArr = new double[4096 * 8 * 2];
        double d2 = 8 * 25;
        double d3 = 0.2d;
        double dPow = Math.pow(0.002d / 0.2d, 0.04d);
        double[] dArr2 = new double[25];
        for (int i2 = 0; i2 < 25; i2++) {
            dArr2[i2] = d3;
            d3 *= dPow;
        }
        dArr2[0] = dArr2[0] * 8.0d;
        dArr2[1] = dArr2[1] * 4.0d;
        dArr2[3] = dArr2[3] * 8.0d;
        dArr2[5] = dArr2[5] * 8.0d;
        for (int i3 = 0; i3 < 25; i3++) {
            complexGaussianDist(dArr, d2 * (i3 + 1), 0.05d + ((0.05d - 0.05d) * (i3 / 40.0d)), dArr2[i3]);
        }
        SF2Layer sF2LayerNewLayer = newLayer(sF2Soundbank, "Bass2", newSimpleFFTSample(sF2Soundbank, "Bass2", dArr, d2));
        SF2LayerRegion sF2LayerRegion = sF2LayerNewLayer.getRegions().get(0);
        sF2LayerRegion.putInteger(54, 1);
        sF2LayerRegion.putInteger(34, -8000);
        sF2LayerRegion.putInteger(38, 0);
        sF2LayerRegion.putInteger(36, 4000);
        sF2LayerRegion.putInteger(37, 1000);
        sF2LayerRegion.putInteger(26, -6000);
        sF2LayerRegion.putInteger(30, 12000);
        sF2LayerRegion.putInteger(8, TFTP.DEFAULT_TIMEOUT);
        sF2LayerRegion.putInteger(48, -100);
        return sF2LayerNewLayer;
    }

    public static SF2Layer new_solostring(SF2Soundbank sF2Soundbank) {
        double[] dArr = new double[4096 * 8 * 2];
        double d2 = 8 * 25;
        double[] dArr2 = new double[18];
        double d3 = 0.2d;
        double dPow = Math.pow(0.01d / 0.2d, 0.025d);
        for (int i2 = 0; i2 < dArr2.length; i2++) {
            d3 *= dPow;
            dArr2[i2] = d3;
        }
        dArr2[0] = dArr2[0] * 5.0d;
        dArr2[1] = dArr2[1] * 5.0d;
        dArr2[2] = dArr2[2] * 5.0d;
        dArr2[3] = dArr2[3] * 4.0d;
        dArr2[4] = dArr2[4] * 4.0d;
        dArr2[5] = dArr2[5] * 3.0d;
        dArr2[6] = dArr2[6] * 3.0d;
        dArr2[7] = dArr2[7] * 2.0d;
        for (int i3 = 0; i3 < dArr2.length; i3++) {
            complexGaussianDist(dArr, d2 * (i3 + 1), 2.0d + ((2.0d - 2.0d) * (i3 / 40.0d)), d3);
        }
        SF2Layer sF2LayerNewLayer = newLayer(sF2Soundbank, "Strings", newSimpleFFTSample(sF2Soundbank, "Strings", dArr, d2));
        SF2LayerRegion sF2LayerRegion = sF2LayerNewLayer.getRegions().get(0);
        sF2LayerRegion.putInteger(54, 1);
        sF2LayerRegion.putInteger(34, -5000);
        sF2LayerRegion.putInteger(38, 1000);
        sF2LayerRegion.putInteger(36, 4000);
        sF2LayerRegion.putInteger(37, -100);
        sF2LayerRegion.putInteger(8, 9500);
        sF2LayerRegion.putInteger(24, -1000);
        sF2LayerRegion.putInteger(6, 15);
        return sF2LayerNewLayer;
    }

    public static SF2Layer new_orchhit(SF2Soundbank sF2Soundbank) {
        double[] dArr = new double[4096 * 8 * 2];
        double d2 = 8 * 25;
        double d3 = 0.2d;
        double dPow = Math.pow(0.001d / 0.2d, 0.025d);
        for (int i2 = 0; i2 < 40; i2++) {
            complexGaussianDist(dArr, d2 * (i2 + 1), 2.0d + ((80.0d - 2.0d) * (i2 / 40.0d)), d3);
            d3 *= dPow;
        }
        complexGaussianDist(dArr, d2 * 4.0d, 300.0d, 1.0d);
        SF2Layer sF2LayerNewLayer = newLayer(sF2Soundbank, "Och Strings", newSimpleFFTSample(sF2Soundbank, "Och Strings", dArr, d2));
        SF2LayerRegion sF2LayerRegion = sF2LayerNewLayer.getRegions().get(0);
        sF2LayerRegion.putInteger(54, 1);
        sF2LayerRegion.putInteger(34, -5000);
        sF2LayerRegion.putInteger(38, 200);
        sF2LayerRegion.putInteger(36, 200);
        sF2LayerRegion.putInteger(37, 1000);
        sF2LayerRegion.putInteger(8, 9500);
        return sF2LayerNewLayer;
    }

    public static SF2Layer new_string2(SF2Soundbank sF2Soundbank) {
        double[] dArr = new double[4096 * 8 * 2];
        double d2 = 8 * 25;
        double d3 = 0.2d;
        double dPow = Math.pow(0.001d / 0.2d, 0.025d);
        for (int i2 = 0; i2 < 40; i2++) {
            complexGaussianDist(dArr, d2 * (i2 + 1), 2.0d + ((80.0d - 2.0d) * (i2 / 40.0d)), d3);
            d3 *= dPow;
        }
        SF2Layer sF2LayerNewLayer = newLayer(sF2Soundbank, "Strings", newSimpleFFTSample(sF2Soundbank, "Strings", dArr, d2));
        SF2LayerRegion sF2LayerRegion = sF2LayerNewLayer.getRegions().get(0);
        sF2LayerRegion.putInteger(54, 1);
        sF2LayerRegion.putInteger(34, -5000);
        sF2LayerRegion.putInteger(38, 1000);
        sF2LayerRegion.putInteger(36, 4000);
        sF2LayerRegion.putInteger(37, -100);
        sF2LayerRegion.putInteger(8, 9500);
        return sF2LayerNewLayer;
    }

    public static SF2Layer new_choir(SF2Soundbank sF2Soundbank) {
        double[] dArr = new double[4096 * 8 * 2];
        double d2 = 8 * 25;
        double d3 = 0.2d;
        double dPow = Math.pow(0.001d / 0.2d, 0.025d);
        double[] dArr2 = new double[40];
        for (int i2 = 0; i2 < dArr2.length; i2++) {
            d3 *= dPow;
            dArr2[i2] = d3;
        }
        dArr2[5] = dArr2[5] * 0.1d;
        dArr2[6] = dArr2[6] * 0.01d;
        dArr2[7] = dArr2[7] * 0.1d;
        dArr2[8] = dArr2[8] * 0.1d;
        for (int i3 = 0; i3 < dArr2.length; i3++) {
            complexGaussianDist(dArr, d2 * (i3 + 1), 2.0d + ((80.0d - 2.0d) * (i3 / 40.0d)), dArr2[i3]);
        }
        SF2Layer sF2LayerNewLayer = newLayer(sF2Soundbank, "Strings", newSimpleFFTSample(sF2Soundbank, "Strings", dArr, d2));
        SF2LayerRegion sF2LayerRegion = sF2LayerNewLayer.getRegions().get(0);
        sF2LayerRegion.putInteger(54, 1);
        sF2LayerRegion.putInteger(34, -5000);
        sF2LayerRegion.putInteger(38, 1000);
        sF2LayerRegion.putInteger(36, 4000);
        sF2LayerRegion.putInteger(37, -100);
        sF2LayerRegion.putInteger(8, 9500);
        return sF2LayerNewLayer;
    }

    public static SF2Layer new_organ(SF2Soundbank sF2Soundbank) {
        Random random = new Random(102030201L);
        double[] dArr = new double[4096 * 1 * 2];
        double d2 = 1 * 15;
        double d3 = 0.2d;
        double dPow = Math.pow(0.001d / 0.2d, 0.025d);
        for (int i2 = 0; i2 < 12; i2++) {
            complexGaussianDist(dArr, d2 * (i2 + 1), 0.01d + ((0.01d - 0.01d) * (i2 / 40.0d)), d3 * (0.5d + (3.0d * random.nextDouble())));
            d3 *= dPow;
        }
        SF2Layer sF2LayerNewLayer = newLayer(sF2Soundbank, "Organ", newSimpleFFTSample(sF2Soundbank, "Organ", dArr, d2));
        SF2LayerRegion sF2LayerRegion = sF2LayerNewLayer.getRegions().get(0);
        sF2LayerRegion.putInteger(54, 1);
        sF2LayerRegion.putInteger(34, -6000);
        sF2LayerRegion.putInteger(38, -1000);
        sF2LayerRegion.putInteger(36, 4000);
        sF2LayerRegion.putInteger(37, -100);
        sF2LayerRegion.putInteger(8, 9500);
        return sF2LayerNewLayer;
    }

    public static SF2Layer new_ch_organ(SF2Soundbank sF2Soundbank) {
        double[] dArr = new double[4096 * 1 * 2];
        double d2 = 1 * 15;
        double d3 = 0.2d;
        double dPow = Math.pow(0.001d / 0.2d, 0.016666666666666666d);
        double[] dArr2 = new double[60];
        for (int i2 = 0; i2 < dArr2.length; i2++) {
            d3 *= dPow;
            dArr2[i2] = d3;
        }
        dArr2[0] = dArr2[0] * 5.0d;
        dArr2[1] = dArr2[1] * 2.0d;
        dArr2[2] = 0.0d;
        dArr2[4] = 0.0d;
        dArr2[5] = 0.0d;
        dArr2[7] = dArr2[7] * 7.0d;
        dArr2[9] = 0.0d;
        dArr2[10] = 0.0d;
        dArr2[12] = 0.0d;
        dArr2[15] = dArr2[15] * 7.0d;
        dArr2[18] = 0.0d;
        dArr2[20] = 0.0d;
        dArr2[24] = 0.0d;
        dArr2[27] = dArr2[27] * 5.0d;
        dArr2[29] = 0.0d;
        dArr2[30] = 0.0d;
        dArr2[33] = 0.0d;
        dArr2[36] = dArr2[36] * 4.0d;
        dArr2[37] = 0.0d;
        dArr2[39] = 0.0d;
        dArr2[42] = 0.0d;
        dArr2[43] = 0.0d;
        dArr2[47] = 0.0d;
        dArr2[50] = dArr2[50] * 4.0d;
        dArr2[52] = 0.0d;
        dArr2[55] = 0.0d;
        dArr2[57] = 0.0d;
        dArr2[10] = dArr2[10] * 0.1d;
        dArr2[11] = dArr2[11] * 0.1d;
        dArr2[12] = dArr2[12] * 0.1d;
        dArr2[13] = dArr2[13] * 0.1d;
        dArr2[17] = dArr2[17] * 0.1d;
        dArr2[18] = dArr2[18] * 0.1d;
        dArr2[19] = dArr2[19] * 0.1d;
        dArr2[20] = dArr2[20] * 0.1d;
        for (int i3 = 0; i3 < 60; i3++) {
            complexGaussianDist(dArr, d2 * (i3 + 1), 0.01d + ((0.01d - 0.01d) * (i3 / 40.0d)), dArr2[i3]);
            d3 *= dPow;
        }
        SF2Layer sF2LayerNewLayer = newLayer(sF2Soundbank, "Organ", newSimpleFFTSample(sF2Soundbank, "Organ", dArr, d2));
        SF2LayerRegion sF2LayerRegion = sF2LayerNewLayer.getRegions().get(0);
        sF2LayerRegion.putInteger(54, 1);
        sF2LayerRegion.putInteger(34, -10000);
        sF2LayerRegion.putInteger(38, -1000);
        return sF2LayerNewLayer;
    }

    public static SF2Layer new_flute(SF2Soundbank sF2Soundbank) {
        double[] dArr = new double[4096 * 8 * 2];
        double d2 = 8 * 15;
        complexGaussianDist(dArr, d2 * 1.0d, 0.001d, 0.5d);
        complexGaussianDist(dArr, d2 * 2.0d, 0.001d, 0.5d);
        complexGaussianDist(dArr, d2 * 3.0d, 0.001d, 0.5d);
        complexGaussianDist(dArr, d2 * 4.0d, 0.01d, 0.5d);
        complexGaussianDist(dArr, d2 * 4.0d, 100.0d, 120.0d);
        complexGaussianDist(dArr, d2 * 6.0d, 100.0d, 40.0d);
        complexGaussianDist(dArr, d2 * 8.0d, 100.0d, 80.0d);
        complexGaussianDist(dArr, d2 * 5.0d, 0.001d, 0.05d);
        complexGaussianDist(dArr, d2 * 6.0d, 0.001d, 0.06d);
        complexGaussianDist(dArr, d2 * 7.0d, 0.001d, 0.04d);
        complexGaussianDist(dArr, d2 * 8.0d, 0.005d, 0.06d);
        complexGaussianDist(dArr, d2 * 9.0d, 0.005d, 0.06d);
        complexGaussianDist(dArr, d2 * 10.0d, 0.01d, 0.1d);
        complexGaussianDist(dArr, d2 * 11.0d, 0.08d, 0.7d);
        complexGaussianDist(dArr, d2 * 12.0d, 0.08d, 0.6d);
        complexGaussianDist(dArr, d2 * 13.0d, 0.08d, 0.6d);
        complexGaussianDist(dArr, d2 * 14.0d, 0.08d, 0.6d);
        complexGaussianDist(dArr, d2 * 15.0d, 0.08d, 0.5d);
        complexGaussianDist(dArr, d2 * 16.0d, 0.08d, 0.5d);
        complexGaussianDist(dArr, d2 * 17.0d, 0.08d, 0.2d);
        complexGaussianDist(dArr, d2 * 1.0d, 10.0d, 8.0d);
        complexGaussianDist(dArr, d2 * 2.0d, 10.0d, 8.0d);
        complexGaussianDist(dArr, d2 * 3.0d, 10.0d, 8.0d);
        complexGaussianDist(dArr, d2 * 4.0d, 10.0d, 8.0d);
        complexGaussianDist(dArr, d2 * 5.0d, 10.0d, 8.0d);
        complexGaussianDist(dArr, d2 * 6.0d, 20.0d, 9.0d);
        complexGaussianDist(dArr, d2 * 7.0d, 20.0d, 9.0d);
        complexGaussianDist(dArr, d2 * 8.0d, 20.0d, 9.0d);
        complexGaussianDist(dArr, d2 * 9.0d, 20.0d, 8.0d);
        complexGaussianDist(dArr, d2 * 10.0d, 30.0d, 8.0d);
        complexGaussianDist(dArr, d2 * 11.0d, 30.0d, 9.0d);
        complexGaussianDist(dArr, d2 * 12.0d, 30.0d, 9.0d);
        complexGaussianDist(dArr, d2 * 13.0d, 30.0d, 8.0d);
        complexGaussianDist(dArr, d2 * 14.0d, 30.0d, 8.0d);
        complexGaussianDist(dArr, d2 * 15.0d, 30.0d, 7.0d);
        complexGaussianDist(dArr, d2 * 16.0d, 30.0d, 7.0d);
        complexGaussianDist(dArr, d2 * 17.0d, 30.0d, 6.0d);
        SF2Layer sF2LayerNewLayer = newLayer(sF2Soundbank, "Flute", newSimpleFFTSample(sF2Soundbank, "Flute", dArr, d2));
        SF2LayerRegion sF2LayerRegion = sF2LayerNewLayer.getRegions().get(0);
        sF2LayerRegion.putInteger(54, 1);
        sF2LayerRegion.putInteger(34, -6000);
        sF2LayerRegion.putInteger(38, -1000);
        sF2LayerRegion.putInteger(36, 4000);
        sF2LayerRegion.putInteger(37, -100);
        sF2LayerRegion.putInteger(8, 9500);
        return sF2LayerNewLayer;
    }

    public static SF2Layer new_horn(SF2Soundbank sF2Soundbank) {
        double[] dArr = new double[4096 * 8 * 2];
        double d2 = 8 * 15;
        double d3 = 0.5d;
        double dPow = Math.pow(1.0E-11d / 0.5d, 0.025d);
        for (int i2 = 0; i2 < 40; i2++) {
            if (i2 == 0) {
                complexGaussianDist(dArr, d2 * (i2 + 1), 0.1d, d3 * 0.2d);
            } else {
                complexGaussianDist(dArr, d2 * (i2 + 1), 0.1d, d3);
            }
            d3 *= dPow;
        }
        complexGaussianDist(dArr, d2 * 2.0d, 100.0d, 1.0d);
        SF2Layer sF2LayerNewLayer = newLayer(sF2Soundbank, "Horn", newSimpleFFTSample(sF2Soundbank, "Horn", dArr, d2));
        SF2LayerRegion sF2LayerRegion = sF2LayerNewLayer.getRegions().get(0);
        sF2LayerRegion.putInteger(54, 1);
        sF2LayerRegion.putInteger(34, -6000);
        sF2LayerRegion.putInteger(38, -1000);
        sF2LayerRegion.putInteger(36, 4000);
        sF2LayerRegion.putInteger(37, -100);
        sF2LayerRegion.putInteger(26, -500);
        sF2LayerRegion.putInteger(30, 12000);
        sF2LayerRegion.putInteger(11, TFTP.DEFAULT_TIMEOUT);
        sF2LayerRegion.putInteger(8, 4500);
        return sF2LayerNewLayer;
    }

    public static SF2Layer new_trumpet(SF2Soundbank sF2Soundbank) {
        double[] dArr = new double[4096 * 8 * 2];
        double d2 = 8 * 15;
        double d3 = 0.5d;
        double dPow = Math.pow(1.0E-5d / 0.5d, 0.0125d);
        double[] dArr2 = new double[80];
        for (int i2 = 0; i2 < 80; i2++) {
            dArr2[i2] = d3;
            d3 *= dPow;
        }
        dArr2[0] = dArr2[0] * 0.05d;
        dArr2[1] = dArr2[1] * 0.2d;
        dArr2[2] = dArr2[2] * 0.5d;
        dArr2[3] = dArr2[3] * 0.85d;
        for (int i3 = 0; i3 < 80; i3++) {
            complexGaussianDist(dArr, d2 * (i3 + 1), 0.1d, dArr2[i3]);
        }
        complexGaussianDist(dArr, d2 * 5.0d, 300.0d, 3.0d);
        SF2Layer sF2LayerNewLayer = newLayer(sF2Soundbank, "Trumpet", newSimpleFFTSample(sF2Soundbank, "Trumpet", dArr, d2));
        SF2LayerRegion sF2LayerRegion = sF2LayerNewLayer.getRegions().get(0);
        sF2LayerRegion.putInteger(54, 1);
        sF2LayerRegion.putInteger(34, -10000);
        sF2LayerRegion.putInteger(38, 0);
        sF2LayerRegion.putInteger(36, 4000);
        sF2LayerRegion.putInteger(37, -100);
        sF2LayerRegion.putInteger(26, -4000);
        sF2LayerRegion.putInteger(30, -2500);
        sF2LayerRegion.putInteger(11, TFTP.DEFAULT_TIMEOUT);
        sF2LayerRegion.putInteger(8, 4500);
        sF2LayerRegion.putInteger(9, 10);
        return sF2LayerNewLayer;
    }

    public static SF2Layer new_brass_section(SF2Soundbank sF2Soundbank) {
        double[] dArr = new double[4096 * 8 * 2];
        double d2 = 8 * 15;
        double d3 = 0.5d;
        double dPow = Math.pow(0.005d / 0.5d, 0.03333333333333333d);
        double[] dArr2 = new double[30];
        for (int i2 = 0; i2 < 30; i2++) {
            dArr2[i2] = d3;
            d3 *= dPow;
        }
        dArr2[0] = dArr2[0] * 0.8d;
        dArr2[1] = dArr2[1] * 0.9d;
        double d4 = 5.0d;
        for (int i3 = 0; i3 < 30; i3++) {
            complexGaussianDist(dArr, d2 * (i3 + 1), 0.1d * d4, dArr2[i3] * d4);
            d4 += 6.0d;
        }
        complexGaussianDist(dArr, d2 * 6.0d, 300.0d, 2.0d);
        SF2Layer sF2LayerNewLayer = newLayer(sF2Soundbank, "Brass Section", newSimpleFFTSample(sF2Soundbank, "Brass Section", dArr, d2));
        SF2LayerRegion sF2LayerRegion = sF2LayerNewLayer.getRegions().get(0);
        sF2LayerRegion.putInteger(54, 1);
        sF2LayerRegion.putInteger(34, -9200);
        sF2LayerRegion.putInteger(38, -1000);
        sF2LayerRegion.putInteger(36, 4000);
        sF2LayerRegion.putInteger(37, -100);
        sF2LayerRegion.putInteger(26, -3000);
        sF2LayerRegion.putInteger(30, 12000);
        sF2LayerRegion.putInteger(11, TFTP.DEFAULT_TIMEOUT);
        sF2LayerRegion.putInteger(8, 4500);
        return sF2LayerNewLayer;
    }

    public static SF2Layer new_trombone(SF2Soundbank sF2Soundbank) {
        double[] dArr = new double[4096 * 8 * 2];
        double d2 = 8 * 15;
        double d3 = 0.5d;
        double dPow = Math.pow(0.001d / 0.5d, 0.0125d);
        double[] dArr2 = new double[80];
        for (int i2 = 0; i2 < 80; i2++) {
            dArr2[i2] = d3;
            d3 *= dPow;
        }
        dArr2[0] = dArr2[0] * 0.3d;
        dArr2[1] = dArr2[1] * 0.7d;
        for (int i3 = 0; i3 < 80; i3++) {
            complexGaussianDist(dArr, d2 * (i3 + 1), 0.1d, dArr2[i3]);
        }
        complexGaussianDist(dArr, d2 * 6.0d, 300.0d, 2.0d);
        SF2Layer sF2LayerNewLayer = newLayer(sF2Soundbank, "Trombone", newSimpleFFTSample(sF2Soundbank, "Trombone", dArr, d2));
        SF2LayerRegion sF2LayerRegion = sF2LayerNewLayer.getRegions().get(0);
        sF2LayerRegion.putInteger(54, 1);
        sF2LayerRegion.putInteger(34, -8000);
        sF2LayerRegion.putInteger(38, -1000);
        sF2LayerRegion.putInteger(36, 4000);
        sF2LayerRegion.putInteger(37, -100);
        sF2LayerRegion.putInteger(26, -2000);
        sF2LayerRegion.putInteger(30, 12000);
        sF2LayerRegion.putInteger(11, TFTP.DEFAULT_TIMEOUT);
        sF2LayerRegion.putInteger(8, 4500);
        sF2LayerRegion.putInteger(9, 10);
        return sF2LayerNewLayer;
    }

    public static SF2Layer new_sax(SF2Soundbank sF2Soundbank) {
        double[] dArr = new double[4096 * 8 * 2];
        double d2 = 8 * 15;
        double d3 = 0.5d;
        double dPow = Math.pow(0.01d / 0.5d, 0.025d);
        for (int i2 = 0; i2 < 40; i2++) {
            if (i2 == 0 || i2 == 2) {
                complexGaussianDist(dArr, d2 * (i2 + 1), 0.1d, d3 * 4.0d);
            } else {
                complexGaussianDist(dArr, d2 * (i2 + 1), 0.1d, d3);
            }
            d3 *= dPow;
        }
        complexGaussianDist(dArr, d2 * 4.0d, 200.0d, 1.0d);
        SF2Layer sF2LayerNewLayer = newLayer(sF2Soundbank, "Sax", newSimpleFFTSample(sF2Soundbank, "Sax", dArr, d2));
        SF2LayerRegion sF2LayerRegion = sF2LayerNewLayer.getRegions().get(0);
        sF2LayerRegion.putInteger(54, 1);
        sF2LayerRegion.putInteger(34, -6000);
        sF2LayerRegion.putInteger(38, -1000);
        sF2LayerRegion.putInteger(36, 4000);
        sF2LayerRegion.putInteger(37, -100);
        sF2LayerRegion.putInteger(26, -3000);
        sF2LayerRegion.putInteger(30, 12000);
        sF2LayerRegion.putInteger(11, TFTP.DEFAULT_TIMEOUT);
        sF2LayerRegion.putInteger(8, 4500);
        return sF2LayerNewLayer;
    }

    public static SF2Layer new_oboe(SF2Soundbank sF2Soundbank) {
        double[] dArr = new double[4096 * 8 * 2];
        double d2 = 8 * 15;
        complexGaussianDist(dArr, d2 * 5.0d, 100.0d, 80.0d);
        complexGaussianDist(dArr, d2 * 1.0d, 0.01d, 0.53d);
        complexGaussianDist(dArr, d2 * 2.0d, 0.01d, 0.51d);
        complexGaussianDist(dArr, d2 * 3.0d, 0.01d, 0.48d);
        complexGaussianDist(dArr, d2 * 4.0d, 0.01d, 0.49d);
        complexGaussianDist(dArr, d2 * 5.0d, 0.01d, 5.0d);
        complexGaussianDist(dArr, d2 * 6.0d, 0.01d, 0.51d);
        complexGaussianDist(dArr, d2 * 7.0d, 0.01d, 0.5d);
        complexGaussianDist(dArr, d2 * 8.0d, 0.01d, 0.59d);
        complexGaussianDist(dArr, d2 * 9.0d, 0.01d, 0.61d);
        complexGaussianDist(dArr, d2 * 10.0d, 0.01d, 0.52d);
        complexGaussianDist(dArr, d2 * 11.0d, 0.01d, 0.49d);
        complexGaussianDist(dArr, d2 * 12.0d, 0.01d, 0.51d);
        complexGaussianDist(dArr, d2 * 13.0d, 0.01d, 0.48d);
        complexGaussianDist(dArr, d2 * 14.0d, 0.01d, 0.51d);
        complexGaussianDist(dArr, d2 * 15.0d, 0.01d, 0.46d);
        complexGaussianDist(dArr, d2 * 16.0d, 0.01d, 0.35d);
        complexGaussianDist(dArr, d2 * 17.0d, 0.01d, 0.2d);
        complexGaussianDist(dArr, d2 * 18.0d, 0.01d, 0.1d);
        complexGaussianDist(dArr, d2 * 19.0d, 0.01d, 0.5d);
        complexGaussianDist(dArr, d2 * 20.0d, 0.01d, 0.1d);
        SF2Layer sF2LayerNewLayer = newLayer(sF2Soundbank, "Oboe", newSimpleFFTSample(sF2Soundbank, "Oboe", dArr, d2));
        SF2LayerRegion sF2LayerRegion = sF2LayerNewLayer.getRegions().get(0);
        sF2LayerRegion.putInteger(54, 1);
        sF2LayerRegion.putInteger(34, -6000);
        sF2LayerRegion.putInteger(38, -1000);
        sF2LayerRegion.putInteger(36, 4000);
        sF2LayerRegion.putInteger(37, -100);
        sF2LayerRegion.putInteger(8, 9500);
        return sF2LayerNewLayer;
    }

    public static SF2Layer new_bassoon(SF2Soundbank sF2Soundbank) {
        double[] dArr = new double[4096 * 8 * 2];
        double d2 = 8 * 15;
        complexGaussianDist(dArr, d2 * 2.0d, 100.0d, 40.0d);
        complexGaussianDist(dArr, d2 * 4.0d, 100.0d, 20.0d);
        complexGaussianDist(dArr, d2 * 1.0d, 0.01d, 0.53d);
        complexGaussianDist(dArr, d2 * 2.0d, 0.01d, 5.0d);
        complexGaussianDist(dArr, d2 * 3.0d, 0.01d, 0.51d);
        complexGaussianDist(dArr, d2 * 4.0d, 0.01d, 0.48d);
        complexGaussianDist(dArr, d2 * 5.0d, 0.01d, 1.49d);
        complexGaussianDist(dArr, d2 * 6.0d, 0.01d, 0.51d);
        complexGaussianDist(dArr, d2 * 7.0d, 0.01d, 0.5d);
        complexGaussianDist(dArr, d2 * 8.0d, 0.01d, 0.59d);
        complexGaussianDist(dArr, d2 * 9.0d, 0.01d, 0.61d);
        complexGaussianDist(dArr, d2 * 10.0d, 0.01d, 0.52d);
        complexGaussianDist(dArr, d2 * 11.0d, 0.01d, 0.49d);
        complexGaussianDist(dArr, d2 * 12.0d, 0.01d, 0.51d);
        complexGaussianDist(dArr, d2 * 13.0d, 0.01d, 0.48d);
        complexGaussianDist(dArr, d2 * 14.0d, 0.01d, 0.51d);
        complexGaussianDist(dArr, d2 * 15.0d, 0.01d, 0.46d);
        complexGaussianDist(dArr, d2 * 16.0d, 0.01d, 0.35d);
        complexGaussianDist(dArr, d2 * 17.0d, 0.01d, 0.2d);
        complexGaussianDist(dArr, d2 * 18.0d, 0.01d, 0.1d);
        complexGaussianDist(dArr, d2 * 19.0d, 0.01d, 0.5d);
        complexGaussianDist(dArr, d2 * 20.0d, 0.01d, 0.1d);
        SF2Layer sF2LayerNewLayer = newLayer(sF2Soundbank, "Flute", newSimpleFFTSample(sF2Soundbank, "Flute", dArr, d2));
        SF2LayerRegion sF2LayerRegion = sF2LayerNewLayer.getRegions().get(0);
        sF2LayerRegion.putInteger(54, 1);
        sF2LayerRegion.putInteger(34, -6000);
        sF2LayerRegion.putInteger(38, -1000);
        sF2LayerRegion.putInteger(36, 4000);
        sF2LayerRegion.putInteger(37, -100);
        sF2LayerRegion.putInteger(8, 9500);
        return sF2LayerNewLayer;
    }

    public static SF2Layer new_clarinet(SF2Soundbank sF2Soundbank) {
        double[] dArr = new double[4096 * 8 * 2];
        double d2 = 8 * 15;
        complexGaussianDist(dArr, d2 * 1.0d, 0.001d, 0.5d);
        complexGaussianDist(dArr, d2 * 2.0d, 0.001d, 0.02d);
        complexGaussianDist(dArr, d2 * 3.0d, 0.001d, 0.2d);
        complexGaussianDist(dArr, d2 * 4.0d, 0.01d, 0.1d);
        complexGaussianDist(dArr, d2 * 4.0d, 100.0d, 60.0d);
        complexGaussianDist(dArr, d2 * 6.0d, 100.0d, 20.0d);
        complexGaussianDist(dArr, d2 * 8.0d, 100.0d, 20.0d);
        complexGaussianDist(dArr, d2 * 5.0d, 0.001d, 0.1d);
        complexGaussianDist(dArr, d2 * 6.0d, 0.001d, 0.09d);
        complexGaussianDist(dArr, d2 * 7.0d, 0.001d, 0.02d);
        complexGaussianDist(dArr, d2 * 8.0d, 0.005d, 0.16d);
        complexGaussianDist(dArr, d2 * 9.0d, 0.005d, 0.96d);
        complexGaussianDist(dArr, d2 * 10.0d, 0.01d, 0.9d);
        complexGaussianDist(dArr, d2 * 11.0d, 0.08d, 1.2d);
        complexGaussianDist(dArr, d2 * 12.0d, 0.08d, 1.8d);
        complexGaussianDist(dArr, d2 * 13.0d, 0.08d, 1.6d);
        complexGaussianDist(dArr, d2 * 14.0d, 0.08d, 1.2d);
        complexGaussianDist(dArr, d2 * 15.0d, 0.08d, 0.9d);
        complexGaussianDist(dArr, d2 * 16.0d, 0.08d, 0.5d);
        complexGaussianDist(dArr, d2 * 17.0d, 0.08d, 0.2d);
        complexGaussianDist(dArr, d2 * 1.0d, 10.0d, 8.0d);
        complexGaussianDist(dArr, d2 * 2.0d, 10.0d, 8.0d);
        complexGaussianDist(dArr, d2 * 3.0d, 10.0d, 8.0d);
        complexGaussianDist(dArr, d2 * 4.0d, 10.0d, 8.0d);
        complexGaussianDist(dArr, d2 * 5.0d, 10.0d, 8.0d);
        complexGaussianDist(dArr, d2 * 6.0d, 20.0d, 9.0d);
        complexGaussianDist(dArr, d2 * 7.0d, 20.0d, 9.0d);
        complexGaussianDist(dArr, d2 * 8.0d, 20.0d, 9.0d);
        complexGaussianDist(dArr, d2 * 9.0d, 20.0d, 8.0d);
        complexGaussianDist(dArr, d2 * 10.0d, 30.0d, 8.0d);
        complexGaussianDist(dArr, d2 * 11.0d, 30.0d, 9.0d);
        complexGaussianDist(dArr, d2 * 12.0d, 30.0d, 9.0d);
        complexGaussianDist(dArr, d2 * 13.0d, 30.0d, 8.0d);
        complexGaussianDist(dArr, d2 * 14.0d, 30.0d, 8.0d);
        complexGaussianDist(dArr, d2 * 15.0d, 30.0d, 7.0d);
        complexGaussianDist(dArr, d2 * 16.0d, 30.0d, 7.0d);
        complexGaussianDist(dArr, d2 * 17.0d, 30.0d, 6.0d);
        SF2Layer sF2LayerNewLayer = newLayer(sF2Soundbank, "Clarinet", newSimpleFFTSample(sF2Soundbank, "Clarinet", dArr, d2));
        SF2LayerRegion sF2LayerRegion = sF2LayerNewLayer.getRegions().get(0);
        sF2LayerRegion.putInteger(54, 1);
        sF2LayerRegion.putInteger(34, -6000);
        sF2LayerRegion.putInteger(38, -1000);
        sF2LayerRegion.putInteger(36, 4000);
        sF2LayerRegion.putInteger(37, -100);
        sF2LayerRegion.putInteger(8, 9500);
        return sF2LayerNewLayer;
    }

    public static SF2Layer new_timpani(SF2Soundbank sF2Soundbank) {
        double[] dArr = new double[2 * 32768];
        complexGaussianDist(dArr, 48.0d * 2.0d, 0.2d, 1.0d);
        complexGaussianDist(dArr, 48.0d * 3.0d, 0.2d, 0.7d);
        complexGaussianDist(dArr, 48.0d * 5.0d, 10.0d, 1.0d);
        complexGaussianDist(dArr, 48.0d * 6.0d, 9.0d, 1.0d);
        complexGaussianDist(dArr, 48.0d * 8.0d, 15.0d, 1.0d);
        complexGaussianDist(dArr, 48.0d * 9.0d, 18.0d, 0.8d);
        complexGaussianDist(dArr, 48.0d * 11.0d, 21.0d, 0.5d);
        complexGaussianDist(dArr, 48.0d * 13.0d, 28.0d, 0.3d);
        complexGaussianDist(dArr, 48.0d * 14.0d, 22.0d, 0.1d);
        randomPhase(dArr, new Random(3049912L));
        ifft(dArr);
        normalize(dArr, 0.5d);
        double[] dArrRealPart = realPart(dArr);
        double length = dArrRealPart.length;
        for (int i2 = 0; i2 < dArrRealPart.length; i2++) {
            double d2 = 1.0d - (i2 / length);
            int i3 = i2;
            dArrRealPart[i3] = dArrRealPart[i3] * d2 * d2;
        }
        fadeUp(dArrRealPart, 40);
        double[] dArr2 = new double[2 * 16384];
        Random random = new Random(3049912L);
        for (int i4 = 0; i4 < dArr2.length; i4 += 2) {
            dArr2[i4] = 2.0d * (random.nextDouble() - 0.5d) * 0.1d;
        }
        fft(dArr2);
        for (int i5 = 16384 / 2; i5 < dArr2.length; i5++) {
            dArr2[i5] = 0.0d;
        }
        for (int i6 = 4096; i6 < 8192; i6++) {
            dArr2[i6] = 1.0d - ((i6 - 4096) / 4096.0d);
        }
        for (int i7 = 0; i7 < 300; i7++) {
            double d3 = 1.0d - (i7 / 300.0d);
            int i8 = i7;
            dArr2[i8] = dArr2[i8] * (1.0d + (20.0d * d3 * d3));
        }
        for (int i9 = 0; i9 < 24; i9++) {
            dArr2[i9] = 0.0d;
        }
        randomPhase(dArr2, new Random(3049912L));
        ifft(dArr2);
        normalize(dArr2, 0.9d);
        double[] dArrRealPart2 = realPart(dArr2);
        double d4 = 1.0d;
        for (int i10 = 0; i10 < dArrRealPart2.length; i10++) {
            int i11 = i10;
            dArrRealPart2[i11] = dArrRealPart2[i11] * d4;
            d4 *= 0.9998d;
        }
        for (int i12 = 0; i12 < dArrRealPart2.length; i12++) {
            int i13 = i12;
            dArrRealPart[i13] = dArrRealPart[i13] + (dArrRealPart2[i12] * 0.02d);
        }
        normalize(dArrRealPart, 0.9d);
        SF2Sample sF2SampleNewSimpleDrumSample = newSimpleDrumSample(sF2Soundbank, "Timpani", dArrRealPart);
        SF2Layer sF2Layer = new SF2Layer(sF2Soundbank);
        sF2Layer.setName("Timpani");
        sF2Layer.setGlobalZone(new SF2GlobalRegion());
        sF2Soundbank.addResource(sF2Layer);
        SF2LayerRegion sF2LayerRegion = new SF2LayerRegion();
        sF2LayerRegion.putInteger(38, 12000);
        sF2LayerRegion.putInteger(48, -100);
        sF2LayerRegion.setSample(sF2SampleNewSimpleDrumSample);
        sF2Layer.getRegions().add(sF2LayerRegion);
        return sF2Layer;
    }

    public static SF2Layer new_melodic_toms(SF2Soundbank sF2Soundbank) {
        double[] dArr = new double[2 * 16384];
        complexGaussianDist(dArr, 30.0d, 0.5d, 1.0d);
        randomPhase(dArr, new Random(3049912L));
        ifft(dArr);
        normalize(dArr, 0.8d);
        double[] dArrRealPart = realPart(dArr);
        double length = dArrRealPart.length;
        for (int i2 = 0; i2 < dArrRealPart.length; i2++) {
            int i3 = i2;
            dArrRealPart[i3] = dArrRealPart[i3] * (1.0d - (i2 / length));
        }
        double[] dArr2 = new double[2 * 16384];
        Random random = new Random(3049912L);
        for (int i4 = 0; i4 < dArr2.length; i4 += 2) {
            dArr2[i4] = 2.0d * (random.nextDouble() - 0.5d) * 0.1d;
        }
        fft(dArr2);
        for (int i5 = 16384 / 2; i5 < dArr2.length; i5++) {
            dArr2[i5] = 0.0d;
        }
        for (int i6 = 4096; i6 < 8192; i6++) {
            dArr2[i6] = 1.0d - ((i6 - 4096) / 4096.0d);
        }
        for (int i7 = 0; i7 < 200; i7++) {
            double d2 = 1.0d - (i7 / 200.0d);
            int i8 = i7;
            dArr2[i8] = dArr2[i8] * (1.0d + (20.0d * d2 * d2));
        }
        for (int i9 = 0; i9 < 30; i9++) {
            dArr2[i9] = 0.0d;
        }
        randomPhase(dArr2, new Random(3049912L));
        ifft(dArr2);
        normalize(dArr2, 0.9d);
        double[] dArrRealPart2 = realPart(dArr2);
        double d3 = 1.0d;
        for (int i10 = 0; i10 < dArrRealPart2.length; i10++) {
            int i11 = i10;
            dArrRealPart2[i11] = dArrRealPart2[i11] * d3;
            d3 *= 0.9996d;
        }
        for (int i12 = 0; i12 < dArrRealPart2.length; i12++) {
            int i13 = i12;
            dArrRealPart[i13] = dArrRealPart[i13] + (dArrRealPart2[i12] * 0.5d);
        }
        for (int i14 = 0; i14 < 5; i14++) {
            int i15 = i14;
            dArrRealPart[i15] = dArrRealPart[i15] * (i14 / 5.0d);
        }
        normalize(dArrRealPart, 0.99d);
        SF2Sample sF2SampleNewSimpleDrumSample = newSimpleDrumSample(sF2Soundbank, "Melodic Toms", dArrRealPart);
        sF2SampleNewSimpleDrumSample.setOriginalPitch(63);
        SF2Layer sF2Layer = new SF2Layer(sF2Soundbank);
        sF2Layer.setName("Melodic Toms");
        sF2Layer.setGlobalZone(new SF2GlobalRegion());
        sF2Soundbank.addResource(sF2Layer);
        SF2LayerRegion sF2LayerRegion = new SF2LayerRegion();
        sF2LayerRegion.putInteger(38, 12000);
        sF2LayerRegion.putInteger(48, -100);
        sF2LayerRegion.setSample(sF2SampleNewSimpleDrumSample);
        sF2Layer.getRegions().add(sF2LayerRegion);
        return sF2Layer;
    }

    public static SF2Layer new_reverse_cymbal(SF2Soundbank sF2Soundbank) {
        double[] dArr = new double[2 * 16384];
        Random random = new Random(3049912L);
        for (int i2 = 0; i2 < dArr.length; i2 += 2) {
            dArr[i2] = 2.0d * (random.nextDouble() - 0.5d);
        }
        for (int i3 = 16384 / 2; i3 < dArr.length; i3++) {
            dArr[i3] = 0.0d;
        }
        for (int i4 = 0; i4 < 100; i4++) {
            dArr[i4] = 0.0d;
        }
        for (int i5 = 0; i5 < 1024; i5++) {
            dArr[i5] = 1.0d - (i5 / 1024.0d);
        }
        SF2Sample sF2SampleNewSimpleFFTSample = newSimpleFFTSample(sF2Soundbank, "Reverse Cymbal", dArr, 100.0d, 20);
        SF2Layer sF2Layer = new SF2Layer(sF2Soundbank);
        sF2Layer.setName("Reverse Cymbal");
        sF2Layer.setGlobalZone(new SF2GlobalRegion());
        sF2Soundbank.addResource(sF2Layer);
        SF2LayerRegion sF2LayerRegion = new SF2LayerRegion();
        sF2LayerRegion.putInteger(34, -200);
        sF2LayerRegion.putInteger(36, -12000);
        sF2LayerRegion.putInteger(54, 1);
        sF2LayerRegion.putInteger(38, -1000);
        sF2LayerRegion.putInteger(37, 1000);
        sF2LayerRegion.setSample(sF2SampleNewSimpleFFTSample);
        sF2Layer.getRegions().add(sF2LayerRegion);
        return sF2Layer;
    }

    public static SF2Layer new_snare_drum(SF2Soundbank sF2Soundbank) {
        double[] dArr = new double[2 * 16384];
        complexGaussianDist(dArr, 24.0d, 0.5d, 1.0d);
        randomPhase(dArr, new Random(3049912L));
        ifft(dArr);
        normalize(dArr, 0.5d);
        double[] dArrRealPart = realPart(dArr);
        double length = dArrRealPart.length;
        for (int i2 = 0; i2 < dArrRealPart.length; i2++) {
            int i3 = i2;
            dArrRealPart[i3] = dArrRealPart[i3] * (1.0d - (i2 / length));
        }
        double[] dArr2 = new double[2 * 16384];
        Random random = new Random(3049912L);
        for (int i4 = 0; i4 < dArr2.length; i4 += 2) {
            dArr2[i4] = 2.0d * (random.nextDouble() - 0.5d) * 0.1d;
        }
        fft(dArr2);
        for (int i5 = 16384 / 2; i5 < dArr2.length; i5++) {
            dArr2[i5] = 0.0d;
        }
        for (int i6 = 4096; i6 < 8192; i6++) {
            dArr2[i6] = 1.0d - ((i6 - 4096) / 4096.0d);
        }
        for (int i7 = 0; i7 < 300; i7++) {
            double d2 = 1.0d - (i7 / 300.0d);
            int i8 = i7;
            dArr2[i8] = dArr2[i8] * (1.0d + (20.0d * d2 * d2));
        }
        for (int i9 = 0; i9 < 24; i9++) {
            dArr2[i9] = 0.0d;
        }
        randomPhase(dArr2, new Random(3049912L));
        ifft(dArr2);
        normalize(dArr2, 0.9d);
        double[] dArrRealPart2 = realPart(dArr2);
        double d3 = 1.0d;
        for (int i10 = 0; i10 < dArrRealPart2.length; i10++) {
            int i11 = i10;
            dArrRealPart2[i11] = dArrRealPart2[i11] * d3;
            d3 *= 0.9998d;
        }
        for (int i12 = 0; i12 < dArrRealPart2.length; i12++) {
            int i13 = i12;
            dArrRealPart[i13] = dArrRealPart[i13] + dArrRealPart2[i12];
        }
        for (int i14 = 0; i14 < 5; i14++) {
            int i15 = i14;
            dArrRealPart[i15] = dArrRealPart[i15] * (i14 / 5.0d);
        }
        SF2Sample sF2SampleNewSimpleDrumSample = newSimpleDrumSample(sF2Soundbank, "Snare Drum", dArrRealPart);
        SF2Layer sF2Layer = new SF2Layer(sF2Soundbank);
        sF2Layer.setName("Snare Drum");
        sF2Layer.setGlobalZone(new SF2GlobalRegion());
        sF2Soundbank.addResource(sF2Layer);
        SF2LayerRegion sF2LayerRegion = new SF2LayerRegion();
        sF2LayerRegion.putInteger(38, 12000);
        sF2LayerRegion.putInteger(56, 0);
        sF2LayerRegion.putInteger(48, -100);
        sF2LayerRegion.setSample(sF2SampleNewSimpleDrumSample);
        sF2Layer.getRegions().add(sF2LayerRegion);
        return sF2Layer;
    }

    public static SF2Layer new_bass_drum(SF2Soundbank sF2Soundbank) {
        double[] dArr = new double[2 * 16384];
        complexGaussianDist(dArr, 10.0d, 2.0d, 1.0d);
        complexGaussianDist(dArr, 17.2d, 2.0d, 1.0d);
        randomPhase(dArr, new Random(3049912L));
        ifft(dArr);
        normalize(dArr, 0.9d);
        double[] dArrRealPart = realPart(dArr);
        double length = dArrRealPart.length;
        for (int i2 = 0; i2 < dArrRealPart.length; i2++) {
            int i3 = i2;
            dArrRealPart[i3] = dArrRealPart[i3] * (1.0d - (i2 / length));
        }
        double[] dArr2 = new double[2 * 4096];
        Random random = new Random(3049912L);
        for (int i4 = 0; i4 < dArr2.length; i4 += 2) {
            dArr2[i4] = 2.0d * (random.nextDouble() - 0.5d) * 0.1d;
        }
        fft(dArr2);
        for (int i5 = 4096 / 2; i5 < dArr2.length; i5++) {
            dArr2[i5] = 0.0d;
        }
        for (int i6 = 1024; i6 < 2048; i6++) {
            dArr2[i6] = 1.0d - ((i6 - 1024) / 1024.0d);
        }
        for (int i7 = 0; i7 < 512; i7++) {
            dArr2[i7] = (10 * i7) / 512.0d;
        }
        for (int i8 = 0; i8 < 10; i8++) {
            dArr2[i8] = 0.0d;
        }
        randomPhase(dArr2, new Random(3049912L));
        ifft(dArr2);
        normalize(dArr2, 0.9d);
        double[] dArrRealPart2 = realPart(dArr2);
        double d2 = 1.0d;
        for (int i9 = 0; i9 < dArrRealPart2.length; i9++) {
            int i10 = i9;
            dArrRealPart2[i10] = dArrRealPart2[i10] * d2;
            d2 *= 0.999d;
        }
        for (int i11 = 0; i11 < dArrRealPart2.length; i11++) {
            int i12 = i11;
            dArrRealPart[i12] = dArrRealPart[i12] + (dArrRealPart2[i11] * 0.5d);
        }
        for (int i13 = 0; i13 < 5; i13++) {
            int i14 = i13;
            dArrRealPart[i14] = dArrRealPart[i14] * (i13 / 5.0d);
        }
        SF2Sample sF2SampleNewSimpleDrumSample = newSimpleDrumSample(sF2Soundbank, "Bass Drum", dArrRealPart);
        SF2Layer sF2Layer = new SF2Layer(sF2Soundbank);
        sF2Layer.setName("Bass Drum");
        sF2Layer.setGlobalZone(new SF2GlobalRegion());
        sF2Soundbank.addResource(sF2Layer);
        SF2LayerRegion sF2LayerRegion = new SF2LayerRegion();
        sF2LayerRegion.putInteger(38, 12000);
        sF2LayerRegion.putInteger(56, 0);
        sF2LayerRegion.putInteger(48, -100);
        sF2LayerRegion.setSample(sF2SampleNewSimpleDrumSample);
        sF2Layer.getRegions().add(sF2LayerRegion);
        return sF2Layer;
    }

    public static SF2Layer new_tom(SF2Soundbank sF2Soundbank) {
        double[] dArr = new double[2 * 16384];
        complexGaussianDist(dArr, 30.0d, 0.5d, 1.0d);
        randomPhase(dArr, new Random(3049912L));
        ifft(dArr);
        normalize(dArr, 0.8d);
        double[] dArrRealPart = realPart(dArr);
        double length = dArrRealPart.length;
        for (int i2 = 0; i2 < dArrRealPart.length; i2++) {
            int i3 = i2;
            dArrRealPart[i3] = dArrRealPart[i3] * (1.0d - (i2 / length));
        }
        double[] dArr2 = new double[2 * 16384];
        Random random = new Random(3049912L);
        for (int i4 = 0; i4 < dArr2.length; i4 += 2) {
            dArr2[i4] = 2.0d * (random.nextDouble() - 0.5d) * 0.1d;
        }
        fft(dArr2);
        for (int i5 = 16384 / 2; i5 < dArr2.length; i5++) {
            dArr2[i5] = 0.0d;
        }
        for (int i6 = 4096; i6 < 8192; i6++) {
            dArr2[i6] = 1.0d - ((i6 - 4096) / 4096.0d);
        }
        for (int i7 = 0; i7 < 200; i7++) {
            double d2 = 1.0d - (i7 / 200.0d);
            int i8 = i7;
            dArr2[i8] = dArr2[i8] * (1.0d + (20.0d * d2 * d2));
        }
        for (int i9 = 0; i9 < 30; i9++) {
            dArr2[i9] = 0.0d;
        }
        randomPhase(dArr2, new Random(3049912L));
        ifft(dArr2);
        normalize(dArr2, 0.9d);
        double[] dArrRealPart2 = realPart(dArr2);
        double d3 = 1.0d;
        for (int i10 = 0; i10 < dArrRealPart2.length; i10++) {
            int i11 = i10;
            dArrRealPart2[i11] = dArrRealPart2[i11] * d3;
            d3 *= 0.9996d;
        }
        for (int i12 = 0; i12 < dArrRealPart2.length; i12++) {
            int i13 = i12;
            dArrRealPart[i13] = dArrRealPart[i13] + (dArrRealPart2[i12] * 0.5d);
        }
        for (int i14 = 0; i14 < 5; i14++) {
            int i15 = i14;
            dArrRealPart[i15] = dArrRealPart[i15] * (i14 / 5.0d);
        }
        normalize(dArrRealPart, 0.99d);
        SF2Sample sF2SampleNewSimpleDrumSample = newSimpleDrumSample(sF2Soundbank, "Tom", dArrRealPart);
        sF2SampleNewSimpleDrumSample.setOriginalPitch(50);
        SF2Layer sF2Layer = new SF2Layer(sF2Soundbank);
        sF2Layer.setName("Tom");
        sF2Layer.setGlobalZone(new SF2GlobalRegion());
        sF2Soundbank.addResource(sF2Layer);
        SF2LayerRegion sF2LayerRegion = new SF2LayerRegion();
        sF2LayerRegion.putInteger(38, 12000);
        sF2LayerRegion.putInteger(48, -100);
        sF2LayerRegion.setSample(sF2SampleNewSimpleDrumSample);
        sF2Layer.getRegions().add(sF2LayerRegion);
        return sF2Layer;
    }

    public static SF2Layer new_closed_hihat(SF2Soundbank sF2Soundbank) {
        double[] dArr = new double[2 * 16384];
        Random random = new Random(3049912L);
        for (int i2 = 0; i2 < dArr.length; i2 += 2) {
            dArr[i2] = 2.0d * (random.nextDouble() - 0.5d) * 0.1d;
        }
        fft(dArr);
        for (int i3 = 16384 / 2; i3 < dArr.length; i3++) {
            dArr[i3] = 0.0d;
        }
        for (int i4 = 4096; i4 < 8192; i4++) {
            dArr[i4] = 1.0d - ((i4 - 4096) / 4096.0d);
        }
        for (int i5 = 0; i5 < 2048; i5++) {
            dArr[i5] = 0.2d + (0.8d * (i5 / 2048.0d));
        }
        randomPhase(dArr, new Random(3049912L));
        ifft(dArr);
        normalize(dArr, 0.9d);
        double[] dArrRealPart = realPart(dArr);
        double d2 = 1.0d;
        for (int i6 = 0; i6 < dArrRealPart.length; i6++) {
            int i7 = i6;
            dArrRealPart[i7] = dArrRealPart[i7] * d2;
            d2 *= 0.9996d;
        }
        for (int i8 = 0; i8 < 5; i8++) {
            int i9 = i8;
            dArrRealPart[i9] = dArrRealPart[i9] * (i8 / 5.0d);
        }
        SF2Sample sF2SampleNewSimpleDrumSample = newSimpleDrumSample(sF2Soundbank, "Closed Hi-Hat", dArrRealPart);
        SF2Layer sF2Layer = new SF2Layer(sF2Soundbank);
        sF2Layer.setName("Closed Hi-Hat");
        sF2Layer.setGlobalZone(new SF2GlobalRegion());
        sF2Soundbank.addResource(sF2Layer);
        SF2LayerRegion sF2LayerRegion = new SF2LayerRegion();
        sF2LayerRegion.putInteger(38, 12000);
        sF2LayerRegion.putInteger(56, 0);
        sF2LayerRegion.putInteger(57, 1);
        sF2LayerRegion.setSample(sF2SampleNewSimpleDrumSample);
        sF2Layer.getRegions().add(sF2LayerRegion);
        return sF2Layer;
    }

    public static SF2Layer new_open_hihat(SF2Soundbank sF2Soundbank) {
        double[] dArr = new double[2 * 16384];
        Random random = new Random(3049912L);
        for (int i2 = 0; i2 < dArr.length; i2 += 2) {
            dArr[i2] = 2.0d * (random.nextDouble() - 0.5d);
        }
        for (int i3 = 16384 / 2; i3 < dArr.length; i3++) {
            dArr[i3] = 0.0d;
        }
        for (int i4 = 0; i4 < 200; i4++) {
            dArr[i4] = 0.0d;
        }
        for (int i5 = 0; i5 < 8192; i5++) {
            dArr[i5] = i5 / 8192.0d;
        }
        SF2Sample sF2SampleNewSimpleFFTSample = newSimpleFFTSample(sF2Soundbank, "Open Hi-Hat", dArr, 1000.0d, 5);
        SF2Layer sF2Layer = new SF2Layer(sF2Soundbank);
        sF2Layer.setName("Open Hi-Hat");
        sF2Layer.setGlobalZone(new SF2GlobalRegion());
        sF2Soundbank.addResource(sF2Layer);
        SF2LayerRegion sF2LayerRegion = new SF2LayerRegion();
        sF2LayerRegion.putInteger(36, 1500);
        sF2LayerRegion.putInteger(54, 1);
        sF2LayerRegion.putInteger(38, 1500);
        sF2LayerRegion.putInteger(37, 1000);
        sF2LayerRegion.putInteger(56, 0);
        sF2LayerRegion.putInteger(57, 1);
        sF2LayerRegion.setSample(sF2SampleNewSimpleFFTSample);
        sF2Layer.getRegions().add(sF2LayerRegion);
        return sF2Layer;
    }

    public static SF2Layer new_crash_cymbal(SF2Soundbank sF2Soundbank) {
        double[] dArr = new double[2 * 16384];
        Random random = new Random(3049912L);
        for (int i2 = 0; i2 < dArr.length; i2 += 2) {
            dArr[i2] = 2.0d * (random.nextDouble() - 0.5d);
        }
        for (int i3 = 16384 / 2; i3 < dArr.length; i3++) {
            dArr[i3] = 0.0d;
        }
        for (int i4 = 0; i4 < 100; i4++) {
            dArr[i4] = 0.0d;
        }
        for (int i5 = 0; i5 < 1024; i5++) {
            dArr[i5] = i5 / 1024.0d;
        }
        SF2Sample sF2SampleNewSimpleFFTSample = newSimpleFFTSample(sF2Soundbank, "Crash Cymbal", dArr, 1000.0d, 5);
        SF2Layer sF2Layer = new SF2Layer(sF2Soundbank);
        sF2Layer.setName("Crash Cymbal");
        sF2Layer.setGlobalZone(new SF2GlobalRegion());
        sF2Soundbank.addResource(sF2Layer);
        SF2LayerRegion sF2LayerRegion = new SF2LayerRegion();
        sF2LayerRegion.putInteger(36, 1800);
        sF2LayerRegion.putInteger(54, 1);
        sF2LayerRegion.putInteger(38, 1800);
        sF2LayerRegion.putInteger(37, 1000);
        sF2LayerRegion.putInteger(56, 0);
        sF2LayerRegion.setSample(sF2SampleNewSimpleFFTSample);
        sF2Layer.getRegions().add(sF2LayerRegion);
        return sF2Layer;
    }

    public static SF2Layer new_side_stick(SF2Soundbank sF2Soundbank) {
        double[] dArr = new double[2 * 16384];
        Random random = new Random(3049912L);
        for (int i2 = 0; i2 < dArr.length; i2 += 2) {
            dArr[i2] = 2.0d * (random.nextDouble() - 0.5d) * 0.1d;
        }
        fft(dArr);
        for (int i3 = 16384 / 2; i3 < dArr.length; i3++) {
            dArr[i3] = 0.0d;
        }
        for (int i4 = 4096; i4 < 8192; i4++) {
            dArr[i4] = 1.0d - ((i4 - 4096) / 4096.0d);
        }
        for (int i5 = 0; i5 < 200; i5++) {
            double d2 = 1.0d - (i5 / 200.0d);
            int i6 = i5;
            dArr[i6] = dArr[i6] * (1.0d + (20.0d * d2 * d2));
        }
        for (int i7 = 0; i7 < 30; i7++) {
            dArr[i7] = 0.0d;
        }
        randomPhase(dArr, new Random(3049912L));
        ifft(dArr);
        normalize(dArr, 0.9d);
        double[] dArrRealPart = realPart(dArr);
        double d3 = 1.0d;
        for (int i8 = 0; i8 < dArrRealPart.length; i8++) {
            int i9 = i8;
            dArrRealPart[i9] = dArrRealPart[i9] * d3;
            d3 *= 0.9996d;
        }
        for (int i10 = 0; i10 < 10; i10++) {
            int i11 = i10;
            dArrRealPart[i11] = dArrRealPart[i11] * (i10 / 10.0d);
        }
        SF2Sample sF2SampleNewSimpleDrumSample = newSimpleDrumSample(sF2Soundbank, "Side Stick", dArrRealPart);
        SF2Layer sF2Layer = new SF2Layer(sF2Soundbank);
        sF2Layer.setName("Side Stick");
        sF2Layer.setGlobalZone(new SF2GlobalRegion());
        sF2Soundbank.addResource(sF2Layer);
        SF2LayerRegion sF2LayerRegion = new SF2LayerRegion();
        sF2LayerRegion.putInteger(38, 12000);
        sF2LayerRegion.putInteger(56, 0);
        sF2LayerRegion.putInteger(48, -50);
        sF2LayerRegion.setSample(sF2SampleNewSimpleDrumSample);
        sF2Layer.getRegions().add(sF2LayerRegion);
        return sF2Layer;
    }

    public static SF2Sample newSimpleFFTSample(SF2Soundbank sF2Soundbank, String str, double[] dArr, double d2) {
        return newSimpleFFTSample(sF2Soundbank, str, dArr, d2, 10);
    }

    public static SF2Sample newSimpleFFTSample(SF2Soundbank sF2Soundbank, String str, double[] dArr, double d2, int i2) {
        int length = dArr.length / 2;
        AudioFormat audioFormat = new AudioFormat(44100.0f, 16, 1, true, false);
        double sampleRate = (d2 / length) * audioFormat.getSampleRate() * 0.5d;
        randomPhase(dArr);
        ifft(dArr);
        double[] dArrRealPart = realPart(dArr);
        normalize(dArrRealPart, 0.9d);
        float[] fArr = toFloat(dArrRealPart);
        float[] fArrLoopExtend = loopExtend(fArr, fArr.length + 512);
        fadeUp(fArrLoopExtend, i2);
        byte[] bytes = toBytes(fArrLoopExtend, audioFormat);
        SF2Sample sF2Sample = new SF2Sample(sF2Soundbank);
        sF2Sample.setName(str);
        sF2Sample.setData(bytes);
        sF2Sample.setStartLoop(256L);
        sF2Sample.setEndLoop(length + 256);
        sF2Sample.setSampleRate((long) audioFormat.getSampleRate());
        sF2Sample.setOriginalPitch((int) (81.0d + ((12.0d * Math.log(sampleRate / 440.0d)) / Math.log(2.0d))));
        sF2Sample.setPitchCorrection((byte) ((-(r0 - ((int) r0))) * 100.0d));
        sF2Soundbank.addResource(sF2Sample);
        return sF2Sample;
    }

    public static SF2Sample newSimpleFFTSample_dist(SF2Soundbank sF2Soundbank, String str, double[] dArr, double d2, double d3) {
        int length = dArr.length / 2;
        AudioFormat audioFormat = new AudioFormat(44100.0f, 16, 1, true, false);
        double sampleRate = (d2 / length) * audioFormat.getSampleRate() * 0.5d;
        randomPhase(dArr);
        ifft(dArr);
        double[] dArrRealPart = realPart(dArr);
        for (int i2 = 0; i2 < dArrRealPart.length; i2++) {
            dArrRealPart[i2] = (1.0d - Math.exp(-Math.abs(dArrRealPart[i2] * d3))) * Math.signum(dArrRealPart[i2]);
        }
        normalize(dArrRealPart, 0.9d);
        float[] fArr = toFloat(dArrRealPart);
        float[] fArrLoopExtend = loopExtend(fArr, fArr.length + 512);
        fadeUp(fArrLoopExtend, 80);
        byte[] bytes = toBytes(fArrLoopExtend, audioFormat);
        SF2Sample sF2Sample = new SF2Sample(sF2Soundbank);
        sF2Sample.setName(str);
        sF2Sample.setData(bytes);
        sF2Sample.setStartLoop(256L);
        sF2Sample.setEndLoop(length + 256);
        sF2Sample.setSampleRate((long) audioFormat.getSampleRate());
        sF2Sample.setOriginalPitch((int) (81.0d + ((12.0d * Math.log(sampleRate / 440.0d)) / Math.log(2.0d))));
        sF2Sample.setPitchCorrection((byte) ((-(r0 - ((int) r0))) * 100.0d));
        sF2Soundbank.addResource(sF2Sample);
        return sF2Sample;
    }

    public static SF2Sample newSimpleDrumSample(SF2Soundbank sF2Soundbank, String str, double[] dArr) {
        int length = dArr.length;
        AudioFormat audioFormat = new AudioFormat(44100.0f, 16, 1, true, false);
        byte[] bytes = toBytes(toFloat(realPart(dArr)), audioFormat);
        SF2Sample sF2Sample = new SF2Sample(sF2Soundbank);
        sF2Sample.setName(str);
        sF2Sample.setData(bytes);
        sF2Sample.setStartLoop(256L);
        sF2Sample.setEndLoop(length + 256);
        sF2Sample.setSampleRate((long) audioFormat.getSampleRate());
        sF2Sample.setOriginalPitch(60);
        sF2Soundbank.addResource(sF2Sample);
        return sF2Sample;
    }

    public static SF2Layer newLayer(SF2Soundbank sF2Soundbank, String str, SF2Sample sF2Sample) {
        SF2LayerRegion sF2LayerRegion = new SF2LayerRegion();
        sF2LayerRegion.setSample(sF2Sample);
        SF2Layer sF2Layer = new SF2Layer(sF2Soundbank);
        sF2Layer.setName(str);
        sF2Layer.getRegions().add(sF2LayerRegion);
        sF2Soundbank.addResource(sF2Layer);
        return sF2Layer;
    }

    public static SF2Instrument newInstrument(SF2Soundbank sF2Soundbank, String str, Patch patch, SF2Layer... sF2LayerArr) {
        SF2Instrument sF2Instrument = new SF2Instrument(sF2Soundbank);
        sF2Instrument.setPatch(patch);
        sF2Instrument.setName(str);
        sF2Soundbank.addInstrument(sF2Instrument);
        for (SF2Layer sF2Layer : sF2LayerArr) {
            SF2InstrumentRegion sF2InstrumentRegion = new SF2InstrumentRegion();
            sF2InstrumentRegion.setLayer(sF2Layer);
            sF2Instrument.getRegions().add(sF2InstrumentRegion);
        }
        return sF2Instrument;
    }

    public static void ifft(double[] dArr) {
        new FFT(dArr.length / 2, 1).transform(dArr);
    }

    public static void fft(double[] dArr) {
        new FFT(dArr.length / 2, -1).transform(dArr);
    }

    public static void complexGaussianDist(double[] dArr, double d2, double d3, double d4) {
        for (int i2 = 0; i2 < dArr.length / 4; i2++) {
            int i3 = i2 * 2;
            dArr[i3] = dArr[i3] + (d4 * (1.0d / (d3 * Math.sqrt(6.283185307179586d))) * Math.exp((-0.5d) * Math.pow((i2 - d2) / d3, 2.0d)));
        }
    }

    public static void randomPhase(double[] dArr) {
        for (int i2 = 0; i2 < dArr.length; i2 += 2) {
            double dRandom = Math.random() * 2.0d * 3.141592653589793d;
            double d2 = dArr[i2];
            dArr[i2] = Math.sin(dRandom) * d2;
            dArr[i2 + 1] = Math.cos(dRandom) * d2;
        }
    }

    public static void randomPhase(double[] dArr, Random random) {
        for (int i2 = 0; i2 < dArr.length; i2 += 2) {
            double dNextDouble = random.nextDouble() * 2.0d * 3.141592653589793d;
            double d2 = dArr[i2];
            dArr[i2] = Math.sin(dNextDouble) * d2;
            dArr[i2 + 1] = Math.cos(dNextDouble) * d2;
        }
    }

    public static void normalize(double[] dArr, double d2) {
        double d3 = 0.0d;
        for (int i2 = 0; i2 < dArr.length; i2++) {
            if (dArr[i2] > d3) {
                d3 = dArr[i2];
            }
            if ((-dArr[i2]) > d3) {
                d3 = -dArr[i2];
            }
        }
        if (d3 == 0.0d) {
            return;
        }
        double d4 = d2 / d3;
        for (int i3 = 0; i3 < dArr.length; i3++) {
            int i4 = i3;
            dArr[i4] = dArr[i4] * d4;
        }
    }

    public static void normalize(float[] fArr, double d2) {
        double d3 = 0.5d;
        for (int i2 = 0; i2 < fArr.length; i2++) {
            if (fArr[i2 * 2] > d3) {
                d3 = fArr[i2 * 2];
            }
            if ((-fArr[i2 * 2]) > d3) {
                d3 = -fArr[i2 * 2];
            }
        }
        double d4 = d2 / d3;
        for (int i3 = 0; i3 < fArr.length; i3++) {
            fArr[i3 * 2] = (float) (fArr[r1] * d4);
        }
    }

    public static double[] realPart(double[] dArr) {
        double[] dArr2 = new double[dArr.length / 2];
        for (int i2 = 0; i2 < dArr2.length; i2++) {
            dArr2[i2] = dArr[i2 * 2];
        }
        return dArr2;
    }

    public static double[] imgPart(double[] dArr) {
        double[] dArr2 = new double[dArr.length / 2];
        for (int i2 = 0; i2 < dArr2.length; i2++) {
            dArr2[i2] = dArr[i2 * 2];
        }
        return dArr2;
    }

    public static float[] toFloat(double[] dArr) {
        float[] fArr = new float[dArr.length];
        for (int i2 = 0; i2 < fArr.length; i2++) {
            fArr[i2] = (float) dArr[i2];
        }
        return fArr;
    }

    public static byte[] toBytes(float[] fArr, AudioFormat audioFormat) {
        return AudioFloatConverter.getConverter(audioFormat).toByteArray(fArr, new byte[fArr.length * audioFormat.getFrameSize()]);
    }

    public static void fadeUp(double[] dArr, int i2) {
        double d2 = i2;
        for (int i3 = 0; i3 < i2; i3++) {
            int i4 = i3;
            dArr[i4] = dArr[i4] * (i3 / d2);
        }
    }

    public static void fadeUp(float[] fArr, int i2) {
        double d2 = i2;
        for (int i3 = 0; i3 < i2; i3++) {
            fArr[i3] = (float) (fArr[r1] * (i3 / d2));
        }
    }

    public static double[] loopExtend(double[] dArr, int i2) {
        double[] dArr2 = new double[i2];
        int length = dArr.length;
        int i3 = 0;
        for (int i4 = 0; i4 < dArr2.length; i4++) {
            dArr2[i4] = dArr[i3];
            i3++;
            if (i3 == length) {
                i3 = 0;
            }
        }
        return dArr2;
    }

    public static float[] loopExtend(float[] fArr, int i2) {
        float[] fArr2 = new float[i2];
        int length = fArr.length;
        int i3 = 0;
        for (int i4 = 0; i4 < fArr2.length; i4++) {
            fArr2[i4] = fArr[i3];
            i3++;
            if (i3 == length) {
                i3 = 0;
            }
        }
        return fArr2;
    }
}
