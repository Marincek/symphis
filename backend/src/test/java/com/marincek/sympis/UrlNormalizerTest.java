package com.marincek.sympis;

import com.marincek.sympis.service.UrlNormalizer;
import com.marincek.sympis.service.impl.OracleUrlNormalizer;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import static junit.framework.TestCase.assertTrue;


public class UrlNormalizerTest {

    private UrlNormalizer urlNormalizer = new OracleUrlNormalizer();

    @Test
    public void testUrls() throws MalformedURLException, UnsupportedEncodingException {

        assertTrue(areEqual("http://www.example.com/?hello=world&foo=bar","http://www.example.com/?foo=bar&hello=world"));

        assertTrue(areEqual("http://www.example.com/?hello=world&foo=bar","http://example.com/?foo=bar&hello=world"));

        assertTrue(areEqual("https%3A%2F%2Fwww.baeldung.com%2Fspring-boot-data-sql-and-schema-sql","https://www.baeldung.com/spring-boot-data-sql-and-schema-sql"));

    }

    @Test(expected = MalformedURLException.class)
    public void testWrongUrl() throws MalformedURLException, UnsupportedEncodingException {
        assertTrue(areEqual("www.example.com/?hello=world&foo=bar","http://www.example.com/?foo=bar&hello=world"));
    }

    private boolean areEqual(String urlA, String urlB) throws MalformedURLException, UnsupportedEncodingException {
        return urlNormalizer.normalize(urlA).equals(urlNormalizer.normalize(urlB));
    }

}
