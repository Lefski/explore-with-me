package ru.practicum.ewm.model.comment;

import java.util.Comparator;

public class CommentComparator implements Comparator<Comment> {
    @Override
    public int compare(Comment comment1, Comment comment2) {
        return comment1.getCreated().compareTo(comment2.getCreated());
    }
}
