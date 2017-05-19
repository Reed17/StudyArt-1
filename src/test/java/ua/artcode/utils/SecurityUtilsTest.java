package ua.artcode.utils;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by zhenia on 28.04.17.
 */
public class SecurityUtilsTest {
    SecurityUtils securityUtils = new SecurityUtils();

    @Test
    public void md5HashTest() {
        assertThat(securityUtils.encryptPass("password"), equalTo("5f4dcc3b5aa765d61d8327deb882cf99"));
        assertThat(securityUtils.encryptPass("password123"), equalTo("482c811da5d5b4bc6d497ffa98491e38"));
        assertThat(securityUtils.encryptPass("md5HashMePls"), equalTo("aaef8883e4009db8315556aee374946a"));
    }
}
