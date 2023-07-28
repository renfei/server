package net.renfei.server.core.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.config.ServerProperties;
import net.renfei.server.core.service.BaseService;
import net.renfei.server.core.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 电子邮件服务
 *
 * @author renfei
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl extends BaseService implements EmailService {
    @Value("${spring.mail.username}")
    private String from;
    private final JavaMailSender mailSender;
    private final ServerProperties properties;

    /**
     * 邮件发送服务
     *
     * @param to       收件人
     * @param name     收件人昵称
     * @param subject  邮件主题
     * @param contents 邮件内容
     * @return
     */
    @Override
    public boolean send(String to, String name, String subject, List<String> contents) {
        return send(to, name, subject, contents, null);
    }

    @Override
    public boolean send(String to, String cc, String name, String subject, List<String> contents) {
        return send(to, cc, name, subject, contents, null);
    }

    /**
     * 邮件发送服务
     *
     * @param to       收件人
     * @param name     收件人昵称
     * @param subject  邮件主题
     * @param contents 邮件内容
     * @return
     */
    @Override
    public boolean send(String to, String name, String subject, String contents) {
        return send(to, null, name, subject, contents, null);
    }

    @Override
    public boolean send(String to, String cc, String name, String subject, String contents) {
        return send(to, cc, name, subject, contents, null);
    }

    @Override
    public boolean send(String to, String name, String subject, List<String> contents, Map<String, File> attachment) {
        return send(to, null, name, subject, contents, attachment);
    }

    /**
     * 邮件发送服务
     *
     * @param to         收件人
     * @param cc         抄送
     * @param name       收件人昵称
     * @param subject    邮件主题
     * @param contents   邮件内容
     * @param attachment 附件列表
     * @return
     */
    @Override
    public boolean send(String to, String cc, String name, String subject, List<String> contents, Map<String, File> attachment) {
        return send(to, cc, name, subject, getContent(contents), attachment);
    }

    /**
     * 邮件发送服务
     *
     * @param to         收件人
     * @param name       收件人昵称
     * @param subject    邮件主题
     * @param contents   邮件内容
     * @param attachment 附件列表
     * @return
     */
    @Override
    public boolean send(String to, String name, String subject, String contents, Map<String, File> attachment) {
        return send(to, null, name, subject, contents, attachment);
    }

    @Override
    public boolean send(String to, String cc, String name, String subject, String contents, Map<String, File> attachment) {
        String txt = "";
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("templates/email.html");
            Resource resource = resources[0];
            //获得文件流，因为在jar文件中，不能直接通过文件资源路径拿到文件，但是可以在jar包中拿到文件流
            InputStream stream = resource.getInputStream();
            StringBuilder buffer = new StringBuilder();
            byte[] bytes = new byte[1024];
            try {
                for (int n; (n = stream.read(bytes)) != -1; ) {
                    buffer.append(new String(bytes, 0, n));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            txt = buffer.toString();
            txt = txt.replace("${NAME}", name);
            txt = txt.replace("${CONTENT}", contents);
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM y HH:mm:ss 'GMT+8'");
            Date d = new Date();
            String datetime = sdf.format(d);
            txt = txt.replace("${DATETIME}", datetime);
            sdf = new SimpleDateFormat("yyyy");
            String year = sdf.format(d);
            txt = txt.replace("${YEAR}", year);
        } catch (Exception e) {
            txt = contents;
        }
        //////
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(properties.getSiteName() + " <" + from + ">");
            helper.setReplyTo(properties.getEmailReplyTo() + " <" + properties.getEmailReplyTo() + ">");
            helper.setTo(to);
            if (!ObjectUtils.isEmpty(cc)) {
                helper.setCc(cc);
            }
            helper.setSubject(subject);
            helper.setText(txt, true);
            if (attachment != null) {
                for (String key : attachment.keySet()
                ) {
                    FileSystemResource file = new FileSystemResource(attachment.get(key));
                    helper.addInline(key, file);
                }
            }
            mailSender.send(message);
            return true;
        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    private String getContent(List<String> contents) {
        StringBuilder sb = new StringBuilder();
        for (String s : contents
        ) {
            sb.append("<p>").append(s).append("</p>");
        }
        return sb.toString();
    }
}
