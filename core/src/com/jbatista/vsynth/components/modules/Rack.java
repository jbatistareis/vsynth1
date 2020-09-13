package com.jbatista.vsynth.components.modules;

import com.jbatista.bricks.KeyboardNote;
import com.jbatista.bricks.components.Patch;
import com.jbatista.bricks.components.builtin.Keyboard;
import com.jbatista.bricks.components.builtin.SoundOut;

public class Rack {

    public enum Mode {MONO, POLY}

    private final OscillatorGroup oscillatorGroup = new OscillatorGroup();
    private final Keyboard keyboard = new Keyboard();
    private final SoundOut soundOut = new SoundOut();

    private final Patch[] patches = new Patch[3];
    private final double[] frame = new double[2];

    private int bufferIndex;
    private Mode mode = Mode.MONO;

    public Rack() {
        for (int i = 0; i < patches.length; i++) patches[i] = new Patch();

        oscillatorGroup.getSoundOutput().connectPatch(patches[0]);
        soundOut.getInput(0).connectPatch(patches[0]);
    }

    public void getFrame(double[] buffer, int size) {
        oscillatorGroup.pressKey1(keyboard.getOutput(0).read());
        oscillatorGroup.pressKey2(keyboard.getOutput(mode.equals(Mode.MONO) ? 0 : 1).read());

        for (bufferIndex = 0; bufferIndex < size; bufferIndex += 2) {
            soundOut.getDoubleFrame(frame);
            buffer[bufferIndex] = frame[0];
            buffer[bufferIndex + 1] = frame[1];
        }
    }

    public void pressKey(KeyboardNote keyboardNote) {
        keyboard.pressKey(keyboardNote);
    }

    public void releaseKey(KeyboardNote keyboardNote) {
        keyboard.pressKey(keyboardNote);
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;

        switch (this.mode) {
            case MONO:
                keyboard.getController(0).setValue(1);
                break;

            case POLY:
                keyboard.getController(0).setValue(2);
                break;
        }
    }

}
