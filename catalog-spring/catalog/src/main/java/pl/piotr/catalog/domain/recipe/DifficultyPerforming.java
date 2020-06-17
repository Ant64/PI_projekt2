package pl.piotr.catalog.domain.recipe;

public enum  DifficultyPerforming {
    BEGINNER("Początkujący"), EASY("Łatwy"), AVERAGE("Średni"),
    HEAVY("Cięzki"), ADVANCED("Zaawansowany");

    private final String translatedToPolish;

    DifficultyPerforming(String valueInPolish) {
        this.translatedToPolish = valueInPolish;
    }

    public String getTranslatedToPolish() {
        return translatedToPolish;
    }
}
