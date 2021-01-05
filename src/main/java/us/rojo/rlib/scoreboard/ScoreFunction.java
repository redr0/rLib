package us.rojo.rlib.scoreboard;

import us.rojo.rlib.util.TimeUtils;

public interface ScoreFunction<T> {

    public static ScoreFunction<Float> TIME_FANCY = value -> {
        if (value >= 60.0f) {
            return TimeUtils.formatIntoMMSS(value.intValue());
        } else {
            return Math.round(10.0 * value) / 10.0 + "s";
        }
    };

    public static ScoreFunction<Float> TIME_SIMPLE = value -> TimeUtils.formatIntoMMSS(value.intValue());

    String apply(T p0);
}

