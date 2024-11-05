package com.akilisha.espresso.demos;

import com.akilisha.espresso.api.plugin.Directories;
import com.akilisha.espresso.api.plugin.DirectoryInfo;
import com.akilisha.espresso.jett.Espresso;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WordPressExtDemo {

    /**
     * Make sure you have php fpm running in the background to handle php request parsing
     * php-cgi.exe -b 127.0.0.1:9000
     *
     * @param args
     */
    public static void main(String[] args) {
        System.setProperty("APPLICATION_HOME", "C:\\Projects\\wordpress");
        Directories.PLUGINS.put(DirectoryInfo.PLUGINS_HOME, "C:\\Projects\\java\\espresso");
        Directories.PLUGINS.put(DirectoryInfo.CTX_EXTENSIONS, "extend-fastcgi/build/libs");
        var app = Espresso.express();
        app.listen(9080, new String[]{"-securePort", "9084"});
    }
}
