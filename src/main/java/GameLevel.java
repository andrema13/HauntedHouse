enum GameLevel {
    BASIC(1),
    NORMAL(2),
    HARD(3);

    /**
     * Game Level variable that contains the game level of the game
     */
    private int gameLevelValue;

    public int getGameLevelValue() {
        return gameLevelValue;
    }

    GameLevel(int gameLevel) {
        this.gameLevelValue = gameLevel;
    }
}
