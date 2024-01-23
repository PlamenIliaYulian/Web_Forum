package com.PlamenIliaYulian.Web_Forum.repositories;

import com.PlamenIliaYulian.Web_Forum.models.Tag;

public interface TagRepository {

    Tag getTagByName(String name);

    Tag createTag(Tag tag);

    void deleteTag(Tag tag);

    Tag updateTag(Tag tag);
}
