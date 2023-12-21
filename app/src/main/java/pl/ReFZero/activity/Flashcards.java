/*
Flashcards class.
By receiving information from the previous activity, it creates a list of flashcards
and starts with the appropriate first language. Allows to change and rotate flashcards.
*/
package pl.ReFZero.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import pl.ReFZero.R;
import pl.ReFZero.exceptions.FailedToCreateWordListException;
import pl.ReFZero.exceptions.LanguageClassException;
import pl.ReFZero.exceptions.LanguageNotSupportedException;
import pl.ReFZero.model.EnglishWord;
import pl.ReFZero.model.GermanWord;
import pl.ReFZero.model.LanguageType;
import pl.ReFZero.model.NorwegianWord;
import pl.ReFZero.model.SpanishWord;

@SuppressLint("MissingInflatedId")
public class Flashcards extends AppCompatActivity {
    private final Random rand = new Random();
    private static final int MAX_RANDOM_VALUE = 50;
    private Integer random = rand.nextInt(MAX_RANDOM_VALUE);
    private ObjectMapper mapper = new ObjectMapper();
    boolean reversed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcards);

        Button b_next = findViewById(R.id.b_next);
        Button b_reverse = findViewById(R.id.b_reverse);

        TextView word_content = findViewById(R.id.word_content);
        TextView word_number = findViewById(R.id.word_number);

        Bundle extras = getIntent().getExtras();
        String firstLanguage = extras.getString("firstLanguage");
        String secondLanguage = extras.getString("secondLanguage");
        String numberFileName = extras.getString("fileNameToUse");

        if (numberFileName != null) {
            // Depending on the language provided, appropriate methods will be performed
            LanguageType providedLanguage = provideLanguage(firstLanguage, secondLanguage);
            switch (providedLanguage) {
                case ENGLISH: {
                    ArrayList<EnglishWord> englishWords = createListWords(EnglishWord[].class, numberFileName, providedLanguage);
                    b_next.setOnClickListener(v -> {
                        randomizeNumber();
                        if (firstLanguage.equals(LanguageType.POLISH.getStringValue()))
                            createFlashcard(word_number, word_content, englishWords, EnglishWord::getId, EnglishWord::getPolish);
                        else
                            createFlashcard(word_number, word_content, englishWords, EnglishWord::getId, EnglishWord::getEnglish);
                    });

                    b_reverse.setOnClickListener(v -> {
                        if (secondLanguage.equals(LanguageType.POLISH.getStringValue()))
                            reversed = reverseFlashcard(word_content, englishWords, EnglishWord::getPolish, EnglishWord::getEnglish);
                        else
                            reversed = reverseFlashcard(word_content, englishWords, EnglishWord::getEnglish, EnglishWord::getPolish);
                    });
                    break;
                }
                case GERMAN: {
                    ArrayList<GermanWord> germanWords = createListWords(GermanWord[].class, numberFileName, providedLanguage);
                    b_next.setOnClickListener(v -> {
                        randomizeNumber();
                        if (firstLanguage.equals(LanguageType.POLISH.getStringValue()))
                            createFlashcard(word_number, word_content, germanWords, GermanWord::getId, GermanWord::getPolish);
                        else
                            createFlashcard(word_number, word_content, germanWords, GermanWord::getId, GermanWord::getGerman);
                    });

                    b_reverse.setOnClickListener(v -> {
                        if (secondLanguage.equals(LanguageType.POLISH.getStringValue()))
                            reversed = reverseFlashcard(word_content, germanWords, GermanWord::getPolish, GermanWord::getGerman);
                        else
                            reversed = reverseFlashcard(word_content, germanWords, GermanWord::getGerman, GermanWord::getPolish);
                    });
                    break;
                }
                case SPANISH: {
                    ArrayList<SpanishWord> spanishWords = createListWords(SpanishWord[].class, numberFileName, providedLanguage);
                    b_next.setOnClickListener(v -> {
                        randomizeNumber();
                        if (firstLanguage.equals(LanguageType.POLISH.getStringValue()))
                            createFlashcard(word_number, word_content, spanishWords, SpanishWord::getId, SpanishWord::getPolish);
                        else
                            createFlashcard(word_number, word_content, spanishWords, SpanishWord::getId, SpanishWord::getSpanish);
                    });

                    b_reverse.setOnClickListener(v -> {
                        if (secondLanguage.equals(LanguageType.POLISH.getStringValue()))
                            reversed = reverseFlashcard(word_content, spanishWords, SpanishWord::getPolish, SpanishWord::getSpanish);
                        else
                            reversed = reverseFlashcard(word_content, spanishWords, SpanishWord::getSpanish, SpanishWord::getPolish);
                    });
                    break;
                }
                case NORWEGIAN: {
                    ArrayList<NorwegianWord> norwegianWords = createListWords(NorwegianWord[].class, numberFileName, providedLanguage);
                    b_next.setOnClickListener(v -> {
                        randomizeNumber();
                        if (firstLanguage.equals(LanguageType.POLISH.getStringValue()))
                            createFlashcard(word_number, word_content, norwegianWords, NorwegianWord::getId, NorwegianWord::getPolish);
                        else
                            createFlashcard(word_number, word_content, norwegianWords, NorwegianWord::getId, NorwegianWord::getNorwegian);
                    });

                    b_reverse.setOnClickListener(v -> {
                        if (secondLanguage.equals(LanguageType.POLISH.getStringValue()))
                            reversed = reverseFlashcard(word_content, norwegianWords, NorwegianWord::getPolish, NorwegianWord::getNorwegian);
                        else
                            reversed = reverseFlashcard(word_content, norwegianWords, NorwegianWord::getNorwegian, NorwegianWord::getPolish);
                    });
                    break;
                }
                default:
                    throw new LanguageNotSupportedException("Provided language is not supported! Check extras from previous activity.");
            }
            b_next.performClick();
        }
    }

    // Initializes the flashcard
    private <T> void createFlashcard(
            TextView wordNumber,
            TextView message,
            ArrayList<T> list,
            Function<T, Integer> getterId,
            Function<T, String> getterLanguage) {
        wordNumber.setText(String.valueOf(getterId.apply(list.get(random))));
        message.setText(getterLanguage.apply(list.get(random)));
        reversed = false;
    }

    // Allows to change the language of the flashcard
    private <T> boolean reverseFlashcard(
            TextView message,
            ArrayList<T> list,
            Function<T, String> firstGetter,
            Function<T, String> secondGetter) {
        if (!reversed) {
            message.setText(firstGetter.apply(list.get(random)));
            return true;
        } else {
            message.setText(secondGetter.apply(list.get(random)));
            return false;
        }
    }

    // Randomization of the number of the flashcards, preventing repetition of numbers
    private void randomizeNumber() {
        Integer currentRandom = random;
        while (currentRandom.equals(random)) {
            random = rand.nextInt(50);
        }
    }

    // Create a collection of words for flashcards
    private <T> ArrayList<T> createListWords(Class<T[]> languageClass, String
            numberFileName, LanguageType providedLanguage) {
        List<T> wordList;
        String fullFileName = createFullFileName(providedLanguage.getStringValue(), numberFileName);
        if (languageClass == null) {
            throw new LanguageClassException("languageClass array is null or empty!");
        } else {
            try {
                InputStream inputStream = getAssets().open(fullFileName);
                wordList = Arrays.asList(mapper.readValue(inputStream, languageClass));
                inputStream.close();
            } catch (IOException ioException) {
                throw new FailedToCreateWordListException("Failed to create word list. Check provided language");
            }
        }
        return new ArrayList<>(wordList);
    }

    // Creating a file name from which words will be created from the language information from the previous activity
    private String createFullFileName(String fromLanguage, String numberFileName) {
        return fromLanguage + "/" + fromLanguage + "_" + numberFileName + ".json";
    }

    // Provides information about what language will be used
    private LanguageType provideLanguage(String first, String second) {
        if (first.equals("english") || second.equals("english")) {
            return LanguageType.ENGLISH;
        } else if (first.equals("german") || second.equals("german")) {
            return LanguageType.GERMAN;
        } else if (first.equals("spanish") || second.equals("spanish")) {
            return LanguageType.SPANISH;
        } else if (first.equals("norwegian") || second.equals("norwegian")) {
            return LanguageType.NORWEGIAN;
        }
        throw new LanguageNotSupportedException("Provided language is not supported! Check provided language.");
    }
}
