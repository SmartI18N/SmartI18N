package org.smarti18n.messages;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Marc Bellmann &lt;marc.bellmann@googlemail.com&gt;
 */
public class CoreIntegrationTest extends AbstractIntegrationTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void notNull() throws Exception {
        assertThat(this.restTemplate.getForEntity("http://localhost:" + this.port, String.class), is(notNullValue()));
    }
}
