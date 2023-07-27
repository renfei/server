package net.renfei.server.core.constant;

/**
 * 运行环境
 *
 * @author renfei
 */
public class RuntimeEnvironment {
    /**
     * 本地环境
     */
    public static final String LOCAL = "local";
    /**
     * 线上开发环境
     */
    public static final String DEVELOPMENT = "dev";
    /**
     * 线上测试环境
     */
    public static final String TEST = "test";
    /**
     * 用户验收测试环境
     */
    public static final String USER_ACCEPTANCE = "uat";
    /**
     * 功能验收测试环境（软件测试工程师使用）
     */
    public static final String FEATURE_ACCEPTANCE_TEST = "fat";
    /**
     * 灰度环境预发布（生产数据库）
     */
    public static final String PREVIEW = "pre";
    /**
     * 生产环境
     */
    public static final String PRODUCTION = "prod";
}
