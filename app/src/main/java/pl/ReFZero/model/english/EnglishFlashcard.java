package pl.ReFZero.model.english;

// The class that determines what the displayed flashcard for a given language should look like.

import android.content.res.AssetManager;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import pl.ReFZero.model.LanguageProvider;
import pl.ReFZero.utility.ListCreator;

public class EnglishFlashcard implements LanguageProvider {

    private final List<EnglishWord> englishWords;
    private Boolean isPolish = false;

    private final Random rand = new Random();
    private static final int MAX_RANDOM_VALUE = 50;
    private Integer random = rand.nextInt(MAX_RANDOM_VALUE);

    public EnglishFlashcard(AssetManager am, String path) {
        this.englishWords = initializeWordList(am, path);
    }

    public void createFlashcard(
            String startLanguage,
            TextView wordNumber,
            TextView message) {
        randomizeNumber();
        if (startLanguage.equals("english")) {
            wordNumber.setText(String.valueOf(englishWords.get(random).getId()));
            message.setText(englishWords.get(random).getEnglish());
            isPolish = false;
        } else {
            wordNumber.setText(String.valueOf(englishWords.get(random).getId()));
            message.setText(englishWords.get(random).getPolish());
            isPolish = true;
        }
    }

    public void reverseFlashcard(TextView message) {
        if (!isPolish) {
            message.setText(englishWords.get(random).getPolish());
            isPolish = true;
        } else {
            message.setText(englishWords.get(random).getEnglish());
            isPolish = false;
        }
    }

    private List<EnglishWord> initializeWordList(AssetManager am, String path) {
        return ListCreator.createListWords(EnglishWord[].class, am, path);
    }

    private void randomizeNumber() {
        Integer currentRandom = random;
        while (currentRandom.equals(random)) {
            random = rand.nextInt(50);
        }
    }
}
