package ru.markin.vkutils.common.network.util;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(exclude = {"firstInt", "secondInt"})
public final class IntAndInt {

    @Getter
    @Setter
    private int firstInt;
    @Getter
    @Setter
    private int secondInt;

    public IntAndInt() {
    }

    public IntAndInt(int firstInt, int secondInt) {
        this.firstInt = firstInt;
        this.secondInt = secondInt;
    }

    @Override
    public String toString() {
        return "LongAndInt{" +
                "firstInt=" + firstInt +
                ", secondInt=" + secondInt +
                '}';
    }
}
