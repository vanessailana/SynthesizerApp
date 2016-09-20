package com.csun_comp380_15884.group3.synthesizerapp.dsp;

/**
 * Created by marvin on 9/10/16.
 */
public class ParamSmooth {

    private float a,b,z;
    public ParamSmooth()
    {
        a = .99f;
        b = 1.0f - a;
        z = 0.0f;
    }

    public float process(float input)
    {
        return z = input*b + z*a;
    }


}
