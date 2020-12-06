package com.jbatista.vsynth.components.panels;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.jbatista.vsynth.components.modules.InstrumentBoard;
import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisWindow;

public class EnvelopesPanel extends VisWindow {

    private final InstrumentBoard instrumentBoard;

    private final HorizontalGroup grpOscLvl = new HorizontalGroup();
    private final VisSlider oscAtkLvl = new VisSlider(0, 1, 0.005f, true);
    private final VisSlider oscDecLvl = new VisSlider(0, 1, 0.005f, true);
    private final VisSlider oscSusLvl = new VisSlider(0, 1, 0.005f, true);
    private final VisSlider oscRelLvl = new VisSlider(0, 1, 0.005f, true);

    private final HorizontalGroup grpFilLvl = new HorizontalGroup();
    private final VisSlider filAtkLvl = new VisSlider(0, 1, 0.005f, true);
    private final VisSlider filDecLvl = new VisSlider(0, 1, 0.005f, true);
    private final VisSlider filSusLvl = new VisSlider(0, 1, 0.005f, true);
    private final VisSlider filRelLvl = new VisSlider(0, 1, 0.005f, true);

    private final HorizontalGroup grpPitLvl = new HorizontalGroup();
    private final VisSlider pitAtkLvl = new VisSlider(0, 1, 0.005f, true);
    private final VisSlider pitDecLvl = new VisSlider(0, 1, 0.005f, true);
    private final VisSlider pitSusLvl = new VisSlider(0, 1, 0.005f, true);
    private final VisSlider pitRelLvl = new VisSlider(0, 1, 0.005f, true);


    private final HorizontalGroup grpOscSpd = new HorizontalGroup();
    private final VisSlider oscAtkSpd = new VisSlider(0, 1, 0.005f, true);
    private final VisSlider oscDecSpd = new VisSlider(0, 1, 0.005f, true);
    private final VisSlider oscSusSpd = new VisSlider(0, 1, 0.005f, true);
    private final VisSlider oscRelSpd = new VisSlider(0, 1, 0.005f, true);

    private final HorizontalGroup grpFilSpd = new HorizontalGroup();
    private final VisSlider filAtkSpd = new VisSlider(0, 1, 0.005f, true);
    private final VisSlider filDecSpd = new VisSlider(0, 1, 0.005f, true);
    private final VisSlider filSusSpd = new VisSlider(0, 1, 0.005f, true);
    private final VisSlider filRelSpd = new VisSlider(0, 1, 0.005f, true);

    private final HorizontalGroup grpPitSpd = new HorizontalGroup();
    private final VisSlider pitAtkSpd = new VisSlider(0, 1, 0.005f, true);
    private final VisSlider pitDecSpd = new VisSlider(0, 1, 0.005f, true);
    private final VisSlider pitSusSpd = new VisSlider(0, 1, 0.005f, true);
    private final VisSlider pitRelSpd = new VisSlider(0, 1, 0.005f, true);

    public EnvelopesPanel(InstrumentBoard instrumentBoard) {
        super("Envelopes");
        setMovable(false);
        this.instrumentBoard = instrumentBoard;

        grpOscLvl.space(5);
        grpOscLvl.addActor(oscAtkLvl);
        grpOscLvl.addActor(oscDecLvl);
        grpOscLvl.addActor(oscSusLvl);
        grpOscLvl.addActor(oscRelLvl);

        grpFilLvl.space(5);
        grpFilLvl.addActor(filAtkLvl);
        grpFilLvl.addActor(filDecLvl);
        grpFilLvl.addActor(filSusLvl);
        grpFilLvl.addActor(filRelLvl);

        grpPitLvl.space(5);
        grpPitLvl.addActor(pitAtkLvl);
        grpPitLvl.addActor(pitDecLvl);
        grpPitLvl.addActor(pitSusLvl);
        grpPitLvl.addActor(pitRelLvl);


        grpOscSpd.space(5);
        grpOscSpd.addActor(oscAtkSpd);
        grpOscSpd.addActor(oscDecSpd);
        grpOscSpd.addActor(oscSusSpd);
        grpOscSpd.addActor(oscRelSpd);

        grpFilSpd.space(5);
        grpFilSpd.addActor(filAtkSpd);
        grpFilSpd.addActor(filDecSpd);
        grpFilSpd.addActor(filSusSpd);
        grpFilSpd.addActor(filRelSpd);

        grpPitSpd.space(5);
        grpPitSpd.addActor(pitAtkSpd);
        grpPitSpd.addActor(pitDecSpd);
        grpPitSpd.addActor(pitSusSpd);
        grpPitSpd.addActor(pitRelSpd);
        
        
        add("Oscillators").colspan(3).row();
        add(grpOscLvl);
        add(new Separator()).padLeft(5).padRight(5).fillY();
        add(grpOscSpd).row();
        add(new Separator()).colspan(3).fillX().row();

        add("Filter").colspan(3).row();
        add(grpFilLvl);
        add(new Separator()).padLeft(5).padRight(5).fillY();
        add(grpFilSpd).row();
        add(new Separator()).colspan(3).fillX().row();

        add("Pitch").colspan(3).row();
        add(grpPitLvl);
        add(new Separator()).padLeft(5).padRight(5).fillY();
        add(grpPitSpd).row();
        add(new Separator()).colspan(3).fillX().row();

        add("Level");
        add();
        add("Speed").row();
    }

}
