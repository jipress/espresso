package com.akilisha.espresso.ext.fcgi;

import com.akilisha.espresso.ext.fcgi.config.ServletContextExtension;

public class FastCgiExtension implements ServletContextExtension {

    String contextPath = "/";
    String resourceRoot = System.getProperty("APPLICATION_HOME");

    @Override
    public String contextPath() {
        return this.contextPath;
    }

    @Override
    public String resourceRoot() {
        return this.resourceRoot;
    }

    @Override
    public void resourceRoot(String resourceRoot) {
        this.resourceRoot = resourceRoot;
    }
}