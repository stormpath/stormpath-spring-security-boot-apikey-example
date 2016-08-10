## Introduction

The purpose of this example is to demonstrate using the `grant_type=client_credentials` OAuth2 workflow to access a restricted endpoint in a Spring Security Spring Boot WebMVC application with Stormpath integrated.

The only explicitly defined endpoint in this example is `/getNewApiKey`

The `/oauth/token` endpoint is provided by the Stormpath integration.

## Setup

Make sure that you've created a Stormpath account and that this application has access to your `apiKey.properties` file and a Stormpath Application href.

## Build

`mvn clean install`

## Run

```
STORMPATH_API_KEY_FILE=<path to apiKey.properties> \
STORMPATH_APPLICATION_HREF=<full href to Stormpath Application> \
java -jar target/*.jar
```

## Use

The examples below use the httpie client (https://github.com/jkbrzt/httpie)

1. Get an api client key pair

    `http POST localhost:8080/getNewApiKey email=<valid email address> password=<valid password>`
    
    Response:
    
    ```
    {
        "STATUS": "SUCCESS",
        "keyID": "<api key id>",
        "keySecret": "<api key secret>",
        "msg": "This is for testing purposes only!"
    }
    ```
    
2. Make a `client_credentials` request to get an access token

    `http --auth <api key id>:<api key secret> -f POST localhost:8080/oauth/token grant_type=client_credentials`
    
    Response:
    
    ```
    {
        "access_token": "eyJraWQiOiJSOTJTQkhKQzFVNERBSU1HUTNNSE9HVk1YIiwic3R0IjoiYWNjZXNzIiwiYWxnIjoiSFMyNTYifQ...",
        "expires_in": 3600,
        "token_type": "Bearer"
    }
    ```
    
3. Hit the `/restricted` endpoint using the access token

   First, we'll hit the endpoint without authentication:
   
   `http localhost:8080/restricted`
   
   Response:
   
   ```
   {
       "fieldErrors": null,
       "message": "Full authentication is required to access this resource",
       "status": "error.accessDenied"
   }
   ```

    Next, we'll hit the endpoint with authentication:
    
    ```
    http localhost:8080/restricted \
    Authorization:"Bearer eyJraWQiOiJSOTJTQkhKQzFVNERBSU1HUTNNSE9HVk1YIiwic3R0IjoiYWNjZXNzIiwiYWxnIjoiSFMyNTYifQ..."
    ```
    
    Response:
    
    `You must have authenticated, or you wouldn't be here.`