@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%"=="" @echo off
@rem ##########################################################################
@rem
@rem  application startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.
@rem This is normally unused
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and APPLICATION_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS="-Dfile.encoding=UTF-8"

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if %ERRORLEVEL% equ 0 goto execute

echo. 1>&2
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH. 1>&2
echo. 1>&2
echo Please set the JAVA_HOME variable in your environment to match the 1>&2
echo location of your Java installation. 1>&2

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo. 1>&2
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME% 1>&2
echo. 1>&2
echo Please set the JAVA_HOME variable in your environment to match the 1>&2
echo location of your Java installation. 1>&2

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\planet-user.jar;%APP_HOME%\lib\mapstruct-1.5.5.Final.jar;%APP_HOME%\lib\grpc-protobuf-1.58.0.jar;%APP_HOME%\lib\javax.annotation-api-1.3.2.jar;%APP_HOME%\lib\http-server-undertow-1.1.17.jar;%APP_HOME%\lib\logging-logback-1.1.17.jar;%APP_HOME%\lib\config-hocon-1.1.17.jar;%APP_HOME%\lib\openapi-management-1.1.17.jar;%APP_HOME%\lib\json-module-1.1.17.jar;%APP_HOME%\lib\validation-module-1.1.17.jar;%APP_HOME%\lib\micrometer-module-1.1.17.jar;%APP_HOME%\lib\grpc-client-1.1.17.jar;%APP_HOME%\lib\database-jdbc-1.1.17.jar;%APP_HOME%\lib\jbcrypt-0.4.jar;%APP_HOME%\lib\postgresql-42.7.4.jar;%APP_HOME%\lib\http-server-common-1.1.17.jar;%APP_HOME%\lib\database-common-1.1.17.jar;%APP_HOME%\lib\logging-common-1.1.17.jar;%APP_HOME%\lib\netty-common-1.1.17.jar;%APP_HOME%\lib\telemetry-common-1.1.17.jar;%APP_HOME%\lib\validation-common-1.1.17.jar;%APP_HOME%\lib\config-common-1.1.17.jar;%APP_HOME%\lib\json-common-1.1.17.jar;%APP_HOME%\lib\http-common-1.1.17.jar;%APP_HOME%\lib\common-1.1.17.jar;%APP_HOME%\lib\application-graph-1.1.17.jar;%APP_HOME%\lib\grpc-protobuf-lite-1.58.0.jar;%APP_HOME%\lib\grpc-netty-1.65.1.jar;%APP_HOME%\lib\grpc-stub-1.65.1.jar;%APP_HOME%\lib\grpc-util-1.65.1.jar;%APP_HOME%\lib\grpc-core-1.65.1.jar;%APP_HOME%\lib\grpc-context-1.65.1.jar;%APP_HOME%\lib\grpc-api-1.65.1.jar;%APP_HOME%\lib\guava-32.1.3-jre.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\proto-google-common-protos-2.22.0.jar;%APP_HOME%\lib\protobuf-java-3.24.0.jar;%APP_HOME%\lib\checker-qual-3.42.0.jar;%APP_HOME%\lib\jakarta.annotation-api-2.1.1.jar;%APP_HOME%\lib\config-1.4.3.jar;%APP_HOME%\lib\HikariCP-5.1.0.jar;%APP_HOME%\lib\undertow-core-2.3.18.Final.jar;%APP_HOME%\lib\xnio-nio-3.8.16.Final.jar;%APP_HOME%\lib\xnio-api-3.8.16.Final.jar;%APP_HOME%\lib\jboss-threads-3.5.1.Final.jar;%APP_HOME%\lib\wildfly-client-config-1.0.1.Final.jar;%APP_HOME%\lib\jboss-logging-3.5.3.Final.jar;%APP_HOME%\lib\logback-classic-1.4.14.jar;%APP_HOME%\lib\micrometer-registry-prometheus-1.12.13.jar;%APP_HOME%\lib\micrometer-core-1.12.13.jar;%APP_HOME%\lib\opentelemetry-semconv-1.25.0-alpha.jar;%APP_HOME%\lib\opentelemetry-api-1.37.0.jar;%APP_HOME%\lib\error_prone_annotations-2.23.0.jar;%APP_HOME%\lib\failureaccess-1.0.1.jar;%APP_HOME%\lib\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;%APP_HOME%\lib\jul-to-slf4j-2.0.16.jar;%APP_HOME%\lib\slf4j-api-2.0.16.jar;%APP_HOME%\lib\netty-codec-http2-4.1.100.Final.jar;%APP_HOME%\lib\netty-handler-proxy-4.1.100.Final.jar;%APP_HOME%\lib\perfmark-api-0.26.0.jar;%APP_HOME%\lib\netty-codec-http-4.1.100.Final.jar;%APP_HOME%\lib\netty-handler-4.1.100.Final.jar;%APP_HOME%\lib\netty-transport-native-unix-common-4.1.100.Final.jar;%APP_HOME%\lib\wildfly-common-1.5.4.Final.jar;%APP_HOME%\lib\logback-core-1.4.14.jar;%APP_HOME%\lib\micrometer-observation-1.12.13.jar;%APP_HOME%\lib\micrometer-commons-1.12.13.jar;%APP_HOME%\lib\HdrHistogram-2.1.12.jar;%APP_HOME%\lib\LatencyUtils-2.0.3.jar;%APP_HOME%\lib\simpleclient_common-0.16.0.jar;%APP_HOME%\lib\opentelemetry-context-1.37.0.jar;%APP_HOME%\lib\netty-codec-socks-4.1.100.Final.jar;%APP_HOME%\lib\netty-codec-4.1.100.Final.jar;%APP_HOME%\lib\netty-transport-4.1.100.Final.jar;%APP_HOME%\lib\netty-buffer-4.1.100.Final.jar;%APP_HOME%\lib\netty-resolver-4.1.100.Final.jar;%APP_HOME%\lib\netty-common-4.1.100.Final.jar;%APP_HOME%\lib\jackson-core-2.17.3.jar;%APP_HOME%\lib\gson-2.10.1.jar;%APP_HOME%\lib\annotations-4.1.1.4.jar;%APP_HOME%\lib\animal-sniffer-annotations-1.23.jar;%APP_HOME%\lib\simpleclient-0.16.0.jar;%APP_HOME%\lib\simpleclient_tracer_otel-0.16.0.jar;%APP_HOME%\lib\simpleclient_tracer_otel_agent-0.16.0.jar;%APP_HOME%\lib\simpleclient_tracer_common-0.16.0.jar


@rem Execute application
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %APPLICATION_OPTS%  -classpath "%CLASSPATH%" ru.planet.user.Application %*

:end
@rem End local scope for the variables with windows NT shell
if %ERRORLEVEL% equ 0 goto mainEnd

:fail
rem Set variable APPLICATION_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
set EXIT_CODE=%ERRORLEVEL%
if %EXIT_CODE% equ 0 set EXIT_CODE=1
if not ""=="%APPLICATION_EXIT_CONSOLE%" exit %EXIT_CODE%
exit /b %EXIT_CODE%

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega