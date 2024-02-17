package com.PlamenIliaYulian.Web_Forum.repositories.contracts;

import com.PlamenIliaYulian.Web_Forum.models.Tag;

import java.util.List;

public interface TagRepository {

    Tag getTagByName(String name);

    Tag createTag(Tag tag);

    Tag updateTag(Tag tag);

    List<Tag> getAllTags();

    Tag getTagById(int id);
}
