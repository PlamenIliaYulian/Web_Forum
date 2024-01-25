package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.models.Post;
import com.PlamenIliaYulian.Web_Forum.models.Tag;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.TagRepository;
import com.PlamenIliaYulian.Web_Forum.services.contracts.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


@Service
public class TagServiceImpl implements TagService {

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
    public Tag createTag(Tag tag) {
        return tagRepository.createTag(tag);
    }

    @Override
    public void deleteTag(Tag tag) {
        Set<Post> relatedPosts = tag.getRelatedPosts();
        relatedPosts.clear();
        tag.setDeleted(true);
        tagRepository.updateTag(tag);
    }

    @Override
    public Tag updateTag(Tag tag) {
        return tagRepository.updateTag(tag);
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.getAllTags();
    }
}
