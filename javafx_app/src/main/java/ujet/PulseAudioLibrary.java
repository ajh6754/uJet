package ujet;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

// INTERFACE FOR INTERFACING WITH PULSEAUDIO

public interface PulseAudioLibrary extends Library
{
    // loading custom pulse audio library here
    PulseAudioLibrary AUDIO_LIBRARY = Native.load("ujet", PulseAudioLibrary.class);

    // initialize sources
    void initialize_sources();
}   