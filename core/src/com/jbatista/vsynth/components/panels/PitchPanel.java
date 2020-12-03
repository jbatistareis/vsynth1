package com.jbatista.vsynth.components.panels;

import com.jbatista.vsynth.components.modules.InstrumentBoard;
import com.kotcrab.vis.ui.widget.VisWindow;

public class PitchPanel extends VisWindow {

    private final InstrumentBoard instrumentBoard;

    public PitchPanel(InstrumentBoard instrumentBoard) {
        super("Pitch");
        setMovable(false);
        this.instrumentBoard = instrumentBoard;
    }

}
