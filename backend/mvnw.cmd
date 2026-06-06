@echo off
setlocal
set BASE_DIR=%~dp0
set WRAPPER_DIR=%BASE_DIR%.mvn\wrapper
set MAVEN_HOME=%WRAPPER_DIR%\apache-maven-3.9.9
if not exist "%MAVEN_HOME%\bin\mvn.cmd" (
  powershell -NoProfile -ExecutionPolicy Bypass -Command "New-Item -ItemType Directory -Force '%WRAPPER_DIR%' | Out-Null; Invoke-WebRequest -Uri 'https://archive.apache.org/dist/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.zip' -OutFile '%WRAPPER_DIR%\maven.zip'; Expand-Archive -Force '%WRAPPER_DIR%\maven.zip' '%WRAPPER_DIR%'"
)
"%MAVEN_HOME%\bin\mvn.cmd" %*
