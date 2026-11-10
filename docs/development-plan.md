# Development plan

Complete and test one backend API at a time. Then build the Android screens against the agreed contracts and integrate each feature. The days are milestones you can work through at your pace.

| Day | Build | Ready to move on when |
| --- | --- | --- |
| **1 · Districts** | `GET /api/v1/districts`. Run the Java 21 Spring Boot server with Maven and follow the request through the controller, service, and JSON response. | `mvn test` passes. The API returns 14 unique districts in order, and the example invalid path and method return useful errors. |
| **2 · Outlets** | `GET /api/v1/districts/{districtId}/outlets`. Start with Ernakulam to test the flow, then validate the other 13 districts against BEVCO’s directory. Build the Directions URL from verified coordinates or the full address. | An invalid district returns `404`. Every district has been checked against the directory. Verify that sample directions open the intended outlet. |
| **3 · Nearest outlet** | `POST /api/v1/outlets/nearest`. Verify coordinates, calculate straight-line distance in kilometres with the Haversine formula, and return the nearest outlet in the verified set. | Check known points and ties, both coordinate bounds, and the response when no verified coordinates are available. Show partial coverage when applicable. |
| **4 · Prices** | `GET /api/v1/prices`. Establish the official source link first. Confirm source availability and reuse terms, then add validated, searchable retail rows when available. Keep the last good snapshot on import failure and show its dates. | Verify the published FL-1 retail column, effective dates, units, and the official link fallback. |
| **5 · Android screens** | Create a native Android project in Android Studio with Java and XML layouts. Use fixtures to implement Home, Outlets, Nearest Outlet, and Price List screens. | Navigation, larger text, loading, empty, and retry states work in the emulator. |
| **6 · District and outlet integration** | Replace the fixtures with the first two APIs. Wire Directions to Google Maps or the browser. | Select Ernakulam on an emulator and a phone, see real outlet data, then open the selected outlet’s directions. |
| **7 · Location and price integration** | Add foreground location permission, connect the nearest API, then connect the price API and open the official list from the app. | Test permission denial, approximate location, timeout, no coordinates, source-only pricing, and available price rows. |
| **8 · APK** | Configure the backend URL for the build, generate an APK, install it on Android, and walk through all three user journeys. | A clean install loads districts and outlets, opens Directions, finds the nearest verified outlet with permission, and opens the price list. Provide the APK and install steps. |

## APK and backend setup

- **Emulator:** point the debug build at the development machine through `10.0.2.2`. Use the documented emulator network alias, rather than the emulator’s own loopback address.
- **Physical phone:** for private testing, use the development machine’s LAN IP on the same Wi-Fi. Bind the server to the LAN (for example, `BEVCO_HOST=0.0.0.0`) and allow the port through the local firewall. Allow HTTP only in the debug build when needed. Stop the development server when done testing.
- **Use away from home Wi-Fi:** the APK needs a reachable HTTPS backend. The backend host and URL are a Day 8 setup decision. The user must be able to browse after installing the APK in the intended environment.
- **Install:** use Android Studio’s APK build action for a debug APK, then install it via `adb install` or copy it to the phone and open it. If distributing a release build, use a private release signing key and an HTTPS API URL. Keep the key out of source control.

Official Android references: [Emulator networking](https://developer.android.com/studio/run/emulator-networking), [Build an APK](https://developer.android.com/studio/run#build-menu), [App signing](https://developer.android.com/studio/publish/app-signing).
