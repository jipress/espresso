package com.akilisha.espresso.demos;

import com.akilisha.espresso.api.plugin.Directories;
import com.akilisha.espresso.api.plugin.DirectoryInfo;
import com.akilisha.espresso.jett.Espresso;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PiwigoExtDemo {

    public static void main(String[] args) {
        System.setProperty("APPLICATION_HOME", "C:\\Projects\\piwigo");
        Directories.PLUGINS.put(DirectoryInfo.PLUGINS_HOME, "C:\\Projects\\java\\espresso");
        Directories.PLUGINS.put(DirectoryInfo.CTX_EXTENSIONS, "extension-fastcgi/build/libs");
        var app = Espresso.express();
        app.listen(9082, new String[]{"-securePort", "9086"});
    }
}
