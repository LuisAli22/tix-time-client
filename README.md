# tix-time-client
TiX Time Client is the application that runs on the client. Its task is to send packages to the server and report the
full statistics from it.

How to run the client:
* GUI mode by using the native installer and running the application
* GUI mode by running java -jar fxlauncher.jar (from command line)
* CLI mode by using java -jar fxlauncher.jar username password installation port (from command line)

How to generate executable jar file:
* Run gradle embedApplicationManifest and look for fxlauncher.jar under build/fxlauncher/ dir

How to generate native installer:
* Clone this repository into a computer/VM with the desired native platform (e.g. get a Macbook to generate native OSX installer)
* Run gradle generateNativeInstaller and look for the installer file under build/installer/bundles/ dir

How to push client code changes or dependency changes to new+existing users:
* Run gradle deployApp
* Manual alternative: use scp to copy all files under build/fxlauncher/ dir to server assets folder
