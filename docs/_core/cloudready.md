## Dockerize

I believe the term _Dockerize_ in the majority of cases simply means _using docker to run an application_. I'm sure the
same term may have other different meanings based on other contexts, but for the purpose of these docs, it simply means
_using docker to run a jipress application_. With definitions out of the way, let's dig in.

The current list of environment variables, and their respective default value are:

| __variable__    | __default value__ |
|-----------------|-------------------|
| host            | 0.0.0.0           |  
| port            | 3000              |  
| securePort      | 3443              |  
| redirectSecure  | false             |  
| keystorePass    | cert              |  
| keystorePath    | changeit          |  
| deployEnv       | development       |  
| pluginsHome     | /app/plugins      |  
| viewEngines     | view              |  
| bodyParsers     | content           |  
| routerHandles   | router            |  
| watch           | false             |  
| resourcesCtx    | /                 |  
| baseDirectory   | www               |  
| welcomeFiles    | index.html        |  
| acceptRanges    | true              |  
| listDirectories | false             |  

Regarding application data and volumes, the current approach for starting a container is to:

1. mount the static resource directory
2. mount the security certs folder (if need be)
3. mount plugins home directories and subdirectories

All these directories exist on the local file system of the docker host so that they are available for use by the
application running in a docker container.

The example below shows the docker script for starting the container on a Windows environment. The switch to running
the same script on a Linux environment should be self-explanatory.

```bash
docker run --rm ^
    -p 3000:3000 ^
    -v <projects_home>\www:/app/www ^
    -v <projects_home>\cert:/app/cert ^
    -v <projects_home>\plugins-routable\build\libs:/app/plugins/router ^
    jipress-jetty:latest
```

Additional information for docker usage can be found in
the [project's docker home page](https://hub.docker.com/repository/docker/m41na/espresso/general)