package net.renfei.server.core.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.constant.LogLevelEnum;
import net.renfei.server.core.entity.SystemVersionInfo;
import net.renfei.server.core.service.BaseService;
import net.renfei.server.core.service.SecurityService;
import net.renfei.server.core.service.SystemService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Map;

/**
 * 系统服务
 *
 * @author renfei
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemServiceImpl extends BaseService implements SystemService, ApplicationContextAware {
    private ApplicationContext applicationContext;
    private final SecurityService securityService;

    /**
     * 查询系统版本信息
     *
     * @return
     */
    @Override
    public SystemVersionInfo querySystemVersionInfo() {
        try {
            File resource = new ClassPathResource("git.json").getFile();
            if (!resource.exists()) {
                return SystemVersionInfo.builder().build();
            }
            ObjectMapper mapper = new ObjectMapper();
            Map<?, ?> map = mapper.readValue(resource, Map.class);
            return SystemVersionInfo.builder()
                    .codeBranch(map.get("git.branch") == null ? null : map.get("git.branch").toString())
                    .buildTime(map.get("git.build.time") == null ? null : map.get("git.build.time").toString())
                    .buildUserEmail(map.get("git.build.user.email") == null ? null : map.get("git.build.user.email").toString())
                    .buildUsername(map.get("git.build.user.name") == null ? null : map.get("git.build.user.name").toString())
                    .buildVersion(map.get("git.build.version") == null ? null : map.get("git.build.version").toString())
                    .commitId(map.get("git.commit.id") == null ? null : map.get("git.commit.id").toString())
                    .commitIdAbbrev(map.get("git.commit.id.abbrev") == null ? null : map.get("git.commit.id.abbrev").toString())
                    .tags(map.get("git.tags") == null ? null : map.get("git.tags").toString())
                    .build();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return SystemVersionInfo.builder().build();
        }
    }

    /**
     * 系统停机
     *
     * @param request 请求对象
     */
    @Override
    public void shutdownSystem(HttpServletRequest request) {
        securityService.insertAuditLog(LogLevelEnum.FATAL, "SYSTEM",
                "系统停机", "系统管理员下达系统停机指令", null);
        log.warn("系统管理员下达系统停机指令，系统主动停机退出。");
        ((ConfigurableApplicationContext) applicationContext).close();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
