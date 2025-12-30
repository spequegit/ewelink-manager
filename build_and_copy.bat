@call mvn clean install
@call if errorlevel 0 (
    @echo copying...
    @pscp -pw raspberry "target\ewelink-manager-2.0.0-jar-with-dependencies.jar" pi@192.168.1.110:"ewelink-manager.jar"
    @pscp -pw raspberry "application.yml" pi@192.168.1.110:"application.yml"
)
