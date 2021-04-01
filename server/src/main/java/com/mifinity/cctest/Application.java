package com.mifinity.cctest;

import io.micronaut.runtime.Micronaut;
import org.h2.tools.Server;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class Application {

    public static void main(String[] args) throws Exception{
        // Bridge JUL to Slf4j
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        Server.createWebServer().start();
        Micronaut.run(Application.class, args);
    }
}
