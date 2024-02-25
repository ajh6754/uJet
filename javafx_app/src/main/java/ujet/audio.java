package ujet;

import javax.sound.sampled.*;

import javafx.fxml.FXML;

//import org.jitsi.*;

// if we can somehow access the software mixer, we should be able to have access to the apps sending info to the mixer?

public class audio {
    protected Mixer.Info[] mixer_info = AudioSystem.getMixerInfo();
    //protected Line[] lines = AudioSystem.getMixer(mixer_info[0]).getTargetLines();

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
