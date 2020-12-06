package com.jbatista.vsynth.components.panels;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.jbatista.vsynth.components.modules.InstrumentBoard;
import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisWindow;

public class ModulationPanel extends VisWindow {

    private final InstrumentBoard instrumentBoard;

    private final VisSlider sldLfoFreq = new VisSlider(0, 1, 0.01f, false);
    private final VisSlider sldLfoShape = new VisSlider(0, 4, 1, false);
    private final VisCheckBox chkLfoModOsc2 = new VisCheckBox("Mod. only OSC 2");
    private final VisSlider sldOscillatorStr = new VisSlider(0, 1, 0.01f, false);
    private final VisSlider sldFilterStr = new VisSlider(0, 1, 0.01f, false);

    public ModulationPanel(InstrumentBoard instrumentBoard) {
        super("Modulation");
        setMovable(false);
        this.instrumentBoard = instrumentBoard;

        // layout
        add("LFO frequency").row();
        add(sldLfoFreq).row();
        add("Shape").row();
        add(sldLfoShape).row();
        add(chkLfoModOsc2).row();
        add(new Separator()).fillX().row();
        add("OSC mod str").row();
        add(sldOscillatorStr).row();
        add("Filter EG str").row();
        add(sldFilterStr).row();

        // functions
        sldLfoFreq.setValue((float) instrumentBoard.getLfoValue());
        sldLfoShape.setValue(instrumentBoard.getLfoShape());
        chkLfoModOsc2.setChecked(instrumentBoard.isOnlyModulateOscillator2());
        sldOscillatorStr.setValue((float) instrumentBoard.getOscillatorModStr());
        sldFilterStr.setValue((float) instrumentBoard.getFilterModStr());

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

        sldOscillatorStr.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setOscillatorModStr(sldOscillatorStr.getValue());
            }
        });

        sldFilterStr.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setFilterModStr(sldFilterStr.getValue());
            }
        });
    }

}
