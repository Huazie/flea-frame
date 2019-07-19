package com.huazie.frame.jersey.client.request.impl;

import com.huazie.frame.common.util.ObjectUtils;
import com.huazie.frame.common.util.ReflectUtils;
import com.huazie.frame.common.util.StringUtils;
import com.huazie.frame.common.util.json.GsonUtils;
import com.huazie.frame.common.util.xml.JABXUtils;
import com.huazie.frame.jersey.client.request.Request;
import com.huazie.frame.jersey.client.request.RequestConfig;
import com.huazie.frame.jersey.client.request.RequestConfigEnum;
import com.huazie.frame.jersey.client.request.RequestModeEnum;
import com.huazie.frame.jersey.client.response.Response;
import com.huazie.frame.jersey.common.client.config.FleaJerseyClientConfig;
import com.huazie.frame.jersey.common.data.FleaJerseyRequest;
import com.huazie.frame.jersey.common.data.FleaJerseyRequestData;
import com.huazie.frame.jersey.common.data.FleaJerseyResponse;
import com.huazie.frame.jersey.common.data.FleaJerseyResponseData;
import com.huazie.frame.jersey.common.data.RequestBusinessData;
import com.huazie.frame.jersey.common.data.RequestPublicData;
import com.huazie.frame.jersey.common.data.ResponseBusinessData;
import com.huazie.frame.jersey.common.data.ResponsePublicData;
import com.huazie.frame.jersey.common.exception.FleaJerseyClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.net.URLEncoder;

/**
 * <p> Flea 抽象请求 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class FleaRequest implements Request {

    private final static Logger LOGGER = LoggerFactory.getLogger(FleaRequest.class);

    private RequestConfig config; // 请求配置

    protected RequestModeEnum modeEnum; // 请求方式

    /**
     * <p> 不带参数的构造方法 </p>
     *
     * @since 1.0.0
     */
    public FleaRequest() {
        init();
    }

    /**
     * <p> 带请求配置参数的构造的方法 </p>
     *
     * @param config 请求配置
     * @since 1.0.0
     */
    public FleaRequest(RequestConfig config) {
        this.config = config;
        init();
    }

    @Override
    public <T> Response<T> doRequest(Class<T> clazz) throws Exception {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("FleaRequest##doRequest(Class<T>) Start");
        }

        if (ObjectUtils.isEmpty(config) || config.isEmpty()) {
            // 未初始化请求配置，请检查！
            throw new FleaJerseyClientException("ERROR-JERSEY-CLIENT0000000000");
        }

        // 客户端编码
        String clientCode = config.getClientCode();
        if (StringUtils.isBlank(clientCode)) {
            // 【{0}】未配置，请检查！！！
            throw new FleaJerseyClientException("ERROR-JERSEY-CLIENT0000000008", RequestConfigEnum.CLIENT_CODE.getKey());
        }

        // 业务入参
        Object input = config.getInputObj();
        if (ObjectUtils.isEmpty(input)) {
            // 【{0}】未配置，请检查！！！
            throw new FleaJerseyClientException("ERROR-JERSEY-CLIENT0000000008", RequestConfigEnum.INPUT_OBJECT.getKey());
        }

        // 资源地址
        String resourceUrl = config.getResourceUrl();
        if (StringUtils.isBlank(resourceUrl)) {
            // 【{0}】未配置，请检查！！！
            throw new FleaJerseyClientException("ERROR-JERSEY-CLIENT0000000008", RequestConfigEnum.RESOURCE_URL.getKey());
        }

        // 资源编码
        String resourceCode = config.getResourceCode();
        if (StringUtils.isBlank(resourceCode)) {
            // 【{0}】未配置，请检查！！！
            throw new FleaJerseyClientException("ERROR-JERSEY-CLIENT0000000008", RequestConfigEnum.RESOURCE_CODE.getKey());
        }

        // 服务编码
        String serviceCode = config.getServiceCode();
        if (StringUtils.isBlank(serviceCode)) {
            // 【{0}】未配置，请检查！！！
            throw new FleaJerseyClientException("ERROR-JERSEY-CLIENT0000000008", RequestConfigEnum.SERVICE_CODE.getKey());
        }

        // 业务入参
        String clientInput = config.getClientInput();
        Class inputClazz = ReflectUtils.forName(clientInput);
        if (ObjectUtils.isEmpty(inputClazz)) {
            // 请检查客户端配置【client_code = {0}】: 【{1} = {2}】非法
            throw new FleaJerseyClientException("ERROR-JERSEY-CLIENT0000000010", clientCode, RequestConfigEnum.CLIENT_INPUT.getKey(), clientInput);
        }
        if (!inputClazz.isInstance(input)) {
            // 请检查客户端配置【client_code = {0}】：配置的入参【client_input = {1}】类型和实际传入的入参【{2}】类型不一致
            throw new FleaJerseyClientException("ERROR-JERSEY-CLIENT0000000004", clientCode, clientInput, input.getClass().getName());
        }

        // 业务出参
        String clientOutput = config.getClientOutput();
        Class mClazz = ReflectUtils.forName(clientOutput);
        if (ObjectUtils.isEmpty(mClazz)) {
            // 请检查客户端配置【client_code = {0}】: 【{1} = {2}】非法
            throw new FleaJerseyClientException("ERROR-JERSEY-CLIENT0000000010", clientCode, RequestConfigEnum.CLIENT_OUTPUT.getKey(), clientOutput);
        }
        if (!mClazz.equals(clazz)) {
            // 请检查客户端配置【client_code = {0}】：配置的出参【client_output = {1}】类型和实际需要返回的出参【{2}】类型不一致
            throw new FleaJerseyClientException("ERROR-JERSEY-CLIENT0000000007", clientCode, clientOutput, clazz.getName());
        }

        WebTarget target = ClientBuilder.newClient().target(resourceUrl).path(resourceCode);

        FleaJerseyRequest request = createFleaJerseyRequest(resourceCode, serviceCode, input);

        FleaJerseyResponse response = request(target, request);

        if (ObjectUtils.isEmpty(response)) {
            // 资源服务请求异常：响应报文为空
            throw new FleaJerseyClientException("ERROR-JERSEY-CLIENT0000000005");
        }

        FleaJerseyResponseData responseData = response.getResponseData();
        if (ObjectUtils.isEmpty(responseData)) {
            // 资源服务请求异常：响应报文为空
            throw new FleaJerseyClientException("ERROR-JERSEY-CLIENT0000000005");
        }

        ResponsePublicData responsePublicData = responseData.getPublicData();
        if (ObjectUtils.isEmpty(responsePublicData)) {
            // 资源服务请求异常：响应公共报文为空
            throw new FleaJerseyClientException("ERROR-JERSEY-CLIENT0000000006");
        }

        Response<T> responseResult = new Response<T>();
        T output = null;

        // 判断资源服务是否请求成功
        if (responsePublicData.isSuccess()) {
            // 获取资源服务响应业务报文
            ResponseBusinessData businessData = responseData.getBusinessData();
            if (ObjectUtils.isNotEmpty(businessData)) {
                output = GsonUtils.toEntity(businessData.getOutput(), clazz);
            }
            // 设置业务出参
            responseResult.setOutput(output);
        } else {
            // 错误码
            responseResult.setRetCode(responsePublicData.getResultCode());
            // 错误信息
            responseResult.setRetMess(responsePublicData.getResultMess());
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("FleaRequest##doRequest(Class<T>) End");
        }

        return responseResult;
    }

    /**
     * <p> 从请求配置中获取媒体类型 </p>
     *
     * @return 媒体类型
     * @throws Exception
     * @since 1.0.0
     */
    protected MediaType toMediaType() throws Exception {
        // 媒体类型
        String mediaTypeStr = config.getMediaType();
        if (StringUtils.isBlank(mediaTypeStr)) {
            // 【{0}】未配置，请检查！！！
            throw new FleaJerseyClientException("ERROR-JERSEY-CLIENT0000000008", RequestConfigEnum.MEDIA9_TYPE.getKey());
        }

        try {
            return MediaType.valueOf(mediaTypeStr);
        } catch (Exception e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("FleaJerseyRequestFactory##buildFleaRequest(RequestConfig) Exception = {}", e.getMessage());
            }
            // 请检查客户端配置【client_code = {0}】: 【{1} = {2}】非法
            throw new FleaJerseyClientException("ERROR-JERSEY-CLIENT0000000010", config.getClientCode(), RequestConfigEnum.MEDIA9_TYPE.getKey(), mediaTypeStr);
        }

    }

    /**
     * <p> 将请求报文 转换为 XML字符串 </p>
     *
     * @param request 请求报文对象
     * @return 请求XML字符串
     * @throws Exception
     * @since 1.0.0
     */
    protected String toRequestXml(FleaJerseyRequest request) throws Exception {
        String input = request.getRequestData().getBusinessData().getInput();
        if (ObjectUtils.isNotEmpty(input)) {
            input = URLEncoder.encode(input, "UTF-8");
        }
        request.getRequestData().getBusinessData().setInput(input); // 重新设置入参
        // 将请求报文转换成xml
        return JABXUtils.toXml(request, false);
    }

    /**
     * <p> 请求方式初始化 </p>
     *
     * @since 1.0.0
     */
    protected abstract void init();

    /**
     * <p> 实际请求处理 </p>
     *
     * @param target  WebTarget对象
     * @param request Flea Jersey请求对象
     * @return Flea Jersey响应对象
     * @throws Exception
     * @since 1.0.0
     */
    protected abstract FleaJerseyResponse request(WebTarget target, FleaJerseyRequest request) throws Exception;

    public RequestConfig getConfig() {
        return config;
    }

    public void setConfig(RequestConfig config) {
        this.config = config;
    }

    @Override
    public RequestModeEnum getRequestMode() {
        return modeEnum;
    }

    /**
     * <p> 创建FleaJerseyRequest对象 </p>
     *
     * @param resourceCode 资源编码
     * @param serviceCode  服务编码
     * @param input        业务入参
     * @return FleaJerseyRequest对象
     * @since 1.0.0
     */
    private FleaJerseyRequest createFleaJerseyRequest(String resourceCode, String serviceCode, Object input) {
        FleaJerseyRequest request = new FleaJerseyRequest();
        FleaJerseyRequestData requestData = new FleaJerseyRequestData();
        // 创建请求公共报文
        RequestPublicData publicData = createRequestPublicData(resourceCode, serviceCode);
        // 创建请求业务报文
        RequestBusinessData businessData = createRequestBusinessData(input);
        requestData.setPublicData(publicData);
        requestData.setBusinessData(businessData);
        request.setRequestData(requestData);
        return request;
    }

    /**
     * <p> 创建请求公共报文 </p>
     *
     * @param resourceCode 资源编码
     * @param serviceCode  服务编码
     * @return 请求公共报文
     * @since 1.0.0
     */
    private static RequestPublicData createRequestPublicData(String resourceCode, String serviceCode) {
        RequestPublicData publicData = new RequestPublicData();
        FleaJerseyClientConfig config = FleaJerseyClientConfig.getConfig();
        publicData.setSystemUserId(config.getSystemUserId());
        publicData.setSystemUserPassword(config.getSystemUserPwd());
        publicData.setResourceCode(resourceCode);
        publicData.setServiceCode(serviceCode);
        return publicData;
    }

    /**
     * <p> 创建请求业务报文 </p>
     *
     * @param input 业务入参
     * @return 请求业务报文
     * @since 1.0.0
     */
    private static RequestBusinessData createRequestBusinessData(Object input) {
        RequestBusinessData businessData = new RequestBusinessData();
        String inputJson = GsonUtils.toJsonString(input);
        businessData.setInput(inputJson);
        return businessData;
    }
}