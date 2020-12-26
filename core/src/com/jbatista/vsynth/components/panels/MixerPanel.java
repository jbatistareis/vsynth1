package com.jbatista.vsynth.components.panels;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.jbatista.vsynth.components.modules.InstrumentBoard;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisWindow;

public class MixerPanel extends VisWindow {

    private final InstrumentBoard instrumentBoard;

    private final VisSlider sldOsc1 = new VisSlider(0, 1, 0.01f, false);
    private final VisSlider sldOsc11 = new VisSlider(0, 1, 0.01f, false);
    private final VisSlider sldOsc2 = new VisSlider(0, 1, 0.01f, false);
    private final VisSlider sldNoise = new VisSlider(0, 1, 0.01f, false);
    private final VisSlider sldGlobal = new VisSlider(0, 1, 0.01f, false);

    public MixerPanel(InstrumentBoard instrumentBoard) {
        super("Mixer", false);
        setMovable(false);
        this.instrumentBoard = instrumentBoard;

        // layout
        add("OSC 1").row();
        add(sldOsc1).row();

        add("OSC 1.1").row();
        add(sldOsc11).row();

        add("OSC 2").row();
        add(sldOsc2).row();

        add("Noise").row();
        add(sldNoise).row();

        add("Global").row();
        add(sldGlobal).row();

        sldOsc1.setValue((float) instrumentBoard.getOscillator1Volume());
        sldOsc11.setValue((float) instrumentBoard.getOscillator3Volume());
        sldOsc2.setValue((float) instrumentBoard.getOscillator2Volume());
        sldNoise.setValue((float) instrumentBoard.getNoiseVolume());
        sldGlobal.setValue((float) instrumentBoard.getGlobalVolume());


        sldOsc1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setOscillator1Volume(sldOsc1.getValue());
            }
        });

        sldOsc11.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setOscillator3Volume(sldOsc11.getValue());
            }
        });

        sldOsc2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setOscillator2Volume(sldOsc2.getValue());
            }
        });

        sldNoise.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setNoiseVolume(sldNoise.getValue());
            }
        });

        sldGlobal.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setGlobalVolume(sldGlobal.getValue());
            }
        });
    }

}
