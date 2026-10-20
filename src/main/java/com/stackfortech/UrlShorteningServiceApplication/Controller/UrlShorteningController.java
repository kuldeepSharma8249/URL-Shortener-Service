package com.stackfortech.UrlShorteningServiceApplication.Controller;

import com.stackfortech.UrlShorteningServiceApplication.model.Url;
import com.stackfortech.UrlShorteningServiceApplication.model.UrlDto;
import com.stackfortech.UrlShorteningServiceApplication.model.UrlErrorResponseDto;
import com.stackfortech.UrlShorteningServiceApplication.service.UrlService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import com.stackfortech.UrlShorteningServiceApplication.model.UrlResponseDto;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
public class UrlShorteningController {
    @Autowired
    UrlService urlService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateShortlink(@RequestBody UrlDto urlDto){
        Url urlToRet=urlService.generateShortLink(urlDto);
        if(urlToRet!=null){
            UrlResponseDto urlResponseDto=new UrlResponseDto();
            urlResponseDto.setOriginalUrl(urlToRet.getOriginalUrl());
            urlResponseDto.setExpirationDate(urlToRet.getExpirationDate());
            urlResponseDto.setShortLink(urlToRet.getShortUrl());
            return new ResponseEntity<UrlResponseDto>(urlResponseDto, HttpStatus.OK);
        }
        UrlErrorResponseDto urlErrorResponseDto=new UrlErrorResponseDto();
        urlErrorResponseDto.setStatus("404");
        urlErrorResponseDto.setError("There was an error while processing your request. please try again");
        return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto,HttpStatus.OK);
    }

    @GetMapping("/{shortLink}")
    public ResponseEntity<?> redirectToOrignalUrl(@PathVariable String shortLink , HttpServletResponse response) throws IOException {
        if(StringUtils.isEmpty(shortLink))
        {
            UrlErrorResponseDto urlErrorResponseDto=new UrlErrorResponseDto();
            urlErrorResponseDto.setError("Invalid Url");
            urlErrorResponseDto.setStatus("404");
            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto,HttpStatus.OK);
        }

        Url urlToRet = urlService.getEncodedUrl(shortLink);

        if(urlToRet==null)
        {
            UrlErrorResponseDto urlErrorResponseDto=new UrlErrorResponseDto();
            urlErrorResponseDto.setError("Url does not exist or it might have expired.");
            urlErrorResponseDto.setStatus("404");
            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto,HttpStatus.OK);
        }
        if(urlToRet.getExpirationDate().isBefore(LocalDateTime.now()))
        {
            urlService.deleteShortLink(urlToRet);
            UrlErrorResponseDto urlErrorResponseDto=new UrlErrorResponseDto();
            urlErrorResponseDto.setError("Url expired please try generating a fresh one.");
            urlErrorResponseDto.setStatus("200");
            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto,HttpStatus.OK);
        }
        response.sendRedirect(urlToRet.getOriginalUrl());
        return null;
    }
}
