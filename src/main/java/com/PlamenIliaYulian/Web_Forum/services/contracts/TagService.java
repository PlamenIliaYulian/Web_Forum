package com.PlamenIliaYulian.Web_Forum.services.contracts;

import com.PlamenIliaYulian.Web_Forum.models.Tag;
import com.PlamenIliaYulian.Web_Forum.models.User;

import java.util.List;
import java.util.Set;

public interface TagService {

    /*✔ Iliya ✔*/
    Tag getTagByName(String name);

    /*July - 📌 DONE 📌- last updated 25.01.2024*/
    Tag createTag(Tag tag, User userToCheckIfBlocked);

    /*Plamen*/
    void deleteTag(Tag tag, User userToCheckIfBlocked);

    /*✔ Iliya ✔*/
    Tag updateTag(Tag tag, User userToCheckIfBlocked);

    /*July - 📌 DONE 📌- last updated 25.01.2024.*/
    List<Tag> getAllTags();
}
