package com.jbatista.vsynth.components.modules;

import com.jbatista.bricks.Instrument;
import com.jbatista.bricks.KeyboardNote;
import com.jbatista.bricks.components.OutputConnector;
import com.jbatista.bricks.components.Patch;
import com.jbatista.bricks.components.builtin.*;

public class InstrumentBoard {

    private static final double[] OCTAVE_RATIOS = new double[]{0.25, 0.5, 1, 1.5, 2.0};

    private final Instrument instrument = new Instrument();

    private final EnvelopeGenerator oscillatorEnvelope1 = new EnvelopeGenerator(instrument);
    private final EnvelopeGenerator oscillatorEnvelope2 = new EnvelopeGenerator(instrument);

    private final EnvelopeGenerator pitchEnvelope1 = new EnvelopeGenerator(instrument);
    private final EnvelopeGenerator pitchEnvelope2 = new EnvelopeGenerator(instrument);

    private final Oscillator oscillator1 = new Oscillator(instrument);
    private final Oscillator oscillator2 = new Oscillator(instrument);
    private final Oscillator oscillator3 = new Oscillator(instrument);
    private final Oscillator noise = new Oscillator(instrument);
    private final Lfo lfo = new Lfo(instrument);

    private final Mixer oscillatorMixer1 = new Mixer(instrument);
    private final Mixer oscillatorMixer2 = new Mixer(instrument);
    private final Mixer oscillatorFmMixer1 = new Mixer(instrument);
    private final Mixer oscillatorFmMixer2 = new Mixer(instrument);

    private final Mixer mainMixer = new Mixer(instrument);

    private final EnvelopeGenerator filterEnvelope1 = new EnvelopeGenerator(instrument);
    private final EnvelopeGenerator filterEnvelope2 = new EnvelopeGenerator(instrument);

    private final LowPassFilter lowPassFilter1 = new LowPassFilter(instrument);
    private final LowPassFilter lowPassFilter2 = new LowPassFilter(instrument);

    private final Keyboard keyboard = new Keyboard(instrument);

    private final Patch[] patches = new Patch[33];

    private int octaveOffset1 = 2;
    private int octaveOffset2 = 2;
    private double oscillator1fineTune = 1;
    private double oscillator2fineTune = 1;

    private boolean onlyModulateOscillator2 = false;

    public InstrumentBoard() {
        for (int i = 0; i < patches.length; i++) patches[i] = new Patch();

        // oscillators interaction
        oscillator1.getOutput(0).connectPatch(patches[0]);
        oscillator3.getInput(0).connectPatch(patches[0]);

        oscillator3.getController(0).setValue(1);
        oscillator3.getController(1).setValue(0.5);

        // lfo interaction
        lfo.getOutput(0).connectPatch(patches[1]);
        lfo.getOutput(1).connectPatch(patches[2]);

        oscillatorFmMixer1.getInput(0).connectPatch(patches[1]);
        oscillatorFmMixer2.getInput(0).connectPatch(patches[2]);

        oscillatorFmMixer1.getOutput(0).connectPatch(patches[3]);
        oscillatorFmMixer2.getOutput(0).connectPatch(patches[4]);

        oscillator1.getInput(1).connectPatch(patches[3]);
        oscillator2.getInput(1).connectPatch(patches[4]);
        oscillator3.getInput(1).connectPatch(patches[3]);

        // noise setup: always active, starts muted
        noise.getInput(0).write(1);
        noise.getController(0).setValue(5);
        noise.getController(3).setValue(0);

        noise.getOutput(1).connectPatch(patches[7]);

        // mixer setup: oscillator 1, 3, noise
        oscillator1.getOutput(1).connectPatch(patches[8]);
        oscillator3.getOutput(1).connectPatch(patches[9]);

        oscillatorMixer1.getController(0).setValue(0.05);
        oscillatorMixer1.getController(1).setValue(0.05);
        oscillatorMixer1.getController(2).setValue(0.005);
        oscillatorMixer1.getInput(0).connectPatch(patches[8]);
        oscillatorMixer1.getInput(1).connectPatch(patches[9]);
        oscillatorMixer1.getInput(2).connectPatch(patches[7]);

        // mixer setup: oscillator 2, noise
        oscillator2.getOutput(1).connectPatch(patches[11]);

        oscillatorMixer2.getController(0).setValue(0.05);
        oscillatorMixer2.getController(1).setValue(0.005);
        oscillatorMixer2.getInput(0).connectPatch(patches[11]);
        oscillatorMixer2.getInput(1).connectPatch(patches[7]);

        // oscillator envelope setup
        oscillatorMixer1.getOutput(0).connectPatch(patches[13]);
        oscillatorMixer2.getOutput(0).connectPatch(patches[14]);

        oscillatorEnvelope1.getInput(0).connectPatch(patches[13]);
        oscillatorEnvelope2.getInput(0).connectPatch(patches[14]);

        // filter setup
        oscillatorEnvelope1.getOutput(0).connectPatch(patches[15]);
        oscillatorEnvelope2.getOutput(0).connectPatch(patches[16]);

        lowPassFilter1.getInput(0).connectPatch(patches[15]);
        lowPassFilter2.getInput(0).connectPatch(patches[16]);

        // filter envelope setup
        filterEnvelope1.getInput(0).write(1);
        filterEnvelope2.getInput(0).write(1);

        filterEnvelope1.getOutput(0).connectPatch(patches[17]);
        filterEnvelope2.getOutput(0).connectPatch(patches[18]);

        lowPassFilter1.getInput(1).connectPatch(patches[17]);
        lowPassFilter2.getInput(1).connectPatch(patches[18]);

        // mixer setup: main
        lowPassFilter1.getOutput(0).connectPatch(patches[19]);
        lowPassFilter2.getOutput(0).connectPatch(patches[20]);

        mainMixer.getInput(0).connectPatch(patches[19]);
        mainMixer.getInput(1).connectPatch(patches[20]);

        // oscillator pitch envelopes
        pitchEnvelope1.getOutput(0).connectPatch(patches[21]);
        pitchEnvelope2.getOutput(0).connectPatch(patches[22]);

        oscillator1.getInput(0).connectPatch(patches[21]);
        oscillator2.getInput(0).connectPatch(patches[22]);

        // polyphony
        keyboard.getOutput(0).connectPatch(patches[23]);

        pitchEnvelope1.getInput(0).connectPatch(patches[23]);
        pitchEnvelope1.getInput(1).connectPatch(patches[23]);
        oscillatorEnvelope1.getInput(1).connectPatch(patches[23]);
        filterEnvelope1.getInput(1).connectPatch(patches[23]);

        pitchEnvelope1.getController(8).setValue(1);

        pitchEnvelope2.getController(8).setValue(1);

        setMono();
    }

    public synchronized void pressKey(KeyboardNote note) {
        keyboard.pressKey(note);
    }

    public synchronized void releaseKey(KeyboardNote note) {
        keyboard.releaseKey(note);
    }

    public void setMono() {
        keyboard.getController(0).setValue(1);

        // 2nd key
        pitchEnvelope2.getInput(0).disconnectAllPatches();
        pitchEnvelope2.getInput(1).disconnectAllPatches();
        oscillatorEnvelope2.getInput(1).disconnectAllPatches();
        filterEnvelope2.getInput(1).disconnectAllPatches();

        pitchEnvelope2.getInput(0).connectPatch(patches[23]);
        pitchEnvelope2.getInput(1).connectPatch(patches[23]);
        oscillatorEnvelope2.getInput(1).connectPatch(patches[23]);
        filterEnvelope2.getInput(1).connectPatch(patches[23]);
    }

    public void setPoly() {
        keyboard.getController(0).setValue(2);

        // 2nd key
        pitchEnvelope2.getInput(0).disconnectAllPatches();
        pitchEnvelope2.getInput(1).disconnectAllPatches();
        oscillatorEnvelope2.getInput(1).disconnectAllPatches();
        filterEnvelope2.getInput(1).disconnectAllPatches();

        keyboard.getOutput(1).connectPatch(patches[32]);

        pitchEnvelope2.getInput(0).connectPatch(patches[32]);
        pitchEnvelope2.getInput(1).connectPatch(patches[32]);
        oscillatorEnvelope2.getInput(1).connectPatch(patches[32]);
        filterEnvelope2.getInput(1).connectPatch(patches[32]);
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void runInstrumentProcess() {
        instrument.runProcess();
    }

    public OutputConnector getSoundOutput() {
        return mainMixer.getOutput(0);
    }

    //<editor-fold desc="modulation settings" defaultstate="collapsed">
    // 0, 1, 2, 3, 4
    public void setLfoShape(int shape) {
        lfo.getController(0).setValue(shape);
    }

    public int getLfoShape() {
        return (int) lfo.getController(0).getValue();
    }

    public void onlyModulateOscillator2(boolean value) {
        this.onlyModulateOscillator2 = value;

        if (this.onlyModulateOscillator2) {
            oscillator1.getInput(1).disconnectPatch(patches[5]);
            oscillator3.getInput(1).disconnectPatch(patches[6]);
        } else {
            oscillator1.getInput(1).connectPatch(patches[5]);
            oscillator3.getInput(1).connectPatch(patches[6]);
        }
    }

    public boolean isOnlyModulateOscillator2() {
        return onlyModulateOscillator2;
    }

    // 0 ~ 1
    public void setLfoValue(double value) {
        lfo.getController(1).setValue(value);
    }

    public double getLfoValue() {
        return lfo.getController(1).getValue();
    }

    public double getLfoFrequency() {
        return lfo.getController(1).getDisplayValue();
    }

    public double getOscillatorModStr() {
        return oscillator1.getController(3).getValue();
    }

    public void setOscillatorModStr(double value) {
        oscillator1.getController(2).setValue(value);
        oscillator2.getController(2).setValue(value);
        oscillator3.getController(2).setValue(value);
    }

    public boolean isFilterClosing() {
        return lowPassFilter1.getController(2).getValue() == 1;
    }

    public void setFilterCloses(boolean value) {
        final double result = value ? 1 : 0;

        lowPassFilter1.getController(2).setValue(result);
        lowPassFilter2.getController(2).setValue(result);
    }
    //</editor-fold>

    //<editor-fold desc="oscillator settings" defaultstate="collapsed">
    // 0, 1, 2, 3, 4
    public void setOscillator1Shape(int shape) {
        oscillator1.getController(0).setValue(shape);
    }

    public int getOscillator1Shape() {
        return (int) oscillator1.getController(0).getValue();
    }

    // 0, 1, 2, 3, 4
    public void setOscillator2Shape(int shape) {
        oscillator2.getController(0).setValue(shape);
    }

    public int getOscillator2Shape() {
        return (int) oscillator2.getController(0).getValue();
    }

    // 0, 1, 2, 3, 4
    public void setOctaveOffset1(int octaveOffset1) {
        this.octaveOffset1 = Math.max(0, Math.min(octaveOffset1, 4));

        pitchEnvelope1
                .getInput(0)
                .setRatio(OCTAVE_RATIOS[this.octaveOffset1] * this.oscillator1fineTune);
    }

    public int getOctaveOffset1() {
        return octaveOffset1;
    }

    // 0, 1, 2, 3, 4
    public void setOctaveOffset2(int octaveOffset2) {
        this.octaveOffset2 = Math.max(0, Math.min(octaveOffset2, 4));

        pitchEnvelope2
                .getInput(0)
                .setRatio(OCTAVE_RATIOS[this.octaveOffset2] * this.oscillator2fineTune);
    }

    public int getOctaveOffset2() {
        return octaveOffset2;
    }

    public double getOscillator1fineTune() {
        return oscillator1fineTune;
    }

    public void setOscillator1fineTune(double oscillator1fineTune) {
        this.oscillator1fineTune = Math.max(0.5, Math.min(oscillator1fineTune, 1.5));

        pitchEnvelope1
                .getInput(0)
                .setRatio(OCTAVE_RATIOS[octaveOffset1] * this.oscillator1fineTune);
    }

    public double getOscillator2fineTune() {
        return oscillator2fineTune;
    }

    public void setOscillator2fineTune(double oscillator2fineTune) {
        this.oscillator2fineTune = Math.max(0.5, Math.min(oscillator2fineTune, 1.5));

        pitchEnvelope2
                .getInput(0)
                .setRatio(OCTAVE_RATIOS[octaveOffset2] * this.oscillator2fineTune);
    }

    public void setOscillatorAttackLevel(double oscillatorAttackLevel) {
        oscillatorEnvelope1.getController(0).setValue(oscillatorAttackLevel);
        oscillatorEnvelope2.getController(0).setValue(oscillatorAttackLevel);
    }

    public double getOscillatorAttackLevel() {
        return oscillatorEnvelope1.getController(0).getValue();
    }

    public void setOscillatorDecayLevel(double oscillatorDecayLevel) {
        oscillatorEnvelope1.getController(1).setValue(oscillatorDecayLevel);
        oscillatorEnvelope2.getController(1).setValue(oscillatorDecayLevel);
    }

    public double getOscillatorDecayLevel() {
        return oscillatorEnvelope1.getController(1).getValue();
    }

    public void setOscillatorSustainLevel(double oscillatorSustainLevel) {
        oscillatorEnvelope1.getController(2).setValue(oscillatorSustainLevel);
        oscillatorEnvelope2.getController(2).setValue(oscillatorSustainLevel);
    }

    public double getOscillatorSustainLevel() {
        return oscillatorEnvelope1.getController(2).getValue();
    }

    public void setOscillatorReleaseLevel(double oscillatorReleaseLevel) {
        oscillatorEnvelope1.getController(3).setValue(oscillatorReleaseLevel);
        oscillatorEnvelope2.getController(3).setValue(oscillatorReleaseLevel);
    }

    public double getOscillatorReleaseLevel() {
        return oscillatorEnvelope1.getController(3).getValue();
    }

    public void setOscillatorAttackSpeed(double oscillatorAttackSpeed) {
        oscillatorEnvelope1.getController(4).setValue(oscillatorAttackSpeed);
        oscillatorEnvelope2.getController(4).setValue(oscillatorAttackSpeed);
    }

    public double getOscillatorAttackSpeed() {
        return oscillatorEnvelope1.getController(4).getValue();
    }

    public void setOscillatorDecaySpeed(double oscillatorDecaySpeed) {
        oscillatorEnvelope1.getController(5).setValue(oscillatorDecaySpeed);
        oscillatorEnvelope2.getController(5).setValue(oscillatorDecaySpeed);
    }

    public double getOscillatorDecaySpeed() {
        return oscillatorEnvelope1.getController(5).getValue();
    }

    public void setOscillatorSustainSpeed(double oscillatorSustainSpeed) {
        oscillatorEnvelope1.getController(6).setValue(oscillatorSustainSpeed);
        oscillatorEnvelope2.getController(6).setValue(oscillatorSustainSpeed);
    }

    public double getOscillatorSustainSpeed() {
        return oscillatorEnvelope1.getController(6).getValue();
    }

    public void setOscillatorReleaseSpeed(double oscillatorReleaseSpeed) {
        oscillatorEnvelope1.getController(7).setValue(oscillatorReleaseSpeed);
        oscillatorEnvelope2.getController(7).setValue(oscillatorReleaseSpeed);
    }

    public double getOscillatorReleaseSpeed() {
        return oscillatorEnvelope1.getController(7).getValue();
    }
    //</editor-fold>

    //<editor-fold desc="pitch envelope" defaultstate="collapsed">
    public void setPitchAttackLevel(double pitchAttackLevel) {
        pitchEnvelope1.getController(0).setValue(pitchAttackLevel);
        pitchEnvelope2.getController(0).setValue(pitchAttackLevel);
    }

    public double getPitchAttackLevel() {
        return pitchEnvelope1.getController(0).getValue();
    }

    public void setPitchDecayLevel(double pitchDecayLevel) {
        pitchEnvelope1.getController(1).setValue(pitchDecayLevel);
        pitchEnvelope2.getController(1).setValue(pitchDecayLevel);
    }

    public double getPitchDecayLevel() {
        return pitchEnvelope1.getController(1).getValue();
    }

    public void setPitchSustainLevel(double pitchSustainLevel) {
        pitchEnvelope1.getController(2).setValue(pitchSustainLevel);
        pitchEnvelope2.getController(2).setValue(pitchSustainLevel);
    }

    public double getPitchSustainLevel() {
        return pitchEnvelope1.getController(2).getValue();
    }

    public void setPitchReleaseLevel(double pitchReleaseLevel) {
        pitchEnvelope1.getController(3).setValue(pitchReleaseLevel);
        pitchEnvelope2.getController(3).setValue(pitchReleaseLevel);
    }

    public double getPitchReleaseLevel() {
        return pitchEnvelope1.getController(3).getValue();
    }

    public void setPitchAttackSpeed(double pitchAttackSpeed) {
        pitchEnvelope1.getController(4).setValue(pitchAttackSpeed);
        pitchEnvelope2.getController(4).setValue(pitchAttackSpeed);
    }

    public double getPitchAttackSpeed() {
        return pitchEnvelope1.getController(4).getValue();
    }

    public void setPitchDecaySpeed(double pitchDecaySpeed) {
        pitchEnvelope1.getController(5).setValue(pitchDecaySpeed);
        pitchEnvelope2.getController(5).setValue(pitchDecaySpeed);
    }

    public double getPitchDecaySpeed() {
        return pitchEnvelope1.getController(5).getValue();
    }

    public void setPitchSustainSpeed(double pitchSustainSpeed) {
        pitchEnvelope1.getController(6).setValue(pitchSustainSpeed);
        pitchEnvelope2.getController(6).setValue(pitchSustainSpeed);
    }

    public double getPitchSustainSpeed() {
        return pitchEnvelope1.getController(6).getValue();
    }

    public void setPitchReleaseSpeed(double pitchReleaseSpeed) {
        pitchEnvelope1.getController(7).setValue(pitchReleaseSpeed);
        pitchEnvelope2.getController(7).setValue(pitchReleaseSpeed);
    }

    public double getPitchReleaseSpeed() {
        return pitchEnvelope1.getController(7).getValue();
    }
    //</editor-fold>

    //<editor-fold desc="volume settings" defaultstate="collapsed">
    public double getOscillator1Volume() {
        return oscillator1.getController(3).getValue();
    }

    // 0 ~ 1
    public void setOscillator1Volume(double oscillator1Volume) {
        oscillator1.getController(3).setValue(oscillator1Volume);
    }

    public double getOscillator2Volume() {
        return oscillator2.getController(3).getValue();
    }

    // 0 ~ 1
    public void setOscillator2Volume(double oscillator2Volume) {
        oscillator2.getController(3).setValue(oscillator2Volume);
    }

    public double getOscillator3Volume() {
        return oscillator3.getController(3).getValue();
    }

    // 0 ~ 1
    public void setOscillator3Volume(double oscillator3Volume) {
        oscillator3.getController(3).setValue(oscillator3Volume);
    }

    public double getNoiseVolume() {
        return noise.getController(3).getValue();
    }

    // 0 ~ 1
    public void setNoiseVolume(double noiseVolume) {
        noise.getController(3).setValue(noiseVolume);
    }

    public double getGlobalVolume() {
        return mainMixer.getController(0).getValue();
    }

    // 0 ~ 1
    public void setGlobalVolume(double globalVolume) {
        mainMixer.getController(0).setValue(globalVolume);
        mainMixer.getController(1).setValue(globalVolume);
    }
    //</editor-fold>

    //<editor-fold desc="filter settings" defaultstate="collapsed">
    public double getFilterValue() {
        return lowPassFilter1.getController(0).getValue();
    }

    // 0 ~ 1
    public void setFilterCutoff(double cutoff) {
        lowPassFilter1.getController(0).setValue(cutoff);
        lowPassFilter2.getController(0).setValue(cutoff);
    }

    public double getFilterCutoff() {
        return lowPassFilter1.getController(0).getValue();
    }

    public double getFilterCutoffFrequency() {
        return lowPassFilter1.getController(0).getDisplayValue();
    }

    // 0 ~ 1
    public void setFilterResonance(double resonance) {
        lowPassFilter1.getController(1).setValue(resonance);
        lowPassFilter2.getController(1).setValue(resonance);
    }

    public double getFilterRessonance() {
        return lowPassFilter1.getController(1).getValue();
    }

    public void setFilterAttackLevel(double filterAttackLevel) {
        filterEnvelope1.getController(0).setValue(filterAttackLevel);
        filterEnvelope2.getController(0).setValue(filterAttackLevel);
    }

    public double getFilterAttackLevel() {
        return filterEnvelope1.getController(0).getValue();
    }

    public void setFilterDecayLevel(double filterDecayLevel) {
        filterEnvelope1.getController(1).setValue(filterDecayLevel);
        filterEnvelope2.getController(1).setValue(filterDecayLevel);
    }

    public double getFilterDecayLevel() {
        return filterEnvelope1.getController(1).getValue();
    }

    public void setFilterSustainLevel(double filterSustainLevel) {
        filterEnvelope1.getController(2).setValue(filterSustainLevel);
        filterEnvelope2.getController(2).setValue(filterSustainLevel);
    }

    public double getFilterSustainLevel() {
        return filterEnvelope1.getController(2).getValue();
    }

    public void setFilterReleaseLevel(double filterReleaseLevel) {
        filterEnvelope1.getController(3).setValue(filterReleaseLevel);
        filterEnvelope2.getController(3).setValue(filterReleaseLevel);
    }

    public double getFilterReleaseLevel() {
        return filterEnvelope1.getController(3).getValue();
    }

    public void setFilterAttackSpeed(double filterAttackSpeed) {
        filterEnvelope1.getController(4).setValue(filterAttackSpeed);
        filterEnvelope2.getController(4).setValue(filterAttackSpeed);
    }

    public double getFilterAttackSpeed() {
        return filterEnvelope1.getController(4).getValue();
    }

    public void setFilterDecaySpeed(double filterDecaySpeed) {
        filterEnvelope1.getController(5).setValue(filterDecaySpeed);
        filterEnvelope2.getController(5).setValue(filterDecaySpeed);
    }

    public double getFilterDecaySpeed() {
        return filterEnvelope1.getController(5).getValue();
    }

    public void setFilterSustainSpeed(double filterSustainSpeed) {
        filterEnvelope1.getController(6).setValue(filterSustainSpeed);
        filterEnvelope2.getController(6).setValue(filterSustainSpeed);
    }

    public double getFilterSustainSpeed() {
        return filterEnvelope1.getController(6).getValue();
    }

    public void setFilterReleaseSpeed(double filterReleaseSpeed) {
        filterEnvelope1.getController(7).setValue(filterReleaseSpeed);
        filterEnvelope2.getController(7).setValue(filterReleaseSpeed);
    }

    public double getFilterReleaseSpeed() {
        return filterEnvelope1.getController(7).getValue();
    }
    //</editor-fold>

}
