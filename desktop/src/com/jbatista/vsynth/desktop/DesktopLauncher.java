package com.jbatista.vsynth.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.jbatista.vsynth.Main;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title = "VSynth 1";
        config.vSyncEnabled = true;
        config.width = 1220;
        config.height = 342;

        new LwjglApplication(new Main(), config);
    }
}
