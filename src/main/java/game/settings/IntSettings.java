package game.settings;

import core.settings.IntSetting;

public enum IntSettings implements IntSetting {
    BOARD_SIZE_X(4, 60, 10),
    BOARD_SIZE_Y(4, 90, 30);

    IntSettings(int min, int max, int defaultValue) {
        this.min = min;
        this.max = max;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    @Override
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }

    @Override
    public Integer valueFromFraction(float fraction) {
        return Math.round(min + fraction * (max - min));
    }

    @Override
    public float fractionFromValue(Integer value) {
        return (float) (value - min) / (max - min);
    }

    @Override
    public int defaultValue() {
        return defaultValue;
    }

    private final int min, max, defaultValue;
    private int value;
}
