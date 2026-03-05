# Project Plan

A simple farm game called CozyFarm where users can manage farm plots directly via Android home screen widgets. Key features include planting, growing, and harvesting 4 different types of crops (demo) with varying growth times and values. Harvesting earns money to buy more seeds and eventually unlock decorations and new features. The app should follow MVVM + Clean Architecture.

## Project Brief

# CozyFarm Project Brief

## Features
- **Widget-Based Farming**: Perform core game actions like planting, growing, and harvesting crops directly from Android home screen widgets using **Jetpack Compose Glance**.
- **Dynamic Crop Cycle**: Support for 4 distinct crop types with unique growth durations and market values, creating a varied gameplay loop.
- **In-Game Economy**: A functional currency system where players earn money from harvests to reinvest in seeds and farm upgrades.
- **Progression & Customization**: Unlockable decorative items and farm features that become available as players reach specific financial milestones.

## High-Level Technical Stack
- **Kotlin**: The primary language for robust and concise Android development.
- **Jetpack Compose**: Used for building the main application dashboard and UI components.
- **Jetpack Compose Glance**: Specifically utilized to create interactive, Material 3-styled home screen widgets.
- **MVVM + Clean Architecture**: Ensures a clear separation of concerns between game logic, data management, and the UI.
- **Kotlin Coroutines**: Handles growth timers and asynchronous game state updates efficiently.
- **KSP (Kotlin Symbol Processing)**: Used for high-performance code generation.
- **Material Design 3**: Strict adherence to M3 guidelines for a vibrant, energetic, and modern aesthetic with full edge-to-edge support.

## Implementation Steps
**Total Duration:** 10m 43s

### Task_1_Core_Logic_Persistence: Initialize the data layer with Room database entities for Crops and PlayerStats. Implement a Repository and ViewModel to manage the farming cycle logic, including growth timers and the economy system. Add missing Glance dependencies to libs.versions.toml.
- **Status:** COMPLETED
- **Updates:** Successfully initialized the data layer with Room database entities for Crops and PlayerStats. Implemented a Repository and ViewModel to manage the farming cycle logic, including growth timers and the economy system. Added missing Glance dependencies to libs.versions.toml.
- **Acceptance Criteria:**
  - Room DB and entities defined
  - Repository and ViewModel handle growth/economy logic
  - Glance dependencies added to project

### Task_2_Interactive_UI: Build the main Jetpack Compose dashboard for monitoring the farm and purchasing seeds. Implement interactive Home Screen Widgets using Jetpack Compose Glance to allow planting and harvesting directly from the home screen.
- **Status:** COMPLETED
- **Updates:** Successfully built the main Jetpack Compose dashboard for monitoring the farm and purchasing seeds. Implemented interactive Home Screen Widgets using Jetpack Compose Glance to allow planting and harvesting directly from the home screen.
- **Acceptance Criteria:**
  - Main dashboard shows farm state and shop
  - Glance widgets are functional for crop actions
  - Navigation between screens works
- **Duration:** 9m 44s

### Task_3_Visual_Identity: Implement a vibrant Material 3 theme with light and dark color schemes. Ensure full edge-to-edge support and create an adaptive app icon that reflects the farming theme.
- **Status:** COMPLETED
- **Updates:** Successfully implemented a vibrant Material 3 theme with light and dark color schemes. Ensure full edge-to-edge support and create an adaptive app icon that reflects the farming theme.
- Re-designed the color palette in Color.kt with vibrant Green and Orange colors.
- Updated Theme.kt to fully support Dynamic Color and fallback to vibrant colors.
- Ensured full edge-to-edge support in MainActivity.kt and DashboardScreen.kt.
- Refined the adaptive app icon with a white sprout silhouette on a sunny-amber background.
- Integrated DashboardScreen in MainActivity.kt.
- Verified build stability and UI consistency.
- **Acceptance Criteria:**
  - Material 3 theme with vibrant colors implemented
  - Edge-to-edge display active
  - Adaptive app icon created and functional
- **Duration:** 59s

### Task_4_Final_Run_Verify: Execute a final build and run the application to verify stability, feature completeness, and adherence to MVVM architecture. Ensure the farming cycle is seamless across the app and widgets.
- **Status:** IN_PROGRESS
- **Acceptance Criteria:**
  - Build pass and app does not crash
  - All game features work as expected
  - Material 3 and MVVM guidelines followed
  - Existing tests pass
- **StartTime:** 2026-03-05 16:23:14 ICT

