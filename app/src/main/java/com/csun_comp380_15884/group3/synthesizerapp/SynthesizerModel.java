package com.csun_comp380_15884.group3.synthesizerapp;

import com.csun_comp380_15884.group3.synthesizerapp.dsp.Oscillator;
import com.csun_comp380_15884.group3.synthesizerapp.dsp.OscillatorState;
import com.csun_comp380_15884.group3.synthesizerapp.dsp.ParamSmooth;

/**
 * Created by marvin on 9/15/16.
 */

public class SynthesizerModel {

    int sampleRate;

    Oscillator [] oscillator;
    OscillatorState [] oscillatorState;

    float master;
    float [] oscillatorAmp;

    ParamSmooth [] mPS ;
    
    float [] params;
    private int frequency;

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

        params = new float [PN.kNumberOfParameters.getValue()];

        oscillator = new Oscillator[4];
        oscillatorState = new OscillatorState[4];
        oscillatorAmp = new float[4];

        for(int i = 0; i < 4; i++) {
            oscillator[i] = new Oscillator();
            oscillatorState[i] = new OscillatorState();
            oscillatorState[i].mPhaseIncrement = 440.f / getSampleRate();
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

        for(int i = 0; i < 4; i++) {
            oscillatorState[i].mPhaseIncrement = mPS[PN.kFrequency.getValue()].
                    process(params[PN.kFrequency.getValue()]);


        }

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
            case R.id.frequency:
                params[PN.kFrequency.getValue()] = (16.f+2000.f*value)/getSampleRate();
                break;
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
            case R.id.frequency:
                return params[PN.kFrequency.getValue()];
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
        float [] osc = new float[4];
        float left = 0.0f;
        float right = 0.0f;
        for (int i = 0; i < buffsize; i += 2)
        {
            update();

            left = 0.0f;
            right = 0.0f;

            osc[0] = oscillator[0].process(oscillatorState[0]);
            osc[1] = oscillator[1].process(oscillatorState[1]);
            osc[2] = oscillator[2].process(oscillatorState[2]);
            osc[3] = oscillator[3].process(oscillatorState[3]);

            for(int j = 0; j < 4; j++)
            {
                oscillator[j].mInput[0] = osc[0];
                oscillator[j].mInput[1] = osc[1];
                oscillator[j].mInput[2] = osc[2];
                oscillator[j].mInput[3] = osc[3];
                left += osc[j]*oscillatorAmp[j];
                right += osc[j]*oscillatorAmp[j];
            }

            outputs[i] = (short) (8191*master*left); //Left output
            outputs[i + 1] = (short) (8191*master*right); //Right output

        }

    }
}
