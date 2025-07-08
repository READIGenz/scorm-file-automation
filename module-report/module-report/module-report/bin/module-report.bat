@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  module-report startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and MODULE_REPORT_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\module-report-1.0-SNAPSHOT.jar;%APP_HOME%\lib\module-core-1.0-SNAPSHOT.jar;%APP_HOME%\lib\log4j-1.2.17.jar;%APP_HOME%\lib\cucumber-runner-1.3.4.jar;%APP_HOME%\lib\jaxb-api-2.2.11.jar;%APP_HOME%\lib\jaxb-core-2.2.11.jar;%APP_HOME%\lib\jaxb-impl-2.2.11.jar;%APP_HOME%\lib\activation-1.1.1.jar;%APP_HOME%\lib\cucumber-core-3.0.2.jar;%APP_HOME%\lib\jackson-mapper-asl-1.9.13.jar;%APP_HOME%\lib\hamcrest-junit-2.0.0.0.jar;%APP_HOME%\lib\java-hamcrest-2.0.0.0.jar;%APP_HOME%\lib\java-client-3.3.0.jar;%APP_HOME%\lib\cucumber-report-generator-1.3.4.jar;%APP_HOME%\lib\selenium-java-2.48.2.jar;%APP_HOME%\lib\selenium-htmlunit-driver-2.48.2.jar;%APP_HOME%\lib\htmlunit-2.18.jar;%APP_HOME%\lib\batik-transcoder-1.6-1.jar;%APP_HOME%\lib\batik-script-1.6-1.jar;%APP_HOME%\lib\batik-bridge-1.6-1.jar;%APP_HOME%\lib\batik-svg-dom-1.6-1.jar;%APP_HOME%\lib\batik-dom-1.6-1.jar;%APP_HOME%\lib\xercesImpl-2.11.0.jar;%APP_HOME%\lib\xalan-2.7.2.jar;%APP_HOME%\lib\serializer-2.7.2.jar;%APP_HOME%\lib\xml-apis-1.4.01.jar;%APP_HOME%\lib\itextpdf-5.5.12.jar;%APP_HOME%\lib\json-io-4.10.0.jar;%APP_HOME%\lib\cucumber-junit-1.2.5.jar;%APP_HOME%\lib\cucumber-testng-1.2.5.jar;%APP_HOME%\lib\maven-reporting-impl-2.0.4.3.jar;%APP_HOME%\lib\doxia-site-renderer-1.0.jar;%APP_HOME%\lib\doxia-module-apt-1.0.jar;%APP_HOME%\lib\doxia-module-fml-1.0.jar;%APP_HOME%\lib\doxia-module-xdoc-1.0.jar;%APP_HOME%\lib\doxia-module-xhtml-1.0.jar;%APP_HOME%\lib\doxia-core-1.0.jar;%APP_HOME%\lib\maven-doxia-tools-1.0.2.jar;%APP_HOME%\lib\plexus-velocity-1.1.7.jar;%APP_HOME%\lib\maven-project-2.0.6.jar;%APP_HOME%\lib\maven-artifact-manager-2.0.6.jar;%APP_HOME%\lib\maven-settings-2.0.6.jar;%APP_HOME%\lib\maven-profile-2.0.6.jar;%APP_HOME%\lib\maven-plugin-registry-2.0.6.jar;%APP_HOME%\lib\plexus-container-default-1.0-alpha-30.jar;%APP_HOME%\lib\plexus-classworlds-1.2-alpha-9.jar;%APP_HOME%\lib\junit-4.12.jar;%APP_HOME%\lib\flying-saucer-pdf-9.1.7.jar;%APP_HOME%\lib\commons-lang3-3.6.jar;%APP_HOME%\lib\selenium-chrome-driver-2.48.2.jar;%APP_HOME%\lib\selenium-edge-driver-2.48.2.jar;%APP_HOME%\lib\selenium-firefox-driver-2.48.2.jar;%APP_HOME%\lib\selenium-ie-driver-2.48.2.jar;%APP_HOME%\lib\selenium-safari-driver-2.48.2.jar;%APP_HOME%\lib\selenium-support-2.48.2.jar;%APP_HOME%\lib\selenium-leg-rc-2.48.2.jar;%APP_HOME%\lib\selenium-remote-driver-2.48.2.jar;%APP_HOME%\lib\selenium-api-2.48.2.jar;%APP_HOME%\lib\httpmime-4.5.jar;%APP_HOME%\lib\httpclient-4.5.1.jar;%APP_HOME%\lib\commons-codec-1.10.jar;%APP_HOME%\lib\javassist-3.22.0-CR2.jar;%APP_HOME%\lib\joda-time-2.9.9.jar;%APP_HOME%\lib\plexus-i18n-1.0-beta-7.jar;%APP_HOME%\lib\doxia-decoration-model-1.0.jar;%APP_HOME%\lib\maven-artifact-2.0.6.jar;%APP_HOME%\lib\maven-model-2.0.6.jar;%APP_HOME%\lib\maven-repository-metadata-2.0.6.jar;%APP_HOME%\lib\wagon-provider-api-1.0-beta-2.jar;%APP_HOME%\lib\plexus-utils-3.0.1.jar;%APP_HOME%\lib\maven-plugin-api-2.0.4.jar;%APP_HOME%\lib\maven-reporting-api-2.0.8.jar;%APP_HOME%\lib\cucumber-html-0.2.7.jar;%APP_HOME%\lib\gherkin-5.0.0.jar;%APP_HOME%\lib\tag-expressions-1.1.1.jar;%APP_HOME%\lib\cucumber-expressions-5.0.19.jar;%APP_HOME%\lib\datatable-1.0.3.jar;%APP_HOME%\lib\jackson-core-asl-1.9.13.jar;%APP_HOME%\lib\gson-2.3.1.jar;%APP_HOME%\lib\guava-18.0.jar;%APP_HOME%\lib\cglib-3.1.jar;%APP_HOME%\lib\commons-validator-1.4.1.jar;%APP_HOME%\lib\freemarker-2.3.26-incubating.jar;%APP_HOME%\lib\cucumber-core-1.2.5.jar;%APP_HOME%\lib\testng-6.9.10.jar;%APP_HOME%\lib\hamcrest-core-1.3.jar;%APP_HOME%\lib\itext-2.1.7.jar;%APP_HOME%\lib\bctsp-jdk14-1.38.jar;%APP_HOME%\lib\flying-saucer-core-9.1.7.jar;%APP_HOME%\lib\doxia-sink-api-1.0.jar;%APP_HOME%\lib\datatable-dependencies-1.0.3.jar;%APP_HOME%\lib\webbit-0.4.14.jar;%APP_HOME%\lib\httpcore-4.4.3.jar;%APP_HOME%\lib\commons-beanutils-1.8.3.jar;%APP_HOME%\lib\commons-logging-1.2.jar;%APP_HOME%\lib\asm-4.2.jar;%APP_HOME%\lib\commons-digester-1.8.1.jar;%APP_HOME%\lib\velocity-1.5.jar;%APP_HOME%\lib\commons-collections-3.2.1.jar;%APP_HOME%\lib\batik-gvt-1.6-1.jar;%APP_HOME%\lib\cucumber-html-0.2.3.jar;%APP_HOME%\lib\cucumber-jvm-deps-1.0.5.jar;%APP_HOME%\lib\gherkin-2.12.2.jar;%APP_HOME%\lib\jcommander-1.48.jar;%APP_HOME%\lib\bsh-2.0b4.jar;%APP_HOME%\lib\commons-io-2.4.jar;%APP_HOME%\lib\commons-exec-1.3.jar;%APP_HOME%\lib\jna-platform-4.1.0.jar;%APP_HOME%\lib\jna-4.1.0.jar;%APP_HOME%\lib\netty-3.5.2.Final.jar;%APP_HOME%\lib\batik-parser-1.6-1.jar;%APP_HOME%\lib\batik-awt-util-1.6-1.jar;%APP_HOME%\lib\js-1.5R4.1.jar;%APP_HOME%\lib\commons-lang-2.1.jar;%APP_HOME%\lib\oro-2.0.8.jar;%APP_HOME%\lib\cglib-nodep-2.1_3.jar;%APP_HOME%\lib\htmlunit-core-js-2.17.jar;%APP_HOME%\lib\nekohtml-1.9.22.jar;%APP_HOME%\lib\cssparser-0.9.16.jar;%APP_HOME%\lib\websocket-client-9.2.12.v20150709.jar;%APP_HOME%\lib\batik-css-1.6-1.jar;%APP_HOME%\lib\batik-xml-1.6-1.jar;%APP_HOME%\lib\batik-util-1.6-1.jar;%APP_HOME%\lib\sac-1.3.jar;%APP_HOME%\lib\websocket-common-9.2.12.v20150709.jar;%APP_HOME%\lib\jetty-io-9.2.12.v20150709.jar;%APP_HOME%\lib\jetty-util-9.2.12.v20150709.jar;%APP_HOME%\lib\batik-gui-util-1.6-1.jar;%APP_HOME%\lib\websocket-api-9.2.12.v20150709.jar;%APP_HOME%\lib\batik-ext-1.6-1.jar;%APP_HOME%\lib\xmlParserAPIs-2.0.2.jar

@rem Execute module-report
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %MODULE_REPORT_OPTS%  -classpath "%CLASSPATH%" com.consorsbank.test.core.report.Main %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable MODULE_REPORT_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%MODULE_REPORT_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
