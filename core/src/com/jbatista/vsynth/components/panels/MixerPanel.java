package com.jbatista.vsynth.components.panels;

import com.jbatista.vsynth.components.modules.InstrumentBoard;
import com.kotcrab.vis.ui.widget.VisWindow;

public class MixerPanel extends VisWindow {

    private final InstrumentBoard instrumentBoard;

    public MixerPanel(InstrumentBoard instrumentBoard) {
        super("Mixer");
        this.instrumentBoard = instrumentBoard;
    }

}
