package com.jbatista.vsynth.components.modules;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.jbatista.bricks.components.Patch;
import com.jbatista.bricks.components.builtin.SoundOut;
import com.jbatista.vsynth.components.panels.*;
import com.kotcrab.vis.ui.widget.VisTable;

public class Rack extends VisTable {

    public enum Mode {MONO, POLY}

    private final InstrumentBoard instrumentBoard;
    private final SoundOut soundOut;

    private final EnvelopesPanel envelopesPanel;
    private final FilterPanel filterPanel;
    private final KeyboardPanel keyboardPanel;
    private final MixerPanel mixerPanel;
    private final ModulationPanel modulationPanel;
    private final OscillatorsPanel oscillatorsPanel;
    private final PitchPanel pitchPanel;
    private final KeyboardInputProcessor keyboardInputProcessor;

    private final HorizontalGroup keyboardArea = new HorizontalGroup();

    private final Patch patch = new Patch();
    private final double[] frame = new double[2];

    private int bufferIndex;
    private Mode mode = Mode.MONO;

    public Rack(InstrumentBoard instrumentBoard) {
        this.instrumentBoard = instrumentBoard;
        setFillParent(true);

        soundOut = new SoundOut(this.instrumentBoard.getInstrument());

        envelopesPanel = new EnvelopesPanel(this.instrumentBoard);
        filterPanel = new FilterPanel(this.instrumentBoard);
        keyboardPanel = new KeyboardPanel(this.instrumentBoard);
        mixerPanel = new MixerPanel(this.instrumentBoard);
        modulationPanel = new ModulationPanel(this.instrumentBoard);
        oscillatorsPanel = new OscillatorsPanel(this.instrumentBoard);
        pitchPanel = new PitchPanel(this.instrumentBoard);
        keyboardInputProcessor = new KeyboardInputProcessor(keyboardPanel);

        this.instrumentBoard.getSoundOutput().connectPatch(patch);
        soundOut.getInput(0).connectPatch(patch);

        add(modulationPanel).expand();
        add(oscillatorsPanel).expand();
        add(filterPanel).expand();
        add(envelopesPanel).expand();
        add(mixerPanel).expand().row();

        keyboardArea.space(5);
        keyboardArea.addActor(pitchPanel);
        keyboardArea.addActor(keyboardPanel);
        keyboardArea.expand();
        add(keyboardArea).colspan(5).expand().row();
    }

    public void getFrame(float[] buffer, int size) {
        instrumentBoard.pressKey1(keyboardPanel.getKeyboard().getOutput(0).read());
        instrumentBoard.pressKey2(keyboardPanel.getKeyboard().getOutput(mode.equals(Mode.MONO) ? 0 : 1).read());

        for (bufferIndex = 0; bufferIndex < size; bufferIndex += 2) {
            instrumentBoard.runInstrumentProcess();

            soundOut.getDoubleFrame(frame);
            buffer[bufferIndex] = (float) frame[0];
            buffer[bufferIndex + 1] = (float) frame[1];
        }
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;

        switch (this.mode) {
            case MONO:
                keyboardPanel.getKeyboard().getController(0).setValue(1);
                break;

            case POLY:
                keyboardPanel.getKeyboard().getController(0).setValue(2);
                break;
        }
    }

    public InputProcessor getInputProcessor() {
        return keyboardInputProcessor;
    }

}
