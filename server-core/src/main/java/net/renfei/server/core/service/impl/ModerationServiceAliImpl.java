package net.renfei.server.core.service.impl;

import com.aliyun.green20220302.models.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.config.ServerProperties;
import net.renfei.server.core.constant.ImageModerationTypeEnum;
import net.renfei.server.core.constant.TextModerationTypeEnum;
import net.renfei.server.core.entity.ImageModerationResult;
import net.renfei.server.core.entity.TextModerationResult;
import net.renfei.server.core.service.BaseService;
import net.renfei.server.core.service.ModerationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.aliyun.green20220302.Client;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 内容安全服务（阿里云实现）
 *
 * @author renfei
 */
@Slf4j
@Service
public class ModerationServiceAliImpl extends BaseService implements ModerationService {
    private final Client client;
    private final ServerProperties serverProperties;

    public ModerationServiceAliImpl(ServerProperties serverProperties) throws Exception {
        this.serverProperties = serverProperties;
        Config config = new Config();
        config.setAccessKeyId(serverProperties.getAliyun().getAccessKeyId());
        config.setAccessKeySecret(serverProperties.getAliyun().getAccessKeySecret());
        //接入区域和地址请根据实际情况修改
        config.setRegionId(serverProperties.getAliyun().getModeration().getRegion());
        config.setEndpoint(serverProperties.getAliyun().getModeration().getEndpoint());
        //连接时超时时间，单位毫秒（ms）。
        config.setReadTimeout(6000);
        //读取时超时时间，单位毫秒（ms）。
        config.setConnectTimeout(3000);
        this.client = new Client(config);
    }

    public TextModerationResult textModeration(String value, TextModerationTypeEnum moderationType) {
        // 检测参数构造。
        Map<String, String> serviceParameters = new HashMap<>();
        serviceParameters.put("content", value);
        ObjectMapper objectMapper = new ObjectMapper();
        TextModerationRequest textModerationRequest = new TextModerationRequest();
        textModerationRequest.setService(moderationType.getValue());
        try {
            textModerationRequest.setServiceParameters(objectMapper.writeValueAsString(serviceParameters));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        // 创建RuntimeObject实例并设置运行参数。
        RuntimeOptions runtime = new RuntimeOptions();
        runtime.readTimeout = 10000;
        runtime.connectTimeout = 10000;
        try {
            // 调用方法获取检测结果。
            TextModerationResponse response = client.textModerationWithOptions(textModerationRequest, runtime);
            if (response != null) {
                if (HttpStatus.INTERNAL_SERVER_ERROR.value() == response.getStatusCode()
                        || (response.getBody() != null && HttpStatus.INTERNAL_SERVER_ERROR.value() == (response.getBody().getCode()))) {
                    log.error(response.getBody() == null ? "" : response.getBody().getMessage());
                    return TextModerationResult.builder()
                            .pass(true)
                            .build();
                }
                if (response.getStatusCode() == HttpStatus.OK.value()) {
                    TextModerationResponseBody result = response.getBody();
                    Integer code = result.getCode();
                    if (code != null && code == HttpStatus.OK.value()) {
                        TextModerationResponseBody.TextModerationResponseBodyData data = result.getData();
                        if (StringUtils.hasLength(data.getLabels())) {
                            return TextModerationResult.builder()
                                    .pass(false)
                                    .labels(data.getLabels())
                                    .reason(objectMapper.readValue(data.getReason(), TextModerationResult.Reason.class))
                                    .build();
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return TextModerationResult.builder()
                .pass(true)
                .build();
    }

    public ImageModerationResult imageModeration(String imageUrl, String dataId, ImageModerationTypeEnum moderationType) {
        // 检测参数构造。
        Map<String, String> serviceParameters = new HashMap<>();
        //公网可访问的URL。
        serviceParameters.put("imageUrl", imageUrl);
        serviceParameters.put("dataId", dataId);
        ObjectMapper objectMapper = new ObjectMapper();
        ImageModerationRequest request = new ImageModerationRequest();
        // 图片检测service: baselineCheck通用基线检测。
        request.setService(moderationType.getValue());
        // 创建RuntimeObject实例并设置运行参数。
        RuntimeOptions runtime = new RuntimeOptions();
        runtime.readTimeout = 10000;
        runtime.connectTimeout = 10000;
        try {
            request.setServiceParameters(objectMapper.writeValueAsString(serviceParameters));
            ImageModerationResponse response = client.imageModerationWithOptions(request, runtime);
            if (response != null) {
                if (HttpStatus.INTERNAL_SERVER_ERROR.value() == response.getStatusCode()
                        || (response.getBody() != null && HttpStatus.INTERNAL_SERVER_ERROR.value() == (response.getBody().getCode()))) {
                    log.error(response.getBody() == null ? "" : response.getBody().getMsg());
                    return ImageModerationResult.builder()
                            .pass(true)
                            .build();
                }
                if (response.getStatusCode() == HttpStatus.OK.value()) {
                    ImageModerationResponseBody body = response.getBody();
                    if (body.getCode() == HttpStatus.OK.value()) {
                        ImageModerationResponseBody.ImageModerationResponseBodyData data = body.getData();
                        List<ImageModerationResponseBody.ImageModerationResponseBodyDataResult> results = data.getResult();
                        List<ImageModerationResult.Result> resultList = new ArrayList<>(results.size());
                        boolean pass = false;
                        for (ImageModerationResponseBody.ImageModerationResponseBodyDataResult result : results) {
                            if ("nonLabel".equals(result.getLabel())) {
                                pass = true;
                            }
                            resultList.add(ImageModerationResult.Result.builder()
                                    .label(result.getLabel())
                                    .confidence(result.getConfidence())
                                    .build());
                        }
                        return ImageModerationResult.builder()
                                .pass(pass)
                                .dataId(data.getDataId())
                                .result(resultList)
                                .build();
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ImageModerationResult.builder()
                .pass(true)
                .build();
    }
}
