/**
 * Personium
 * Copyright 2017 FUJITSU LIMITED
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.personium.engine.extension.httpclient;

import io.personium.engine.extension.support.AbstractExtensionScriptableObject;
import io.personium.engine.extension.support.ExtensionErrorConstructor;
import io.personium.engine.extension.support.ExtensionLogger;

import java.io.InputStream;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSFunction;

/**
 * Engine-Extension HttpClient.
 */
@SuppressWarnings("serial")
public class Ext_HttpClient extends AbstractExtensionScriptableObject {

    /**
     * Public name to JavaScript.
     */
    @Override
    public String getClassName() {
        return "HttpClient";
    }

    /**
     * constructor.
     */
    @JSConstructor
    public Ext_HttpClient() {
        ExtensionLogger logger = new ExtensionLogger(this.getClass());
        setLogger(this.getClass(), logger);
    }

    /**
     * get.
     * @param uri String
     * @param headers JSONObject
     * @param respondsAsStream true:stream/false:text
     * @return JSONObject
     */
    @SuppressWarnings("unchecked")
    @JSFunction
    public NativeObject get(String uri, NativeObject headers, boolean respondsAsStream) {
        NativeObject result = null;

        if (null == uri || uri.isEmpty()) {
            String message = "URL parameter is not set.";
            this.getLogger().info(message);
            throw ExtensionErrorConstructor.construct(message);
        }

        try (CloseableHttpClient httpclient = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(uri);

            // Set request headers.
            if (null != headers) {
                for (Entry<Object, Object> e : headers.entrySet()){
                    request.addHeader(e.getKey().toString(), e.getValue().toString());
                }
            }

            HttpResponse res = null;
            res = httpclient.execute(request);

            // Retrieve the status.
            int status = res.getStatusLine().getStatusCode();
            if (status != HttpStatus.SC_OK) {
                System.out.println("StatusCode:" + status);
                return null;
            }

            // Retrieve the response headers.
            JSONObject res_headers = new JSONObject();
            Header[] resHeaders = res.getAllHeaders();
            for (Header header : resHeaders) {
            	res_headers.put(header.getName(), header.getValue());
            }

            // Set NativeObject.
            result = new NativeObject();
            result.put("status", result, (Number)status);
            result.put("headers", result, (JSONObject)res_headers);
            HttpEntity entity = res.getEntity();
            if (entity != null) {
                if (respondsAsStream) {
                    // InputStream.
                    result.put("body", result, new BufferedHttpEntity(res.getEntity()).getContent());
                } else {
                    // String.
                    result.put("body", result, EntityUtils.toString(entity, "UTF-8"));
                }
            }

        } catch (Exception e) {
            String message = "An error occurred.";
            this.getLogger().warn(message, e);
            String errorMessage = String.format("%s Cause: [%s]",
                    message, e.getClass().getName() + ": " + e.getMessage());
            throw ExtensionErrorConstructor.construct(errorMessage);
        }
        return result;
    }

    /**
     * Post (String).
     * @param uri String
     * @param headers NativeObject
     * @param contentType String
     * @param body String
     * @return NativeObject
     */
    @JSFunction
    public NativeObject post(String uri, NativeObject headers, String contentType, String body) {
    	return post(uri, headers, contentType, body, null);
    }

    /**
     * Post (InputStream).
     * @param uri String
     * @param headers NativeObject
     * @param contentType String
     * @param body InputStream
     * @return NativeObject
     */
    @JSFunction
    public NativeObject post(String uri, NativeObject headers, String contentType, InputStream is) {
    	return post(uri, headers, contentType, null, is);
    }

    private NativeObject post(String uri, NativeObject headers, String contentType, String body, InputStream is) {
    	NativeObject result = null;

    	boolean respondsAsStream = false;
        if (is != null){
        	respondsAsStream = true;
        }

        if (null == uri || uri.isEmpty()) {
            String message = "URL parameter is not set.";
            this.getLogger().info(message);
            throw ExtensionErrorConstructor.construct(message);
        }
        if (null == contentType || contentType.isEmpty()) {
            String message = "contentType parameter is not set.";
            this.getLogger().info(message);
            throw ExtensionErrorConstructor.construct(message);
        }
        if (!respondsAsStream){
            if (null == body || body.isEmpty()) {
                String message = "body parameter is not set.";
                this.getLogger().info(message);
                throw ExtensionErrorConstructor.construct(message);
            }
        }

        try (CloseableHttpClient httpclient = HttpClientBuilder.create().build()) {
            HttpPost request = null;

            // set params from body
            request = new HttpPost(uri);

            if (respondsAsStream){
                // InputStream
            	request.setEntity(new InputStreamEntity(is));
            } else {
                // String
            	request.setEntity(new ByteArrayEntity(body.getBytes("UTF-8")));
            }

            // set contentType
            request.setHeader("Content-Type", contentType);

            // set heades
            if (null != headers) {
                for (Entry<Object, Object> e : headers.entrySet()){
                    request.addHeader(e.getKey().toString(), e.getValue().toString());
                }
            }

            // execute
            HttpResponse res = null;
            res = httpclient.execute(request);

            // Retrieve the status.
            int status = res.getStatusLine().getStatusCode();
            if (status != HttpStatus.SC_OK) {
                System.out.println("StatusCode:" + status);
                return null;
            }

            // response headers
            JSONObject res_headers = new JSONObject();
            Header[] resHeaders = res.getAllHeaders();
            for (Header header : resHeaders) {
                System.out.println(header.getName() + ":" + header.getValue());
                res_headers.put(header.getName(), header.getValue());
            }

            // get entity
            String res_body = "";
            HttpEntity resEntity = res.getEntity();
            if (resEntity != null) {
            	res_body = EntityUtils.toString(resEntity, "UTF-8");
            }

            // set NativeObject
            result = new NativeObject();
            result.put("status", result, (Number)status);
            result.put("headers", result, res_headers);
            result.put("body", result, res_body);

        }catch (Exception e) {
            String message = "An error occurred.";
            this.getLogger().warn(message, e);
            String errorMessage = String.format("%s Cause: [%s]",
                    message, e.getClass().getName() + ": " + e.getMessage());
            throw ExtensionErrorConstructor.construct(errorMessage);
        }
        return result;
    }
}
