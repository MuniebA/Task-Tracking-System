# Task Management App

## Project Overview

This is a comprehensive Task Management Android application developed as part of the COS30017 Software Development for Mobile Devices course. The app provides users with a flexible and intuitive way to manage their tasks, with features like task creation, categorization, priority setting, and multiple view modes.

## Features

### Task Management
- Create, edit, and delete tasks
- Set task name, description, and due date
- Mark tasks as important
- Track task completion status

### Task Views
- Home View: All active tasks
- Important View: High-priority tasks
- Overdue View: Tasks past their due date
- Completed View: Finished tasks

### Key Functionalities
- Date picker for setting due dates
- Task priority marking
- Task completion tracking
- Persistent storage using SQLite database

## Technology Stack

- **Language**: Kotlin
- **Android Components**:
  - Activities
  - Fragments
  - RecyclerView
  - Bottom Navigation
- **Database**: SQLite
- **Architecture**: MVVM-inspired design
- **UI**: Material Design principles

## Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/assignment_5/
│   │   │   ├── Activities/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   └── AddEditTaskActivity.kt
│   │   │   ├── Fragments/
│   │   │   │   ├── HomeFragment.kt
│   │   │   │   ├── ImportantFragment.kt
│   │   │   │   ├── OverdueFragment.kt
│   │   │   │   └── CompletedFragment.kt
│   │   │   ├── Adapters/
│   │   │   │   └── TaskAdapter.kt
│   │   │   ├── Data/
│   │   │   │   ├── Task.kt
│   │   │   │   └── DatabaseHelper.kt
│   │   └── res/
│   │       ├── layout/
│   │       ├── drawable/
│   │       └── values/
```

## Setup and Installation

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK
- Kotlin plugin

### Steps
1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Run the app on an emulator or physical device

## Key Components

### MainActivity
- Manages bottom navigation
- Handles fragment switching
- Provides floating action button for task creation

### DatabaseHelper
- Manages SQLite database operations
- Handles task CRUD (Create, Read, Update, Delete) operations

### TaskAdapter
- RecyclerView adapter for displaying tasks
- Supports different task states (important, overdue, completed)

## Screens and Navigation

1. **Home Screen**: Default view of active tasks
2. **Add/Edit Task Screen**: Allows creating and editing tasks
3. **Important Tasks**: View of high-priority tasks
4. **Overdue Tasks**: Tasks past their due date
5. **Completed Tasks**: Finished tasks

## Author

Munieb Awad Elsheikhidris Abdelrahman
Student ID: 101233250

## License

This project is licensed under the MIT License.

## Acknowledgments

- Swinburne University of Technology
- Course: COS30017 Software Development for Mobile Devices

## Future Improvements
- Implement task notifications
- Add task categorization
- Implement cloud sync
- Enhance UI/UX design
