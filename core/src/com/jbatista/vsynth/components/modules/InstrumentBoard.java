package com.jbatista.vsynth.components.modules;

import com.jbatista.bricks.Instrument;
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

    private final Passthrough noisePassthrough = new Passthrough(instrument);
    private final Passthrough oscillatorFmPassthrough = new Passthrough(instrument);

    private final Mixer oscillatorMixer1 = new Mixer(instrument);
    private final Mixer oscillatorMixer2 = new Mixer(instrument);
    private final Mixer oscillatorFmMixer1 = new Mixer(instrument);
    private final Mixer oscillatorFmMixer2 = new Mixer(instrument);

    private final Mixer mainMixer = new Mixer(instrument);

    private final EnvelopeGenerator filterEnvelope1 = new EnvelopeGenerator(instrument);
    private final EnvelopeGenerator filterEnvelope2 = new EnvelopeGenerator(instrument);

    private final LowPassFilter lowPassFilter1 = new LowPassFilter(instrument);
    private final LowPassFilter lowPassFilter2 = new LowPassFilter(instrument);

    private final Patch[] patches = new Patch[23];

    private boolean key1ZeroFreq = false;
    private boolean key2ZeroFreq = false;

    private double oscillatorModulationWheel = 0;
    private double filterModulationWheel = 0;

    private int octaveOffset1 = 2;
    private int octaveOffset2 = 2;
    private double oscillator2fineTune = 1;

    private boolean onlyModulateOscillator2 = false;

    public InstrumentBoard() {
        for (int i = 0; i < patches.length; i++) patches[i] = new Patch();

        // oscillators activation
        oscillator3.getInput(0).connectPatch(new Patch());
        noise.getInput(0).connectPatch(new Patch());

        // oscillators interaction
        oscillator1.getOutput(0).connectPatch(patches[0]);
        oscillator3.getInput(0).connectPatch(patches[0]);

        oscillator3.getController(0).setValue(1);
        oscillator3.getController(2).setValue(0.5);

        // lfo interaction
        lfo.getOutput(0).connectPatch(patches[1]);
        lfo.getOutput(1).connectPatch(patches[2]);

        oscillatorFmMixer1.getInput(0).connectPatch(patches[1]);
        oscillatorFmMixer2.getInput(0).connectPatch(patches[2]);

        oscillatorFmMixer1.getOutput(0).connectPatch(patches[3]);
        oscillatorFmMixer2.getOutput(0).connectPatch(patches[4]);

        oscillatorFmPassthrough.getInput(0).connectPatch(patches[3]);

        oscillatorFmPassthrough.getOutput(0).connectPatch(patches[5]);
        oscillatorFmPassthrough.getOutput(1).connectPatch(patches[6]);

        oscillator1.getInput(1).connectPatch(patches[5]);
        oscillator2.getInput(1).connectPatch(patches[4]);
        oscillator3.getInput(1).connectPatch(patches[6]);

        // noise setup: always active, starts muted
        noise.getInput(0).write(1);
        noise.getController(0).setValue(5);
        noise.getController(4).setValue(0);

        noise.getOutput(1).connectPatch(patches[7]);
        noisePassthrough.getInput(0).connectPatch(patches[7]);

        // mixer setup: oscillator 1, 3, noise
        oscillator1.getOutput(1).connectPatch(patches[8]);
        oscillator3.getOutput(1).connectPatch(patches[9]);
        noisePassthrough.getOutput(0).connectPatch(patches[10]);

        oscillatorMixer1.getController(0).setValue(0.05);
        oscillatorMixer1.getController(1).setValue(0.05);
        oscillatorMixer1.getController(2).setValue(0.05);
        oscillatorMixer1.getInput(0).connectPatch(patches[8]);
        oscillatorMixer1.getInput(1).connectPatch(patches[9]);
        oscillatorMixer1.getInput(2).connectPatch(patches[10]);

        // mixer setup: oscillator 2, noise
        oscillator2.getOutput(1).connectPatch(patches[11]);
        noisePassthrough.getOutput(1).connectPatch(patches[12]);

        oscillatorMixer2.getController(0).setValue(0.05);
        oscillatorMixer2.getController(1).setValue(0.05);
        oscillatorMixer2.getInput(0).connectPatch(patches[11]);
        oscillatorMixer2.getInput(1).connectPatch(patches[12]);

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
    }

    public synchronized void pressKey1(double frequency) {
        key1ZeroFreq = (frequency == 0);

        pitchEnvelope1.getInput(0).write(key1ZeroFreq ? 0 : (frequency * OCTAVE_RATIOS[octaveOffset1]));
        pitchEnvelope1.getInput(1).write(key1ZeroFreq ? 0 : 1);

        oscillatorEnvelope1.getInput(1).write(key1ZeroFreq ? 0 : 1);

        filterEnvelope1.getInput(1).write(key1ZeroFreq ? 0 : 1);
    }

    public synchronized void pressKey2(double frequency) {
        key2ZeroFreq = (frequency == 0);

        pitchEnvelope2.getInput(0).write(key2ZeroFreq ? 0 : (frequency * OCTAVE_RATIOS[octaveOffset2] * oscillator2fineTune));
        pitchEnvelope2.getInput(1).write(key2ZeroFreq ? 0 : 1);

        oscillatorEnvelope2.getInput(1).write(key2ZeroFreq ? 0 : 1);

        filterEnvelope2.getInput(1).write(key2ZeroFreq ? 0 : 1);
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

    //<editor-fold desc="modulation settings">
    // 0, 1, 2, 3, 4
    public void setLfoShape(int shape) {
        lfo.getController(0).setValue(shape);
    }

    public int getLfoShape() {
        return (int) lfo.getController(0).getValue();
    }

    public void onlyModulateOscillator2(boolean value) {
        this.onlyModulateOscillator2 = value;
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

    public double getOscillatorModulationWheel() {
        return oscillatorModulationWheel;
    }

    // -1 ~ 1
    public void setOscillatorModulationWheel(double oscillatorModulationWheel) {
        this.oscillatorModulationWheel = Math.max(-1, Math.min(oscillatorModulationWheel, 1));

        if (!onlyModulateOscillator2) oscillatorFmMixer1.getInput(1).write(this.oscillatorModulationWheel);
        oscillatorFmMixer2.getInput(1).write(this.oscillatorModulationWheel);
    }

    public double getFilterModulationWheel() {
        return filterModulationWheel;
    }

    // -1 ~ 1
    public void setFilterModulationWheel(double filterModulationWheel) {
        this.filterModulationWheel = Math.max(-1, Math.min(filterModulationWheel, 1));

        lowPassFilter1.getInput(1).write(this.filterModulationWheel);
        lowPassFilter2.getInput(1).write(this.filterModulationWheel);
    }
    //</editor-fold>

    //<editor-fold desc="oscillator settings">
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
    }

    public int getOctaveOffset1() {
        return octaveOffset1;
    }

    // 0, 1, 2, 3, 4
    public void setOctaveOffset2(int octaveOffset2) {
        this.octaveOffset2 = Math.max(0, Math.min(octaveOffset2, 4));
    }

    public int getOctaveOffset2() {
        return octaveOffset2;
    }

    public double getOscillator2fineTune() {
        return oscillator2fineTune;
    }

    public void setOscillator2fineTune(double oscillator2fineTune) {
        this.oscillator2fineTune = Math.max(0.5, Math.min(oscillator2fineTune, 1.5));
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

    //<editor-fold desc="pitch envelope">
    public void setPitchAttackLevel1(double pitchAttackLevel) {
        pitchEnvelope1.getController(0).setValue(pitchAttackLevel);
    }

    public double getPitchAttackLevel1() {
        return pitchEnvelope1.getController(0).getValue();
    }

    public void setPitchDecayLevel1(double pitchDecayLevel) {
        pitchEnvelope1.getController(1).setValue(pitchDecayLevel);
    }

    public double getPitchDecayLevel1() {
        return pitchEnvelope1.getController(1).getValue();
    }

    public void setPitchSustainLevel1(double pitchSustainLevel) {
        pitchEnvelope1.getController(2).setValue(pitchSustainLevel);
    }

    public double getPitchSustainLevel1() {
        return pitchEnvelope1.getController(2).getValue();
    }

    public void setPitchReleaseLevel1(double pitchReleaseLevel) {
        pitchEnvelope1.getController(3).setValue(pitchReleaseLevel);
    }

    public double getPitchReleaseLevel1() {
        return pitchEnvelope1.getController(3).getValue();
    }

    public void setPitchAttackSpeed1(double pitchAttackSpeed) {
        pitchEnvelope1.getController(4).setValue(pitchAttackSpeed);
    }

    public double getPitchAttackSpeed1() {
        return pitchEnvelope1.getController(4).getValue();
    }

    public void setPitchDecaySpeed1(double pitchDecaySpeed) {
        pitchEnvelope1.getController(5).setValue(pitchDecaySpeed);
    }

    public double getPitchDecaySpeed1() {
        return pitchEnvelope1.getController(5).getValue();
    }

    public void setPitchSustainSpeed1(double pitchSustainSpeed) {
        pitchEnvelope1.getController(6).setValue(pitchSustainSpeed);
    }

    public double getPitchSustainSpeed1() {
        return pitchEnvelope1.getController(6).getValue();
    }

    public void setPitchReleaseSpeed1(double pitchReleaseSpeed) {
        pitchEnvelope1.getController(7).setValue(pitchReleaseSpeed);
    }

    public double getPitchReleaseSpeed1() {
        return pitchEnvelope1.getController(7).getValue();
    }

    public void setPitchAttackLevel2(double pitchAttackLevel) {
        pitchEnvelope2.getController(0).setValue(pitchAttackLevel);
    }

    public double getPitchAttackLevel2() {
        return pitchEnvelope2.getController(0).getValue();
    }

    public void setPitchDecayLevel2(double pitchDecayLevel) {
        pitchEnvelope2.getController(1).setValue(pitchDecayLevel);
    }

    public double getPitchDecayLevel2() {
        return pitchEnvelope2.getController(1).getValue();
    }

    public void setPitchSustainLevel2(double pitchSustainLevel) {
        pitchEnvelope2.getController(2).setValue(pitchSustainLevel);
    }

    public double getPitchSustainLevel2() {
        return pitchEnvelope2.getController(2).getValue();
    }

    public void setPitchReleaseLevel2(double pitchReleaseLevel) {
        pitchEnvelope2.getController(3).setValue(pitchReleaseLevel);
    }

    public double getPitchReleaseLevel2() {
        return pitchEnvelope2.getController(3).getValue();
    }

    public void setPitchAttackSpeed2(double pitchAttackSpeed) {
        pitchEnvelope2.getController(4).setValue(pitchAttackSpeed);
    }

    public double getPitchAttackSpeed2() {
        return pitchEnvelope2.getController(4).getValue();
    }

    public void setPitchDecaySpeed2(double pitchDecaySpeed) {
        pitchEnvelope2.getController(5).setValue(pitchDecaySpeed);
    }

    public double getPitchDecaySpeed2() {
        return pitchEnvelope2.getController(5).getValue();
    }

    public void setPitchSustainSpeed2(double pitchSustainSpeed) {
        pitchEnvelope2.getController(6).setValue(pitchSustainSpeed);
    }

    public double getPitchSustainSpeed2() {
        return pitchEnvelope2.getController(6).getValue();
    }

    public void setPitchReleaseSpeed2(double pitchReleaseSpeed) {
        pitchEnvelope2.getController(7).setValue(pitchReleaseSpeed);
    }

    public double getPitchReleaseSpeed2() {
        return pitchEnvelope2.getController(7).getValue();
    }
    //</editor-fold>

    //<editor-fold desc="volume settings">
    public double getOscillator1Volume() {
        return oscillator1.getController(4).getValue();
    }

    // 0 ~ 1
    public void setOscillator1Volume(double oscillator1Volume) {
        oscillator1.getController(4).setValue(oscillator1Volume);
    }

    public double getOscillator2Volume() {
        return oscillator2.getController(4).getValue();
    }

    // 0 ~ 1
    public void setOscillator2Volume(double oscillator2Volume) {
        oscillator2.getController(4).setValue(oscillator2Volume);
    }

    public double getOscillator3Volume() {
        return oscillator3.getController(4).getValue();
    }

    // 0 ~ 1
    public void setOscillator3Volume(double oscillator3Volume) {
        oscillator3.getController(4).setValue(oscillator3Volume);
    }

    public double getNoiseVolume() {
        return noise.getController(4).getValue();
    }

    // 0 ~ 1
    public void setNoiseVolume(double noiseVolume) {
        noise.getController(4).setValue(noiseVolume);
    }
    //</editor-fold>

    //<editor-fold desc="filter settings">
    public double getFilterValue() {
        return lowPassFilter1.getController(0).getValue();
    }

    // 0 ~ 1
    public void setFilterValue(double filterValue) {
        lowPassFilter1.getController(0).setValue(filterValue);
        lowPassFilter2.getController(0).setValue(filterValue);
    }

    public double getFilterFrequency() {
        return lowPassFilter1.getController(0).getDisplayValue();
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
