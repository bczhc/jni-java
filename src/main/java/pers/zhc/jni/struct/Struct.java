package pers.zhc.jni.struct;

import pers.zhc.jni.JNI;

/**
 * @author bczhc
 */
public class Struct {
    public enum Endianness {
        BIG,
        LITTLE
    }
    
    public static Endianness getEndianness() {
        switch (JNI.Struct.getEndianness()) {
            case 0:
                return Endianness.BIG;
            case 1:
                return Endianness.LITTLE;
            default:
                throw new RuntimeException("Invalid JNI returned value");
        }
    }
}
