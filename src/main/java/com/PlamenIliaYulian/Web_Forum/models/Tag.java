package com.PlamenIliaYulian.Web_Forum.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag implements Comparable<Tag> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private int tagId;

    @Column(name = "name")
    private String tag;

    @JsonIgnore
    @Column(name = "is_deleted")
    private boolean isDeleted;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "posts_tags",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    private Set<Post> relatedPosts;

    public Tag() {
    }

    public Tag(int tagId, String tag, boolean isDeleted) {
        this.tagId = tagId;
        this.tag = tag;
        this.isDeleted = isDeleted;
    }

    public Set<Post> getRelatedPosts() {
        return relatedPosts;
    }

    public void setRelatedPosts(Set<Post> relatedPosts) {
        this.relatedPosts = relatedPosts;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String name) {
        this.tag = name;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return tagId == tag.tagId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagId);
    }

    @Override
    public int compareTo(Tag o) {
        return Integer.compare(this.getTagId(), o.getTagId());
    }

    @Override
    public String toString() {
        return String.format("%s", getTag());
    }
}
