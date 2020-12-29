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
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera));

        instrumentBoard = new InstrumentBoard();
        rack = new Rack(instrumentBoard);
        rack.setFillParent(true);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(rack.getInputProcessor());
        Gdx.input.setInputProcessor(multiplexer);

        stage.addActor(rack);
        audioThread.start();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.16f, 1);
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

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        camera.viewportWidth = width;
        camera.viewportHeight = height;

        stage.getViewport().setScreenSize(width, height);
    }

    private void processAudio() {
        while (audioOn) {
            rack.getFrame(audioBuffer, 512);
            audioDevice.writeSamples(audioBuffer, 0, 512);
        }
    }

}
