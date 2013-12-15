package gigs.engine.event.rec;

import java.util.Comparator;

import gigs.engine.event.user.Tag;

public class TagComparator implements Comparator<Tag> {
    @Override
    public int compare(Tag o1, Tag o2) {
        return o1.interest - o2.interest;
    }
}
