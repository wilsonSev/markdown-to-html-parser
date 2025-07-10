package markup;

import java.util.List;

public abstract class MarkUpElements implements MarkDown {
    private final List<MarkDown> elList;
    private final String markdownSymbol;
    private final String typstSymbol;

    public MarkUpElements(List<MarkDown> elList, String markdownSymbol, String typstSymbol) {
        this.elList = elList;
        this.markdownSymbol = markdownSymbol;
        this.typstSymbol = typstSymbol;
    }

    @Override
    public void toMarkdown(StringBuilder str) {
        str.append(markdownSymbol);
        for (MarkDown md : elList) {
            md.toMarkdown(str);
        }
        str.append(markdownSymbol);
    }

    @Override
    public void toTypst(StringBuilder str) {
        if (!typstSymbol.isEmpty()) {
            str.append(typstSymbol);
            str.append("[");
            for (MarkDown md : elList) {
                md.toTypst(str);
            }
            str.append("]");
        } else {
            for (MarkDown md : elList) {
                md.toTypst(str);
            }
        }
    }
}
