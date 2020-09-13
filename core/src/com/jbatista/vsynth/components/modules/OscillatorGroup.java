package com.jbatista.vsynth.components.modules;

import com.jbatista.bricks.components.OutputConnector;
import com.jbatista.bricks.components.Patch;
import com.jbatista.bricks.components.builtin.*;

public class OscillatorGroup {

    private static final double[] OCTAVE_RATIOS = new double[]{0.25, 0.5, 1, 1.5, 2.0};

    private final EnvelopeGenerator oscillatorEnvelope1 = new EnvelopeGenerator();
    private final EnvelopeGenerator oscillatorEnvelope2 = new EnvelopeGenerator();

    private final Oscillator oscillator1 = new Oscillator();
    private final Oscillator oscillator2 = new Oscillator();
    private final Oscillator oscillator3 = new Oscillator();
    private final Oscillator noise = new Oscillator();
    private final Lfo lfo = new Lfo();

    private final Passthrough noisePassthrough = new Passthrough();
    private final Passthrough oscillatorFmPassthrough = new Passthrough();

    private final Mixer oscillatorMixer1 = new Mixer();
    private final Mixer oscillatorMixer2 = new Mixer();
    private final Mixer oscillatorFmMixer1 = new Mixer();
    private final Mixer oscillatorFmMixer2 = new Mixer();

    private final Mixer mainMixer = new Mixer();

    private final EnvelopeGenerator filterEnvelope1 = new EnvelopeGenerator();
    private final EnvelopeGenerator filterEnvelope2 = new EnvelopeGenerator();

    private final LowPassFilter lowPassFilter1 = new LowPassFilter();
    private final LowPassFilter lowPassFilter2 = new LowPassFilter();

    private final Patch[] patches = new Patch[21];
    private final Patch[] oscillatorPatches = new Patch[4];

    private double key1Frequency;
    private double key2Frequency;

    private double oscillatorModulationWheel = 0;
    private double filterModulationWheel = 0;

    private int octaveOffset1 = 2;
    private int octaveOffset2 = 2;
    private double oscillator2fineTune = 1;

    private boolean onlyModulateOscillator2 = false;

    public OscillatorGroup() {
        for (int i = 0; i < patches.length; i++) patches[i] = new Patch();
        for (int i = 0; i < oscillatorPatches.length; i++) oscillatorPatches[i] = new Patch();

        // oscillators activation
        oscillator1.getInput(0).connectPatch(oscillatorPatches[0]);
        oscillator2.getInput(0).connectPatch(oscillatorPatches[1]);
        oscillator3.getInput(0).connectPatch(oscillatorPatches[2]);
        noise.getInput(0).connectPatch(oscillatorPatches[3]);


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

        lowPassFilter1.getOutput(0).connectPatch(patches[17]);
        lowPassFilter2.getOutput(0).connectPatch(patches[18]);

        filterEnvelope1.getInput(0).connectPatch(patches[17]);
        filterEnvelope2.getInput(0).connectPatch(patches[18]);

        // mixer setup: main
        filterEnvelope1.getOutput(0).connectPatch(patches[19]);
        filterEnvelope2.getOutput(0).connectPatch(patches[20]);

        mainMixer.getInput(0).connectPatch(patches[19]);
        mainMixer.getInput(1).connectPatch(patches[20]);
    }

    public void pressKey1(double frequency) {
        key1Frequency = frequency * OCTAVE_RATIOS[octaveOffset1];

        oscillator1.getInput(0).write(key1Frequency);
        oscillatorEnvelope1.getInput(1).write(1);
        filterEnvelope1.getInput(1).write(1);
    }

    public void pressKey2(double frequency) {
        key2Frequency = frequency * OCTAVE_RATIOS[octaveOffset2] * oscillator2fineTune;

        oscillator2.getInput(0).write(key2Frequency);
        oscillatorEnvelope2.getInput(1).write(1);
        filterEnvelope2.getInput(1).write(1);
    }

    public OutputConnector getSoundOutput() {
        return mainMixer.getOutput(0);
    }

    // modulation settings
    // 0, 1, 2, 3, 4
    public void setLfoShape(int shape) {
        lfo.getController(0).setValue(shape);
    }

    public int getLfoShape() {
        return (int) lfo.getController(0).getDisplayValue();
    }

    public void onlyModulateOscillator2(boolean value) {
        this.onlyModulateOscillator2 = value;
    }

    public boolean isOnlyModulateOscillator2() {
        return onlyModulateOscillator2;
    }

    // 0 ~ 1
    public void setLfoFrequency(double value) {
        lfo.getController(1).setValue(value);
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

    // oscillators
    // 0, 1, 2, 3, 4
    public void setOscillator1Shape(int shape) {
        oscillator1.getController(0).setValue(shape);
    }

    public int getOscillator1Shape() {
        return (int) oscillator1.getController(0).getDisplayValue();
    }

    // 0, 1, 2, 3, 4
    public void setOscillator2Shape(int shape) {
        oscillator2.getController(0).setValue(shape);
    }

    public int getOscillator2Shape() {
        return (int) oscillator2.getController(0).getDisplayValue();
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

    // volume settings
    public double getOscillator1Volume() {
        return oscillator1.getController(4).getDisplayValue();
    }

    // 0 ~ 1
    public void setOscillator1Volume(double oscillator1Volume) {
        oscillator1.getController(4).setValue(oscillator1Volume);
    }

    public double getOscillator2Volume() {
        return oscillator2.getController(4).getDisplayValue();
    }

    // 0 ~ 1
    public void setOscillator2Volume(double oscillator2Volume) {
        oscillator2.getController(4).setValue(oscillator2Volume);
    }

    public double getOscillator3Volume() {
        return oscillator3.getController(4).getDisplayValue();
    }

    // 0 ~ 1
    public void setOscillator3Volume(double oscillator3Volume) {
        oscillator3.getController(4).setValue(oscillator3Volume);
    }

    public double getNoiseVolume() {
        return noise.getController(4).getDisplayValue();
    }

    // 0 ~ 1
    public void setNoiseVolume(double noiseVolume) {
        noise.getController(4).setValue(noiseVolume);
    }

}