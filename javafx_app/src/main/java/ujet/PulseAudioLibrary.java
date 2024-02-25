package ujet;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

// INTERFACE FOR INTERFACING WITH PULSEAUDIO

public interface PulseAudioLibrary extends Library
{
    // loading pulse audio
    PulseAudioLibrary AUDIO_LIBRARY = Native.load("pulse", PulseAudioLibrary.class);

    

}   