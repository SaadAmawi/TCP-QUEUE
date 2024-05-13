@echo off
setlocal enabledelayedexpansion

:: Get start time
for /F "tokens=1-4 delims=:.," %%a in ("%time%") do (
    set /A "start=(%%a*3600)+(%%b*60)+%%c+%%d/100"
)

echo Running Logins...
for /F "tokens=*" %%b in (LogIns10.txt) do (
    start "Login Process" cmd /c java ClientAuto %%b ^&^& exit
)

:: Wait for all Java processes to finish
:waitForJava
timeout /t 1 /nobreak >nul
tasklist | find "notepad.exe" > nul
if not errorlevel 1 goto waitForJava

:: Get end time
for /F "tokens=1-4 delims=:.," %%e in ("%time%") do (
    set /A "end=(%%e*3600)+(%%f*60)+%%g+%%h/100"
)

:: Calculate duration
set /A "duration=%end%-%start%"

echo Execution time: %duration% seconds

:end
echo Press any key to exit.
pause >nul
endlocal
