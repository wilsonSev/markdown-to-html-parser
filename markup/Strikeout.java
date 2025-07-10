package markup;

import java.util.List;

public class Strikeout extends MarkUpElements {
    public Strikeout(List<MarkDown> elList) {
        super(elList, "~", "#strike");
    }
}
