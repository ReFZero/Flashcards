package pl.ReFZero.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.ReFZero.exceptions.FailedToMatchException;

public class Validator {

    // Checks the provided file path is in the correct format
    public static boolean validateFullFilePath(String filePath){
        String regex = "^(english|norwegian|spanish|german)/\\1_\\d{4}_\\d{4}\\.json$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(filePath);
        if (matcher.matches()) return true;
        else throw new FailedToMatchException("Invalid file path.");
    }
}
