package util;

import exception.BusinessException;

import java.util.HashMap;
import java.util.Map;

import static util.Constants.CLOSE_ARRAY_BRACKET;
import static util.Constants.CLOSE_FIGURE_BRACKET;
import static util.Constants.COLON;
import static util.Constants.COMMA;
import static util.Constants.EMPTY_LINE;
import static util.Constants.OPEN_ARRAY_BRACKET;
import static util.Constants.OPEN_FIGURE_BRACKET;
import static util.Constants.QUOTATION_MARK;

public class JsonProcessorUtil {

    public static Map<String, String> createFieldValueData(String json) {
        json = json.substring(1, json.length() - 2);
        char[] jsonChars = (json + COMMA).toCharArray();

        final Map<String, String> fieldValueData = new HashMap<>();

        String keyData = EMPTY_LINE;
        StringBuilder currentValue = new StringBuilder(EMPTY_LINE);
        int squareBracketCounter = 0;
        int figureBracketCounter = 0;

        for (char symbol : jsonChars) {

            if (OPEN_ARRAY_BRACKET.equals(symbol)) {
                squareBracketCounter++;
            }
            if (CLOSE_ARRAY_BRACKET.equals(symbol)) {
                squareBracketCounter--;
            }
            if (OPEN_FIGURE_BRACKET.equals(symbol)) {
                figureBracketCounter++;
            }
            if (CLOSE_FIGURE_BRACKET.equals(symbol)) {
                figureBracketCounter--;
            }

            if (COLON.equals(symbol) && squareBracketCounter == 0 && figureBracketCounter == 0) {
                keyData = currentValue.toString().trim().replace(QUOTATION_MARK, EMPTY_LINE);
                currentValue = new StringBuilder();
                continue;
            }

            if (COMMA.equals(symbol) && squareBracketCounter == 0 && figureBracketCounter == 0) {
                String valueData = currentValue.toString().trim().replace(QUOTATION_MARK, EMPTY_LINE);
                currentValue = new StringBuilder();
                fieldValueData.put(keyData, valueData);
                keyData = EMPTY_LINE;
                continue;
            }
            currentValue.append(symbol);
        }
        if (figureBracketCounter != 0 || squareBracketCounter != 0) {
            throw new BusinessException("Invalid token");
        }
        return fieldValueData;
    }
}
