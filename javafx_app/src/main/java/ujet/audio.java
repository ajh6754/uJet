package ujet;

import javax.sound.sampled.*;

import javafx.fxml.FXML;

import com.sun.jna.Pointer;

// if we can somehow access the software mixer, we should be able to have access to the apps sending info to the mixer?

public class audio {
    // VARIABLES

    // obtain context to work with
    private Pointer context;

    // mainloop to create context with
    private Pointer mainloop;

    // starts the PulseAudio Daemon for intercepting audio channels

    public void initialize()
    {
        // Create a main event loop
        this.mainloop = PulseAudioLibrary.AUDIO_LIBRARY.pa_mainloop_new();
        // initialize the first context
        this.context = PulseAudioLibrary.AUDIO_LIBRARY.pa_context_new(this.mainloop, "uJet");

        // shutdown hook to deallocate
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            PulseAudioLibrary.AUDIO_LIBRARY.pa_context_unref(this.context);
        }));
    }

    @FXML
    private void getInfo()
    {

    }
}
