@echo off
title FarmAssist - React Vite Frontend (Port 5173)
echo =====================================================================
echo   Starting FarmAssist React Frontend Server (Port 5173)...
echo =====================================================================
cd /d "%~dp0frontend"
call npm.cmd run dev
pause
