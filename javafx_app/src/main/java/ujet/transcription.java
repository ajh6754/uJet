package ujet;

import com.assemblyai.api.RealtimeTranscriber;
import com.assemblyai.api.resources.realtime.types.PartialTranscript;

import javax.sound.sampled.*;
import java.io.IOException;

import static java.lang.Thread.interrupted;

public final class transcription {
    
    public String text = "";

    public static void handlePartialTranscript(PartialTranscript transcript) {
        if (!transcript.getText().isEmpty()) {
            System.out.println("Partial: " + transcript.getText());
        }
    }

    public static void main(String... args) throws IOException {
        Thread thread = new Thread(() -> {
            try {
                RealtimeTranscriber realtimeTranscriber = RealtimeTranscriber.builder()
                        .apiKey("cebb373d43634ade8fa2a0b9bee83808")
                        .sampleRate(16_000)
                        .onSessionBegins(sessionBegins -> System.out.println(
                                "Session opened with ID: " + sessionBegins.getSessionId()))
                        .onPartialTranscript(transcript -> handlePartialTranscript(transcript))
                        .onFinalTranscript(transcript -> System.out.println("Final: " + transcript.getText()))
                        .onError(err -> System.out.println("Error: " + err.getMessage()))
                        .endUtteranceSilenceThreshold(700)
                        .build();

                System.out.println("Connecting to real-time transcript service");
                realtimeTranscriber.connect();

                // This is where the magic happens
                System.out.println("Starting recording");
                AudioFormat format = new AudioFormat(16_000, 16, 1, true, false);
                // `line` is your microphone
                TargetDataLine line = AudioSystem.getTargetDataLine(format);
                line.open(format);
                byte[] data = new byte[line.getBufferSize()];
                line.start();
                while (!interrupted()) {
                    // Read the next chunk of data from the TargetDataLine.
                    line.read(data, 0, data.length);
                    realtimeTranscriber.sendAudio(data);
                }

                System.out.println("Stopping recording");
                line.close();

                System.out.println("Closing real-time transcript connection");
                realtimeTranscriber.close();
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();

        System.out.println("Press ENTER key to stop...");
        System.in.read();
        thread.interrupt();
        System.exit(0);
    }
}