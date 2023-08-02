package net.renfei.server.core.controller;

import jakarta.servlet.http.HttpServletRequest;
import net.renfei.server.core.BaseClazz;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 基础Controller
 *
 * @author renfei
 */
public abstract class BaseController extends BaseClazz {
    @Autowired
    protected HttpServletRequest request;
}
