package markup;

import java.util.List;

public class Emphasis extends MarkUpElements {
    public Emphasis(List<MarkDown> elList) {
        super(elList, "*", "#emph");
    }
}
