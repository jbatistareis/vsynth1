package com.jbatista.vsynth.components.panels;

import com.jbatista.vsynth.components.modules.InstrumentBoard;
import com.kotcrab.vis.ui.widget.VisWindow;

public class OscillatorsPanel extends VisWindow {

    private final InstrumentBoard instrumentBoard;

    public OscillatorsPanel(InstrumentBoard instrumentBoard) {
        super("Oscillators");
        setMovable(false);
        this.instrumentBoard = instrumentBoard;
    }

}
