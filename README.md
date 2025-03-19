## Build tools & versions used
Android Studio: Ladybug | 2024.2.1 Patch 3
Gradle: 8.9
Kotlin: 2.1.0
Android SDK (compileSdk): 35
Reference `gradle/libs.versions.toml` for more details


## Steps to run the app
1. On the command line navigate to this projet's root directory, then run:
2. `./gradlew assembleDebug`
3. `adb install -r app/build/outputs/apk/debug/app-debug.apk`

## What areas of the app did you focus on?
To build the MVP, I focused on the following areas:
1. MVVM Architecture: With a focus on separation of concerns and testability.
2. Data Fetching & Error Handling: Implemented a structured repository pattern using Retrofit and Flow.
3. Image Loading & Caching: Integrated Coil for optimized image rendering.
4. State Management: Used Kotlin Coroutines & Flow for real-time updates.
5. UI with Jetpack Compose: Built a modern UI using Composable functions.
6. Dependency Injection: Used Hilt for dependency injection.
7. Testing: Wrote unit tests for the repository and viewmodel.

## What was the reason for your focus? What problems were you trying to solve?
The problem statement and MVP necessitated this focus because I needed to:
1. Ensure API responses are handled correctly, preventing malformed/empty data from corrupting UI state.
2. Used the Coil library to optimize image loading and caching, as requested.
3. The Viewmodel set up ensured data wasn't fetched unless explicitly requested by the UI or on start up.

## How long did you spend on this project?
Approximately 1.5-2 hrs over 3 days.

## Did you make any trade-offs for this project? What would you have done differently with more time?
With more time, I would have:
1. Improved offline caching using Room or DataStore.
2. Made the UI more visually appealing.
3. Accounted for multiple device types with a two pane display for larger screens. 
4. If the API supported pagination and search for large datasets, I would have implemented it. 

## What do you think is the weakest part of your project?
The weakest part of the project is the lack of offline caching. This would have improved the user experience by allowing them to view the last fetched data when offline.

## Did you copy any code or dependencies? Please make sure to attribute them here!
Everything was fairly straightforward to implement from scratch. Reference `gradle/libs.versions.toml` for dependencies. 

## Is there any other information you’d like us to know?
I enjoyed working on this project and would love to hear your feedback. Thank you for the opportunity!