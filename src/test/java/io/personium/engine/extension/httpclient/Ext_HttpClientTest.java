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
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
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
	private static final String URI_HTTP_GET_TEXT = "http://get.example/";
	private static final String URI_HTTP_GET_STREAM = "https://demo.personium.io/baas-demo/1/flags/JP.png";

	// http_post
	private static final String URI_HTTP_POST_TEXT = "http://post.example/";
	private static final String POST_PARAMS = "key1=value1&key2=value2&key3=value3";
	private static final String POST_CONTENT_TYPE = "application/x-www-form-urlencoded;";

	// headers
	private static final String HEADER_KEY = "Accept";
	private static final String HEADER_VALUE = "application/json";
	
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
    	String res_status = (String)result.get("status");
    	String res_headers = (String)result.get("headers");
    	String res_body = (String)result.get("body");

    	assertEquals(Integer.parseInt(res_status), HttpStatus.SC_OK);
    }

    @Test
    public void http_stream() {
    	NativeObject headers = new NativeObject();
    	headers.put(HEADER_KEY, headers, HEADER_VALUE);

    	Ext_HttpClient ext_httpClient = new Ext_HttpClient();

    	/**
         * ext_httpClient.get
         * String uri, NativeObject headers
         */
    	NativeObject result = ext_httpClient.get(URI_HTTP_GET_STREAM, headers, true);
    	String res_status = (String)result.get("status");
    	String res_headers = (String)result.get("headers");
    	InputStream is = (InputStream)result.get("body");

    	// stream to write file
//    	  String IMAGE_FILEPATH = "/tmp/";
//    	  String IMAGE_FILENAME = "JP.png";
//        writeInputStream(is, IMAGE_FILEPATH, IMAGE_FILENAME);

    	assertEquals(Integer.parseInt(res_status), HttpStatus.SC_OK);
    }

    @Test
    public void http_post_text() {
    	NativeObject headers = new NativeObject();
    	headers.put(HEADER_KEY, headers, HEADER_VALUE);

    	Ext_HttpClient ext_httpClient = new Ext_HttpClient();

    	/**
         * ext_httpClient.post
         * String uri, String body, String contentType, NativeObject headers
         */
    	NativeObject result = ext_httpClient.post(URI_HTTP_POST_TEXT, POST_PARAMS,
    			POST_CONTENT_TYPE, headers);
    	String res_status = (String)result.get("status");
    	String res_body = (String)result.get("body");
    	String res_headers = (String)result.get("headers");

    	assertEquals(Integer.parseInt(res_status), HttpStatus.SC_OK);
    }

    // Stream to File.
    private void writeInputStream(InputStream is, String path, String name) {
    	int BTYESIZE = 512;
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
}
