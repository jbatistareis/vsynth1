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

public class PitchPanel extends VisWindow {

    private final InstrumentBoard instrumentBoard;

    private final VerticalGroup osc1Group = new VerticalGroup();
    private final VisLabel lblOsc1FineTune = new VisLabel("OSC 1 fine tune");
    private final VisSlider sldOsc1FineTune = new VisSlider(0.5f, 1.5f, 0.025f, false);

    private final VerticalGroup octaveGroup1 = new VerticalGroup();
    private final VisLabel lblOctave1 = new VisLabel("Octave");
    private final VisSlider sldOctave1 = new VisSlider(-2, 2, 1, false);

    private final VerticalGroup osc2Group = new VerticalGroup();
    private final VisLabel lblOsc2FineTune = new VisLabel("OSC 2 fine tune");
    private final VisSlider sldOsc2FineTune = new VisSlider(0.5f, 1.5f, 0.025f, false);

    private final VerticalGroup octaveGroup2 = new VerticalGroup();
    private final VisLabel lblOctave2 = new VisLabel("Octave");
    private final VisSlider sldOctave2 = new VisSlider(-2, 2, 1, false);

    public PitchPanel(InstrumentBoard instrumentBoard) {
        super("Pitch");
        setMovable(false);
        this.instrumentBoard = instrumentBoard;

        // layout
        osc1Group.align(Align.center);
        osc1Group.addActor(lblOsc1FineTune);
        osc1Group.addActor(sldOsc1FineTune);
        add(osc1Group).row();

        octaveGroup1.align(Align.center);
        octaveGroup1.addActor(lblOctave1);
        octaveGroup1.addActor(sldOctave1);
        add(octaveGroup1).row();

        add(new Separator()).padTop(5).fillX().row();

        osc2Group.align(Align.center);
        osc2Group.addActor(lblOsc2FineTune);
        osc2Group.addActor(sldOsc2FineTune);
        add(osc2Group).row();

        octaveGroup2.align(Align.center);
        octaveGroup2.addActor(lblOctave2);
        octaveGroup2.addActor(sldOctave2);
        add(octaveGroup2).row();

        // functions
        sldOsc1FineTune.setValue((float) instrumentBoard.getOscillator1fineTune());
        sldOctave1.setValue(instrumentBoard.getOctaveOffset1() - 2);

        sldOsc2FineTune.setValue((float) instrumentBoard.getOscillator2fineTune());
        sldOctave2.setValue(instrumentBoard.getOctaveOffset2() - 2);

        sldOsc1FineTune.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setOscillator1fineTune(sldOsc1FineTune.getValue() + 2);
            }
        });

        sldOctave1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setOctaveOffset1((int) sldOctave1.getValue() + 2);
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
    }

}
