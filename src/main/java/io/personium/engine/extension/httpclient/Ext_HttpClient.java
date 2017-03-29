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
import io.personium.engine.extension.wrapper.PersoniumInputStream;

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
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Engine-Extension HttpClient.
 */
@SuppressWarnings("serial")
public class Ext_HttpClient extends AbstractExtensionScriptableObject {

	
    static Logger log = LoggerFactory.getLogger(Ext_HttpClient.class);

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
    @JSFunction
    public NativeObject get(String uri, NativeObject headers, boolean respondsAsStream) {
        NativeObject result = null;

        if (null == uri || uri.isEmpty()) {
            String message = "URL parameter is not set.";
            this.getLogger().info(message);
            throw ExtensionErrorConstructor.construct(message);
        }

        try (CloseableHttpClient httpclient = HttpClientBuilder.create().build()) {
            HttpGet get = new HttpGet(uri);

            // Set request headers.
            if (null != headers) {
                for (Entry<Object, Object> e : headers.entrySet()){
                	get.addHeader(e.getKey().toString(), e.getValue().toString());
                }
            }

            HttpResponse res = null;
            res = httpclient.execute(get);

            // Retrieve the status.
            int status = res.getStatusLine().getStatusCode();
            log.debug("status:" + status);
            if (status != HttpStatus.SC_OK) {
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

            // Number、JSONObjectでは動作しないことを確認。そのため、Stringで設定する。
//            result.put("status", result, (Number)status);
//            result.put("headers", result, (JSONObject)res_headers);
            result.put("status", result, Integer.toString(status));
            result.put("headers", result, res_headers.toString());

            HttpEntity entity = res.getEntity();
            if (entity != null) {
                if (respondsAsStream) {
                    // InputStream > PersoniumInputStream.
                	InputStream is = new BufferedHttpEntity(res.getEntity()).getContent();
                	PersoniumInputStream pis = new PersoniumInputStream((InputStream) is);
                	result.put("body", result, (PersoniumInputStream)pis);
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
     * postParam (String).
     * @param uri String
     * @param headers NativeObject
     * @param contentType String
     * @param params String
     * @return NativeObject
     */
    @JSFunction
    public NativeObject postParam(String uri, NativeObject headers, String contentType, String params) {
        return post(uri, headers, contentType, params, null, null);
    }

    /**
     * postStream (PersoniumInputStream).
     * @param uri String
     * @param headers NativeObject
     * @param contentType String
     * @param params String
     * @param pis PersoniumInputStream
     * @param fileName String
     * @return NativeObject
     */
// PostのStreamは、動作が確認できていないためコメント。
//    @JSFunction
//    public NativeObject postStream(String uri, NativeObject headers, String contentType, PersoniumInputStream pis, String fileName) {
//        return post(uri, headers, contentType, null, pis, fileName);
//    }

    /**
     * Post.
     * @param uri String
     * @param headers NativeObject
     * @param contentType String
     * @param params String
     * @param is PersoniumInputStream
     * @param fileName String
     * @return NativeObject
     */
    private NativeObject post(String uri, NativeObject headers, String contentType,
                              String params, PersoniumInputStream pis, String fileName) {
    	NativeObject result = null;

    	boolean respondsAsStream = false;
//        if (pis != null){
//            respondsAsStream = true;
//        }

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
            if (null == params || params.isEmpty()) {
                String message = "body parameter is not set.";
                this.getLogger().info(message);
                throw ExtensionErrorConstructor.construct(message);
            }
        }

        try (CloseableHttpClient httpclient = HttpClientBuilder.create().build()) {
            HttpPost post = new HttpPost(uri);

            // set contentType
            post.setHeader("Content-Type", contentType);

            // set heades
            if (null != headers) {
                for (Entry<Object, Object> e : headers.entrySet()){
                	post.addHeader(e.getKey().toString(), e.getValue().toString());
                }
            }

            // set Stream/Paramaters
            if (respondsAsStream){
                // InputStream
            	// 画像ファイルを想定しているが、今後、動画等Streamを含めた対応の仕様を決定する必要がある。
                // POST受け取り用(テスト)のサーバを用意すること。
//                MultipartEntityBuilder meb = MultipartEntityBuilder.create();
//                meb.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

           	    // パラメータ名,画像データ,画像のタイプ,画像ファイル名
//                meb.addBinaryBody("upfile", (InputStream)pis, ContentType.create(contentType), fileName);
//           	  post.setEntity(meb.build());
            } else {
                // String
            	post.setEntity(new ByteArrayEntity(params.getBytes("UTF-8")));
            }

            // execute
            HttpResponse res = httpclient.execute(post);

            // Retrieve the status.
            int status = res.getStatusLine().getStatusCode();
            if (status != HttpStatus.SC_OK) {
                return null;
            }

            // response headers
            JSONObject res_headers = new JSONObject();
            Header[] resHeaders = res.getAllHeaders();
            for (Header header : resHeaders) {
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
            result.put("status", result, (String)Integer.toString(status));
            result.put("headers", result, (String)res_headers.toString());
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
