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
 * 公共DNS解析结果
 *
 * @author renfei
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "公共DNS解析结果")
public class CloudflareDnsAnswer implements Serializable {
    @Serial
    private static final long serialVersionUID = -3316408227872898096L;
    /**
     * DNS 查询的响应代码。
     * 这些定义在这里：<a href="https://www.iana.org/assignments/dns-parameters/dns-parameters.xhtml#dns-parameters-6">...</a>
     */
    @Schema(description = "DNS 查询的响应代码。这些定义在这里：https://www.iana.org/assignments/dns-parameters/dns-parameters.xhtml#dns-parameters-6")
    @JsonProperty("Status")
    private Integer status;
    /**
     * 如果为 true，则意味着已设置截断位。当 DNS 应答大于单个 UDP 或 TCP 数据包时，就会发生这种情况
     */
    @Schema(description = "如果为 true，则意味着已设置截断位。当 DNS 应答大于单个 UDP 或 TCP 数据包时，就会发生这种情况。")
    @JsonProperty("TC")
    private Boolean tc;
    /**
     * 如果为true，则意味着已设置递归所需位
     */
    @Schema(description = "如果为true，则意味着已设置递归所需位")
    @JsonProperty("RD")
    private Boolean rd;
    /**
     * 如果为true，则表示已设置递归可用位
     */
    @Schema(description = "如果为true，则表示已设置递归可用位")
    @JsonProperty("RA")
    private Boolean ra;
    /**
     * 如果为true，则意味着答案中的每条记录都已通过 DNSSEC 验证
     */
    @Schema(description = "如果为true，则意味着答案中的每条记录都已通过 DNSSEC 验证")
    @JsonProperty("AD")
    private Boolean ad;
    /**
     * 如果为 true，则客户端要求禁用 DNSSEC 验证
     */
    @Schema(description = "如果为 true，则客户端要求禁用 DNSSEC 验证")
    @JsonProperty("CD")
    private Boolean cd;
    /**
     * 请求对象
     */
    @Schema(description = "请求对象")
    @JsonProperty("Question")
    private List<Question> question;
    /**
     * 应答对象
     */
    @Schema(description = "应答对象")
    @JsonProperty("Answer")
    private List<Answer> answer;
    /**
     *
     */
    @Schema(description = "")
    @JsonProperty("edns_client_subnet")
    private String clientSubnet;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "请求对象")
    public static class Question {
        /**
         * 请求的记录名称
         */
        @Schema(description = "请求的记录名称")
        private String name;
        /**
         * 请求的 DNS 记录类型。
         * 这些定义在这里：<a href="https://www.iana.org/assignments/dns-parameters/dns-parameters.xhtml#dns-parameters-4">...</a>
         */
        @Schema(description = "请求的 DNS 记录类型。这些定义在这里：https://www.iana.org/assignments/dns-parameters/dns-parameters.xhtml#dns-parameters-4")
        private Integer type;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "应答对象")
    public static class Answer {
        /**
         * 名称
         */
        @Schema(description = "名称")
        private String name;
        /**
         * 在应答被视为过时之前，应答可以存储在缓存中的秒数
         */
        @Schema(description = "在应答被视为过时之前，应答可以存储在缓存中的秒数")
        @JsonProperty("TTL")
        private Long ttl;
        /**
         * DNS 记录的类型。
         * 这些定义在这里：<a href="https://www.iana.org/assignments/dns-parameters/dns-parameters.xhtml#dns-parameters-4">...</a>
         */
        @Schema(description = "DNS 记录的类型。这些定义在这里：https://www.iana.org/assignments/dns-parameters/dns-parameters.xhtml#dns-parameters-4")
        private Integer type;
        /**
         * 给定名称和类型的 DNS 记录的值
         */
        @Schema(description = "给定名称和类型的 DNS 记录的值")
        private String data;
    }
}
