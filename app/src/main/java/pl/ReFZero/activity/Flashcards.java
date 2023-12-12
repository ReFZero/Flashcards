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
import pl.ReFZero.model.NorwegianWord;
import pl.ReFZero.model.SpanishWord;

@SuppressLint("MissingInflatedId")
public class Flashcards extends AppCompatActivity {

    private ObjectMapper mapper = new ObjectMapper();
    boolean reversed;
    private Random rand = new Random();
    private Integer random = rand.nextInt(50);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcards);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Button bNext = findViewById(R.id.b_next);
        Button bReverse = findViewById(R.id.b_reverse);

        TextView message = findViewById(R.id.word_content);
        TextView wordNumber = findViewById(R.id.word_number);

        Bundle extras = getIntent().getExtras();
        String firstLanguage = extras.getString("firstLanguage");
        String secondLanguage = extras.getString("secondLanguage");
        String numberFileName = extras.getString("fileNameToUse");

        String providedLanguage = provideLanguage(firstLanguage, secondLanguage);

        if (numberFileName != null) {
            switch (providedLanguage) {
                case "english": {
                    ArrayList<EnglishWord> englishWords = createListWords(EnglishWord[].class, numberFileName, providedLanguage);
                    bNext.setOnClickListener(v -> {
                        randomizeNumber();
                        switch (firstLanguage) {
                            case "polish":
                                message.setText(englishWords.get(random).getPolish());
                                wordNumber.setText(String.valueOf(englishWords.get(random).getId()));
                                reversed = false;
                                break;
                            case "english":
                                message.setText(englishWords.get(random).getEnglish());
                                wordNumber.setText(String.valueOf(englishWords.get(random).getId()));
                                reversed = false;
                                break;
                        }
                    });

                    bReverse.setOnClickListener(v -> {
                        if (secondLanguage.equals("polish"))
                            reversed = reverseFlashcard(reversed, message, englishWords, EnglishWord::getPolish, EnglishWord::getEnglish);
                        else if (secondLanguage.equals("english"))
                            reversed = reverseFlashcard(reversed, message, englishWords, EnglishWord::getEnglish, EnglishWord::getPolish);
                    });
                    break;
                }
                case "german": {
                    ArrayList<GermanWord> germanWords = createListWords(GermanWord[].class, numberFileName, providedLanguage);
                    bNext.setOnClickListener(v -> {
                        randomizeNumber();
                        switch (firstLanguage) {
                            case "polish":
                                message.setText(germanWords.get(random).getPolish());
                                wordNumber.setText(String.valueOf(germanWords.get(random).getId()));
                                reversed = false;
                                break;
                            case "german":
                                message.setText(germanWords.get(random).getGerman());
                                wordNumber.setText(String.valueOf(germanWords.get(random).getId()));
                                reversed = false;
                                break;
                        }
                    });

                    bReverse.setOnClickListener(v -> {
                            if (secondLanguage.equals("polish"))
                                reversed = reverseFlashcard(reversed, message, germanWords, GermanWord::getPolish, GermanWord::getGerman);
                            else if (secondLanguage.equals("german"))
                                reversed = reverseFlashcard(reversed, message, germanWords, GermanWord::getGerman, GermanWord::getPolish);
                    });
                    break;
                }
                case "spanish": {
                    ArrayList<SpanishWord> spanishWords = createListWords(SpanishWord[].class, numberFileName, providedLanguage);
                    bNext.setOnClickListener(v -> {
                        randomizeNumber();
                        switch (firstLanguage) {
                            case "polish":
                                message.setText(spanishWords.get(random).getPolish());
                                wordNumber.setText(String.valueOf(spanishWords.get(random).getId()));
                                reversed = false;
                                break;
                            case "spanish":
                                message.setText(spanishWords.get(random).getSpanish());
                                wordNumber.setText(String.valueOf(spanishWords.get(random).getId()));
                                reversed = false;
                                break;
                        }
                    });

                    bReverse.setOnClickListener(v -> {
                        if (secondLanguage.equals("polish"))
                            reversed = reverseFlashcard(reversed, message, spanishWords, SpanishWord::getPolish, SpanishWord::getSpanish);
                        else if (secondLanguage.equals("spanish"))
                            reversed = reverseFlashcard(reversed, message, spanishWords, SpanishWord::getSpanish, SpanishWord::getPolish);
                    });
                    break;
                }
                case "norwegian": {
                    ArrayList<NorwegianWord> norwegianWords = createListWords(NorwegianWord[].class, numberFileName, providedLanguage);
                    bNext.setOnClickListener(v -> {
                        randomizeNumber();
                        switch (firstLanguage) {
                            case "polish":
                                message.setText(norwegianWords.get(random).getPolish());
                                wordNumber.setText(String.valueOf(norwegianWords.get(random).getId()));
                                reversed = false;
                                break;
                            case "norwegian":
                                message.setText(norwegianWords.get(random).getNorwegian());
                                wordNumber.setText(String.valueOf(norwegianWords.get(random).getId()));
                                reversed = false;
                                break;
                        }
                    });

                    bReverse.setOnClickListener(v -> {
                        if (secondLanguage.equals("polish"))
                            reversed = reverseFlashcard(reversed, message, norwegianWords, NorwegianWord::getPolish, NorwegianWord::getNorwegian);
                        else if (secondLanguage.equals("norwegian"))
                            reversed = reverseFlashcard(reversed, message, norwegianWords, NorwegianWord::getNorwegian, NorwegianWord::getPolish);
                    });
                    break;
                }
                default:
                    throw new RuntimeException("Error: Provided language is not supported!");
            }
            bNext.performClick();
        }

    }

    private <T> boolean reverseFlashcard(boolean reverse,
                                         TextView message,
                                         ArrayList<T> list,
                                         Function<T, String> firstGetter,
                                         Function<T, String> secondGetter) {
        if (!reverse) {
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
            numberFileName, String providedLanguage) {
        List<T> wordList = new ArrayList<>(50);
        String fullFileName = createFullFileName(providedLanguage, numberFileName);
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

    private String provideLanguage(String first, String second) {
        if (first.equals("english") || second.equals("english")) {
            return "english";
        } else if (first.equals("german") || second.equals("german")) {
            return "german";
        } else if (first.equals("spanish") || second.equals("spanish")) {
            return "spanish";
        } else if (first.equals("norwegian") || second.equals("norwegian")) {
            return "norwegian";
        }
        return "Error";
    }
}
