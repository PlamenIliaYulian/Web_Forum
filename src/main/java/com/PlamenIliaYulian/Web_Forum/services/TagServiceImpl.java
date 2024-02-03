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

    /*Ilia*/
    @Override
    public Tag getTagByName(String name) {
        return tagRepository.getTagByName(name);
    }

    @Override
    public Tag createTag(Tag tag, User userToCheckIfBlocked) {
        PermissionHelper.isBlocked(userToCheckIfBlocked, UNAUTHORIZED_OPERATION);

        try {
            tag = tagRepository.getTagByName(tag.getTag());
        } catch (EntityNotFoundException e) {
            return tagRepository.createTag(tag);
        }
        return tag;

    }

    @Override
    public void deleteTag(Tag tag, User userToCheckIfBlocked) {
        PermissionHelper.isAdmin(userToCheckIfBlocked, UNAUTHORIZED_OPERATION);
        Set<Post> relatedPosts = tag.getRelatedPosts();
        relatedPosts.clear();
        tag.setDeleted(true);
        tagRepository.updateTag(tag);
    }

    @Override
    public Tag updateTag(Tag tag, User userToCheckIfBlocked) {
        PermissionHelper.isBlocked(userToCheckIfBlocked, UNAUTHORIZED_OPERATION);
        return tagRepository.updateTag(tag);
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.getAllTags();
    }

    @Override
    public Tag getTagById(int id) {
        return tagRepository.getTagById(id);
    }
}
