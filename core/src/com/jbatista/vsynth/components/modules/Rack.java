package com.jbatista.vsynth.components.modules;

import com.jbatista.bricks.KeyboardNote;
import com.jbatista.bricks.components.Patch;
import com.jbatista.bricks.components.builtin.Keyboard;
import com.jbatista.bricks.components.builtin.SoundOut;

public class Rack {

    public enum Mode {MONO, POLY}

    private final InstrumentBoard instrumentBoard = new InstrumentBoard();
    private final Keyboard keyboard = new Keyboard(instrumentBoard.getInstrument());
    private final SoundOut soundOut = new SoundOut(instrumentBoard.getInstrument());

    private final Patch patch = new Patch();
    private final double[] frame = new double[2];

    private int bufferIndex;
    private Mode mode = Mode.MONO;

    public Rack() {
        instrumentBoard.getSoundOutput().connectPatch(patch);
        soundOut.getInput(0).connectPatch(patch);
    }

    public void getFrame(double[] buffer, int size) {
        instrumentBoard.pressKey1(keyboard.getOutput(0).read());
        instrumentBoard.pressKey2(keyboard.getOutput(mode.equals(Mode.MONO) ? 0 : 1).read());

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
