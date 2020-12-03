package com.jbatista.vsynth.components.panels;

import com.jbatista.vsynth.components.modules.InstrumentBoard;
import com.kotcrab.vis.ui.widget.VisWindow;

public class ModulationPanel extends VisWindow {

    private final InstrumentBoard instrumentBoard;

    public ModulationPanel(InstrumentBoard instrumentBoard) {
        super("Modulation");
        setMovable(false);
        this.instrumentBoard = instrumentBoard;
    }

}
