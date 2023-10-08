package net.renfei.server.core.service;

import net.renfei.server.core.entity.payload.request.TurnstileRequest;
import net.renfei.server.core.entity.payload.response.TurnstileResponse;
import net.renfei.server.main.ApplicationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author renfei
 */
public class TurnstileServiceTests extends ApplicationTest {
    @Autowired
    private TurnstileService turnstileService;

    @Test
    public void siteVerify() {
        TurnstileResponse turnstileResponse = turnstileService.siteVerify(TurnstileRequest.builder()
                .secret("1x0000000000000000000000000000000AA")
                .response("123")
                .build());
        System.out.println(turnstileResponse);
    }
}
