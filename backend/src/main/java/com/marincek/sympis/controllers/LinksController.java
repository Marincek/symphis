package com.marincek.sympis.controllers;

import com.marincek.sympis.controllers.request.LinkRequest;
import com.marincek.sympis.controllers.response.LinkResponse;
import com.marincek.sympis.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/space/links", produces = "application/json" , consumes = "application/json")
public class LinksController {

    private LinkService linksService;

    @Autowired
    public LinksController(LinkService linksService) {
        this.linksService = linksService;
    }

    @RequestMapping(method = {RequestMethod.GET})
    public ResponseEntity<List<LinkResponse>> getAllUserLink(Principal principal) {

        List<LinkResponse> links = linksService.findAllLinksForUser(principal.getName()).stream().map(LinkResponse::new).collect(Collectors.toList());

        return new ResponseEntity<>(links, HttpStatus.OK);
    }

    @RequestMapping(method = {RequestMethod.POST})
    public ResponseEntity<LinkResponse> addLink(Principal principal, @Valid @RequestBody LinkRequest linkRequest) {
        try {
            LinkResponse linkResponse = new LinkResponse(linksService.addLinkForUser(principal.getName() , linkRequest.convert()));
            return new ResponseEntity<>(linkResponse, HttpStatus.OK);
        } catch (MalformedURLException e) {
            return new ResponseEntity<>(new LinkResponse("MalformedURLException"), HttpStatus.BAD_REQUEST);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new LinkResponse("UnsupportedEncodingException"), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value="/tags", method = {RequestMethod.GET})
    public ResponseEntity<List<String>> getTagsForUrl(@RequestParam("url") String url) {
        try {
            return new ResponseEntity<>(linksService.findAllTagsForUrl(url), HttpStatus.OK);
        } catch (MalformedURLException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value="/{id}", method = {RequestMethod.DELETE})
    public ResponseEntity deleteLink(@PathVariable("id") Long id) {
        try {
            linksService.delete(id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }





}
