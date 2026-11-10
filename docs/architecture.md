# Architecture and request flows

## System design

```mermaid
flowchart TD
    A[Android app] -->|JSON over HTTPS| B[Spring Boot REST API]
    B --> C[District and outlet catalog]
    B --> D[Validated price snapshot]
    E[BEVCO outlet directory] -->|Verify addresses and pins| C
    F[BEVCO price documents] -->|Validate retail rows| D
    A -->|Directions link| G[Google Maps]
    A -->|Official price link| F
```

**Day 1 implementation:** a Java 21 Spring Boot app is built with Maven. Spring MVC routes `GET /api/v1/districts` to `DistrictController`. `DistrictService` loads the bundled district JSON at startup. The first API needs no external service or database at runtime.

**Target:** one Java backend serves catalog data and computes straight-line distance. The Android app handles screens, location consent, and external links. Maintain outlet and price snapshots separately so a BEVCO website delay does not slow ordinary API requests. Start with versioned local snapshots. Evaluate a database when updates or search requirements justify one.

**Data sources:** Kerala’s Civil Supplies department publishes the district list. BEVCO’s FL-1 directory is the source for outlet names and addresses. Confirm map pins separately if the directory entry only gives an address. Use the BEVCO Price List and Documents/Reports pages to identify the current price documents. The app displays source and effective date where available.

[Kerala districts](https://epos.kerala.gov.in/) · [BEVCO outlet directory](https://bevco.in/shop-locations/) · [BEVCO price list](https://bevco.in/price-list-2/) · [BEVCO Documents/Reports](https://bevco.in/documents-reports/)

## Browse districts, select an outlet, and open directions

```mermaid
sequenceDiagram
    actor U as User
    participant A as Android app
    participant B as Java API
    participant M as Maps or browser
    U->>A: Open Home
    A->>B: GET /api/v1/districts
    B-->>A: 14 districts
    A-->>U: Show district list
    U->>A: Select Ernakulam
    A->>B: GET /api/v1/districts/ernakulam/outlets
    B-->>A: Outlets with addresses and directions URLs
    A-->>U: Show outlet cards
    U->>A: Tap Directions
    A->>M: Open the selected outlet's directions URL
```

## Find the nearest outlet

```mermaid
sequenceDiagram
    actor U as User
    participant A as Android app
    participant L as Android location
    participant B as Java API
    U->>A: Tap Find nearest outlet
    A->>L: Request foreground location and current fix
    alt Location available
        L-->>A: Latitude and longitude
        A->>B: POST /api/v1/outlets/nearest
        B->>B: Compare verified outlet coordinates
        B-->>A: Outlet, straight-line distance, and coverage
        A-->>U: Show result and Directions button
    else Denied or unavailable
        L-->>A: Permission or location unavailable
        A-->>U: Offer district browsing and location retry
    end
```

Coordinates are used for the lookup and excluded from application logs and storage. Show the result as an estimate when location is approximate. If only part of the catalog has verified pins, label the result “Nearest verified outlet.” Directions come from Google Maps. The API distance is straight-line distance.

[Android location permissions](https://developer.android.com/develop/sensors-and-location/location/permissions/runtime) · [Google Maps URLs](https://developers.google.com/maps/documentation/urls/get-started)

## View prices

```mermaid
sequenceDiagram
    actor U as User
    participant A as Android app
    participant B as Java API
    participant P as Official price page
    U->>A: Tap Price list
    A->>B: GET /api/v1/prices
    B-->>A: Snapshot results and official source URL
    alt Verified retail rows available
        A-->>U: Show searchable price list and source dates
    else Source link only or request failed
        A-->>U: Show official price-list link
    end
    opt User opens official list
        U->>A: Tap Open official price list
        A->>P: Open link in browser
    end
```
