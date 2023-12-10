# Board Game Application
Welcome to the Board Game Application! 
This Java Spring Boot project provides a platform for playing various board games,
such as Mancala, Chess, and more. 
Developers can enjoy a dynamic gaming environment with customizable game setups
and rules.

## Features
- Supports multiple players with different board setups for each game.
- Game rules and setups are customizable for various board games.
- Play games like Mancala and Chess in a user-friendly interface.

## Getting Started
### Prerequisites
- Java Development Kit (JDK) 17 or later
- Maven
- Docker (optional)

### Build and Run Locally
1. Clone the repository:
    ```shell
    git clone https://github.com/monajahromi/BoardGame.git
    ```
2. Navigate to the project directory:
    ```shell
    cd BoardGame 
    ```
3. Build the project:
    ```shell
    mvn clean package
    ```
4. Run the application:
    ```shell
    java -jar target/BoardGame.jar 
    ```

The application will be accessible at http://localhost:8083/games.

## Docker Support
If you prefer using Docker, 
you can build and run the application as follows:

1. Build the Docker image:
    ```shell
    docker build -t board-game .
    ```
2. Run the Docker container:
   ```shell
   docker  run --name  board-game -p 8083:8083    -d board-game  
   ```

The application will be accessible at http://localhost:8083/games.

## Configuration
Database configuration, game rules, and other settings can be customized in the application.properties file.