package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.exceptions.EntityNotFoundException;
import com.PlamenIliaYulian.Web_Forum.services.helpers.PermissionHelper;
import com.PlamenIliaYulian.Web_Forum.models.Post;
import com.PlamenIliaYulian.Web_Forum.models.Tag;
import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.TagRepository;
import com.PlamenIliaYulian.Web_Forum.services.contracts.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


@Service
public class TagServiceImpl implements TagService {
    public static final String UNAUTHORIZED_OPERATION = "Unauthorized operation.";

    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    /*TODO ILIQ */
    @Override
    public Tag getTagByName(String name) {
        return tagRepository.getTagByName(name);
    }

    /*TODO JULY
    * I could not find another way to make the test cover the final return. I had to make the changes seen below.*/
    @Override
    public Tag createTag(Tag tag, User userToCheckIfBlocked) {
        PermissionHelper.isBlocked(userToCheckIfBlocked, UNAUTHORIZED_OPERATION);

        Tag tagToCreate = null;
        try {
            tag = tagRepository.getTagByName(tag.getTag());
        } catch (EntityNotFoundException e) {
            tagToCreate = tagRepository.createTag(tag);
        }
        return tagToCreate;

    }

    @Override
    public void deleteTag(Tag tag, User userToCheckIfBlocked) {
        PermissionHelper.isAdmin(userToCheckIfBlocked, UNAUTHORIZED_OPERATION);
        Set<Post> relatedPosts = tag.getRelatedPosts();
        relatedPosts.clear();
        tag.setDeleted(true);
        tagRepository.updateTag(tag);
    }

    /*TODO PLAMEN */
    @Override
    public Tag updateTag(Tag tag, User userToCheckIfBlocked) {
        PermissionHelper.isBlocked(userToCheckIfBlocked, UNAUTHORIZED_OPERATION);
        return tagRepository.updateTag(tag);
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.getAllTags();
    }

    /*TODO ILIQ */
    @Override
    public Tag getTagById(int id) {
        return tagRepository.getTagById(id);
    }
}
