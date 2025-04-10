package pl.ReFZero.model;

/*
The class used to collect information from activity. Collects information about languages and vocabulary ranges
 */

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import pl.ReFZero.exceptions.LanguageNotSupportedException;

public class DataCollector implements Parcelable {

    private String firstLanguage = "";
    private String secondLanguage = "";
    private String vocabularyRange = "";

    private String fullFilePath = "";
    private LanguageType languageType ;


    public DataCollector() {
    }
    protected DataCollector(Parcel in) {
        this.firstLanguage = in.readString();
        this.secondLanguage = in.readString();
        this.vocabularyRange = in.readString();
    }

    public String getFirstLanguage() {
        return firstLanguage;
    }

    public void setFirstLanguage(String firstLanguage) {
        this.firstLanguage = firstLanguage;
    }

    public String getSecondLanguage() {
        return secondLanguage;
    }

    public void setSecondLanguage(String secondLanguage) {
        this.secondLanguage = secondLanguage;
    }

    public String getVocabularyRange() {
        return vocabularyRange;
    }

    public void setVocabularyRange(String vocabularyRange) {
        this.vocabularyRange = vocabularyRange;
    }
    public String getFullFilePath() {
        return fullFilePath;
    }

    public LanguageType getLanguageType() {
        return languageType;
    }

    public static final Creator<DataCollector> CREATOR = new Creator<>() {
        @Override
        public DataCollector createFromParcel(Parcel in) {
            return new DataCollector(in);
        }

        @Override
        public DataCollector[] newArray(int size) {
            return new DataCollector[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(firstLanguage);
        parcel.writeString(secondLanguage);
        parcel.writeString(vocabularyRange);
    }

    public void setLanguages(String fLanguage, String sLanguage) {
        this.setFirstLanguage(fLanguage);
        this.setSecondLanguage(sLanguage);
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
        throw new LanguageNotSupportedException("Provided language is not supported! Check provided language.");
    }

    private String createFullFilePath() {
        return this.getLanguageType().getStringValue() + "/" + this.getLanguageType().getStringValue() + "_" + this.getVocabularyRange() + ".json";
    }

// Verifies that the data collector contains all the data needed to create the flashcards
    public void updateData(){
        if (!firstLanguage.isBlank() && !secondLanguage.isBlank()){
            this.languageType = provideLanguage(firstLanguage, secondLanguage);
            if (!languageType.getStringValue().isBlank() && !vocabularyRange.isBlank()){
                this.fullFilePath = createFullFilePath();
            }
        }
    }
}
