package pl.ReFZero.utility;

// The utility class for creating and preparing Lists

import android.content.res.AssetManager;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import pl.ReFZero.exceptions.CreateListContainsFileNamesException;
import pl.ReFZero.exceptions.FailedToCreateWordListException;
import pl.ReFZero.exceptions.FailedToMatchException;
import pl.ReFZero.exceptions.LanguageClassException;
import pl.ReFZero.exceptions.LanguageNotSupportedException;
import pl.ReFZero.model.DataCollector;

public class ListCreator {
    public static  <T> ArrayList<T> createListWords(Class<T[]> languageClass, AssetManager am, String path) {
        ObjectMapper mapper = new ObjectMapper();
        List<T> wordList;
        if (languageClass == null) {
            throw new LanguageClassException("languageClass array is null or empty!");
        } else {
            try {
                InputStream inputStream = am.open(path);
                wordList = Arrays.asList(mapper.readValue(inputStream, languageClass));
                inputStream.close();
            } catch (IOException ioException) {
                throw new FailedToCreateWordListException("Failed to create word list. Check provided language");
            }
        }
        return new ArrayList<>(wordList);
    }

    public static List<String> createListContainsFileNames(AssetManager am, String language) {
        List<String> fileNames;
        String delimiter = "/";
        try {
            fileNames = Arrays.asList(am.list(language + delimiter));
        } catch (IOException e) {
            throw new CreateListContainsFileNamesException("Failed to create filename list");
        }
        return fileNames;
    }

    public static List<String> createButtonLabel(AssetManager am, DataCollector dataCollector) {
        String[] parts = {dataCollector.getFirstLanguage(), dataCollector.getSecondLanguage()};
        List<String> listFileNames;
        if (Arrays.stream(parts).anyMatch(s -> s.contains("english")))
            listFileNames = ListCreator.createListContainsFileNames(am, "english");
        else if (Arrays.stream(parts).anyMatch(s -> s.contains("german")))
            listFileNames = ListCreator.createListContainsFileNames(am, "german");
        else if (Arrays.stream(parts).anyMatch(s -> s.contains("spanish")))
            listFileNames = ListCreator.createListContainsFileNames(am,"spanish");
        else if (Arrays.stream(parts).anyMatch(s -> s.contains("norwegian")))
            listFileNames = ListCreator.createListContainsFileNames(am,"norwegian");
        else throw new LanguageNotSupportedException("Language not supported.");
        return listFileNames.stream()
                .map(ListCreator::prepareButtonLabel)
                .collect(Collectors.toList());
    }
    public static String prepareButtonLabel(String input) {

        String regex = ".*_(\\d+)_(\\d+)\\.json";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        // Checking matches
        if (matcher.matches()) {
            String start = removeLeadingZeros(Objects.requireNonNull(matcher.group(1)));
            String end = removeLeadingZeros(Objects.requireNonNull(matcher.group(2)));
            // Creating the "start-end" result string
            return start + "-" + end;
        } else {
            throw new FailedToMatchException("Failed to match.");
        }
    }
    private static String removeLeadingZeros(String input) {
        return input.replaceFirst("^0+", "");
    }
}
