import com.marincek.sympis.domain.Link;
import com.marincek.sympis.domain.User;
import com.marincek.sympis.service.LinkService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class LinkServiceIntegrationTest {

    @Autowired
    private LinkService linkService;

    @Test
    public void testFindAllTagsForUrl() throws MalformedURLException, UnsupportedEncodingException {
        Link link = new Link("http://some.url.com",Arrays.asList("tag1","tag2","tag3"));
        link.setUser(new User("marincek"));

        link = linkService.addLinkForUser("marincek",link);

        assertEquals("http://some.url.com", link.getUrl());
        assertEquals(3, link.getTags().size());
    }

}
