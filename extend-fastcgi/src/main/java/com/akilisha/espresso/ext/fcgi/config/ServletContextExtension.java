package com.akilisha.espresso.ext.fcgi.config;

import com.akilisha.espresso.api.extension.IExtension;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.io.IOException;

import static com.akilisha.espresso.ext.fcgi.config.FastCgiConfigurer.*;

public interface ServletContextExtension extends IExtension {

    @Override
    default <T> void extendWith(T extensionPoint) {
        try {
            if (this.contextPath() != null && this.resourceRoot() != null) {
                ServletContextHandler context = createServletContext(this.contextPath(), this.resourceRoot());
                configureTryFilesFilter(context);
                configureDefaultServletHandler(context);
                configureFastCgiHandler(context, this.resourceRoot());
                ((ContextHandlerCollection) extensionPoint).addHandler(context);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
