# SeleniumGridVPN
Simple Java program to connect to distinct sites and assess the connectivity of the VPN.

## Installation
Download selenium jar and selenium server jar files from https://www.selenium.dev/downloads/.

- **In IntelliJ, go to File -> Project Structure -> Modules -> + -> Add both the directory from the selenium and the selenium server jar file.**

- **In IntelliJ, go to File -> Project Structure -> Libraries -> + -> Search for org.junit.jupiter:junit-jupiter:5.8.1 .**

Like so:

![junit_lib](https://github.com/Andre-MR-Pereira/SeleniumGridVPN/assets/148334469/ff260e21-e06d-4c8d-9e01-b82aa63453ed)

## Usage
### Flags
#### Values
>1. -c/--ChromePath: Path to the Chrome Drivers (chromedriver)
>2. -f/--FirefoxPath: Path to the Firefox Drivers (geckodriver)
>3. [Optional] -v/--VPN: Status of the VPN connection: (Defaults to false)
>   - true: The VPN connection is active
>   - false: The VPN connection is disabled
>4. [Optional environment variable] Port: Change the port Selenium will be listening on to. Defaults to 4444.
>5. [Optional environment variable] ConcurrentBrowsers: Can be set to change how many concurrent browsers spawn. Defaults to 3.
>6. [Optional environment variable] Local: Indicates where selenium grid is running from: (Defaults to true)
>   - true: Local machine will be used, assuming selenium grid in standalone mode is running on the designated port.
>   - false: Docker machines will be used, assuming selenium grid in standalone mode is running on the designated port.
>7. [Optional environment variable] RecordResults: Indicates if the selenium tests should be recorded (only available for Docker atm). Defaults to false.
>8. [Optional environment variable] Headless: Indicates if the selenium tests need the UI open. When headless, no recordings are saved. Defaults to false.
#### Main
Add flags to the execution configuration by going to Run -> Edit Configurations -> Application -> And changing the CLI arguments field

Example: *-ChromePath "Path to Chrome driver executable" -FirefoxPath "Path to Firefox driver executable" -VPN false*
![runConfigs](https://github.com/Andre-MR-Pereira/SeleniumGridVPN/assets/148334469/2f9f6f49-4827-48e4-a861-a9a58f929e0f)

#### JUnit Tests
Add VM flag to enable parallel tests execution by going to Run -> Edit Configurations -> JUnit -> And changing the VM arguments field, like so:
![junitConcurrent](https://github.com/Andre-MR-Pereira/SeleniumGridVPN/assets/148334469/15bedde6-0135-43c5-86d5-335348e72ba3)

Example: -ea -Djunit.jupiter.execution.parallel.enabled=true -Djunit.jupiter.execution.parallel.config.strategy=fixed -Djunit.jupiter.execution.parallel.config.fixed.parallelism=12

Add environment variables to the execution configuration by going to Run -> Edit Configurations -> JUnit -> And changing the environment variables field, like so:

Example: ChromePath=Path to Chrome driver executable;FirefoxPath=Path to Firefox driver executable;VPN=false
![junitConfigs](https://github.com/Andre-MR-Pereira/SeleniumGridVPN/assets/148334469/ebc24d0b-cc89-4ad3-8588-9df1e9197a7b)

## Selenium Grid
#### Locally
>*Assuming the terminal is on the same dir as the jar file, otherwise use the appropriate path to the jar*

>*Suffices for local testing on a single machine*
> 
>Creating standalone session for selenium:
>
>**java -jar selenium-server-4.14.1.jar standalone**

>*If we want to create a distributed system with several machines*
> 
>Opening hub for selenium:
>
>**java -jar selenium-server-4.14.1.jar hub**
>
>Attaching node to the hub:
>
>**java -jar selenium-server-4.14.1.jar node --port 5555**

#### Containerized
Further information can be found here:

[Selenium Grid Standalone with Dynamic Capabilities](https://hub.docker.com/r/selenium/standalone-docker "Selenium grid master image")

[Documentation for Dynamic Grid](https://github.com/SeleniumHQ/docker-selenium/tree/trunk#dynamic-grid "Docker Selenium images documentation")

[Toml file specs](https://www.selenium.dev/documentation/grid/configuration/toml_options "Toml file documentation")

>Pull dynamic grid docker image:
> 
>**docker pull selenium/standalone-docker**
> 
>Opening hub for selenium:
>
>**docker run --rm --name selenium-docker -p 4444:4444 --shm-size="3g" -v ${PWD}/config.toml:/opt/bin/config.toml -v ${PWD}/assets:/opt/selenium/assets -v /var/run/docker.sock:/var/run/docker.sock selenium/standalone-docker:latest**

And the tests only need to point to the correct port. In this case, 4444.
