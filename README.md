# Tetris Game - Java SWT Implementation

A fully functional Tetris game implemented in Java using the Standard Widget Toolkit (SWT) and built with Gradle.

## Features

### Core Features
- **Classic Tetris Gameplay**: All 7 standard Tetromino pieces (I, O, T, J, L, S, Z)
- **Piece Movement**: Left/Right movement, rotation, and soft drop
- **Line Clearing**: Automatic detection and clearing of completed lines
- **Collision Detection**: Proper collision detection with board boundaries and existing pieces
- **Scoring System**: Points awarded based on lines cleared and current level
- **Level Progression**: Game speed increases with level advancement
- **Game Over Detection**: Game ends when new pieces cannot be placed

### Controls
- **Arrow Keys**:
  - ↑ (Up Arrow): Rotate current piece
  - ← (Left Arrow): Move piece left
  - → (Right Arrow): Move piece right
  - ↓ (Down Arrow): Soft drop (move piece down faster)
- **R Key**: Restart game

### Game Mechanics
- **Board Size**: 10 blocks wide × 20 blocks tall
- **Scoring**: 100 points per line × current level
- **Level Up**: Every 10 lines cleared increases level
- **Speed**: Game speed increases with each level

## Requirements

- Java Development Kit (JDK) 21 or higher
- SWT library (automatically managed by Gradle)

## Building and Running

### Clone or Create the Project
```bash
# Create a new directory for the project
mkdir tetris-swt
cd tetris-swt

# Copy all the provided source files into the appropriate directories
```

### Build the Project
```bash
# Build the project
./gradlew build

# Or on Windows
gradlew.bat build
```

### Run the Game
```bash
# Run using Gradle
./gradlew runGame

# Or use the application plugin
./gradlew run
```

## Code Architecture

### Main Classes

1. **TetrisGame.java**
   - Main application class with SWT UI setup
   - Game loop implementation using Timer
   - Input handling for keyboard controls
   - Rendering logic for game board and pieces
   - Game state management (score, level, game over)

2. **Tetromino.java**
   - Represents individual Tetris pieces
   - Handles piece movement and rotation
   - Maintains piece position and current rotation state

3. **TetrominoType.java**
   - Enum defining all 7 standard Tetris piece types
   - Contains rotation data for each piece type
   - Provides shape matrices for rendering

4. **GameBoard.java**
   - Manages the game board state
   - Handles line clearing logic
   - Provides collision detection support

### Key Algorithms

- **Collision Detection**: Checks if a piece can move to a new position by testing each block against board boundaries and existing pieces
- **Line Clearing**: Scans the board for complete lines, removes them, and shifts remaining blocks down
- **Rotation**: Uses predefined rotation matrices for each piece type
- **Game Loop**: Timer-based updates that handle piece falling, input processing, and rendering

## Additional Features Implemented

- **Visual Feedback**: Color-coded pieces for easy identification
- **Game Over Screen**: Modal dialog with restart option
- **Info Panel**: Real-time display of score and level
- **Smooth Gameplay**: Double-buffered canvas for flicker-free rendering
- **Restart Functionality**: Press 'R' to restart the game at any time

## Troubleshooting

1. **SWT Issues**: Make sure you're using the correct SWT dependency for your platform
2. **Java Version**: Ensure you're using JDK 21 or higher
3. **Display Issues**: On some Linux systems, you might need to install additional graphics libraries

## License

This project is provided as an educational example. Feel free to modify and extend it for your own purposes.