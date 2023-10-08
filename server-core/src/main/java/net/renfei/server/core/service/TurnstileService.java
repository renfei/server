package net.renfei.server.core.service;

import net.renfei.server.core.entity.payload.request.TurnstileRequest;
import net.renfei.server.core.entity.payload.response.TurnstileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Turnstile
 * <p>
 * Turnstile is Cloudflare’s smart CAPTCHA alternative.
 * It can be embedded into any website without sending traffic through Cloudflare and works without showing visitors a CAPTCHA.
 *
 * @author renfei
 */
@FeignClient(name = "turnstileService", url = "https://challenges.cloudflare.com")
public interface TurnstileService {
    /**
     * Server-side validation
     *
     * @param request
     * @return
     */
    @PostMapping("/turnstile/v0/siteverify")
    TurnstileResponse siteVerify(TurnstileRequest request);
}
