package com.jbatista.vsynth.components.panels;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.jbatista.vsynth.components.modules.InstrumentBoard;
import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;

public class EnvelopesPanel extends VisTable {

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


        add("Oscillators").colspan(3).padRight(20);
        add("Filter").colspan(3).padRight(20);
        add("Pitch").colspan(3).row();

        add(grpOscLvl);
        add(new Separator()).padLeft(5).padRight(5).fillY();
        add(grpOscSpd).padRight(20);

        add(grpFilLvl);
        add(new Separator()).padLeft(5).padRight(5).fillY();
        add(grpFilSpd).padRight(20);

        add(grpPitLvl);
        add(new Separator()).padLeft(5).padRight(5).fillY();
        add(grpPitSpd).row();

        add("Level");
        add(new Separator());
        add("Speed").padRight(20);

        add("Level");
        add(new Separator());
        add("Speed").padRight(20);

        add("Level");
        add(new Separator());
        add("Speed").row();

        oscAtkLvl.setValue((float) instrumentBoard.getOscillatorAttackLevel());
        oscDecLvl.setValue((float) instrumentBoard.getOscillatorDecayLevel());
        oscSusLvl.setValue((float) instrumentBoard.getOscillatorSustainLevel());
        oscRelLvl.setValue((float) instrumentBoard.getOscillatorReleaseLevel());

        filAtkLvl.setValue((float) instrumentBoard.getFilterAttackLevel());
        filDecLvl.setValue((float) instrumentBoard.getFilterDecayLevel());
        filSusLvl.setValue((float) instrumentBoard.getFilterSustainLevel());
        filRelLvl.setValue((float) instrumentBoard.getFilterReleaseLevel());

        pitAtkLvl.setValue((float) instrumentBoard.getPitchAttackLevel());
        pitDecLvl.setValue((float) instrumentBoard.getPitchDecayLevel());
        pitSusLvl.setValue((float) instrumentBoard.getPitchSustainLevel());
        pitRelLvl.setValue((float) instrumentBoard.getPitchReleaseLevel());


        oscAtkSpd.setValue((float) instrumentBoard.getOscillatorAttackSpeed());
        oscDecSpd.setValue((float) instrumentBoard.getOscillatorDecaySpeed());
        oscSusSpd.setValue((float) instrumentBoard.getOscillatorSustainSpeed());
        oscRelSpd.setValue((float) instrumentBoard.getOscillatorReleaseSpeed());

        filAtkSpd.setValue((float) instrumentBoard.getFilterAttackSpeed());
        filDecSpd.setValue((float) instrumentBoard.getFilterDecaySpeed());
        filSusSpd.setValue((float) instrumentBoard.getFilterSustainSpeed());
        filRelSpd.setValue((float) instrumentBoard.getFilterReleaseSpeed());

        pitAtkSpd.setValue((float) instrumentBoard.getPitchAttackSpeed());
        pitDecSpd.setValue((float) instrumentBoard.getPitchDecaySpeed());
        pitSusSpd.setValue((float) instrumentBoard.getPitchSustainSpeed());
        pitRelSpd.setValue((float) instrumentBoard.getPitchReleaseSpeed());


        oscAtkLvl.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setOscillatorAttackLevel(oscAtkLvl.getValue());
            }
        });
        oscDecLvl.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setOscillatorDecayLevel(oscDecLvl.getValue());
            }
        });
        oscSusLvl.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setOscillatorSustainLevel(oscSusLvl.getValue());
            }
        });
        oscRelLvl.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setOscillatorReleaseLevel(oscRelLvl.getValue());
            }
        });


        filAtkLvl.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setFilterAttackLevel(filAtkLvl.getValue());
            }
        });
        filDecLvl.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setFilterDecayLevel(filDecLvl.getValue());
            }
        });
        filSusLvl.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setFilterSustainLevel(filSusLvl.getValue());
            }
        });
        filRelLvl.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setFilterReleaseLevel(filRelLvl.getValue());
            }
        });


        pitAtkLvl.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setPitchAttackLevel(pitAtkLvl.getValue());
            }
        });
        pitDecLvl.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setPitchDecayLevel(pitDecLvl.getValue());
            }
        });
        pitSusLvl.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setPitchSustainLevel(pitSusLvl.getValue());
            }
        });
        pitRelLvl.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setPitchReleaseLevel(pitRelLvl.getValue());
            }
        });


        oscAtkSpd.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setOscillatorAttackSpeed(oscAtkSpd.getValue());
            }
        });
        oscDecSpd.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setOscillatorDecaySpeed(oscDecSpd.getValue());
            }
        });
        oscSusSpd.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setOscillatorSustainSpeed(oscSusSpd.getValue());
            }
        });
        oscRelSpd.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setOscillatorReleaseSpeed(oscRelSpd.getValue());
            }
        });


        filAtkSpd.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setFilterAttackSpeed(filAtkSpd.getValue());
            }
        });
        filDecSpd.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setFilterDecaySpeed(filDecSpd.getValue());
            }
        });
        filSusSpd.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setFilterSustainSpeed(filSusSpd.getValue());
            }
        });
        filRelSpd.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setFilterReleaseSpeed(filRelSpd.getValue());
            }
        });


        pitAtkSpd.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setPitchAttackSpeed(pitAtkSpd.getValue());
            }
        });
        pitDecSpd.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setPitchDecaySpeed(pitDecSpd.getValue());
            }
        });
        pitSusSpd.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setPitchSustainSpeed(pitSusSpd.getValue());
            }
        });
        pitRelSpd.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setPitchReleaseSpeed(pitRelSpd.getValue());
            }
        });
    }

}
