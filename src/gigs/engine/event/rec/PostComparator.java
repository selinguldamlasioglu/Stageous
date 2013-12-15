package gigs.engine.event.rec;

import java.util.Comparator;

import gigs.engine.event.user.Post;

public class PostComparator implements Comparator<Post> {
    @Override
    public int compare(Post p1, Post p2) {
        return p1.timestmp.compareTo(p2.timestmp);
    }
}
