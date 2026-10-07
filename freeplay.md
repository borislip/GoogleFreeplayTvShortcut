# LauncherX FreePlay deeplinks

Package: `com.google.android.apps.tv.launcherx` (Google TV Home / "LauncherX")
Component: `.gateway.LauncherDeepLink` (alias for `com.google.apps.tiktok.nav.gateway.GatewayActivity`)

Discovered by pulling `dumpsys package com.google.android.apps.tv.launcherx`,
decompiling `base.apk`, and confirming live on-device via `adb` + logcat
(tag `LauncherGatewayHandler` throws `IllegalArgumentException: Invalid
Freeplay deeplink` on bad input, which is handy for probing formats).

## Scheme

```
https://tv.google.com/freeplay/...
```

`http://` also matches the intent-filter, but use `https://`. Always pass
`-p com.google.android.apps.tv.launcherx` explicitly — without it the system
may route the VIEW intent to Chrome instead of the launcher, since App Links
auto-verification for `tv.google.com` can show as "Disabled"
(`adb shell dumpsys package com.google.android.apps.tv.launcherx` ->
"Domain verification status").

## Known-good forms

| Path | Meaning | Status |
|---|---|---|
| `/freeplay/default` | Open the default FreePlay entry point | Confirmed working |
| `/freeplay?channel=<id>` | Tune a specific channel via query param | Confirmed working (started playback even with a made-up id) |
| `/freeplay/<freePlayDeeplinkId>` | Tune a specific channel via path segment | Format accepted; needs a real id from the local EPG DB (`EpgChannelEntry.freePlayDeeplinkId`) — a bogus id logs `FreeplayLivePlaybackModel: Asset key not found for deeplinkId: [...]` |

## Known-bad forms (for reference)

- `/freeplay/` (no id/segment after it) → `Invalid Freeplay deeplink`
- `/freeplay/channel/<id>` → `Invalid Freeplay deeplink`

## Sample commands

Open the default FreePlay page:

```bash
adb shell am start -a android.intent.action.VIEW \
  -d "https://tv.google.com/freeplay/default" \
  -p com.google.android.apps.tv.launcherx
```

Open a specific channel (replace `<CHANNEL_ID>`):

```bash
adb shell am start -a android.intent.action.VIEW \
  -d "https://tv.google.com/freeplay?channel=<CHANNEL_ID>" \
  -p com.google.android.apps.tv.launcherx
```

## Finding real channel IDs

Not yet pulled. The IDs live in the app's local Room DB, table
`EpgChannelEntry`, column `freePlayDeeplinkId` (keyed alongside `channelId`,
`distributorId`). Would need `run-as` (if the app is debuggable) or root to
read `/data/user/0/com.google.android.apps.tv.launcherx/databases/`.

## Other registered deeplink paths (same `tv.google.com` host, for context)

- `/purchase/`, `/show/`, `/entitymenu/`, `/destination/`,
  `/feature_awareness/`, `/news_briefing`, `/jumpstart`, `/gemini`,
  `/briefs`, `/aiquotaexceeded`, `/learnhub`, `/sponsoredsportsbriefs/`
  (all via `.gateway.LauncherDeepLink`)
- `/entity/`, `/asset/`, `/person/`, `/game/` (via `.entity.EntityActivity`)
- `/page/` (via `PageDeepLink`)
- `/aicreation` (via `.gateway.AiCreationDeepLink`)
