package com.csun_comp380_15884.group3.synthesizerapp;

import com.csun_comp380_15884.group3.synthesizerapp.dsp.ADSR;
import com.csun_comp380_15884.group3.synthesizerapp.dsp.ADSRState;
import com.csun_comp380_15884.group3.synthesizerapp.dsp.Oscillator;
import com.csun_comp380_15884.group3.synthesizerapp.dsp.OscillatorState;
import com.csun_comp380_15884.group3.synthesizerapp.dsp.ParamSmooth;
import com.csun_comp380_15884.group3.synthesizerapp.dsp.VoiceState;

/**
 * Created by marvin on 9/15/16.
 */

public class SynthesizerModel {

    int sampleRate;

    Oscillator [] oscillator;
    ADSR [] adsr;
    VoiceState [] vs;

    float master;
    float [] oscillatorAmp;

    ParamSmooth [] mPS ;

    float [] params;
    private int frequency;


    int [] notes;
    static final int EVENTSDONE = 9999999;

    private int activeVoices;

    static public enum PN
    {

        kMaster(0),
        kFrequency(1),

        kOutput0(2),
        kOutput1(3),
        kOutput2(4),
        kOutput3(5),

        kMod0To0(6),
        kMod1To0(7),
        kMod2To0(8),
        kMod3To0(9),

        kMod0To1(10),
        kMod1To1(11),
        kMod2To1(12),
        kMod3To1(13),

        kMod0To2(14),
        kMod1To2(15),
        kMod2To2(16),
        kMod3To2(17),

        kMod0To3(18),
        kMod1To3(19),
        kMod2To3(20),
        kMod3To3(21),

        kNumberOfParameters(22);
        private final int value;
        PN(final int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }


    SynthesizerModel() {


        sampleRate = 44100;

        master = 0.0f;

        mPS = new ParamSmooth[PN.kNumberOfParameters.getValue()];

        for(int i = 0; i < PN.kNumberOfParameters.getValue(); i++)
        {
            mPS[i] = new ParamSmooth();
        }

        activeVoices = 0;

        params = new float [PN.kNumberOfParameters.getValue()];

        oscillator = new Oscillator[4];

        adsr = new ADSR[4];

        for (int i = 0; i < 4; i++) {
            oscillator[i] = new Oscillator();
            adsr[i] = new ADSR();
        }

        oscillatorAmp = new float[4];

        vs = new VoiceState[8];

        for (int i = 0; i < 8; i++) {
            vs[i] = new VoiceState();
        }


        notes = new int[128];

        for (int i = 0; i < 128; i++) {
            notes[i] = EVENTSDONE;
        }

        setParameter(R.id.mod_0_0,0.0f);
        setParameter(R.id.mod_0_1,0.0f);
        setParameter(R.id.mod_0_2,0.0f);
        setParameter(R.id.mod_0_3,0.0f);

        setParameter(R.id.mod_1_0,0.0f);
        setParameter(R.id.mod_1_1,0.0f);
        setParameter(R.id.mod_1_2,0.0f);
        setParameter(R.id.mod_1_3,0.0f);

        setParameter(R.id.mod_2_0,0.0f);
        setParameter(R.id.mod_2_1,0.0f);
        setParameter(R.id.mod_2_2,0.0f);
        setParameter(R.id.mod_2_3,0.0f);

        setParameter(R.id.mod_3_0,0.0f);
        setParameter(R.id.mod_3_1,0.0f);
        setParameter(R.id.mod_3_2,0.0f);
        setParameter(R.id.mod_3_3,0.0f);

        setParameter(R.id.out_0,0.0f);
        setParameter(R.id.out_1,0.0f);
        setParameter(R.id.out_2,0.0f);
        setParameter(R.id.out_3,0.0f);




    }

    public int getSampleRate() {
        return sampleRate;
    }


    //Big parameter update function for use in processReplacing loop and setParameter.
    private void update()
    {
        master = mPS[PN.kMaster.getValue()].process(params[PN.kMaster.getValue()]);


        oscillator[0].mInputAmp[0] = mPS[PN.kMod0To0.getValue()].process(params[PN.kMod0To0.getValue()]);
        oscillator[0].mInputAmp[1] = mPS[PN.kMod0To1.getValue()].process(params[PN.kMod0To1.getValue()]);
        oscillator[0].mInputAmp[2] = mPS[PN.kMod0To2.getValue()].process(params[PN.kMod0To2.getValue()]);
        oscillator[0].mInputAmp[3] = mPS[PN.kMod0To3.getValue()].process(params[PN.kMod0To3.getValue()]);

        oscillator[1].mInputAmp[0] = mPS[PN.kMod1To0.getValue()].process(params[PN.kMod1To0.getValue()]);
        oscillator[1].mInputAmp[1] = mPS[PN.kMod1To1.getValue()].process(params[PN.kMod1To1.getValue()]);
        oscillator[1].mInputAmp[2] = mPS[PN.kMod1To2.getValue()].process(params[PN.kMod1To2.getValue()]);
        oscillator[1].mInputAmp[3] = mPS[PN.kMod1To3.getValue()].process(params[PN.kMod1To3.getValue()]);

        oscillator[2].mInputAmp[0] = mPS[PN.kMod2To0.getValue()].process(params[PN.kMod2To0.getValue()]);
        oscillator[2].mInputAmp[1] = mPS[PN.kMod2To1.getValue()].process(params[PN.kMod2To1.getValue()]);
        oscillator[2].mInputAmp[2] = mPS[PN.kMod2To2.getValue()].process(params[PN.kMod2To2.getValue()]);
        oscillator[2].mInputAmp[3] = mPS[PN.kMod2To3.getValue()].process(params[PN.kMod2To3.getValue()]);

        oscillator[3].mInputAmp[0] = mPS[PN.kMod3To0.getValue()].process(params[PN.kMod3To0.getValue()]);
        oscillator[3].mInputAmp[1] = mPS[PN.kMod3To1.getValue()].process(params[PN.kMod3To1.getValue()]);
        oscillator[3].mInputAmp[2] = mPS[PN.kMod3To2.getValue()].process(params[PN.kMod3To2.getValue()]);
        oscillator[3].mInputAmp[3] = mPS[PN.kMod3To3.getValue()].process(params[PN.kMod3To3.getValue()]);

        oscillatorAmp[0] = mPS[PN.kOutput0.getValue()].process(params[PN.kOutput0.getValue()]);
        oscillatorAmp[1] = mPS[PN.kOutput1.getValue()].process(params[PN.kOutput1.getValue()]);
        oscillatorAmp[2] = mPS[PN.kOutput2.getValue()].process(params[PN.kOutput2.getValue()]);
        oscillatorAmp[3] = mPS[PN.kOutput3.getValue()].process(params[PN.kOutput3.getValue()]);

    }

    public void setParameter(int id, float value) {

        switch (id) {
            case R.id.master:
                params[PN.kMaster.getValue()] = value;
                break;
            case R.id.mod_0_0:
                params[PN.kMod0To0.getValue()] = value;
                break;
            case R.id.mod_0_1:
                params[PN.kMod0To1.getValue()] = value;
                break;
            case R.id.mod_0_2:
                params[PN.kMod0To2.getValue()] = value;
                break;
            case R.id.mod_0_3:
                params[PN.kMod0To3.getValue()] = value;
                break;
            case R.id.mod_1_0:
                params[PN.kMod1To0.getValue()] = value;
                break;
            case R.id.mod_1_1:
                params[PN.kMod1To1.getValue()] = value;
                break;
            case R.id.mod_1_2:
                params[PN.kMod1To2.getValue()] = value;
                break;
            case R.id.mod_1_3:
                params[PN.kMod1To3.getValue()] = value;
                break;
            case R.id.mod_2_0:
                params[PN.kMod2To0.getValue()] = value;
                break;
            case R.id.mod_2_1:
                params[PN.kMod2To1.getValue()] = value;
                break;
            case R.id.mod_2_2:
                params[PN.kMod2To2.getValue()] = value;
                break;
            case R.id.mod_2_3:
                params[PN.kMod2To3.getValue()] = value;
                break;
            case R.id.mod_3_0:
                params[PN.kMod3To0.getValue()] = value;
                break;
            case R.id.mod_3_1:
                params[PN.kMod3To1.getValue()] = value;
                break;
            case R.id.mod_3_2:
                params[PN.kMod3To2.getValue()] = value;
                break;
            case R.id.mod_3_3:
                params[PN.kMod3To3.getValue()] = value;
                break;
            case R.id.out_0:
                params[PN.kOutput0.getValue()] = value;
                break;
            case R.id.out_1:
                params[PN.kOutput1.getValue()] = value;
                break;
            case R.id.out_2:
                params[PN.kOutput2.getValue()] = value;
                break;
            case R.id.out_3:
                params[PN.kOutput3.getValue()] = value;
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
            case R.id.master:
                return params[PN.kMaster.getValue()];
            case R.id.mod_0_0:
                return params[PN.kMod0To0.getValue()];
            case R.id.mod_0_1:
                return params[PN.kMod0To1.getValue()];
            case R.id.mod_0_2:
                return params[PN.kMod0To2.getValue()];
            case R.id.mod_0_3:
                return params[PN.kMod0To3.getValue()];
            case R.id.mod_1_0:
                return params[PN.kMod1To0.getValue()];
            case R.id.mod_1_1:
                return params[PN.kMod1To1.getValue()];
            case R.id.mod_1_2:
                return params[PN.kMod1To2.getValue()];
            case R.id.mod_1_3:
                return params[PN.kMod1To3.getValue()];
            case R.id.mod_2_0:
                return params[PN.kMod2To0.getValue()];
            case R.id.mod_2_1:
                return params[PN.kMod2To1.getValue()];
            case R.id.mod_2_2:
                return params[PN.kMod2To2.getValue()];
            case R.id.mod_2_3:
                return params[PN.kMod2To3.getValue()];
            case R.id.mod_3_0:
                return params[PN.kMod3To0.getValue()];
            case R.id.mod_3_1:
                return params[PN.kMod3To1.getValue()];
            case R.id.mod_3_2:
                return params[PN.kMod3To2.getValue()];
            case R.id.mod_3_3:
                return params[PN.kMod3To3.getValue()];
            case R.id.out_0:
                return params[PN.kOutput0.getValue()];
            case R.id.out_1:
                return params[PN.kOutput1.getValue()];
            case R.id.out_2:
                return params[PN.kOutput2.getValue()];
            case R.id.out_3:
                return params[PN.kOutput3.getValue()];
            default:
                return 0.0f;
        }
    }




    public void processReplacing(short outputs[], int buffsize)
    {
        float [] oper = new float[4];
        float left = 0.0f;
        float right = 0.0f;


        int frame = 0;
        int frames = 0;
        int event = 0;


        int i = 0;

        int sampleFrames = buffsize/2;

        if(activeVoices > 0 || notes[event] < sampleFrames) {
            while (frame < sampleFrames) {

                frames = notes[event++];
                if (frames > sampleFrames)
                {
                    frames = sampleFrames;
                }
                frames -= frame;
                frame += frames;

                while (--frames >= 0) {
                    update();
                    left = 0.0f;
                    right = 0.0f;

                    for (int v = 0; v < 1; v++) {

                        if (vs[v].mAS[0].mStage != ADSRState.ADSRStage.kIdle
                                || vs[v].mAS[1].mStage != ADSRState.ADSRStage.kIdle
                                || vs[v].mAS[2].mStage != ADSRState.ADSRStage.kIdle
                                || vs[v].mAS[3].mStage != ADSRState.ADSRStage.kIdle) {

                            for (int j = 0; j < 4; j++) {
                                oscillator[0].mInput[j] = oper[j] = oscillator[j].process(vs[v].mOS[j]) * adsr[j].process(vs[v].mAS[j]);
                                oscillator[1].mInput[j] = oper[j];
                                oscillator[2].mInput[j] = oper[j];
                                oscillator[3].mInput[j] = oper[j];
                                left += oper[j] * oscillatorAmp[j];
                                right += oper[j] * oscillatorAmp[j];
                            }
                        }
                    }
                    outputs[i] = (short) (8191 * master * left); //Left output
                    outputs[i + 1] = (short) (8191 * master * right); //Right output
                    i += 2;
                }

                if (frame < sampleFrames) {
                    int note = notes[event++];
                    int vel = notes[event++];
                    noteOn(note, vel);
                }

                activeVoices = 1 ;
                for (int v = 0; v < 1; v++) {
                    if (vs[v].mAS[0].mStage == ADSRState.ADSRStage.kIdle
                            && vs[v].mAS[1].mStage == ADSRState.ADSRStage.kIdle
                            && vs[v].mAS[2].mStage == ADSRState.ADSRStage.kIdle
                            && vs[v].mAS[3].mStage == ADSRState.ADSRStage.kIdle) {
                        activeVoices --;
                    }
                }

            }
        }else{
            for (int j = 0; j < buffsize; j+=2 ) {
                outputs[j] = 0;
                outputs[j+1] = 0;
            }
        }

        notes[0] = EVENTSDONE;

    }

    public int findFreeVoice()
    {


        return 0;
    }

    public void noteOn(int note, int velocity)
    {
        if(velocity > 0)
        {

            int v = findFreeVoice();

            vs[v].mKey = note;

            double inc = Math.pow(2.0, (((double)note)-69.0)/12.0) * 440.0;

            for(int i = 0; i < 4; i++) {

                vs[v].mOS[i].mPhaseIncrement = (float)inc/(float)sampleRate;
                vs[v].mAS[i].mStage = ADSRState.ADSRStage.kStageAttack;
                vs[v].mAS[i].mLevel = 1.0f;
            }

            activeVoices = 1;


        }else{

            for (int v = 0; v < 8; v++) {

                if (vs[v].mKey == note)
                {
                    if (vs[v].mAS[0].mStage != ADSRState.ADSRStage.kIdle
                            || vs[v].mAS[1].mStage != ADSRState.ADSRStage.kIdle
                            || vs[v].mAS[2].mStage != ADSRState.ADSRStage.kIdle
                            || vs[v].mAS[3].mStage != ADSRState.ADSRStage.kIdle)
                    {
                        for (int i = 0; i < 4; i++)
                        {
                            vs[v].mAS[i].mStage = ADSRState.ADSRStage.kStageRelease;
                        }
                        vs[v].mKey = -1;
                        return;
                    }

                }
            }

        }
    }

    public void processEvents(int [] midiData, int deltaFrames) {
        int npos = 0;

        switch (midiData[0])
        {
            case 0x80: //note off
                notes[npos++] = deltaFrames; //delta
                notes[npos++] = midiData[1]; //note
                notes[npos++] = 0;           //vel
                break;

            case 0x90: //note on
                notes[npos++] = deltaFrames; //delta
                notes[npos++] = midiData[1]; //note
                notes[npos++] = midiData[2]; //vel
                break;

        }

        notes[npos] = EVENTSDONE;
    }

}
