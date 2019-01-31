package com.marincek.sympis.service;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

public interface UrlNormalizer {

    String normalize(String url) throws MalformedURLException, UnsupportedEncodingException;

}
