package ujet;

import javax.sound.sampled.*;

import javafx.fxml.FXML;

import java.io.IOException;

// if we can somehow access the software mixer, we should be able to have access to the apps sending info to the mixer?

public class audio {
    protected Mixer.Info[] mixer_info = AudioSystem.getMixerInfo();
    //protected Line[] lines = AudioSystem.getMixer(mixer_info[0]).getTargetLines();

    // starts the PulseAudio Daemon for intercepting audio channels
    @FXML
    private void startDaemon() throws IOException
    {
        // start pulseaudio, cool beans
        ProcessBuilder proc = new ProcessBuilder("pulseaudio --start");
        Process pulseaudio = proc.start();
    }

    @FXML
    private void getInfo()
    {
        //System.out.println(lines[0].toString());
        //System.out.println(lines.length);

        for(int i = 0; i < mixer_info.length; i++)
        {
            Line[] lines = AudioSystem.getMixer(mixer_info[i]).getTargetLines();
            System.out.println(mixer_info[i].toString());
            System.out.println(lines.length);
        }
    }
}
