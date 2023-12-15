package pl.ReFZero.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import pl.ReFZero.R;
import pl.ReFZero.model.EnglishWord;
import pl.ReFZero.model.GermanWord;
import pl.ReFZero.model.LanguageType;
import pl.ReFZero.model.NorwegianWord;
import pl.ReFZero.model.SpanishWord;

@SuppressLint("MissingInflatedId")
public class Flashcards extends AppCompatActivity {
    private static final int MAX_RANDOM_VALUE = 50;
    private final Random rand = new Random();
    private Integer random = rand.nextInt(MAX_RANDOM_VALUE);
    private ObjectMapper mapper = new ObjectMapper();
    boolean reversed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcards);

        Button bNext = findViewById(R.id.b_next);
        Button bReverse = findViewById(R.id.b_reverse);

        TextView message = findViewById(R.id.word_content);
        TextView wordNumber = findViewById(R.id.word_number);

        Bundle extras = getIntent().getExtras();
        String firstLanguage = extras.getString("firstLanguage");
        String secondLanguage = extras.getString("secondLanguage");
        String numberFileName = extras.getString("fileNameToUse");

        LanguageType providedLanguage = provideLanguage(firstLanguage, secondLanguage);

        if (numberFileName != null) {
            switch (providedLanguage) {
                case ENGLISH: {
                    ArrayList<EnglishWord> englishWords = createListWords(EnglishWord[].class, numberFileName, providedLanguage);
                    bNext.setOnClickListener(v -> {
                        randomizeNumber();
                        if (firstLanguage.equals(LanguageType.POLISH.getStringValue()))
                            createFlashcard(wordNumber, message, englishWords, EnglishWord::getId, EnglishWord::getPolish);
                        else
                            createFlashcard(wordNumber, message, englishWords, EnglishWord::getId, EnglishWord::getEnglish);
                    });

                    bReverse.setOnClickListener(v -> {
                        if (secondLanguage.equals(LanguageType.POLISH.getStringValue()))
                            reversed = reverseFlashcard(message, englishWords, EnglishWord::getPolish, EnglishWord::getEnglish);
                        else
                            reversed = reverseFlashcard(message, englishWords, EnglishWord::getEnglish, EnglishWord::getPolish);
                    });
                    break;
                }
                case GERMAN: {
                    ArrayList<GermanWord> germanWords = createListWords(GermanWord[].class, numberFileName, providedLanguage);
                    bNext.setOnClickListener(v -> {
                        randomizeNumber();
                        if (firstLanguage.equals(LanguageType.POLISH.getStringValue()))
                            createFlashcard(wordNumber, message, germanWords, GermanWord::getId, GermanWord::getPolish);
                        else
                            createFlashcard(wordNumber, message, germanWords, GermanWord::getId, GermanWord::getGerman);
                    });

                    bReverse.setOnClickListener(v -> {
                        if (secondLanguage.equals(LanguageType.POLISH.getStringValue()))
                            reversed = reverseFlashcard(message, germanWords, GermanWord::getPolish, GermanWord::getGerman);
                        else
                            reversed = reverseFlashcard(message, germanWords, GermanWord::getGerman, GermanWord::getPolish);
                    });
                    break;
                }
                case SPANISH: {
                    ArrayList<SpanishWord> spanishWords = createListWords(SpanishWord[].class, numberFileName, providedLanguage);
                    bNext.setOnClickListener(v -> {
                        randomizeNumber();
                        if (firstLanguage.equals(LanguageType.POLISH.getStringValue()))
                            createFlashcard(wordNumber, message, spanishWords, SpanishWord::getId, SpanishWord::getPolish);
                        else
                            createFlashcard(wordNumber, message, spanishWords, SpanishWord::getId, SpanishWord::getSpanish);
                    });

                    bReverse.setOnClickListener(v -> {
                        if (secondLanguage.equals(LanguageType.POLISH.getStringValue()))
                            reversed = reverseFlashcard(message, spanishWords, SpanishWord::getPolish, SpanishWord::getSpanish);
                        else
                            reversed = reverseFlashcard(message, spanishWords, SpanishWord::getSpanish, SpanishWord::getPolish);
                    });
                    break;
                }
                case NORWEGIAN: {
                    ArrayList<NorwegianWord> norwegianWords = createListWords(NorwegianWord[].class, numberFileName, providedLanguage);
                    bNext.setOnClickListener(v -> {
                        randomizeNumber();
                        if (firstLanguage.equals(LanguageType.POLISH.getStringValue()))
                            createFlashcard(wordNumber, message, norwegianWords, NorwegianWord::getId, NorwegianWord::getPolish);
                        else
                            createFlashcard(wordNumber, message, norwegianWords, NorwegianWord::getId, NorwegianWord::getNorwegian);
                    });

                    bReverse.setOnClickListener(v -> {
                        if (secondLanguage.equals(LanguageType.POLISH.getStringValue()))
                            reversed = reverseFlashcard(message, norwegianWords, NorwegianWord::getPolish, NorwegianWord::getNorwegian);
                        else
                            reversed = reverseFlashcard(message, norwegianWords, NorwegianWord::getNorwegian, NorwegianWord::getPolish);
                    });
                    break;
                }
                default:
                    throw new RuntimeException("Error: Provided language is not supported!");
            }
            bNext.performClick();
        }

    }

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

    private void randomizeNumber() {
        Integer currentRandom = random;
        while (currentRandom.equals(random)) {
            random = rand.nextInt(50);
        }
    }

    private <T> ArrayList<T> createListWords(Class<T[]> languageClass, String
            numberFileName, LanguageType providedLanguage) {
        List<T> wordList = new ArrayList<>(50);
        String fullFileName = createFullFileName(providedLanguage.getStringValue(), numberFileName);
        if (languageClass == null) {
            Log.e("App", "Error: languageClass array is null or empty!");
        } else {
            try {
                InputStream inputStream = getAssets().open(fullFileName);
                wordList = Arrays.asList(mapper.readValue(inputStream, languageClass));
                inputStream.close();
            } catch (IOException ioException) {
                Log.e("App", "Error: File not found! Check 'extras' file name.");
            }
        }
        return new ArrayList<>(wordList);
    }

    private String createFullFileName(String fromLanguage, String numberFileName) {
        return fromLanguage + "/" + fromLanguage + "_" + numberFileName + ".json";
    }

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
        return LanguageType.POLISH;
    }
}
