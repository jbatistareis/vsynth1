package com.jbatista.vsynth.components.panels;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.jbatista.vsynth.components.modules.InstrumentBoard;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisTable;

public class FilterPanel extends VisTable {

    private final VisLabel lblCutoff = new VisLabel("Cutoff");
    private final VisSlider sldCutoff = new VisSlider(0, 1, 0.01f, false);

    private final VisLabel lblRes = new VisLabel("Resonance");
    private final VisSlider sldRes = new VisSlider(0, 1, 0.01f, false);

    private final VisCheckBox chkFilCloses = new VisCheckBox("EG closes");

    public FilterPanel(InstrumentBoard instrumentBoard) {
        // layout
        add(lblCutoff).row();
        add(sldCutoff).row();
        add(lblRes).row();
        add(sldRes).row();
        add(chkFilCloses).row();

        // functions
        sldCutoff.setValue((float) instrumentBoard.getFilterCutoff());
        sldRes.setValue((float) instrumentBoard.getFilterRessonance());
        chkFilCloses.setChecked(instrumentBoard.isFilterClosing());

        sldCutoff.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setFilterCutoff(sldCutoff.getValue());
            }
        });

        sldRes.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setFilterResonance(sldRes.getValue());
            }
        });

        chkFilCloses.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                instrumentBoard.setFilterCloses(chkFilCloses.isChecked());
            }
        });
    }

}
