package com.jbatista.vsynth.components.modules;

import com.badlogic.gdx.InputProcessor;
import com.jbatista.bricks.components.Patch;
import com.jbatista.bricks.components.builtin.SoundOut;
import com.jbatista.vsynth.components.panels.*;
import com.kotcrab.vis.ui.widget.Separator;
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
    private final KeyboardInputProcessor keyboardInputProcessor;

    private final Patch patch = new Patch();
    private final double[] frame = new double[2];

    private int bufferIndex;
    private Mode mode = Mode.MONO;

    public Rack(InstrumentBoard instrumentBoard) {
        this.instrumentBoard = instrumentBoard;

        soundOut = new SoundOut(this.instrumentBoard.getInstrument());

        envelopesPanel = new EnvelopesPanel(this.instrumentBoard);
        filterPanel = new FilterPanel(this.instrumentBoard);
        keyboardPanel = new KeyboardPanel(this.instrumentBoard);
        mixerPanel = new MixerPanel(this.instrumentBoard);
        modulationPanel = new ModulationPanel(this.instrumentBoard);
        oscillatorsPanel = new OscillatorsPanel(this.instrumentBoard);
        keyboardInputProcessor = new KeyboardInputProcessor(keyboardPanel);

        this.instrumentBoard.getSoundOutput().connectPatch(patch);
        soundOut.getInput(0).connectPatch(patch);

        add(modulationPanel).center();
        add(new Separator()).padLeft(4).padRight(4).fillY();
        add(oscillatorsPanel).center();
        add(new Separator()).padLeft(4).padRight(4).fillY();
        add(filterPanel).center();
        add(new Separator()).padLeft(4).padRight(4).fillY();
        add(envelopesPanel).center();
        add(new Separator()).padLeft(4).padRight(4).fillY();
        add(mixerPanel).center().row();

        add(keyboardPanel).colspan(11).padTop(10).center().row();

        setMode(Mode.MONO);
    }

    public void getFrame(float[] buffer, int size) {
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
                instrumentBoard.setMono();
                break;

            case POLY:
                instrumentBoard.setPoly();
                break;
        }
    }

    public InputProcessor getInputProcessor() {
        return keyboardInputProcessor;
    }

}
