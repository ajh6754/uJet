package ujet.JNA;
/* 
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

// INTERFACE FOR INTERFACING WITH PULSEAUDIO

public interface PulseAudioLibrary extends Library
{
    // loading pulse audio
    PulseAudioLibrary AUDIO_LIBRARY = Native.load("pulse", PulseAudioLibrary.class);

    // for creating a new context in which to interact with PulseAudio
    Pointer pa_context_new(Pointer mainloop, String name);

    // for deallocating the context with pulseaudio
    void pa_context_unref(Pointer context);

    // So we have the context

    // const pa_sample_spec * pa_stream_get_sample_spec (pa_stream *s)
    Pointer pa_stream_get_sample_spec(Pointer pa_stream);

    // pa_stream * 	pa_stream_new (pa_context *c, const char *name, const pa_sample_spec *ss, const pa_channel_map *map)
    // Create a new, unconnected stream with the specified name and sample type
    Pointer pa_stream_new(Pointer pa_context, String name, Pointer p_sample_spec, Pointer pa_channel_map);

    // pactl list sink-inputs - May not be what we need, BUT progress towards detecting audio
}   */