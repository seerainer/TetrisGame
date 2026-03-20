# Agent Guidelines for TetrisGame

This repository contains a Tetris game implemented in Java 25 using SWT (Standard Widget Toolkit) and Gradle.

## 1. Build, Run, and Test

**Platform Note:** Use `gradlew.bat` on Windows and `./gradlew` on Linux/macOS.

### Build
- **Compile and Build:**
  ```bash
  ./gradlew build
  ```
- **Clean:**
  ```bash
  ./gradlew clean
  ```

### Run
- **Run the Application:**
  ```bash
  ./gradlew run
  ```
  Note: The application uses SWT. Ensure a UI environment is available.

### Testing
- **Run All Tests:**
  ```bash
  ./gradlew test
  ```
- **Run a Single Test Class:**
  ```bash
  ./gradlew test --tests "io.github.seerainer.tetris.TetrisGameTest"
  ```
- **Run a Single Test Method:**
  ```bash
  ./gradlew test --tests "io.github.seerainer.tetris.TetrisGameTest.testScore"
  ```
- **Test Location:**
  Tests reside in `src/test/java`. (Currently, no tests exist; verify/create the directory structure before adding new tests).

## 2. Code Style & Conventions

Adhere strictly to the existing coding style found in `src/main/java/io/github/seerainer/tetris`.

### General
- **Java Version:** Java 25.
- **Type Inference:** Use `final var` for local variables whenever possible.
- **Immutability:** Mark local variables, parameters, and fields as `final` by default.
- **Switch Expressions:** Use modern Java switch expressions (`case ->`).

### Formatting
- **Indentation:** 4 spaces.
- **Braces:** Start braces on the same line (K&R style).
- **Line Length:** Aim for < 120 characters, though not strictly enforced.

### Naming
- **Classes:** PascalCase (e.g., `TetrisGame`, `GameBoard`).
- **Methods/Variables:** camelCase (e.g., `createRandomTetromino`, `currentPiece`).
- **Constants:** UPPER_SNAKE_CASE (e.g., `BOARD_WIDTH`, `BLOCK_SIZE`).
- **Packages:** Lowercase (e.g., `io.github.seerainer.tetris`).

### SWT & UI
- **Threading:** SWT UI updates must occur on the UI thread. Use `Display.asyncExec(() -> { ... })` for updates from background threads (like timers).
- **Resource Management:** Ensure SWT resources (Colors, Fonts, Images) are disposed of if they are not system resources, though currently `SystemColor` is heavily used which does not require disposal.

### Error Handling
- Use specific exceptions where possible.
- For UI-related errors, log appropriately or show a user-friendly message box if critical.

### Example Snippet
```java
private void updateScore(final int clearedLines) {
    final var newLevel = (linesCleared / 10) + 1;
    if (newLevel == level) {
        return;
    }
    level = newLevel;
}
```

## 3. Directory Structure
- `src/main/java`: Source code.
- `src/main/resources`: Non-code assets (images, configs).
- `src/test/java`: Test code.
- `build.gradle`: Project build configuration.
