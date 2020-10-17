package com.jbatista.vsynth.components.panels;

import com.jbatista.vsynth.components.modules.InstrumentBoard;
import com.kotcrab.vis.ui.widget.VisWindow;

public class OutputPanel extends VisWindow {

    private final InstrumentBoard instrumentBoard;

    public OutputPanel(InstrumentBoard instrumentBoard) {
        super("Output");
        this.instrumentBoard = instrumentBoard;
    }

}
