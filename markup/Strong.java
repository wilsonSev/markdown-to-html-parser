package markup;

import java.util.List;

public class Strong extends MarkUpElements {
    public Strong(List<MarkDown> elList) {
        super(elList, "__", "#strong");
    }
}
