package pl.piotr.catalog.domain.recipe;

public enum DishType {
    BREAKFAST("Śniadanie"), LUNCH("Przekąska"), DINNER("Obiad"),
    AFTERNOON_TEA("Podwieczorek"), SUPPER("Kolacja");

    private final String translatedToPolish;

    DishType(String valueInPolish) {
        this.translatedToPolish = valueInPolish;
    }

    public String getTranslatedToPolish() {
        return translatedToPolish;
    }
}
