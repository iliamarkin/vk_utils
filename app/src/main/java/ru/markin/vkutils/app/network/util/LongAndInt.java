package ru.markin.vkutils.app.network.util;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(exclude = {"longValue", "intValue"})
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
                "longValue=" + longValue +
                ", intValue=" + intValue +
                '}';
    }
}
