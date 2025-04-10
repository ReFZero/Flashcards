package pl.ReFZero.model.spanish;

// The class that determines what the displayed flashcard for a given language should look like.

import android.content.res.AssetManager;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import pl.ReFZero.model.LanguageProvider;
import pl.ReFZero.utility.ListCreator;

public class SpanishFlashcard implements LanguageProvider {

    private final List<SpanishWord> spanishWords;
    private Boolean isPolish = false;

    private final Random rand = new Random();
    private static final int MAX_RANDOM_VALUE = 50;
    private Integer random = rand.nextInt(MAX_RANDOM_VALUE);

    public SpanishFlashcard(AssetManager am, String path) {
        this.spanishWords = initializeWordList(am, path);
    }

    public void createFlashcard(
            String startLanguage,
            TextView wordNumber,
            TextView message) {
        randomizeNumber();
        if (startLanguage.equals("spanish")) {
            wordNumber.setText(String.valueOf(spanishWords.get(random).getId()));
            message.setText(spanishWords.get(random).getSpanish());
            isPolish = false;
        } else {
            wordNumber.setText(String.valueOf(spanishWords.get(random).getId()));
            message.setText(spanishWords.get(random).getPolish());
            isPolish = true;
        }
    }

    public void reverseFlashcard(TextView message) {
        if (!isPolish) {
            message.setText(spanishWords.get(random).getPolish());
            isPolish = true;
        } else {
            message.setText(spanishWords.get(random).getSpanish());
            isPolish = false;
        }
    }

    private List<SpanishWord> initializeWordList(AssetManager am, String path) {
        return ListCreator.createListWords(SpanishWord[].class, am, path);
    }

    private void randomizeNumber() {
        Integer currentRandom = random;
        while (currentRandom.equals(random)) {
            random = rand.nextInt(50);
        }
    }
}

