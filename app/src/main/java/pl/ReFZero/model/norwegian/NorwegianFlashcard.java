package pl.ReFZero.model.norwegian;

// The class that determines what the displayed flashcard for a given language should look like.

import android.content.res.AssetManager;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import pl.ReFZero.model.LanguageProvider;
import pl.ReFZero.utility.ListCreator;

public class NorwegianFlashcard implements LanguageProvider {

    private final List<NorwegianWord> norwegianWords;
    private Boolean isPolish = false;

    private final Random rand = new Random();
    private static final int MAX_RANDOM_VALUE = 50;
    private Integer random = rand.nextInt(MAX_RANDOM_VALUE);

    public NorwegianFlashcard(AssetManager am, String path) {
        this.norwegianWords = initializeWordList(am, path);
    }

    public void createFlashcard(
            String startLanguage,
            TextView wordNumber,
            TextView message) {
        randomizeNumber();
        if (startLanguage.equals("norwegian")) {
            wordNumber.setText(String.valueOf(norwegianWords.get(random).getId()));
            message.setText(norwegianWords.get(random).getNorwegian());
            isPolish = false;
        } else {
            wordNumber.setText(String.valueOf(norwegianWords.get(random).getId()));
            message.setText(norwegianWords.get(random).getPolish());
            isPolish = true;
        }
    }

    public void reverseFlashcard(TextView message) {
        if (!isPolish) {
            message.setText(norwegianWords.get(random).getPolish());
            isPolish = true;
        } else {
            message.setText(norwegianWords.get(random).getNorwegian());
            isPolish = false;
        }
    }

    private List<NorwegianWord> initializeWordList(AssetManager am, String path) {
        return ListCreator.createListWords(NorwegianWord[].class, am, path);
    }

    private void randomizeNumber() {
        Integer currentRandom = random;
        while (currentRandom.equals(random)) {
            random = rand.nextInt(50);
        }
    }
}


