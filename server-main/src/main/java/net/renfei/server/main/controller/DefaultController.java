package net.renfei.server.main.controller;

import net.renfei.server.core.controller.BaseController;
import org.springframework.web.bind.annotation.RestController;

/**
 * 默认 Controller，解决部分 module 未开启时的 404 提示
 *
 * @author renfei
 */
@RestController
public class DefaultController extends BaseController {
}
