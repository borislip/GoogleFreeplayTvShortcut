# Google Free Play TV shortcut

A tiny, invisible-UI Android launcher shortcut. Tapping its icon fires:

```
Intent(ACTION_VIEW, "https://tv.google.com/freeplay/default")
  .setPackage("com.google.android.apps.tv.launcherx")
```

which opens the default FreePlay page in Google's own TV launcher
(`com.google.android.apps.tv.launcherx`, used on Chromecast with Google TV /
Google Streamer boxes). It then immediately finishes — no UI of its own —
so it's meant to be pinned/launched from a third-party Android TV launcher
that doesn't otherwise expose a way to jump straight to FreePlay.

It does not contain or redistribute any of Google's branding; the icon is a
generic TV/play glyph.

## Install

Grab `app-debug.apk` from the latest [GitHub Actions run](../../actions) (or
a [Release](../../releases) if one exists) and sideload it:

```bash
adb install -r app-debug.apk
```

Requires a device that already has `com.google.android.apps.tv.launcherx`
installed (Chromecast with Google TV, Google Streamer, and similar boxes).

See [`freeplay.md`](freeplay.md) for how the `tv.google.com/freeplay/...`
deeplink scheme was reverse engineered, including other valid/invalid forms.

## Build

No local Android SDK needed — GitHub Actions builds the APK on every push
to `main` via `.github/workflows/build.yml` and uploads it as a workflow
artifact (`gradle assembleDebug`, debug-signed).

## Signed release build

`.github/workflows/release.yml` is manually triggered (Actions tab → "Release
APK" → Run workflow, pick a version name) and produces a release-signed APK
attached to a GitHub Release. It needs these repo secrets (Settings →
Secrets and variables → Actions):

| Secret | Value |
|---|---|
| `KEYSTORE_BASE64` | base64 of the release `.jks` keystore file |
| `KEYSTORE_PASSWORD` | the keystore password |
| `KEY_ALIAS` | the key alias inside the keystore |
| `KEY_PASSWORD` | the key password (same as the store password for a PKCS12 keystore) |

The keystore itself is never committed to this repo — only its base64 lives
in GitHub's encrypted secrets store.
