call mvn clean
echo. & echo.
echo Cleaned old files
echo. & echo.
call mvn install
echo. & echo.
echo Installed new files
echo. & echo.
call mvn validate jfx:jar
echo. & echo.
echo .jar created in target/jfx/app/
echo. & echo.
call mvn validate jfx:native
echo. & echo.
echo .exe created in target/jfx/native/
echo. & echo.
pause
