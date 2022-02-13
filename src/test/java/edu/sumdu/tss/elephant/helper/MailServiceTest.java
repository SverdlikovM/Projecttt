package edu.sumdu.tss.elephant.helper;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.Transport;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;

import edu.sumdu.tss.elephant.Helper;
import edu.sumdu.tss.elephant.helper.enums.Lang;
import edu.sumdu.tss.elephant.helper.utils.StringUtils;
import edu.sumdu.tss.elephant.model.User;

public class MailServiceTest {

    @BeforeAll
    static void loadProps() {
        Keys.loadParams(new File(Helper.configFile));
        mockStatic(Transport.class);
    }

    @Test
    void sendActivationLink() throws MessagingException {
        String token = StringUtils.randomAlphaString(User.API_KEY_SIZE);
        String e_address = Keys.get("EMAIL.FROM");
        Lang language = Lang.EN;

        doNothing().when(Transport.class);
        MailService.sendActivationLink(token, e_address, language);
    }

    @Test
    void sendResetLink() throws MessagingException {
        String token = StringUtils.randomAlphaString(User.API_KEY_SIZE);
        String e_address = Keys.get("EMAIL.FROM");
        Lang language = Lang.UK;

        doNothing().when(Transport.class);
        MailService.sendResetLink(token, e_address, language);
    }
}