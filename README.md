# WiiHope Native

Android native version of WiiHope built with Kotlin, Jetpack Compose, Firebase, FCM, and Media3.

## Build without Android Studio

```powershell
.\gradlew.bat assembleDebug
```

The debug APK is generated at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Current foundation

- Jetpack Compose UI with bottom navigation.
- Native Media3 `MediaSessionService` for background audio.
- System media controls through Android's media session.
- Bible audio catalog ported from the Flutter project.
- Music loaded from Firestore collection `wimusica`.
- Firebase Cloud Messaging service prepared for app notifications.

## Next work

- Add Firebase Auth screens and user profile.
- Persist favorites and playback state with DataStore or Room.
- Add richer media notification artwork and Android Auto-ready metadata.
- Harden Firestore rules before production.
