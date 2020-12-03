package com.jbatista.vsynth.components.panels;

import com.jbatista.vsynth.components.modules.InstrumentBoard;
import com.kotcrab.vis.ui.widget.VisWindow;

public class FilterPanel extends VisWindow {

    private final InstrumentBoard instrumentBoard;

    public FilterPanel(InstrumentBoard instrumentBoard) {
        super("Filter");
        setMovable(false);
        this.instrumentBoard = instrumentBoard;
    }

}
