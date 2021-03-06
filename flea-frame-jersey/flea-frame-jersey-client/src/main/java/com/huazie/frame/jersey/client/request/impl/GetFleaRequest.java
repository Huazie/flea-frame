package com.huazie.frame.jersey.client.request.impl;

import com.huazie.frame.common.slf4j.FleaLogger;
import com.huazie.frame.common.slf4j.impl.FleaLoggerProxy;
import com.huazie.frame.jersey.client.request.RequestConfig;
import com.huazie.frame.jersey.client.request.RequestModeEnum;
import com.huazie.frame.jersey.common.FleaJerseyConstants;
import com.huazie.frame.jersey.common.data.FleaJerseyRequest;
import com.huazie.frame.jersey.common.data.FleaJerseyResponse;

import javax.ws.rs.client.WebTarget;

/**
 * <p> GET请求 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class GetFleaRequest extends FleaRequest {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(GetFleaRequest.class);

    /**
     * <p> 不带参数的构造方法 </p>
     *
     * @since 1.0.0
     */
    public GetFleaRequest() {
    }

    /**
     * <p> 带参数的构造方法 </p>
     *
     * @param config 请求配置
     * @since 1.0.0
     */
    public GetFleaRequest(RequestConfig config) {
        super(config);
    }

    @Override
    protected void init() {
        modeEnum = RequestModeEnum.GET;
    }

    @Override
    protected FleaJerseyResponse request(WebTarget target, FleaJerseyRequest request) throws Exception {

        // 将请求报文转换成请求数据字符串
        String requestData = toRequestData(request);

        Object obj = null;
        if (LOGGER.isDebugEnabled()) {
            obj = new Object() {};
            LOGGER.debug1(obj, "GET Request, Start");
            LOGGER.debug1(obj, "GET Request, RequestData = {}", requestData);
        }

        // GET请求发送
        FleaJerseyResponse response = target
                .queryParam(FleaJerseyConstants.FormDataConstants.FORM_DATA_KEY_REQUEST, requestData)
                .request(toMediaType())
                .get(FleaJerseyResponse.class);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug1(obj, "GET Request, FleaJerseyResponse = {}", response);
            LOGGER.debug1(obj, "GET Request, End");
        }

        return response;
    }
}
