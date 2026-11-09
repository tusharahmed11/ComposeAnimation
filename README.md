# ⚡ Compose Motion Lab

**Compose Motion Lab** is an interactive, real-time physics and motion showcase built **100% with pure Jetpack Compose** and **Kotlin Coroutines** (zero third-party animation libraries).

Designed with a **Deep Obsidian & Cyber-Neon** dark UI, the app serves as both a visual reference and a hands-on laboratory for exploring every layer of the Jetpack Compose Animation system—from high-level declarative layout transitions down to nanosecond `withFrameNanos` frame-clock loops.

---

## ✨ Key Highlights

- **⏱️ Global Time-Scale Controller (`0.2x` → `2.0x`)**: Powered by `CompositionLocalProvider(LocalAnimationSpeed)`, allowing you to slow down any spring oscillation, easing curve, or layout morph to `0.2x` slow-motion to inspect every frame.
- **🧭 4-Tab Custom Animated Navigation**: Divided into 4 top-level categories (**Layouts**, **Values**, **Machines**, **Physics**) with custom `Canvas` vector icons and a 2-segment sub-module switcher per tab.
- **📐 Blueprint Motion Stages**: Every animation runs inside a technical dot-grid stage with live monospace telemetry strips (`Position`, `Velocity`, `Angle`, `Frame Δt`) and unclipped overshoot zones.

---

## 🧪 Interactive Modules (8 Sections • 24+ Live Demos)

### 1. Layouts (`Layouts` Tab)
- **Visibility & Morph (`HighLevelLayoutsScreen.kt`)**
  - `AnimatedVisibility` matrix with 5 preset combinations (`Fade + Scale`, `Vertical Slide`, `Horizontal Slide`, `2D Diagonal`, and staggered child `animateEnterExit` cascades) paired with live `Spring` / `Tween` / `Keyframes` / `Snap` spec switching.
  - `AnimatedContent` directional financial stock ticker roll and a 4-step checkout wizard using `SizeTransform` and animated step progress connectors.
  - `Crossfade` state dissolver between Live GPU Telemetry, an animated Shimmer Skeleton, and an Empty State.
  - `Modifier.animateContentSize` accordion text expansion and interactive tag pill cloud with configurable alignment (`TopStart`, `Center`, `BottomEnd`).
- **Shared & Lookahead (`LookaheadAndSharedScreen.kt`)**
  - `SharedTransitionLayout` master-detail view interpolating `sharedElement` (hero icon) and `sharedBounds` (title & price badge) across distinct hierarchy trees.
  - `LookaheadScope` + `Modifier.animateBounds` pre-calculated layout choreography morphing 3 sibling nodes between a vertical stack and a 3-column Bento grid.

### 2. Values & Specs (`Values` Tab)
- **State Values (`ValueAnimationsScreen.kt`)**
  - `animateFloatAsState` & `animateDpAsState` 3D perspective card (`rotationX`, `rotationZ`, `cameraDistance`, `scale`, `alpha`, dynamic corner radius, and elevation).
  - `animateColorAsState`, `animateOffsetAsState`, `animateSizeAsState`, and `animateRectAsState` 2D vector kinematics.
  - `animateIntAsState`, `animateIntOffsetAsState`, and `animateIntSizeAsState` discrete pixel-exact interpolation.
  - Custom `TwoWayConverter<MyColorProfile, AnimationVector4D>` interpolating 4 simultaneous channels (`R`, `G`, `B`, `Luminescence`) with live progress bars.
- **Spec Gallery (`AnimationSpecGalleryScreen.kt`)**
  - **Spring Physics Gallery**: Side-by-side comparison across all 4 damping ratios (`HighBouncy ζ=0.2`, `MediumBouncy ζ=0.5`, `LowBouncy ζ=0.75`, `NoBouncy ζ=1.0`) and 4 stiffness tiers (`VeryLow` to `High`) with unclipped overshoot tracks.
  - **Tween & Easing Curves Race**: 5-lane race comparing `FastOutSlowIn`, `LinearOutSlowIn`, `FastOutLinearIn`, `LinearEasing`, and an overshooting `CubicBezierEasing(0.34f, 1.56f, 0.64f, 1.0f)`.
  - **Keyframes, Repeatable & Snap**: Multi-waypoint path with mid-flight pullback, finite reverse loop iterations, and delayed instant step jumps.

### 3. State Machines & Loops (`Machines` Tab)
- **State Machines (`StateMachinesScreen.kt`)**
  - `updateTransition` multi-state media controller (`Idle`, `Buffering`, `Playing`, `Paused`) synchronizing size, corner geometry, color, and custom `Canvas` vector iconography.
  - `Transition.createChildTransition` hierarchical state machine coupling parent card expansion with a bouncy child checkmark pop.
  - `Transition.AnimatedVisibility` & `Transition.AnimatedContent` extensions driven by a single shared `Transition` instance.
- **Infinite Loops (`InfiniteAnimationsScreen.kt`)**
  - **Radar Beacon & Sweep**: 3-phase expanding radial waves with harmonic alpha decay and a continuous 360° polar scanner sweep via `rememberInfiniteTransition`.
  - **Shimmer Sweep Engine**: Diagonal `Brush.linearGradient` translation across skeleton UI cards.
  - **Glowing Breathing Core**: Bi-directional color, stroke width, and scale oscillation using `RepeatMode.Reverse`.

### 4. Physics & Low-Level Engines (`Physics` Tab)
- **Touch Gestures (`GesturesPhysicsScreen.kt`)**
  - **Swipe-to-Dismiss Physics**: `pointerInput` + `VelocityTracker` horizontal drag with tilt, scale, threshold indicators, directional fling dismissal, and bouncy spring snapback.
  - **AnchoredDraggable Bottom Sheet**: Interactive 3-anchor (`COLLAPSED`, `HALF_EXPANDED`, `FULLY_EXPANDED`) vertical sheet powered by `AnchoredDraggableState` and `DraggableAnchors`.
- **Coroutines & Clock (`LowLevelEnginesScreen.kt`)**
  - **Imperative `Animatable` Engine**: Coroutine sequential chaining, instant `snapTo`, hard `updateBounds` clamping, and mid-flight velocity handoff/diversion.
  - **`TargetBasedAnimation` Nano Clock**: Explicit `withFrameNanos` vsync loop evaluating animation vectors on every display tick with live frame delta (`Δt ms`) telemetry.
  - **`DecayAnimation` Ballistics**: Targetless rotational roulette wheel comparing `exponentialDecay` and `splineBasedDecay` from initial fling velocities.

---

## 🗂️ Project Structure

```text
app/src/main/java/com/imtusharahmed/composeanimation/
├── MainActivity.kt                      # Edge-to-edge entry point
└── ui/
    ├── common/
    │   └── MotionLabControls.kt         # MotionCard, MotionStage, TelemetryStrip, SpeedMultiplierBar
    ├── main/
    │   └── ComposeMotionLabApp.kt       # Scaffold, BottomNav, Collapsible Speed Drawer, Sub-section Switcher
    ├── screens/
    │   ├── layouts/
    │   │   └── HighLevelLayoutsScreen.kt
    │   ├── shared/
    │   │   └── LookaheadAndSharedScreen.kt
    │   ├── values/
    │   │   └── ValueAnimationsScreen.kt
    │   ├── specs/
    │   │   └── AnimationSpecGalleryScreen.kt
    │   ├── transitions/
    │   │   └── StateMachinesScreen.kt
    │   ├── infinite/
    │   │   └── InfiniteAnimationsScreen.kt
    │   ├── physics/
    │   │   └── GesturesPhysicsScreen.kt
    │   └── lowlevel/
    │       └── LowLevelEnginesScreen.kt
    └── theme/
        ├── Color.kt                     # Deep Obsidian & Cyber-Neon palette tokens
        ├── Theme.kt                     # Material 3 dark color scheme & system bar configuration
        └── Type.kt                      # Typography definitions
```

---

## 🛠️ Tech Stack & Requirements

- **Language**: Kotlin `2.2+`
- **UI Framework**: Jetpack Compose (`Compose BOM 2026.02.01`, Material 3, Compose Animation & Foundation)
- **Min SDK**: `29` (Android 10)
- **Target / Compile SDK**: `37`

---

## 🚀 Getting Started

1. **Clone the repository**
   ```bash
   git clone https://github.com/imtusharahmed/ComposeAnimation.git
   cd ComposeAnimation
   ```

2. **Build & Install Debug APK via Gradle**
   ```bash
   ./gradlew assembleDebug
   ```
   Or on Windows PowerShell:
   ```powershell
   .\gradlew.bat installDebug
   ```

3. **Open in Android Studio**
   Open the root folder in Android Studio and run the `app` configuration on any Android device or emulator (enable 90Hz/120Hz display refresh rate if available for the smoothest spring physics).

