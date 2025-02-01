# Lightning Network Monitor

Kotlin app built with Jetpack Compose designed to track and display the top 100 most connected nodes in the Lightning Network.

## Key Features

- **MVI Architecture**: Unidirectional data flow with states and intents.
- **Coroutines + Flow**: Efficient concurrency management.
- **LeakCanary**: Memory leak detection.
- **Timber**: Advanced logging and monitoring.

## Technologies Used

- Kotlin
- Jetpack Compose
- Hilt
- Retrofit
- Coroutines + Flow
- LeakCanary
- Timber

## Branch Convention

We follow a structured branch naming convention to organize our development workflow. Here are the main types of branches:

- **`main`**: Stable and production-ready code.
- **`develop`**: Integration branch for new features.
- **`feat/`**: New features.
- **`fix/`**: Bug fixes.
- **`refactor/`**: Code refactoring.
- **`config/`**: Configuration changes.
- **`docs/`**: Documentation updates.
- **`test/`**: Test development.
- **`chore/`**: Maintenance tasks.
- **`style/`**: Code formatting changes.
- **`ci/`**: Continuous integration changes.
- **`hotfix/`**: Critical bug fixes.
- **`release/`**: Release preparation.

### Examples

- `feat/add-pull-to-refresh`
- `fix/handle-null-values`
- `refactor/simplify-state-management`
- `config/update-hilt-version`
- `docs/update-readme-instructions`
- `test/add-unit-tests-for-error-states`
- `chore/update-timber-dependency`
- `style/format-code-with-ktlint`
- `ci/add-github-actions-workflow`
- `hotfix/fix-critical-bug-in-login`
- `release/v1.2.0`

## Commit Convention

We follow the Conventional Commits specification. Here are some examples:

- `feat(ui): add new feature`
- `fix(repository): handle null values`
- `refactor(viewmodel): simplify state management`
- `config(build): update dependencies`
- `docs(readme): add instructions`
- `test(viewmodel): add unit tests`
- `chore: update dependencies`
- `style: format code`
- `ci: add GitHub Actions workflow`

For more details, refer to [Conventional Commits](https://www.conventionalcommits.org/).

## How to Run

1. Clone the repository.
2. Open the project in Android Studio.
3. Build and run the app on an emulator or physical device.

## Running Tests

To run the unit tests, use the following command:

```bash
./gradlew test
