/// Filename: audio.c
///
/// This file is for interfacing with PulseAudio's library of C functions, but better.
///
/// @author Albert Jiro Hynes

#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <time.h>
//#include <jni.h>
#include <pulse/pulseaudio.h>

#define BUFFER_SIZE 4096
#define MAX_FILENAME_SIZE 200
#define MODE 0666

// GLOBALS

// var to hold if sink inputs were obtained
int got_sinks = 1;


// the sample specificaiton
pa_sample_spec sample_spec = 
{
    .format = PA_SAMPLE_S16LE,
    .rate = 44100,
    .channels = 2
};

// contains audio bytes + source
typedef struct audio_info
{
    char byte_data[BUFFER_SIZE]; // raw audio data
    char * source;               // name of app creating it
} info;



/// FUNCTIONS

// the callback function for obtaining/writing audio
void audio_obtained_callback(pa_stream * s, size_t length, void * userdata)
{
    // write to the pipe ig, but how? 

    // get filename
    //char * filename = get(table, s->)

    // see if in hashtable- if not, return
    

    return;
}

// the callback function for obtained sink inputs
void sink_input_callback(pa_context *c, const pa_sink_input_info * info, int eol, void * userdata)
{
    // update sinks
    got_sinks = -1;

    // obtain the sink id
    uint32_t id = info->index;

    // get the proplist associated with the sink input
    const pa_proplist * proplist = info->proplist;

    // Retrieve the value of each property
    const char * name = pa_proplist_gets(proplist, "application.name");

    // create a new filename string
    char * filename = malloc(MAX_FILENAME_SIZE);
    sprintf(filename, "/tmp/%u_%s", id, name);
    printf("%s\n", filename);

    // first, check if file exists- delete then replace.
    if (!access(filename, F_OK)) 
    {
        printf("Already exists\n");
        // delete it
        unlink(filename);
    }
    
    // call mkfifo to MAKE THE PIPE! check for errors along the way
    if (mkfifo(filename, MODE) == -1) 
    {
        perror("Error with mkfifo");
        return ;
    }

    // free the filename
    free(filename);

    // update the GLOBAL HASHTABLE so that the audio obtained callback can deal with it
    //insert(table, name, filename);

    // whenever java reads that, the data is automatically removed.

    // TODO

    // 1. create a new steram inside this function. Gets called for each input so should be good. Use id as the id.

    // Create a new stream
    //pa_stream *stream = pa_stream_new(/* parameters */);

    // Set up the stream parameters
    //pa_stream_connect_playback(stream, NULL, NULL, PA_STREAM_NOFLAGS, NULL, NULL);

    // Set the sink input index as the destination for the stream
    //pa_stream_set_sink_input(stream, sink_input_index, NULL, NULL);

    // Set the stream state callback
    //pa_stream_set_state_callback(stream, stream_state_callback, NULL);

    // Connect the stream to the server
    //pa_stream_connect(stream, NULL, PA_STREAM_NOFLAGS, NULL);

    // this SHOULD create a stream directly from the sink input.

    return ;
}

void context_state_callback(pa_context * context, void *userdata) {
    // get the state
    pa_context_state_t state = pa_context_get_state(context);

    // for each state, have a response ready.
    switch (state) {
        case PA_CONTEXT_UNCONNECTED:
            printf("PulseAudio context state: UNCONNECTED\n");
            break;
        case PA_CONTEXT_CONNECTING:
            printf("PulseAudio context state: CONNECTING\n");
            break;
        case PA_CONTEXT_AUTHORIZING:
            printf("PulseAudio context state: AUTHORIZING\n");
            break;
        case PA_CONTEXT_SETTING_NAME:
            printf("PulseAudio context state: SETTING_NAME\n");
            break;
        case PA_CONTEXT_READY:
            // get sink inputs
            if(got_sinks)
                pa_context_get_sink_input_info_list(context, sink_input_callback, NULL);
            printf("PulseAudio context state: READY\n");
            // PulseAudio context is ready, perform additional setup or operations
            break;
        case PA_CONTEXT_FAILED:
            printf("PulseAudio context state: FAILED\n");
            // Context connection failed, handle error
            break;
        case PA_CONTEXT_TERMINATED:
            printf("PulseAudio context state: TERMINATED\n");
            // Context terminated, cleanup resources and exit
            break;
        default:
            printf("Unknown PulseAudio context state\n");
            break;
    }
}


// first, the function to initialize everything from the context to the connection.
void initialize_sources()
{
    // get a new mainloop to initialize
    pa_mainloop * mainloop = pa_mainloop_new();

    if (!mainloop) {
        fprintf(stderr, "Failed to create main loop\n");
        return ;
    }

    // get the api for this mainloop
    pa_mainloop_api * mainloop_api = pa_mainloop_get_api(mainloop);

    // initializing a pointer for the context
    pa_context * context = pa_context_new(mainloop_api, "my_context");

    // initializing flags
    pa_stream_flags_t flags;

    // connect to the server.
    int status = pa_context_connect(context, NULL, PA_CONTEXT_NOFLAGS, NULL);

    if(status < 0)
    {
        fprintf(stderr, "Failed to connect to server\n");
        return ;
    }

    // get the sink inputs.
    pa_context_get_sink_input_info_list(context, sink_input_callback, NULL);
    pa_context_set_state_callback(context, context_state_callback, NULL);

    // when state changes, use pa_stream_set_state_callback(). THis will help stop audio writing.

    // When new audio is ready, use pa_stream_set_write_callback(). This will deal with audio writing.

    int ret;

    
    while ((ret = pa_mainloop_iterate(mainloop, 0, NULL)) >= 0) 
    {
        if (ret == 0) 
        {
            // No events pending, break the loop
            //printf("Literally nothing\n");
            //break;
        }
        //printf("Ran once?");
    }

    //pa_mainloop_run(mainloop, NULL);

    // deallocate everything
    pa_context_disconnect(context);
    pa_context_unref(context);
    pa_mainloop_free(mainloop); 
}
