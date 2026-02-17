# TuDefence Java → Kotlin 마이그레이션 실행 가이드

## 🚀 시작하기

### 준비물
- Android Studio (최신 버전)
- Git CLI
- Java 11+
- Gradle

### 환경 확인
```bash
cd D:\Lectures\2025_1\mig

# 현재 상태 확인
powershell -ExecutionPolicy Bypass -File migration_helper.ps1
```

---

## 📋 단계별 마이그레이션 실행

### 단계 0: Gradle 설정 업데이트 (준비)

#### 0-1. Kotlin 의존성 추가

**파일**: `gradle/libs.versions.toml`

현재 상태를 확인하고, Kotlin 버전을 추가합니다:

```toml
[versions]
# ... 기존 버전들 ...
kotlin = "2.0.0"
kotlinx-coroutines = "1.8.0"

[plugins]
# ... 기존 플러그인들 ...
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
kotlin-jvm = { id = "org.jetbrains.kotlin.jvm", version.ref = "kotlin" }
```

#### 0-2. 루트 build.gradle.kts 업데이트

**파일**: `TuDefence/build.gradle.kts`

```kotlin
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}
```

#### 0-3. app/build.gradle.kts 업데이트

**파일**: `TuDefence/app/build.gradle.kts`

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    // ... 기존 설정 ...
    
    kotlinOptions {
        jvmTarget = "11"
        languageVersion = "2.0"
    }
}

dependencies {
    // ... 기존 의존성 ...
    implementation(libs.kotlin.stdlib)
    // Kotlin Coroutines (선택사항)
    implementation(libs.kotlinx.coroutines.android)
}
```

#### 0-4. a2dg/build.gradle.kts 업데이트

**파일**: `TuDefence/a2dg/build.gradle.kts`

```kotlin
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    // ... 기존 설정 ...
    
    kotlinOptions {
        jvmTarget = "11"
        languageVersion = "2.0"
    }
}

dependencies {
    // ... 기존 의존성 ...
    implementation(libs.kotlin.stdlib)
}
```

**커밋**:
```bash
cd D:\Lectures\2025_1\mig
git add .
git commit -m "build(gradle): Add Kotlin plugin and dependencies"
git push origin main
```

---

### Phase 1: 게임 프레임워크 기초

#### Phase 1-1: 인터페이스 마이그레이션 (5개 파일)

**파일 목록**:
- `a2dg/src/main/java/.../framework/interfaces/IGameObject.java`
- `a2dg/src/main/java/.../framework/interfaces/ITouchable.java`
- `a2dg/src/main/java/.../framework/interfaces/IRecyclable.java`
- `a2dg/src/main/java/.../framework/interfaces/IBoxCollidable.java`
- `a2dg/src/main/java/.../framework/interfaces/ILayerProvider.java`

**실행 방법**:

1. **Android Studio에서 자동 변환**
   ```
   File > Open: D:\Lectures\2025_1\mig\TuDefence
   
   각 Java 파일을 우클릭
   → Code > Convert Java File to Kotlin
   ```

2. **또는 수동 마이그레이션**
   - Java 파일 내용을 복사
   - Kotlin 파일 생성 (`.kt` 확장자)
   - 문법 변환 및 최적화

3. **커밋**:
   ```bash
   cd D:\Lectures\2025_1\mig
   
   # Kotlin 파일 추가 및 Java 파일 제거
   git add TuDefence/a2dg/src/main/java/**/interfaces/*.kt
   git add TuDefence/a2dg/src/main/java/**/interfaces/*.java  # 삭제 등록
   
   # 또는
   git add TuDefence/a2dg/src/main/java -A
   
   git commit -m "refactor(a2dg): Migrate interfaces to Kotlin

   - IGameObject.java → IGameObject.kt
   - ITouchable.java → ITouchable.kt
   - IRecyclable.java → IRecyclable.kt
   - IBoxCollidable.java → IBoxCollidable.kt
   - ILayerProvider.java → ILayerProvider.kt

   Improvements:
   - Null-safety with non-nullable types
   - Kotlin interface syntax
   "
   
   git push origin main
   ```

4. **검증**:
   ```bash
   cd TuDefence
   .\gradlew.bat :a2dg:build
   # 성공: "BUILD SUCCESSFUL"
   ```

---

#### Phase 1-2: 유틸리티 마이그레이션 (3개 파일)

**파일 목록**:
- `a2dg/src/main/java/.../framework/util/RectUtil.java`
- `a2dg/src/main/java/.../framework/util/Gauge.java`
- `a2dg/src/main/java/.../framework/util/CollisionHelper.java`

**특이사항**: static 메서드 → Kotlin 확장함수로 변환 권장

**예시** (RectUtil.java):
```java
// Java
public class RectUtil {
    public static boolean contains(RectF rect, float x, float y) {
        return rect.contains(x, y);
    }
}

// Kotlin
fun RectF.contains(x: Float, y: Float): Boolean = this.contains(x, y)
// 또는 간단하게
fun RectF.containsPoint(x: Float, y: Float): Boolean = contains(x, y)
```

**커밋**:
```bash
git add TuDefence/a2dg/src/main/java/**/util/*.kt
git add TuDefence/a2dg/src/main/java/**/util/*.java
git commit -m "refactor(a2dg): Migrate utility classes to Kotlin

- RectUtil.java → RectUtil.kt (확장함수로 변환)
- Gauge.java → Gauge.kt
- CollisionHelper.java → CollisionHelper.kt

Improvements:
- Static methods → Extension functions
- Object singleton for constants
"
git push origin main
```

**검증**:
```bash
cd TuDefence
.\gradlew.bat :a2dg:build
```

---

#### Phase 1-3: 맵/리소스 마이그레이션 (6개 파일)

**파일 목록**:
- `a2dg/src/main/java/.../framework/map/Tileset.java`
- `a2dg/src/main/java/.../framework/map/TiledMap.java`
- `a2dg/src/main/java/.../framework/map/MapLayer.java`
- `a2dg/src/main/java/.../framework/map/Converter.java`
- `a2dg/src/main/java/.../framework/res/Sound.java`
- `a2dg/src/main/java/.../framework/res/BitmapPool.java`

**특이사항**: 데이터 클래스 활용

**예시**:
```kotlin
// Java
public class Tileset {
    private int id;
    private String name;
    public Tileset(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public int getId() { return id; }
    public String getName() { return name; }
}

// Kotlin
data class Tileset(
    val id: Int,
    val name: String
)
```

**커밋**:
```bash
git add TuDefence/a2dg/src/main/java/**/map/*.kt
git add TuDefence/a2dg/src/main/java/**/res/*.kt
git add TuDefence/a2dg/src/main/java/**/map/*.java
git add TuDefence/a2dg/src/main/java/**/res/*.java
git commit -m "refactor(a2dg): Migrate map and resource classes to Kotlin

- Tileset.java → Tileset.kt (data class)
- TiledMap.java → TiledMap.kt
- MapLayer.java → MapLayer.kt
- Converter.java → Converter.kt
- Sound.java → Sound.kt
- BitmapPool.java → BitmapPool.kt

Improvements:
- POJO → data class
- Collections → Kotlin Collections
- Null-safety
"
git push origin main
```

**검증**:
```bash
cd TuDefence
.\gradlew.bat :a2dg:build
.\gradlew.bat :a2dg:test
```

---

### Phase 2: 게임 엔진 코어

#### Phase 2-1: 뷰/메트릭스/씬/액티비티 마이그레이션 (4개 파일)

**파일 목록**:
- `a2dg/src/main/java/.../framework/view/Metrics.java`
- `a2dg/src/main/java/.../framework/view/GameView.java`
- `a2dg/src/main/java/.../framework/scene/Scene.java`
- `a2dg/src/main/java/.../framework/activity/GameActivity.java`

**특이사항**:
- Metrics: 싱글톤 → `object` 또는 `companion object`
- GameView: Canvas 작업 유지
- Scene: ArrayList → MutableList
- GameActivity: Android 라이프사이클 유지

**커밋**:
```bash
git add TuDefence/a2dg/src/main/java/**/view/*.kt
git add TuDefence/a2dg/src/main/java/**/scene/*.kt
git add TuDefence/a2dg/src/main/java/**/activity/*.kt
git add TuDefence/a2dg/src/main/java/**/view/*.java
git add TuDefence/a2dg/src/main/java/**/scene/*.java
git add TuDefence/a2dg/src/main/java/**/activity/*.java
git commit -m "refactor(a2dg): Migrate framework core to Kotlin

- Metrics.java → Metrics.kt (object singleton)
- GameView.java → GameView.kt
- Scene.java → Scene.kt (ArrayList → MutableList)
- GameActivity.java → GameActivity.kt

Improvements:
- Kotlin object for singletons
- Better collection handling
- Updated lifecycle callbacks
"
git push origin main
```

**검증**:
```bash
cd TuDefence
.\gradlew.bat :a2dg:build
.\gradlew.bat :a2dg:test
```

---

### Phase 3: 게임 오브젝트

#### Phase 3-1: 스프라이트 마이그레이션 (6개 파일)

**파일 목록**:
- `a2dg/src/main/java/.../framework/objects/Sprite.java`
- `a2dg/src/main/java/.../framework/objects/AnimSprite.java`
- `a2dg/src/main/java/.../framework/objects/SheetSprite.java`
- `a2dg/src/main/java/.../framework/objects/VertScrollBackground.java`
- `a2dg/src/main/java/.../framework/objects/HorzScrollBackground.java`
- `a2dg/src/main/java/.../framework/objects/TiledBackground.java`

**커밋**:
```bash
git add TuDefence/a2dg/src/main/java/**/objects/Sprite.kt
git add TuDefence/a2dg/src/main/java/**/objects/AnimSprite.kt
git add TuDefence/a2dg/src/main/java/**/objects/SheetSprite.kt
git add TuDefence/a2dg/src/main/java/**/objects/VertScrollBackground.kt
git add TuDefence/a2dg/src/main/java/**/objects/HorzScrollBackground.kt
git add TuDefence/a2dg/src/main/java/**/objects/TiledBackground.kt
git add TuDefence/a2dg/src/main/java/**/objects/Sprite.java
# ... 등 .java 파일들
git commit -m "refactor(a2dg): Migrate sprite classes to Kotlin"
git push origin main
```

---

#### Phase 3-2: 특수 오브젝트 마이그레이션 (3개 파일)

**파일 목록**:
- `a2dg/src/main/java/.../framework/objects/Score.java`
- `a2dg/src/main/java/.../framework/objects/JoyStick.java`
- `a2dg/src/main/java/.../framework/objects/Button.java`

**커밋**:
```bash
git add TuDefence/a2dg/src/main/java/**/objects/Score.kt
git add TuDefence/a2dg/src/main/java/**/objects/JoyStick.kt
git add TuDefence/a2dg/src/main/java/**/objects/Button.kt
git add TuDefence/a2dg/src/main/java/**/objects/Score.java
git add TuDefence/a2dg/src/main/java/**/objects/JoyStick.java
git add TuDefence/a2dg/src/main/java/**/objects/Button.java
git commit -m "refactor(a2dg): Migrate special game objects to Kotlin"
git push origin main
```

**검증**:
```bash
cd TuDefence
.\gradlew.bat :a2dg:build
.\gradlew.bat :a2dg:test
```

---

### Phase 4: 게임 로직 (app 모듈)

#### Phase 4-1: 메인 액티비티 마이그레이션 (2개 파일)

**파일 목록**:
- `app/src/main/java/.../app/MainActivity.java`
- `app/src/main/java/.../app/MainGameActivity.java`

**특이사항**: View Binding 유지

**커밋**:
```bash
git add TuDefence/app/src/main/java/**/app/MainActivity.kt
git add TuDefence/app/src/main/java/**/app/MainGameActivity.kt
git add TuDefence/app/src/main/java/**/app/MainActivity.java
git add TuDefence/app/src/main/java/**/app/MainGameActivity.java
git commit -m "refactor(app): Migrate main activities to Kotlin"
git push origin main
```

---

#### Phase 4-2: 게임 씬 마이그레이션 (2개 파일)

**파일 목록**:
- `app/src/main/java/.../game/scene/main/MainScene.java`
- `app/src/main/java/.../game/scene/pause/PauseScene.java`

**커밋**:
```bash
git add TuDefence/app/src/main/java/**/scene/**/*.kt
git add TuDefence/app/src/main/java/**/scene/**/*.java
git commit -m "refactor(app): Migrate game scenes to Kotlin"
git push origin main
```

---

#### Phase 4-3: 게임 오브젝트 마이그레이션 (4개 파일)

**파일 목록**:
- `app/src/main/java/.../game/scene/main/Cannon.java`
- `app/src/main/java/.../game/scene/main/Shell.java`
- `app/src/main/java/.../game/scene/main/Fly.java`
- `app/src/main/java/.../game/scene/main/Explosion.java`

**커밋**:
```bash
git add TuDefence/app/src/main/java/**/scene/main/Cannon.kt
git add TuDefence/app/src/main/java/**/scene/main/Shell.kt
git add TuDefence/app/src/main/java/**/scene/main/Fly.kt
git add TuDefence/app/src/main/java/**/scene/main/Explosion.kt
git add TuDefence/app/src/main/java/**/scene/main/*.java
git commit -m "refactor(app): Migrate game objects to Kotlin"
git push origin main
```

---

#### Phase 4-4: 게임 유틸리티 마이그레이션 (3개 파일)

**파일 목록**:
- `app/src/main/java/.../game/scene/main/WaveGen.java`
- `app/src/main/java/.../game/scene/main/MapSelector.java`
- `app/src/main/java/.../game/scene/main/DesertMapBg.java`

**커밋**:
```bash
git add TuDefence/app/src/main/java/**/scene/main/WaveGen.kt
git add TuDefence/app/src/main/java/**/scene/main/MapSelector.kt
git add TuDefence/app/src/main/java/**/scene/main/DesertMapBg.kt
git add TuDefence/app/src/main/java/**/scene/main/*.java
git commit -m "refactor(app): Migrate game utilities to Kotlin"
git push origin main
```

**검증**:
```bash
cd TuDefence
.\gradlew.bat :app:build
.\gradlew.bat :app:connectedAndroidTest
```

---

## ✅ 최종 정리

### Java 파일 삭제 및 정리

```bash
cd D:\Lectures\2025_1\mig

# 모든 Java 파일 확인
Get-ChildItem -Path TuDefence -Recurse -Filter "*.java" | 
  Where-Object { $_.FullName -match "src\\(main|test)" -and $_.FullName -notmatch "\\build\\" } |
  Select-Object FullName

# 확인 후 삭제 (또는 git rm으로 제거)
git add TuDefence -A
git commit -m "refactor: Remove Java files after Kotlin migration"
git push origin main
```

### 최종 검증

```bash
cd TuDefence

# 전체 빌드
.\gradlew.bat clean build

# 모든 테스트 실행
.\gradlew.bat test

# 에뮬레이터에서 테스트 (선택사항)
.\gradlew.bat connectedAndroidTest
```

---

## 📊 진행 상황 확인

### PowerShell로 진행 상황 모니터링

```powershell
cd D:\Lectures\2025_1\mig
powershell -ExecutionPolicy Bypass -File migration_helper.ps1 --progress
```

### Git 히스토리 확인

```bash
git log --oneline --graph -15
```

---

## 🔧 문제 해결

### "Convert Java File to Kotlin" 옵션이 없음
- Android Studio 메뉴: `Code` 또는 `Tools` 확인
- 또는 우클릭 > Refactor > Convert Java File to Kotlin

### 빌드 오류 발생
```bash
# 캐시 정리
cd TuDefence
.\gradlew.bat clean

# 다시 빌드
.\gradlew.bat build
```

### Kotlin 컴파일 오류
- `build.gradle.kts`에서 `kotlinOptions` 확인
- `jvmTarget = "11"` 설정 확인

### Git 충돌 발생
```bash
# 현재 상태 확인
git status

# 충돌 해결 후
git add .
git commit -m "Resolve merge conflict"
git push origin main
```

---

## 📚 참고 자료

- [Kotlin 공식 문서](https://kotlinlang.org/docs/)
- [Android Kotlin 가이드](https://developer.android.com/kotlin)
- [Conventional Commits](https://www.conventionalcommits.org/)

---

**작성일**: 2025-02-17
**프로젝트**: TuDefence
**상태**: 마이그레이션 가이드 완성

