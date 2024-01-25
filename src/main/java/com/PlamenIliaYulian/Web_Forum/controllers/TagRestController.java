package com.PlamenIliaYulian.Web_Forum.controllers;

import com.PlamenIliaYulian.Web_Forum.exceptions.AuthenticationException;
import com.PlamenIliaYulian.Web_Forum.exceptions.EntityNotFoundException;
import com.PlamenIliaYulian.Web_Forum.helpers.AuthenticationHelper;
import com.PlamenIliaYulian.Web_Forum.helpers.contracts.ModelsMapper;
import com.PlamenIliaYulian.Web_Forum.models.Tag;
import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.models.dtos.TagDto;
import com.PlamenIliaYulian.Web_Forum.services.contracts.TagService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tags")
public class TagRestController {

    private final TagService tagService;
    private final AuthenticationHelper authenticationHelper;

    private final ModelsMapper modelsMapper;


    public TagRestController(TagService tagService, AuthenticationHelper authenticationHelper, ModelsMapper modelsMapper) {
        this.tagService = tagService;
        this.authenticationHelper = authenticationHelper;
        this.modelsMapper = modelsMapper;
    }

    /*TODO - does a person need to be logged in in order to get all the tags?*/
    @GetMapping
    public List<Tag> getAllTags(@RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return tagService.getAllTags();
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /*Ilia*/
    @GetMapping("/{tagName}")
    public Tag getTagByName(@PathVariable String tagName,
                            @RequestHeader HttpHeaders headers) {

        User user = authenticationHelper.tryGetUser(headers);
        return tagService.getTagByName(tagName);
    }

    @PostMapping()
    public Tag createTag(@RequestBody TagDto tagDto,
                         @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Tag tag = modelsMapper.tagFromDto(tagDto);
            return tagService.createTag(tag);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{tagName}")
    public void deleteTag(@RequestHeader HttpHeaders headers,
                          @PathVariable String tagName) {
        User user = authenticationHelper.tryGetUser(headers);
        Tag tag = tagService.getTagByName(tagName);
        tagService.deleteTag(tag);
    }

    /*Ilia*/
    @PutMapping("/{tagName}")
    public Tag updateTag(@RequestHeader HttpHeaders headers,
                         @PathVariable String tagName,
                         @RequestBody TagDto tagDto) {
        User user = authenticationHelper.tryGetUser(headers);
        Tag tagToBeUpdated = modelsMapper.tagFromDto(tagDto, tagName);
        return tagService.updateTag(tagToBeUpdated);
    }
}
