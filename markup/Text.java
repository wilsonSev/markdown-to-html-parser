package markup;

public class Text implements MarkDown {

    private final String text;

    public Text(String text) {
        this.text = text;
    }

    public void toMarkdown(StringBuilder str) {
        str.append(text);
    }

    public void toTypst(StringBuilder str) {
        str.append(text);
    }
}
