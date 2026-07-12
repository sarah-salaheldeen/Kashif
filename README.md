# Kashif (كاشف)

**Discover your city, your way.**

Kashif is a bilingual (Arabic/English) city discovery app for MENA cities, built with modern Android development practices. It helps users explore places around them through natural, mood-based search rather than rigid category filters — powered by real-world map data and AI.

> 🚧 **Status:** Actively in development. Core architecture, authentication, and navigation are in place; features are being built out incrementally.

---

## Overview

Kashif is designed as a portfolio-grade demonstration of production-level Android architecture, with a focus on:

- **Clean Architecture** with clear separation between data, domain, and presentation layers
- **MVI (Model-View-Intent)** for predictable, testable state management
- **Offline-first design**, with local caching so the app remains usable without a constant connection

## Features

- 🗺️ **Place discovery** for MENA cities using real-world map data via the [Overpass API](https://wiki.openstreetmap.org/wiki/Overpass_API) (OpenStreetMap)
- 🧠 **Mood-based search** — describe what you're in the mood for, powered by Gemini AI, instead of browsing fixed categories
- 🌐 **Full Arabic/English bilingual support**, including RTL layout handling
- 🔐 **Authentication** via Firebase Auth, supporting email/password and Google Sign-In (Credential Manager)
- 📦 **Offline caching** with Room, so previously discovered places are available without a connection
- 🧭 **Typed navigation** with Navigation Compose, using nested graphs for auth and main app flows

## Tech Stack

| Layer | Technology |
|---|---|
| UI | Jetpack Compose |
| Architecture | Clean Architecture, MVI |
| DI | Hilt |
| Async | Kotlin Coroutines & Flow |
| Local storage | Room |
| Auth & Backend | Firebase Auth, Firestore |
| Maps / Place data | Overpass API (OpenStreetMap) |
| AI | Gemini AI (mood-based search) |
| Navigation | Navigation Compose (typed, nested graphs) |

## Architecture

The app follows Clean Architecture principles across three layers:

- **Data** — repositories, remote/local data sources, DTOs and mappers
- **Domain** — use cases and core business models, framework-independent
- **Presentation** — Compose UI screens and MVI-based ViewModels (Intent → State → Effect)

Dependency injection is handled with Hilt throughout, and navigation uses typed, nested graphs to separate the authentication flow from the main app flow.

## Setup

1. Clone the repo
2. Add your own API keys to `local.properties` (this file is gitignored and not included in the repo):
   ```properties
   GEMINI_API_KEY=your_key_here
   MAPBOX_ACCESS_TOKEN=your_token_here
   ```
3. Add your own `google-services.json` from your Firebase project (a template/example may be provided separately — this file is not committed).
4. Build and run in Android Studio.

## Roadmap

- [ ] Integrate Gemini AI mood-based search
- [ ] Expand offline caching coverage
- [ ] Add unit and UI test coverage
- [ ] Proxy Gemini API calls through a backend before any production release (to avoid exposing the key client-side)

## Note on API Keys

This is a portfolio project under active development. Any third-party API keys used during development are kept out of version control via `.gitignore`. In a production release, sensitive keys (e.g. Gemini) should be proxied through a backend service rather than called directly from the client.

---

*Built by [Sarah Salaheldeen](https://github.com/sarah-salaheldeen) as a flagship portfolio project demonstrating end-to-end Android development across architecture, UI, backend integration, and AI.*
