package com.marincek.sympis.repository;

import com.marincek.sympis.domain.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LinkRepository extends JpaRepository<Link, Long> {

    @Query("SELECT l FROM Link l WHERE l.user.username = :username")
    List<Link> findAllByUsername(@Param("username") String username);

    /*
        SELECT TAGS.TAG FROM LINK
        LEFT JOIN TAGS ON LINK.LINK_ID = TAGS.LINK_ID
        GROUP BY TAGS.TAG
        ORDER BY COUNT(*) DESC;
     */
    @Query ("SELECT t FROM Link l LEFT JOIN l.tags t WHERE l.url = :url GROUP BY t ORDER BY COUNT(*) DESC")
    List<String> findAllTagsForUrl(@Param ("url") String url);

    @Query ("SELECT l FROM Link l WHERE l.user.username = :username AND l.url = :url")
    Link findByUserAndUrl(@Param ("username") String username, @Param ("url") String url);
}
