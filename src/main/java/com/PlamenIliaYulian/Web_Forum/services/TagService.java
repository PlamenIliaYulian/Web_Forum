package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.models.Tag;

public interface TagService {

    /*TODO Iliya*/
    Tag getTagByName(String name);
    /*TODO July - DONE*/
    Tag createTag(Tag tag);
    /*TODO Plamen*/
    void deleteTag(Tag tag);
    /*TODO Iliya*/
    Tag updateTag(Tag tag);

}
