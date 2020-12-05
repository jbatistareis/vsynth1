package com.jbatista.vsynth.components.panels;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.jbatista.vsynth.components.modules.InstrumentBoard;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisWindow;

public class ModulationPanel extends VisWindow {

    private final InstrumentBoard instrumentBoard;

    private final VerticalGroup lfoGroup = new VerticalGroup();
    private final VisLabel lblLfoFreq = new VisLabel("LFO frequency");
    private final VisSlider sldLfoFreq = new VisSlider(0, 1, 0.01f, false);
    private final VisLabel lblLfoShape = new VisLabel("Shape");
    private final VisSlider sldLfoShape = new VisSlider(0, 4, 1, false);
    private final VisCheckBox chkLfoModOsc2 = new VisCheckBox("Mod. only OSC 2");

    public ModulationPanel(InstrumentBoard instrumentBoard) {
        super("Modulation");
        setMovable(false);
        this.instrumentBoard = instrumentBoard;

        // layout
        lfoGroup.addActor(lblLfoFreq);
        lfoGroup.addActor(sldLfoFreq);
        lfoGroup.addActor(lblLfoShape);
        lfoGroup.addActor(sldLfoShape);
        lfoGroup.addActor(chkLfoModOsc2);
        add(lfoGroup);

        // functions
        sldLfoFreq.setValue((float) instrumentBoard.getLfoValue());
        sldLfoShape.setValue(instrumentBoard.getLfoShape());
        chkLfoModOsc2.setChecked(instrumentBoard.isOnlyModulateOscillator2());

        sldLfoFreq.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setLfoValue(sldLfoFreq.getValue());
            }
        });

        sldLfoShape.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setLfoShape((int) sldLfoShape.getValue());
            }
        });

        chkLfoModOsc2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.onlyModulateOscillator2(chkLfoModOsc2.isChecked());
            }
        });
    }

}
