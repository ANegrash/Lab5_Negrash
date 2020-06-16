package src;

import java.util.Comparator;

public class SortById implements Comparator<LabWork> {
    public int compare(LabWork a, LabWork b)

    {

        return a.id - b.id;

    }
}
