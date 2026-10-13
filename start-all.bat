@echo off
title FarmAssist - Full Stack Launcher
echo =====================================================================
echo   FarmAssist - Agricultural Request Management System
echo   Launching Backend (Port 8080) and Frontend (Port 5173)...
echo =====================================================================

start "FarmAssist Backend (Spring Boot)" cmd /c "%~dp0start-backend.bat"
echo Waiting 8 seconds for Spring Boot backend to initialize...
timeout /t 8 /nobreak >nul
start "FarmAssist Frontend (React Vite)" cmd /c "%~dp0start-frontend.bat"
timeout /t 3 /nobreak >nul

echo.
echo =====================================================================
echo   Opening FarmAssist Web Application in your browser...
echo   URL: http://localhost:5173
echo   Backend API: http://localhost:8080/api
echo =====================================================================
start http://localhost:5173
echo.
pause
