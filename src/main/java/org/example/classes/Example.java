package org.example.classes;

import org.example.generator.Generatable;

import java.util.Arrays;

@Generatable
public class Example {
    int i;
    int[] ii;

    public Example(int i, int[] ii) {
        this.i = i;
        this.ii = ii;
    }

    @Override
    public String toString() {
        return "Example(" + i + ", " + Arrays.toString(ii) + ")";
    }
}
