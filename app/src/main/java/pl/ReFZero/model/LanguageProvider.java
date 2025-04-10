package pl.ReFZero.model;

// The interface enables appropriate support for a specific language

import android.widget.TextView;

public interface LanguageProvider {
    void createFlashcard(String startLanguage, TextView wordNumber, TextView message);

    void reverseFlashcard(TextView message);
}
