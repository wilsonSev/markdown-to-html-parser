package md2html;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Scanner implements Closeable {
    private final Reader reader;
    private final int BUFFER_SIZE = 1024;
    private char[] buffer = new char[BUFFER_SIZE];
    private final StringBuilder token = new StringBuilder();
    private final StringBuilder currentLine = new StringBuilder();
    private final String lineSeparator = System.lineSeparator();
    private int length;
    private int pointer = 0;
    private char delimiter = ' ';
    private boolean nextLineIndicator = false;
    private final StringBuilder word = new StringBuilder();

    public Scanner(File file) throws IOException {
        reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
        length = reader.read(buffer);
    }

    public Scanner(InputStream inputStream) throws IOException {
        reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        length = reader.read(buffer);
    }

    public Scanner(String string) throws IOException {
        reader = new StringReader(string);
        length = reader.read(buffer);
    }

    public void setDelimiter(char tokenSeparator) {
        this.delimiter = tokenSeparator;
    }

    private boolean isEndOfBuffer() throws IOException {
        return length == -1;
    }

    private void movePointer() throws IOException {
        if (pointer == length - 1) {
            buffer = new char[BUFFER_SIZE];
            length = reader.read(buffer);
            pointer = 0;
        } else {
            pointer++;
        }
    }

    private boolean startsWithLineSeparator() throws IOException {
        if (buffer[pointer] == lineSeparator.charAt(0)) {
            if (pointer + lineSeparator.length() > length) {
                char[] newBuffer = new char[pointer + lineSeparator.length()];
                System.arraycopy(buffer, 0, newBuffer, 0, length);
                char[] buffer2 = new char[pointer + lineSeparator.length() - length];
                int length2 = reader.read(buffer2);
                if (length2 == -1) {
                    return false;
                }
                System.arraycopy(buffer2, 0, newBuffer, length, length2);
                length += length2;
                buffer = newBuffer;
            }
            for (int i = 0; i < lineSeparator.length(); i++) {
                if (buffer[pointer + i] != lineSeparator.charAt(i)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean hasNext() throws IOException {
        if (!token.isEmpty()) {
            return true;
        }

        while (!isEndOfBuffer()) {
            char character = buffer[pointer];
            if (character == delimiter) {
                movePointer();
                if (!token.isEmpty()) {
                    return true;
                }
            } else if (startsWithLineSeparator()) {
                for (int i = 0; i < lineSeparator.length(); i++) {
                    movePointer();
                }
                if (!token.isEmpty()) {
                    return true;
                }
            } else {
                token.append(character);
                movePointer();
            }
        }
        return !token.isEmpty();
    }

    public String next() throws IOException {
        if (hasNext()) {
            String value = token.toString();
            token.setLength(0);
            return value;
        }
        return null;
    }

    public int nextInt() throws IOException {
        return Integer.parseInt(next());
    }

    public boolean hasNextLine() throws IOException {
        if (nextLineIndicator) {
            return true;
        }
        while (!isEndOfBuffer()) {
            char character = buffer[pointer];
            if (startsWithLineSeparator()) {
                for (int i = 0; i < lineSeparator.length(); i++) {
                    movePointer();
                }
                nextLineIndicator = true;
                return true;
            } else {
                currentLine.append(character);
                movePointer();
            }
        }
        return false;
    }

    public String nextLine() throws IOException {
        if (hasNextLine()) {
            String line = currentLine.toString();
            currentLine.setLength(0);
            nextLineIndicator = false;
            return line;
        } else {
            return null;
        }
    }

    public boolean hasNextWord() throws IOException {
        if (!word.isEmpty()) {
            return true;
        }
        while (!isEndOfBuffer()) {
            char character = buffer[pointer];
            if (Character.isLetter(character) || character == '\'' || Character.getType(character) == Character.DASH_PUNCTUATION) {
                word.append(character);
                movePointer();
            } else {
                movePointer();
                if (!word.isEmpty()) {
                    return true;
                }
            }
        }
        return !word.isEmpty();
    }

    public String nextWord() throws IOException {
        if (hasNextWord()) {
            String value = word.toString();
            word.setLength(0);
            return value;
        } else {
            return null;
        }
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}