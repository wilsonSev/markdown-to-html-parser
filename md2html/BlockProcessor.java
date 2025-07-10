package md2html;

import java.util.HashMap;
import java.util.Map;

public class BlockProcessor {
    private final StringBuilder processedBlock = new StringBuilder();
    private final StringBuilder block;
    private int index;

    Map<String, String> specialsAlphabet = Map.of(
            "<", "&lt;",
            ">", "&gt;",
            "&", "&amp;"
    );
    Map<String, String> bracketAlphabet = Map.of(
            "*", "em",
            "_", "em",
            "**", "strong",
            "__", "strong",
            "--", "s",
            "++", "u",
            "`", "code"
    );
    Map<String, Integer> openedBrackets = new HashMap<>(Map.of(
            "*", -1,
            "_", -1,
            "**", -1,
            "__", -1,
            "--", -1,
            "++", -1,
            "`", -1
    ));

    public BlockProcessor(StringBuilder block) {
        this.block = block;
    }

    public StringBuilder processHTMLBlock() {
        for (index = 0; index < block.length(); index++) {
            char character = block.charAt(index);
            if (character == '\\') {
                if (index + 1 < block.length()) {
                    index++;
                    processedBlock.append(block.charAt(index));
                }
            } else if (specialsAlphabet.containsKey(String.valueOf(character))) {
                processedBlock.append(specialsAlphabet.get(String.valueOf(character)));
            } else {
                StringBuilder bracket = new StringBuilder();
                bracket.append(character);
                if (index + 1 < block.length()) {
                    if (block.charAt(index) == block.charAt(index + 1)) {
                        bracket.append(block.charAt(index + 1));
                        index++;
                    }
                }
                if (openedBrackets.containsKey(bracket.toString())) {
                    if (openedBrackets.get(String.valueOf(bracket)) != -1) {
                        int length = bracket.length();
                        int pos = openedBrackets.get(bracket.toString());
                        String openSymb = "<" + bracketAlphabet.get(bracket.toString()) + ">";
                        processedBlock.delete(pos, pos + length).insert(pos, openSymb);
                        String closeSymb = "</" + bracketAlphabet.get(bracket.toString()) + ">";
                        processedBlock.append(closeSymb);
                        openedBrackets.put(String.valueOf(bracket), -1);
                    } else {
                        openedBrackets.put(String.valueOf(bracket), processedBlock.length());
                        processedBlock.append(bracket);
                    }
                } else {
                    processedBlock.append(bracket);
                }
            }
        }
        return processedBlock;
    }
}