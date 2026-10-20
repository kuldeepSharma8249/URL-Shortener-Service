package com.stackfortech.UrlShorteningServiceApplication.repository;

import com.stackfortech.UrlShorteningServiceApplication.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends JpaRepository<Url,Long> {
    public Url findByShortUrl(String shortUrl);
}
