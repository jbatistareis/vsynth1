package com.jbatista.vsynth.components;

import com.jbatista.bricks.KeyboardNote;
import com.jbatista.bricks.components.Patch;
import com.jbatista.bricks.components.builtin.*;

public class OscillatorGroup {

    private final EnvelopeGenerator oscillatorEnvelope1 = new EnvelopeGenerator();
    private final EnvelopeGenerator oscillatorEnvelope2 = new EnvelopeGenerator();

    private final Oscillator oscillator1 = new Oscillator();
    private final Oscillator oscillator2 = new Oscillator();
    private final Oscillator oscillator3 = new Oscillator();
    private final Oscillator noise = new Oscillator();
    private final Lfo lfo = new Lfo();

    private final Passthrough noisePassthrough = new Passthrough();

    private final Mixer oscillatorMixer1 = new Mixer();
    private final Mixer oscillatorMixer2 = new Mixer();
    private final Mixer mainMixer = new Mixer();

    private final EnvelopeGenerator filterEnvelope1 = new EnvelopeGenerator();
    private final EnvelopeGenerator filterEnvelope2 = new EnvelopeGenerator();

    private final LowPassFilter lowPassFilter1 = new LowPassFilter();
    private final LowPassFilter lowPassFilter2 = new LowPassFilter();

    private final Patch[] patches = new Patch[20];

    private double key1Frequency;
    private double key2Frequency;

    public OscillatorGroup() {
        for (int i = 0; i < patches.length; i++) patches[i] = new Patch();

        // oscillators interaction
        oscillator1.getOutputs().get(0).connectPatch(patches[0]);
        oscillator3.getInputs().get(0).connectPatch(patches[0]);

        // lfo interaction
        lfo.getOutputs().get(0).connectPatch(patches[1]);
        lfo.getOutputs().get(1).connectPatch(patches[2]);
        lfo.getOutputs().get(2).connectPatch(patches[3]);

        oscillator1.getInputs().get(1).connectPatch(patches[1]);
        oscillator2.getInputs().get(1).connectPatch(patches[2]);
        oscillator3.getInputs().get(1).connectPatch(patches[3]);

        // noise setup: always active
        noise.getController(0).setValue(1);

        noise.getOutputs().get(1).connectPatch(patches[4]);
        noisePassthrough.getInputs().get(0).connectPatch(patches[4]);

        // mixer setup: oscillator 1, 3, noise
        oscillator1.getOutputs().get(1).connectPatch(patches[5]);
        oscillator3.getOutputs().get(1).connectPatch(patches[6]);
        noisePassthrough.getOutputs().get(0).connectPatch(patches[7]);

        oscillatorMixer1.getInputs().get(0).connectPatch(patches[5]);
        oscillatorMixer1.getInputs().get(1).connectPatch(patches[6]);
        oscillatorMixer1.getInputs().get(1).connectPatch(patches[7]);

        // mixer setup: oscillator 2, noise
        oscillator2.getOutputs().get(1).connectPatch(patches[8]);
        noisePassthrough.getOutputs().get(1).connectPatch(patches[9]);

        oscillatorMixer2.getInputs().get(0).connectPatch(patches[8]);
        oscillatorMixer2.getInputs().get(1).connectPatch(patches[9]);

        // oscillator envelope setup: AM
        oscillatorMixer1.getOutputs().get(0).connectPatch(patches[12]);
        oscillatorMixer2.getOutputs().get(0).connectPatch(patches[13]);

        oscillatorEnvelope1.getInputs().get(0).connectPatch(patches[12]);
        oscillatorEnvelope2.getInputs().get(0).connectPatch(patches[13]);

        // filter setup
        oscillatorEnvelope1.getOutputs().get(0).connectPatch(patches[14]);
        oscillatorEnvelope2.getOutputs().get(0).connectPatch(patches[15]);

        lowPassFilter1.getInputs().get(0).connectPatch(patches[14]);
        lowPassFilter2.getInputs().get(0).connectPatch(patches[15]);

        lowPassFilter1.getOutputs().get(0).connectPatch(patches[16]);
        lowPassFilter2.getOutputs().get(0).connectPatch(patches[17]);

        filterEnvelope1.getInputs().get(0).connectPatch(patches[16]);
        filterEnvelope2.getInputs().get(0).connectPatch(patches[17]);

        // mixer setup: main
        filterEnvelope1.getOutputs().get(0).connectPatch(patches[18]);
        filterEnvelope2.getOutputs().get(0).connectPatch(patches[19]);

        mainMixer.getInputs().get(0).connectPatch(patches[18]);
        mainMixer.getInputs().get(1).connectPatch(patches[19]);
    }

    public void pressKey1(KeyboardNote keyboardNote) {
        key1Frequency = keyboardNote.getFrequency();

        oscillator1.getInputs().get(0).write(key1Frequency);
        oscillatorEnvelope1.getInputs().get(1).write(key1Frequency);
        filterEnvelope1.getInputs().get(1).write(key1Frequency);
    }

    public void pressKey2(KeyboardNote keyboardNote) {
        key2Frequency = keyboardNote.getFrequency();

        oscillator2.getInputs().get(0).write(key2Frequency);
        oscillatorEnvelope2.getInputs().get(1).write(key2Frequency);
        filterEnvelope2.getInputs().get(1).write(key2Frequency);
    }

}
