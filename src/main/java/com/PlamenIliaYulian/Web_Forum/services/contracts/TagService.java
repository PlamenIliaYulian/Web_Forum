package com.PlamenIliaYulian.Web_Forum.services.contracts;

import com.PlamenIliaYulian.Web_Forum.models.Tag;

import java.util.List;
import java.util.Set;

public interface TagService {

    /*TODO ✔ Iliya ✔*/
    Tag getTagByName(String name);

    /*TODO July - 📌 DONE 📌- last updated 25.01.2024*/
    Tag createTag(Tag tag);

    /*TODO Plamen*/
    void deleteTag(Tag tag);

    /*TODO ✔ Iliya ✔*/
    Tag updateTag(Tag tag);

    /*TODO July - 📌 DONE 📌- last updated 25.01.2024.*/
    List<Tag> getAllTags();
}
