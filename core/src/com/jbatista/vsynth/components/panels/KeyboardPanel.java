package com.jbatista.vsynth.components.panels;

import com.jbatista.vsynth.components.modules.InstrumentBoard;
import com.kotcrab.vis.ui.widget.VisWindow;

public class KeyboardPanel extends VisWindow {

    private final InstrumentBoard instrumentBoard;

    public KeyboardPanel(InstrumentBoard instrumentBoard) {
        super("Keyboard");
        this.instrumentBoard = instrumentBoard;
    }

}
