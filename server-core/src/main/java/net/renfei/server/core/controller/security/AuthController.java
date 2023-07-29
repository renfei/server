package net.renfei.server.core.controller.security;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.renfei.server.core.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证鉴权接口
 *
 * @author renfei
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "【管理员】认证鉴权接口", description = "【管理员】认证鉴权接口")
@RequestMapping("/api")
public class AuthController extends BaseController {
}
