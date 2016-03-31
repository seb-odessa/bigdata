@SET AUT=HelloWorld
@SET ROOT=I:\Java
@SET JRE=bin\java
@SET JDK=bin\javac
@SET VERSIONS=jdk1.7.0_55

forfiles /p src /m *.java /C "%ROOT%\jdk1.7.0_55\%JDK% @path"

