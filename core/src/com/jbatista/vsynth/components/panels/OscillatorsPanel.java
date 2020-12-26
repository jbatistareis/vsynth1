package com.jbatista.vsynth.components.panels;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.jbatista.vsynth.components.modules.InstrumentBoard;
import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisWindow;

public class OscillatorsPanel extends VisWindow {

    private final InstrumentBoard instrumentBoard;

    private final VerticalGroup osc1Group = new VerticalGroup();
    private final VisLabel lblOsc1FineTune = new VisLabel("Fine tune");
    private final VisSlider sldOsc1FineTune = new VisSlider(0.5f, 1.5f, 0.025f, false);
    private final VisLabel lblOctave1 = new VisLabel("Octave");
    private final VisSlider sldOctave1 = new VisSlider(-2, 2, 1, false);
    private final VisLabel lblOsc1Shape = new VisLabel("Shape");
    private final VisSlider sldOsc1Shape = new VisSlider(0, 4, 1, false);

    private final VerticalGroup osc2Group = new VerticalGroup();
    private final VisLabel lblOsc2FineTune = new VisLabel("Fine tune");
    private final VisSlider sldOsc2FineTune = new VisSlider(0.5f, 1.5f, 0.025f, false);
    private final VisLabel lblOctave2 = new VisLabel("Octave");
    private final VisSlider sldOctave2 = new VisSlider(-2, 2, 1, false);
    private final VisLabel lblOsc2Shape = new VisLabel("Shape");
    private final VisSlider sldOsc2Shape = new VisSlider(0, 4, 1, false);

    public OscillatorsPanel(InstrumentBoard instrumentBoard) {
        super("Oscillators", false);
        setMovable(false);
        this.instrumentBoard = instrumentBoard;

        // layout
        add("OSC 1");
        add();
        add("OSC 2").row();

        osc1Group.align(Align.center);
        osc1Group.addActor(lblOsc1FineTune);
        osc1Group.addActor(sldOsc1FineTune);
        osc1Group.addActor(lblOctave1);
        osc1Group.addActor(sldOctave1);
        osc1Group.addActor(lblOsc1Shape);
        osc1Group.addActor(sldOsc1Shape);

        osc2Group.align(Align.center);
        osc2Group.addActor(lblOsc2FineTune);
        osc2Group.addActor(sldOsc2FineTune);
        osc2Group.addActor(lblOctave2);
        osc2Group.addActor(sldOctave2);
        osc2Group.addActor(lblOsc2Shape);
        osc2Group.addActor(sldOsc2Shape);

        add(osc1Group);
        add(new Separator()).padLeft(5).padRight(5).fillY();
        add(osc2Group).row();

        // functions
        sldOsc1FineTune.setValue((float) instrumentBoard.getOscillator1fineTune());
        sldOctave1.setValue(instrumentBoard.getOctaveOffset1() - 2);
        sldOsc1Shape.setValue(instrumentBoard.getOscillator1Shape());

        sldOsc2FineTune.setValue((float) instrumentBoard.getOscillator2fineTune());
        sldOctave2.setValue(instrumentBoard.getOctaveOffset2() - 2);
        sldOsc2Shape.setValue(instrumentBoard.getOscillator2Shape());

        sldOsc1FineTune.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setOscillator1fineTune(sldOsc1FineTune.getValue());
            }
        });
        sldOctave1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setOctaveOffset1((int) sldOctave1.getValue() + 2);
            }
        });
        sldOsc1Shape.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setOscillator1Shape((int) sldOsc1Shape.getValue());
            }
        });


        sldOsc2FineTune.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setOscillator2fineTune(sldOsc2FineTune.getValue());
            }
        });
        sldOctave2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setOctaveOffset2((int) sldOctave2.getValue() + 2);
            }
        });
        sldOsc2Shape.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setOscillator2Shape((int) sldOsc2Shape.getValue());
            }
        });
    }

}
