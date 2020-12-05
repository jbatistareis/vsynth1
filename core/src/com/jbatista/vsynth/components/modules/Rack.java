package com.jbatista.vsynth.components.modules;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.jbatista.bricks.KeyboardNote;
import com.jbatista.bricks.components.Patch;
import com.jbatista.bricks.components.builtin.Keyboard;
import com.jbatista.bricks.components.builtin.SoundOut;
import com.jbatista.vsynth.components.panels.*;
import com.kotcrab.vis.ui.widget.VisTable;

public class Rack extends VisTable {

    public enum Mode {MONO, POLY}

    private final InstrumentBoard instrumentBoard;
    private final Keyboard keyboard;
    private final SoundOut soundOut;

    private final EnvelopesPanel envelopesPanel;
    private final FilterPanel filterPanel;
    private final KeyboardPanel keyboardPanel;
    private final MixerPanel mixerPanel;
    private final ModulationPanel modulationPanel;
    private final OscillatorsPanel oscillatorsPanel;
    private final OutputPanel outputPanel;
    private final PitchPanel pitchPanel;

    private final HorizontalGroup keyboardArea = new HorizontalGroup();

    private final Patch patch = new Patch();
    private final double[] frame = new double[2];

    private int bufferIndex;
    private Mode mode = Mode.MONO;

    public Rack(InstrumentBoard instrumentBoard) {
        this.instrumentBoard = instrumentBoard;
        setFillParent(true);

        keyboard = new Keyboard(this.instrumentBoard.getInstrument());
        soundOut = new SoundOut(this.instrumentBoard.getInstrument());

        envelopesPanel = new EnvelopesPanel(this.instrumentBoard);
        filterPanel = new FilterPanel(this.instrumentBoard);
        keyboardPanel = new KeyboardPanel(this.instrumentBoard);
        mixerPanel = new MixerPanel(this.instrumentBoard);
        modulationPanel = new ModulationPanel(this.instrumentBoard);
        oscillatorsPanel = new OscillatorsPanel(this.instrumentBoard);
        outputPanel = new OutputPanel(this.instrumentBoard);
        pitchPanel = new PitchPanel(this.instrumentBoard);

        this.instrumentBoard.getSoundOutput().connectPatch(patch);
        soundOut.getInput(0).connectPatch(patch);

        add(modulationPanel).expand();
        add(oscillatorsPanel).expand();
        add(filterPanel).expand();
        add(envelopesPanel).expand().row();

        add(mixerPanel).colspan(2).expand();
        add(outputPanel).colspan(2).expand().row();

        keyboardArea.space(4);
        keyboardArea.addActor(pitchPanel);
        keyboardArea.addActor(keyboardPanel);
        keyboardArea.expand();
        add(keyboardArea).colspan(4).expand().row();

        pressKey(KeyboardNote.A_4);
    }

    public void getFrame(float[] buffer, int size) {
        instrumentBoard.pressKey1(keyboard.getOutput(0).read());
        instrumentBoard.pressKey2(keyboard.getOutput(mode.equals(Mode.MONO) ? 0 : 1).read());

        for (bufferIndex = 0; bufferIndex < size; bufferIndex += 2) {
            instrumentBoard.runInstrumentProcess();

            soundOut.getDoubleFrame(frame);
            buffer[bufferIndex] = (float) frame[0];
            buffer[bufferIndex + 1] = (float) frame[1];
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
