package com.PlamenIliaYulian.Web_Forum.services.contracts;

import com.PlamenIliaYulian.Web_Forum.models.Tag;
import com.PlamenIliaYulian.Web_Forum.models.User;

import java.util.List;

public interface TagService {

    Tag getTagByName(String name);

    Tag createTag(Tag tag, User userToCheckIfBlocked);

    void deleteTag(Tag tag, User userToCheckIfBlocked);

    Tag updateTag(Tag tag, User userToCheckIfBlocked);

    List<Tag> getAllTags();
    Tag getTagById(int id);
}
