## Command line interface options

_CLI_ options allow a user to customize certain characteristics of _jipress_ so that it is tailored to their specific
needs more than the default options can provide right out-of-the-box. Here is a list of the currently available options,
and this list will most certainly keep evolving to reflect the most recent state of the framework.

| __variable name__            | __description__                                      |
|------------------------------|------------------------------------------------------| 
| __default options__          |                                                      |
| pluginsHome                  | plugins home directory                               |
| viewEngines                  | view engine plugins sub-directory                    |
| bodyParsers                  | body parser plugins sub-directory                    |
| routerHandles                | router handle plugins sub-directory                  |
| port                         | listening port                                       |
| host                         | application host                                     |
| securePort                   | secure listening port                                |
| redirectSecure               | redirect from http to https                          |  
| deployEnv                    | deployment environment (dev, prod, test, int, stage) |  
| watch                        | watching plugins dir for changes                     |  
| __keystore options__         |                                                      |
| keystorePass                 | keystore password                                    |  
| keystorePath                 | path to key store                                    |  
| __plugins options__          |                                                      |
| pluginsHome                  | plugins home directory                               |  
| viewEngines                  | view engine plugins sub-directory                    |  
| bodyParsers                  | body parser plugins sub-directory                    |  
| routerHandles                | router handle plugins sub-directory                  |  
| __extensions options__       |                                                      |
| ctxExtensions                | context extension plugins sub-directory              |  
| __static resources options__ |                                                      |
| resourcesCtx                 | context path to use for looking up static files      |  
| baseDirectory                | base directory for static files                      |  
| welcomeFiles                 | comma-separated names of welcome files               |  
| acceptRanges                 | accept ranges when looking up resources              |  
| listDirectories              | list directory content when a folder is reached      |  