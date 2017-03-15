/**
 * personium.io
 * Copyright 2014 FUJITSU LIMITED
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

import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mozilla.javascript.NativeObject;

@SuppressWarnings("unused")
public class Ext_HttpClientTest {

    // http_get
    private static final String URI_HTTP_GET_TEXT     = "http://get.example/";
    private static final String URI_HTTP_GET_STREAM   = "http://get.example/";

    // http_post
    private static final String URI_HTTP_POST_TEXT    = "http://post.example/";
    private static final String URI_HTTP_POST_STREAM  = "http://post.example/";

    private static final String POST_PARAMS_TEXT      = "key1=value1&key2=value2&key3=value3";
    private static final String POST_CONTENT_TYPE     = "application/x-www-form-urlencoded;";

    // file
    private static final String POST_FILE_PATH        = "/tmp/";
    private static final String POST_WRITE_FILE       = "test_write.jpg";
    private static final String POST_READ_JPG         = "test_read.jpg";
    private static final String POST_WRITE_BASE64     = "test_jpflag.jpg";
    private static final String BASE64_DATA           = "/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAIBAQIBAQICAgICAgICAwUDAwMDAwYEBAMFBwYH"
      + "BwcGBwcICQsJCAgKCAcHCg0KCgsMDAwMBwkODw0MDgsMDAz/2wBDAQICAgMDAwYDAwYMCAcIDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDA"
      + "wMDAwMDAwMDAwMDAz/wAARCAALAA8DASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQR"
      + "BRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJip"
      + "KTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQF"
      + "BgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSE"
      + "lKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP0"
      + "9fb3+Pn6/9oADAMBAAIRAxEAPwD9L/8Ags7+258QP2NvhJ4X/wCEBtPsV14qvpbe48Rvai5j0jyRHIsCpIjRebODJgyZwkE21Cfnj73/AIJWftT+Lv2vf2SLDx"
      + "R400r7HrFrfTaV9vSEwxa+kKx/6aibQq5ZnjcR5TzIJNuwfu0978a+BtF+JPhm50XxFo+l6/o97t+0WGpWkd1bT7XDrvjcFWw6qwyOCoPUVb0PQ7Lwxolnpum2"
      + "drp+nafAlta2ttEsUFtEihUjRFAVVVQAFAAAAArzY4Susa8Q6r5GrcvS/wDXz6bH3lfiXJp8KUskhgIrFxqOTxF/elHXTa+zUeW/KuXmS5m2v//Z";

    // headers
    private static final String HEADER_KEY              = "Accept";
    private static final String HEADER_VALUE            = "application/json";
    
    @BeforeClass
    public static void beforeClass() {

    }

    @Before
    public void before() throws Exception {

    }

    @After
    public void after() {

    }

    @Test
    public void http_get_text() {
        NativeObject headers = new NativeObject();
        headers.put(HEADER_KEY, headers, HEADER_VALUE);

        Ext_HttpClient ext_httpClient = new Ext_HttpClient();

        /**
         * ext_httpClient.get
         * String uri, NativeObject headers
         */
        NativeObject result = ext_httpClient.get(URI_HTTP_GET_TEXT, headers, false);
        Number status = (Number)result.get("status");
        JSONObject res_headers = (JSONObject)result.get("headers");
        String body = (String)result.get("body");

        assertEquals(status, HttpStatus.SC_OK);
    }

    @Test
    public void http_get_stream() {
        NativeObject headers = new NativeObject();
        headers.put(HEADER_KEY, headers, HEADER_VALUE);

        Ext_HttpClient ext_httpClient = new Ext_HttpClient();

        /**
         * ext_httpClient.get
         * String uri, NativeObject headers
         */
        NativeObject result = ext_httpClient.get(URI_HTTP_GET_STREAM, headers, true);
        Number status = (Number)result.get("status");
        JSONObject res_headers = (JSONObject)result.get("headers");
        InputStream is = (InputStream)result.get("body");

        // stream to write file
        // Confirm the actually acquired image file.
//        InputStreamToFile(is, POST_FILE_PATH, POST_WRITE_FILE);

        assertEquals(status, HttpStatus.SC_OK);
    }

    @Test
    public void http_post_text() {
        NativeObject headers = new NativeObject();
        headers.put(HEADER_KEY, headers, HEADER_VALUE);

        Ext_HttpClient ext_httpClient = new Ext_HttpClient();

        /**
         * ext_httpClient.post text
         * String uri, String body, String contentType,
         * NativeObject headers, boolean respondsAsStream
         */
        NativeObject result = ext_httpClient.post(
            URI_HTTP_POST_TEXT, headers, POST_CONTENT_TYPE, POST_PARAMS_TEXT);
        Number status = (Number)result.get("status");
        String res_body = (String)result.get("body");
        JSONObject res_headers = (JSONObject)result.get("headers");

        assertEquals(status, HttpStatus.SC_OK);
    }

    @Test
    public void http_post_stream() {
        NativeObject headers = new NativeObject();
        headers.put(HEADER_KEY, headers, HEADER_VALUE);

        Ext_HttpClient ext_httpClient = new Ext_HttpClient();

        /**
         * ext_httpClient.post stream
         * String uri, String body, String contentType,
         * NativeObject headers, boolean respondsAsStream
         */
        InputStream body = Base64ToInputStream(BASE64_DATA);

        // For Test File operation.
//        try {
//            body = FileToInputStream(POST_FILE_PATH + POST_READ_JPG);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        InputStreamToFile(body, POST_FILE_PATH, POST_WRITE_BASE64);
//        body = FileToBase64(POST_FILE_PATH + POST_READ_JPG);

        NativeObject result = ext_httpClient.post(
              URI_HTTP_POST_STREAM, headers, POST_CONTENT_TYPE, body);

        Number status = (Number)result.get("status");
        JSONObject res_headers = (JSONObject)result.get("headers");
        String res_body = (String)result.get("body");

        assertEquals(status, HttpStatus.SC_OK);
    }

    // Conversion processing for testing.
    // InputStream to File.
    private void InputStreamToFile(InputStream is, String path, String name) {
        int BTYESIZE = 1024;
        FileOutputStream out = null;
        try {
            File file = new File(path, name);
            out = new FileOutputStream(file, false);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        try {
            byte [] buffer = new byte[BTYESIZE];
            int len = 0;
            while(true) {
                if ((len = is.read(buffer)) == -1) {
                    throw new EOFException();
                }
                out.write(buffer, 0, len);
                if(len < BTYESIZE) {
                    break;
                }
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // File to Base64.
    private String FileToBase64(String file) {
        int BTYESIZE = 1024;
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        byte [] buffer = new byte[BTYESIZE];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            int len = 0;
            while(true) {
                if ((len = fs.read(buffer)) == -1) {
                    throw new EOFException();
                }
                out.write(buffer, 0, len);
                if(len < BTYESIZE) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(Base64.encodeBase64(out.toByteArray()));
    }

    // File to InputStream.
    private InputStream FileToInputStream(String file) throws FileNotFoundException {
        return (InputStream) new BufferedInputStream(new FileInputStream(file));
    }

    // Base64 to InputStream
    private InputStream Base64ToInputStream(String str) {
        return new ByteArrayInputStream(Base64.decodeBase64(str));
    }

}
