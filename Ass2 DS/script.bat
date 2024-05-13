@echo off
setlocal

:: Get start time
for /F "tokens=1-4 delims=:.," %%a in ("%time%") do (
    set /A "start=(%%a*3600)+(%%b*60)+%%c+%%d/100"
)

echo Running Signups...
for /F "tokens=*" %%a in (SignUps10.txt) do (
    java ClientAuto %%a
)


:: Waiting a little bit for the last command to initiate properly
timeout /t 5 /nobreak >nul

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
