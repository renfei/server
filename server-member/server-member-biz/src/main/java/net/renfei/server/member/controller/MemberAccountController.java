package net.renfei.server.member.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.renfei.server.core.controller.BaseController;
import org.springframework.web.bind.annotation.RestController;

/**
 * 会员账号服务
 *
 * @author renfei
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "【会员账号服务", description = "会员账号服务")
public class MemberAccountController extends BaseController {
}
