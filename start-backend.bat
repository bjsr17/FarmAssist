@echo off
title FarmAssist - Spring Boot Backend Server (Port 8080)
echo =====================================================================
echo   Starting FarmAssist Spring Boot Backend Server (Port 8080)...
echo =====================================================================
cd /d "%~dp0backend"
call .\mvnw.cmd spring-boot:run
pause
