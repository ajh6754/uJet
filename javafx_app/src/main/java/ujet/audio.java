package ujet;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import javafx.fxml.FXML;

// if we can somehow access the software mixer, we should be able to have access to the apps sending info to the mixer?

public class audio {
    // VARIABLES

    // loopback list is a bunch of loopbacks

    // lock for writing to stdout
    private static Object myLock = new Object();


    // realistically, loopbacks could potentially be used, the overhead just sucks

    // you would need to both play the sound and route it to the API, we can try but that could slow things down

    // the way you would achieve it is by just creating a loopback on all input sinks. We don't care about customizibility yet, just get it working.

    // Print that Google Chrome is saying stuff, but the latency related crap might be hard...


    // starts the PulseAudio Daemon for intercepting audio channels
    public void initialize() throws IOException, InterruptedException
    {
        // get the list of input sinks via pact1, interpret what applications are outputting audio
        ProcessBuilder get_sinks = new ProcessBuilder("pactl", "list", "sink-inputs");
        // Start the process
        Process process = get_sinks.start();

        // Read the output of the process
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;

        // the int id and app_name of the sink to save
        int sink_id = 0;
        String app_name = "";

        // initialize the app list

        // for now, just get it to recognize chrome and whatnot, don't care about anything else.
        
        while ((line = reader.readLine()) != null) {
            // for every "Sink Input", get the id
            if(line.contains("Sink Input #"))
                sink_id = Integer.parseInt(line.split("#")[1].trim());

            // for every "application.name" fire a thread that creates a loopback
            if(line.contains("application.name"))
            {
                // get the name of the app
                app_name = line.split("=")[1].trim();

                System.out.println(app_name);

                // create a defensive copy of the id
                final int personal_id = sink_id;
                // create a defensive copy of app_name
                final String personal_app_name = app_name;

                // fire the thread off, 
                Thread thread = new Thread(() -> {
                    Random random = new Random();
                    int r = random.nextInt();

                    // Use the personal app name inside the thread

                    // whenever audio detected, try to copy the stuff from the other threads, but get the data

                    // create the new loopback
                    ProcessBuilder get_loopback = new ProcessBuilder("pactl", "load-module", ""+personal_id);
                    // Start the process
                    try {
                        Process create_loopback = get_loopback.start();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    
                    // create the eternal while loop or something idk

                    // this while loop will work infinitely getting bytes

                    while(true)
                    {
                        // lock so that the commands are ok
                        // the audio buffer
                        byte[] buffer = new byte[4096];
                        int bytes_read = 0;
                        //parec --format=s16le --rate=44100 --channels=2 --device=loopback_device_name
                        ProcessBuilder bytesGetter = new ProcessBuilder("parec", "--format=s16le", "--rate=44100", "--channels=2", "--device=bluez_output.7C_96_D2_91_12_71.1.monitor");//personal_app_name);
                        try {
                            Process getting_bytes = bytesGetter.start();
                            InputStream inputStream = getting_bytes.getInputStream();
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                            int x = 0;

                            // read bytes until over
                            while((bytes_read = inputStream.read(buffer)) != -1)
                            {
                                //System.out.println(personal_app_name + " Just played a sound");
                                outputStream.write(buffer, 0, bytes_read);
                                //outputStream.write()

                                // Get the captured audio data as a byte array
                                //byte[] audioData = outputStream.toByteArray();
                                byte[] audioData = outputStream.toByteArray();
                                if(x < 100 || random.nextInt() % 19 == 0)
                                {
                                    AudioFormat format = new AudioFormat(44100, 16, 2, true, false);
                                    AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(audioData), format, audioData.length / format.getFrameSize());
                                    AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File(r + ".wav"));
                                }

                                x++;
                                // flush the output stream
                                outputStream.flush();

                                // if i were to send these bytes, i'd need to send the name/id as well for easy differentiation
                            }
                            // wait for parex to end
                            process.waitFor();
                            
                            //inputStream.close();
                            //outputStream.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    
                });
                thread.start(); // Start the thread
                
            }
        }
    }

    @FXML
    private void getInfo()
    {

    }
}
