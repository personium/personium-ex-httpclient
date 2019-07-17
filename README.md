# personium-ex-httpclient

## About
[Personium](http://personium.io) Engine Extension to provide HTTP client functionality.

## Installation
Please see [Setup Engine Extensions](https://personium.io/docs/en/server-operator/setup_engine_extensions.html).

## Note
Supported methods are "GET", "POST", "PUT", "PATCH", "DELETE" only at this point.
For older version (v1.0.1), only "Get" & "POST" were supported. Please see the [here](OlderVersion.md) for details.

## Usage (GET)

```` Javascript
  // Always return the same format to the caller
  var createResponse = function(tempCode, tempBody) {
    var isString = typeof tempBody == "string";
    return {
        status: tempCode,
        headers: {"Content-Type":"application/json"},
        body: [isString ? tempBody : JSON.stringify(tempBody)]
    };
  }

  var url = "http://www.example.com/";
  var headers = {'Accept': 'text/plain'};
  var httpClient = new _p.extension.HttpClient();
  var httpCode, response;

  try {
      response = httpClient.get(url, headers);
  } catch (e) {
      // System exception
      return createResponse(500, e);
  }
  httpCode = parseInt(response.status);
  // Get API usually returns HTTP code 200
  if (httpCode !== 200) {
      // Personium exception
      return createResponse(httpCode, response.body);
  }

  // Do something and then return data
  return createResponse(200, response.body);

````

## Usage (POST)

```` Javascript
  // Always return the same format to the caller
  var createResponse = function(tempCode, tempBody) {
    var isString = typeof tempBody == "string";
    return {
        status: tempCode,
        headers: {"Content-Type":"application/json"},
        body: [isString ? tempBody : JSON.stringify(tempBody)]
    };
  }

  var url = "http://www.example.com/";
  var contentType = "application/x-www-form-urlencoded;";
  var headers = {'Accept': 'text/plain'};
  var body = "bodyParameter1=XXXXX&bodyParameter2=YYYYY";
  var httpClient = new _p.extension.HttpClient();
  var httpCode, response;

  try {
      response = httpClient.post(url, headers, contentType, body);
  } catch (e) {
      // System exception
      return createResponse(500, e);
  }
  httpCode = parseInt(response.status);
  // Create API usually returns HTTP code 201
  if (httpCode !== 201) {
      // Personium exception
      return createResponse(httpCode, response.body);
  }

  // Do something and then return data
  return createResponse(200, response.body);

````

## Usage (PUT)

```` Javascript
  // Always return the same format to the caller
  var createResponse = function(tempCode, tempBody) {
    var isString = typeof tempBody == "string";
    return {
        status: tempCode,
        headers: {"Content-Type":"application/json"},
        body: [isString ? tempBody : JSON.stringify(tempBody)]
    };
  }

  var url = "http://www.example.com/";
  var contentType = "application/x-www-form-urlencoded;";
  var headers = {'Accept': 'text/plain'};
  var body = "bodyParameter1=XXXXX&bodyParameter2=YYYYY";
  var httpClient = new _p.extension.HttpClient();
  var httpCode, response;

  try {
      response = httpClient.put(url, headers, contentType, body);
  } catch (e) {
      // System exception
      return createResponse(500, e);
  }
  httpCode = parseInt(response.status);
  // Put API usually returns HTTP code 200
  if (httpCode !== 200) {
      // Personium exception
      return createResponse(httpCode, response.body);
  }

  // Do something and then return data
  return createResponse(200, response.body);

````

## Usage (PATCH)

```` Javascript
  // Always return the same format to the caller
  var createResponse = function(tempCode, tempBody) {
    var isString = typeof tempBody == "string";
    return {
        status: tempCode,
        headers: {"Content-Type":"application/json"},
        body: [isString ? tempBody : JSON.stringify(tempBody)]
    };
  }

  var url = "http://www.example.com/";
  var contentType = "application/x-www-form-urlencoded;";
  var headers = {'Accept': 'text/plain'};
  var body = "bodyParameter1=XXXXX&bodyParameter2=YYYYY";
  var httpClient = new _p.extension.HttpClient();
  var httpCode, response;

  try {
      response = httpClient.patch(url, headers, contentType, body);
  } catch (e) {
      // System exception
      return createResponse(500, e);
  }
  httpCode = parseInt(response.status);
  // Put API usually returns HTTP code 200
  if (httpCode !== 200) {
      // Personium exception
      return createResponse(httpCode, response.body);
  }

  // Do something and then return data
  return createResponse(200, response.body);

````

## Usage (DELETE)

```` Javascript
  // Always return the same format to the caller
  var createResponse = function(tempCode, tempBody) {
    var isString = typeof tempBody == "string";
    return {
        status: tempCode,
        headers: {"Content-Type":"application/json"},
        body: [isString ? tempBody : JSON.stringify(tempBody)]
    };
  }

  var url = "http://www.example.com/";
  var headers = {'Accept': 'text/plain'};
  var httpClient = new _p.extension.HttpClient();
  var httpCode, response;

  try {
      response = httpClient.delete(url, headers);
  } catch (e) {
      // System exception
      return createResponse(500, e);
  }
  httpCode = parseInt(response.status);
  // DELETE API usually returns HTTP code 204
  if (httpCode !== 204) {
      // Personium exception
      return createResponse(httpCode, response.body);
  }

  // Do something and then return data
  return createResponse(204, response.body);

````

## Constructor parameters

### Ignore SSL verification error
You can ignore the SSL verification error by setting "IgnoreHostnameVerification" to "true" when instantiating HttpClient.

```` javascript
var parameters = {"IgnoreHostnameVerification": true};
var httpClient = new _p.extension.HttpClient(parameters);
````
` Ignoring SSL verification error can be severe security problem. It is necessary to pay attention when using it. `

### Set default headers
You can set default headers to HTTP request by setting "DefaultHeaders" when
instantiating HttpClient.

```` javascript
var parameters= {"DefaultHeaders": {"X-Personium-RequestKey": "MyRequestKey"}};
var httpClient = new _p.extension.HttpClient(parameters);
````

## License

```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Copyright 2017 FUJITSU LIMITED
```

