## Goal of this repository

Share a Spring boot API code sample that should reveal the following error message while performing a load test on it : 

```
2024-03-08T19:26:41.700+01:00 ERROR 84541 --- [at-handler-5311] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed: org.springframework.web.reactive.function.client.WebClientRequestException: Connection reset] with root cause

java.net.SocketException: Connection reset
	at java.base/sun.nio.ch.SocketChannelImpl.throwConnectionReset(SocketChannelImpl.java:401) ~[na:na]
	at java.base/sun.nio.ch.SocketChannelImpl.read(SocketChannelImpl.java:434) ~[na:na]
	at io.netty.buffer.PooledByteBuf.setBytes(PooledByteBuf.java:255) ~[netty-buffer-4.1.107.Final.jar:4.1.107.Final]
	at io.netty.buffer.AbstractByteBuf.writeBytes(AbstractByteBuf.java:1132) ~[netty-buffer-4.1.107.Final.jar:4.1.107.Final]
```

## How to reproduce

### 1) start the Wiremock backend

Download latest version of Wiremock standalone server

You can find it [here](https://wiremock.org/docs/download-and-installation/#direct-download), in the `Direct download` section

Then, you can launch it with the following command :

```shell
> cd mocks/bff/wiremock
> java -jar ~/Downloads/wiremock-standalone-3.4.1.jar \
    --port 8081 \
    --no-request-journal \
    --disable-request-logging \
    --async-response-enabled=true \
    --verbose=true
```




### 2) build & launch current repo POC

> Prerequisite : Source code is using latest Java 21 preview features, so you need to have a Java 21 JDK installed 

First, build the project.

You can do it :
- either with your favorite IDE
- or thanks the maven `mvn clean package` from root of the repository

Then you can run it, once again :
- either with your favorite IDE
- or thanks the `java --enable-preview -Dspring.profiles.active=USE-VTHREADS -jar ./target/wiremockConResetSample.jar`

### 3) launch the load test

I've been using K6 to tune my load tests

On Mac, you can install it thanks Homebrew : 

```
> brew install k6
```

Once installed, you can run the `reactive_webclient_light_senario` load test thanks following command :

```shell
> cd loadTests/k6
> k6 run reactive_webclient_light_senario.js
```

or if you want to have a live display of metrics gathered by K6 in a web UI : 

```shell
> cd loadTests/k6
> K6_WEB_DASHBOARD=true k6 run reactive_webclient_light_senario.js
```

