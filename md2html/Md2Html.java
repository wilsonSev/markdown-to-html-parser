package md2html;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Md2Html {

    private static StringBuilder deleteExtraSeparator(StringBuilder block) {
        String lineSeparator = System.lineSeparator();
        for (int i = 0; i < lineSeparator.length(); i++) {
            block.deleteCharAt(block.length() - 1);
        }
        return block;
    }

    private static StringBuilder[] splitOnBlocks(File inputFile) throws IOException {
        try (Scanner scanner = new Scanner(inputFile)) {
            List<StringBuilder> blocks = new ArrayList<>();
            StringBuilder block = new StringBuilder();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (!line.isEmpty()) {
                    block.append(line);
                    block.append(System.lineSeparator());
                } else {
                    if (!block.isEmpty()) {
                        blocks.add(deleteExtraSeparator(block));
                        block = new StringBuilder();
                    }
                }
            }
            if (!block.isEmpty()) {
                blocks.add(deleteExtraSeparator(block));
            }
            return blocks.toArray(StringBuilder[]::new);
        }
    }

    private static StringBuilder createHTMLBlock(StringBuilder block) {
        StringBuilder openSym = new StringBuilder();
        StringBuilder closeSym = new StringBuilder();

        if (block.charAt(0) == '#') {
            for (int i = 0; i < block.length(); i++) {
                if (block.charAt(i) != '#') {
                    if (block.charAt(i) == ' ') {
                        openSym = new StringBuilder("<h" + i + ">");
                        closeSym = new StringBuilder("</h" + i + ">");
                        block.delete(0, i + 1);
                        break;
                    } else {
                        openSym = new StringBuilder("<p>");
                        closeSym = new StringBuilder("</p>");
                        break;
                    }
                }
            }
        } else {
            openSym = new StringBuilder("<p>");
            closeSym = new StringBuilder("</p>");
        }
        StringBuilder newBlock = new StringBuilder();
        newBlock.append(openSym);
        BlockProcessor processor = new BlockProcessor(block);
        newBlock.append(processor.processHTMLBlock());
        newBlock.append(closeSym);
        return newBlock;
    }

    public static void main(String[] args) {
        try {
            File inputFile = new File(args[0]);
            File outputFile = new File(args[1]);

            StringBuilder[] blocks = splitOnBlocks(inputFile);

            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8))) {
                for (StringBuilder block : blocks) {
                    StringBuilder newBlock = createHTMLBlock(block);
                    writer.write(newBlock.toString());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}