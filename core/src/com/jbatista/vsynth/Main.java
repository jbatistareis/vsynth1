package com.jbatista.vsynth;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.jbatista.vsynth.components.modules.InstrumentBoard;
import com.jbatista.vsynth.components.modules.Rack;
import com.kotcrab.vis.ui.VisUI;

public class Main extends ApplicationAdapter {

    private AudioDevice audioDevice;
    private final Thread audioThread = new Thread(this::processAudio, "Synth");
    private final float[] audioBuffer = new float[44100];
    private boolean audioOn = true;

    private Camera camera;
    private Stage stage;

    private InstrumentBoard instrumentBoard;
    private Rack rack;

    @Override
    public void create() {
        VisUI.load();

        audioDevice = Gdx.audio.newAudioDevice(44100, false);

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(new FitViewport(1280, 720, camera));

        instrumentBoard = new InstrumentBoard();
        rack = new Rack(instrumentBoard);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        // multiplexer.addProcessor(instrumentActor.getKeyboardInputProcessor());
        Gdx.input.setInputProcessor(multiplexer);

        stage.addActor(rack);

        audioThread.start();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void dispose() {
        audioOn = false;

        VisUI.dispose();
        stage.dispose();
    }

    private void processAudio() {
        while (audioOn) {
            rack.getFrame(audioBuffer, 256);
            audioDevice.writeSamples(audioBuffer, 0, 256);
        }
    }

}
