package com.stackfortech.UrlShorteningServiceApplication.service;

import com.stackfortech.UrlShorteningServiceApplication.model.Url;
import com.stackfortech.UrlShorteningServiceApplication.model.UrlDto;
import org.springframework.stereotype.Service;

@Service
public interface UrlService {
    public Url generateShortLink(UrlDto urlDto);
    public Url persistUrlLink(Url url);
    public Url getEncodedUrl(String url);
    public void deleteShortLink(Url url);
}
