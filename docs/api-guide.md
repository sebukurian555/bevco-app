# API guide

Base URL for Day 1 is `http://127.0.0.1:8080`. Clients use `/api/v1` routes and JSON. [OpenAPI contract](openapi.yaml)

| Day | Method and route | Success | Other outcomes |
| --- | --- | --- | --- |
| 1 · Implemented | `GET /api/v1/districts` | `200` with the `districts` array sorted by name | `405` with `Allow: GET` for `POST` on the same path |
| 2 · Planned | `GET /api/v1/districts/{districtId}/outlets` | `200` with `districtId` and `outlets`. A known district with no rows returns an empty array. | `404 DISTRICT_NOT_FOUND` for an unknown district ID |
| 3 · Planned | `POST /api/v1/outlets/nearest` | `200` with `outlet`, `distanceKm`, and `coverage` | `400 INVALID_LOCATION` or `503 OUTLET_COORDINATES_UNAVAILABLE` |
| 4 · Planned | `GET /api/v1/prices?q=&limit=25&offset=0` | `200` with `catalogStatus`, `items`, `total`, `sources`, and `officialUrl` | `400 INVALID_QUERY` for invalid paging or search inputs |

## Districts

`GET /api/v1/districts` returns this shape (excerpt):

```json
{
  "districts": [
    { "id": "alappuzha", "name": "Alappuzha" },
    { "id": "ernakulam", "name": "Ernakulam" }
  ]
}
```

Use the stable lowercase `id` in subsequent requests. For example, the Ernakulam outlets path ends in `/districts/ernakulam/outlets`.

Spring MVC also provides `HEAD` and `OPTIONS` handling for this route. `HEAD` returns headers without the JSON body.

## Outlet browsing and directions

Each outlet includes `id`, `districtId`, `name`, `address`, and `directionsUrl`. Return `latitude` and `longitude` together when the pin is verified. Sort outlet rows by name, then ID. Use the official outlet number as the stable ID when the verified source provides one. Keep it a string to preserve leading zeroes.

Build the Directions link as `https://www.google.com/maps/dir/?api=1&destination=<encoded destination>` with the verified pin or full address. Let Maps determine the origin. [Google Maps Directions URLs](https://developers.google.com/maps/documentation/urls/get-started#directions-action)

## Nearest outlet

`POST /api/v1/outlets/nearest` accepts a JSON body. This request uses example coordinates:

```json
{ "latitude": 9.9816, "longitude": 76.2999 }
```

Latitude must be in `[-90, 90]` and longitude in `[-180, 180]`. Compare against verified pins across all districts. Break equal-distance ties by outlet ID and round `distanceKm` only after choosing the nearest candidate. `coverage` shows how many catalog entries were included. For example, `{ "verifiedOutlets": 48, "catalogOutlets": 60 }` means 12 entries were excluded from the lookup. The numbers here are illustrative.

## Price list

When `catalogStatus` is `READY`, each row has `itemCode`, `name`, `sizeMl`, `retailPricePaise`, and `sourceId`. Use the FL-1 retail column, which is what a user viewing prices for a shop needs. Display `retailPricePaise` as rupees in the app. The `sources` array links the row to its BEVCO document and effective date.

`q` is an optional case-insensitive name search. `limit` defaults to 25 and accepts 1–100. `offset` defaults to 0. Return `total` before pagination. Sort by name, then bottle size, then item code, then source ID for consistent pages. When `catalogStatus` is `SOURCE_ONLY`, return `items: []`, `total: 0`, and the official link. A `READY` response with no matching rows means the search returned no results.

## Error response

Errors use the same two fields. The app can use `code` to decide which action to show.

```json
{
  "code": "NOT_FOUND",
  "message": "No endpoint matches this path."
}
```
