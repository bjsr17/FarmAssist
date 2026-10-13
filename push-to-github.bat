@echo off
title FarmAssist - Push to GitHub
echo =====================================================================
echo   Pushing FarmAssist project to GitHub
echo   Repository: https://github.com/bjsr17/FarmAssist
echo =====================================================================
cd /d "%~dp0"

echo.
echo Executing: git push -u origin main
echo (If a GitHub sign-in window appears, please authorize or sign in)
echo.

git push -u origin main

if %ERRORLEVEL% EQU 0 (
    echo.
    echo =====================================================================
    echo   [SUCCESS] Successfully pushed FarmAssist to GitHub!
    echo   View your repository at: https://github.com/bjsr17/FarmAssist
    echo =====================================================================
) else (
    echo.
    echo =====================================================================
    echo   [NOTICE] Git push encountered an issue or authentication prompt.
    echo   If you use a Personal Access Token (PAT), you can push with:
    echo     git push https://<YOUR_TOKEN>@github.com/bjsr17/FarmAssist.git main
    echo =====================================================================
)

echo.
pause
