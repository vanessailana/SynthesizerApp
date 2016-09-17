package com.csun_comp380_15884.group3.synthesizerapp;

/**
 * Created by marvin on 9/15/16.
 */

public class SynthesizerModel {

    //Amplitude converts float to short to write to buffer
    private int amp;
    //Sample Rate
    int sr;

    //Our Oscillator Variables
    Oscillator oscillator;
    OscillatorState oscillatorState;

    //All Variables having to do with parameters
    float masterVolume;
    ParamSmooth paramSmooth [];
    float params [];
    static public enum ParameterNames
    {

        kMasterVolume(0),
        kFrequency(1),
        kNumberOfParameters(2);
        private final int value;
        ParameterNames(final int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }


    SynthesizerModel() {

        amp = 32767;
        sr = 44100;

        masterVolume = 0.0f;

        paramSmooth = new ParamSmooth[ParameterNames.kNumberOfParameters.getValue()];

        for(int i = 0; i < ParameterNames.kNumberOfParameters.getValue(); i++)
        {
            paramSmooth[i] = new ParamSmooth();
        }

        params = new float [ParameterNames.kNumberOfParameters.getValue()];

        oscillator = new Oscillator();

        oscillatorState = new OscillatorState();

        oscillatorState.mPhaseIncrement = 440.f/getSampleRate();

    }

    public int getSampleRate() {
        return sr;
    }


    //Big update function
    private void update()
    {
        masterVolume = paramSmooth[ParameterNames.kMasterVolume.getValue()].
                process(params[ParameterNames.kMasterVolume.getValue()]);

        oscillatorState.mPhaseIncrement = paramSmooth[ParameterNames.kFrequency.getValue()].
                process(params[ParameterNames.kFrequency.getValue()]);


    }

    public void setParameter(int id, float value) {

        switch (id) {
            case R.id.frequency:
                params[ParameterNames.kFrequency.getValue()] =
                        (16.f+2000.f*value)/getSampleRate();
                break;
            case R.id.master_volume:
                params[ParameterNames.kMasterVolume.getValue()] = value;
                break;
            default:
                break;
        }
        update();
    }

    public float getParameter(int id)
    {
        switch (id)
        {
            case R.id.frequency:
                return params[ParameterNames.kFrequency.getValue()];
            case R.id.master_volume:
                return params[ParameterNames.kMasterVolume.getValue()];
            default:
                return 0.0f;
        }
    }




    public void processReplacing(short outputs[], int buffsize)
    {

        for (int i = 0; i < buffsize; i += 2)
        {
            update();
            short output = (short) (masterVolume*amp*oscillator.process(oscillatorState));
            outputs[i] = output; //Left output
            outputs[i + 1] = output; //Right output

        }

    }
}
