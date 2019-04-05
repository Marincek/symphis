package com.marincek.sympis.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="LINKS")
public class Link {

    @Id
    @Column(name = "link_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @ElementCollection
    @CollectionTable(
            name="TAGS",
            joinColumns=@JoinColumn(name="link_id")
    )
    @Column(name="tag")
    private List<String> tags;

    @ManyToOne
    private User user;

    public Link(String url, List<String> tags) {
        this.url = url;
        this.tags = tags;
    }

    public Link() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
