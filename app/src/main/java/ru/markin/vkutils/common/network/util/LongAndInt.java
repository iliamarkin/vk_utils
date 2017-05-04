package ru.markin.vkutils.common.network.util;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(exclude = {"firstInt", "secondInt"})
public final class LongAndInt {

    @Getter
    @Setter
    private long longValue;
    @Getter
    @Setter
    private int intValue;

    public LongAndInt() {
    }

    public LongAndInt(long longValue, int intValue) {
        this.longValue = longValue;
        this.intValue = intValue;
    }

    @Override
    public String toString() {
        return "LongAndInt{" +
                "firstInt=" + longValue +
                ", secondInt=" + intValue +
                '}';
    }
}
