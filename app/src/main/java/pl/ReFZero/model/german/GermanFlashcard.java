package pl.ReFZero.model.german;

// The class that determines what the displayed flashcard for a given language should look like.

import android.content.res.AssetManager;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import pl.ReFZero.model.LanguageProvider;
import pl.ReFZero.utility.ListCreator;

public class GermanFlashcard implements LanguageProvider {

    private final List<GermanWord> germanWords;
    private Boolean isPolish = false;

    private final Random rand = new Random();
    private static final int MAX_RANDOM_VALUE = 50;
    private Integer random = rand.nextInt(MAX_RANDOM_VALUE);

    public GermanFlashcard(AssetManager am, String path) {
        this.germanWords = initializeWordList(am, path);
    }

    public void createFlashcard(
            String startLanguage,
            TextView wordNumber,
            TextView message) {
        randomizeNumber();
        if (startLanguage.equals("german")) {
            wordNumber.setText(String.valueOf(germanWords.get(random).getId()));
            message.setText(germanWords.get(random).getGerman());
            isPolish = false;
        } else {
            wordNumber.setText(String.valueOf(germanWords.get(random).getId()));
            message.setText(germanWords.get(random).getPolish());
            isPolish = true;
        }
    }

    public void reverseFlashcard(TextView message) {
        if (!isPolish) {
            message.setText(germanWords.get(random).getPolish());
            isPolish = true;
        } else {
            message.setText(germanWords.get(random).getGerman());
            isPolish = false;
        }
    }

    private List<GermanWord> initializeWordList(AssetManager am, String path) {
        return ListCreator.createListWords(GermanWord[].class, am, path);
    }

    private void randomizeNumber() {
        Integer currentRandom = random;
        while (currentRandom.equals(random)) {
            random = rand.nextInt(50);
        }
    }
}
