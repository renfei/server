package net.renfei.server.core.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 阿里公共DNS解析结果
 *
 * @author renfei
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "阿里公共DNS解析结果")
public class AliPublicDns implements Serializable {
    @Serial
    private static final long serialVersionUID = -3316408227872898096L;
    @JsonProperty("Status")
    private Integer status;
    @JsonProperty("TC")
    private Boolean tc;
    @JsonProperty("RD")
    private Boolean rd;
    @JsonProperty("RA")
    private Boolean ra;
    @JsonProperty("AD")
    private Boolean ad;
    @JsonProperty("CD")
    private Boolean cd;
    @JsonProperty("Question")
    private Question question;
    @JsonProperty("Answer")
    private List<Answer> answer;
    @JsonProperty("edns_client_subnet")
    private String clientSubnet;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Question {
        private String name;
        private Integer type;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Answer {
        private String name;
        @JsonProperty("TTL")
        private Long ttl;
        private Integer type;
        private String data;
    }
}
