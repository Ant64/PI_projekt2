package pl.piotr.catalog.domain.recipe;

public enum DishIngredient {
    CHOCOLATE("Czekolada"), EGGS("Jajka"), BUCKWHEAT("Kasza gryczana"),
    BARLEY("Kasza jęczmienna"), PASTA("Makaron"), SAUSAGE("Kiełbasa"),
    CHICKEN("Kurczak"), PORK("Wieprzowina"), BEEF("Wołowina"),
    SWISS_CHEESE("Ser szwajcarski"), CHEESE("Ser żółty"), PARMESAN("Parmezan"),
    SOUR_CREAM("Śmietana"), COD("Dorsz"), SALMON("Łosoś"), HERRING("Śledz"),
    RICE("Ryż"), POTATOES("Ziemniaki"), ONION("Cebula"), BEETROOT("Buraki"),
    LETTUCE("Sałata"), TOMATOES("Pomidory"), CABBAGE("Kapusta");

    private final String translatedToPolish;

    DishIngredient(String valueInPolish) {
        this.translatedToPolish = valueInPolish;
    }

    public String getTranslatedToPolish() {
        return translatedToPolish;
    }
}
