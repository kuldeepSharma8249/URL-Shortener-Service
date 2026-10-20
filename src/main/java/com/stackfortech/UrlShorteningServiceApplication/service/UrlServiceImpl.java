package com.stackfortech.UrlShorteningServiceApplication.service;
import ch.qos.logback.core.util.StringUtil;
import com.google.common.hash.Hashing;
import com.stackfortech.UrlShorteningServiceApplication.model.Url;
import com.stackfortech.UrlShorteningServiceApplication.model.UrlDto;
import com.stackfortech.UrlShorteningServiceApplication.repository.UrlRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static ch.qos.logback.core.util.StringUtil.*;

@Component
public class UrlServiceImpl implements UrlService{

    @Autowired
    private UrlRepository urlRepository;

    @Override
    public Url generateShortLink(UrlDto urlDto) {
        if(StringUtils.isNotEmpty(urlDto.getUrl())){
            String encodedUrl= encodedUrl(urlDto.getUrl());
            Url urlToPersist=new Url();
            urlToPersist.setCreationDate(LocalDateTime.now());
            urlToPersist.setOriginalUrl(urlDto.getUrl());
            urlToPersist.setShortUrl(encodedUrl);
            urlToPersist.setExpirationDate(getExpirationDate(urlDto.getExpirationDate(),urlToPersist.getCreationDate()));
            Url urlToRet= persistShortLink(urlToPersist);
            if(urlToRet!=null){
                return urlToRet;
            }else {
                return null;
            }
        }
        return null;
    }

    private Url persistShortLink(Url url) {
        Url urlToRet= urlRepository.save(url);
        return urlToRet;
    }

    private LocalDateTime getExpirationDate(String expirationDate, LocalDateTime creationDate) {
        if(expirationDate==null || expirationDate.isBlank()){
            return creationDate.plusSeconds(60);
        }
        LocalDateTime expirationDateToRet= LocalDateTime.parse(expirationDate);
        return expirationDateToRet;
    }

    private String encodedUrl(String url) {
        String encodedUrl="";
        LocalDateTime time=LocalDateTime.now();
        encodedUrl = Hashing.murmur3_32_fixed()
                .hashString(url.concat(time.toString()), StandardCharsets.UTF_8)
                .toString();
        return encodedUrl;
    }

    @Override
    public Url persistUrlLink(Url url) {
        Url urlToRet = urlRepository.save(url);
        return urlToRet;
    }

    @Override
    public Url getEncodedUrl(String url) {
        Url urlToRet=urlRepository.findByShortUrl(url);
        return urlToRet;
    }

    @Override
    public void deleteShortLink(Url url) {
        urlRepository.delete(url);
    }
}
