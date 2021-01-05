package us.rojo.rlib.scoreboard;

public class ScoreboardConfiguration {

    private TitleGetter titleGetter;
    private ScoreGetter scoreGetter;

    public TitleGetter getTitleGetter() {
        return titleGetter;
    }

    public void setTitleGetter(TitleGetter titleGetter) {
        this.titleGetter = titleGetter;
    }

    public ScoreGetter getScoreGetter() {
        return this.scoreGetter;
    }

    public void setScoreGetter(ScoreGetter scoreGetter) {
        this.scoreGetter = scoreGetter;
    }
}

