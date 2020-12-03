package com.jbatista.vsynth.components.panels;

import com.jbatista.vsynth.components.modules.InstrumentBoard;
import com.kotcrab.vis.ui.widget.VisWindow;

public class EnvelopesPanel extends VisWindow {

    private final InstrumentBoard instrumentBoard;

    public EnvelopesPanel(InstrumentBoard instrumentBoard) {
        super("Envelopes");
        setMovable(false);
        this.instrumentBoard = instrumentBoard;
    }

}
