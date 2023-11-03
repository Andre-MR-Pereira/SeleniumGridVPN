# SeleniumGridVPN
Simple Java program to connect to distinct sites and assess the connectivity of the VPN.

## Installation
Download selenium jar and selenium server jar files from https://www.selenium.dev/downloads/.

- **In IntelliJ, go to File -> Project Structure -> Modules -> + -> Add both the directory from the selenium and the selenium server jar file.**

- **In IntelliJ, go to File -> Project Structure -> Libraries -> + -> Search for org.junit.jupiter:junit-jupiter:5.8.1 .**

Like so:
![alt text](Assets/junit_lib.PNG)

## Usage
### Flags
Add flags to the execution configuration by going to Run -> Edit Configurations -> And changing the CLI arguments field, separating 
1. -c/--ChromePath: Path to the Chrome Drivers (chromedriver)
2. -f/--FirefoxPath: Path to the Firefox Drivers (geckodriver)
3. -v/--VPN: Status of the VPN connection:
   - 1/true/On: The VPN connection is active
   - 0/false/Off: The VPN connection is disabled

Example: *-ChromePath "Path to Chrome driver executable" -FirefoxPath "Path to Firefox driver executable" -VPN 0*
![alt text](Assets/runConfigs.PNG)

### Selenium Grid
Opening hub for selenium:

**java -jar selenium-server-4.14.1.jar hub**

Attaching node to the hub:

**java -jar selenium-server-4.14.1.jar node --port 5555 --selenium-manager true**