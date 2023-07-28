package net.renfei.server.core.service;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 电子邮件服务
 *
 * @author renfei
 */
public interface EmailService {
    boolean send(String to, String name, String subject, List<String> contents);

    boolean send(String to, String cc, String name, String subject, List<String> contents);

    boolean send(String to, String name, String subject, String contents);

    boolean send(String to, String cc, String name, String subject, String contents);

    boolean send(String to, String name, String subject, List<String> contents, Map<String, File> attachment);

    boolean send(String to, String cc, String name, String subject, List<String> contents, Map<String, File> attachment);

    boolean send(String to, String name, String subject, String contents, Map<String, File> attachment);

    boolean send(String to, String cc, String name, String subject, String contents, Map<String, File> attachment);
}
