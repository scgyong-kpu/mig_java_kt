# TuDefence Java → Kotlin 마이그레이션 Git 커밋 전략

## 📋 개요

마이그레이션을 단계별로 나누어 각 Phase마다 의미 있는 git commit을 생성합니다.
이를 통해 언제든 이전 상태로 롤백할 수 있고, 각 단계의 진행 상황을 명확히 추적할 수 있습니다.

## 🔄 커밋 계획

### 준비 단계 (Setup Phase)
```
1. Gradle 설정 업데이트
   commit: "build(gradle): Add Kotlin plugin and dependencies"
   - build.gradle.kts (루트)
   - app/build.gradle.kts
   - a2dg/build.gradle.kts
   - gradle/libs.versions.toml

2. 소스 폴더 구조 생성
   commit: "refactor: Create Kotlin source directories"
   - a2dg/src/main/kotlin/
   - a2dg/src/test/kotlin/
   - app/src/main/kotlin/
   - app/src/test/kotlin/
```

### Phase 1: 게임 프레임워크 기초 (a2dg 라이브러리)

#### Step 1-1: 인터페이스 마이그레이션
```
commit: "refactor(a2dg): Migrate interfaces to Kotlin"
files:
  - IGameObject.java → IGameObject.kt
  - ITouchable.java → ITouchable.kt
  - IRecyclable.java → IRecyclable.kt
  - IBoxCollidable.java → IBoxCollidable.kt
  - ILayerProvider.java → ILayerProvider.kt
```

#### Step 1-2: 유틸리티 마이그레이션
```
commit: "refactor(a2dg): Migrate utility classes to Kotlin"
files:
  - RectUtil.java → RectUtil.kt
  - Gauge.java → Gauge.kt
  - CollisionHelper.java → CollisionHelper.kt
```

#### Step 1-3: 맵/리소스 마이그레이션
```
commit: "refactor(a2dg): Migrate map and resource classes to Kotlin"
files:
  - Tileset.java → Tileset.kt
  - TiledMap.java → TiledMap.kt
  - MapLayer.java → MapLayer.kt
  - Converter.java → Converter.kt
  - Sound.java → Sound.kt
  - BitmapPool.java → BitmapPool.kt

test: ./gradlew :a2dg:build
```

### Phase 2: 게임 엔진 코어 (a2dg 라이브러리)

#### Step 2-1: 뷰/메트릭스/씬/액티비티 마이그레이션
```
commit: "refactor(a2dg): Migrate framework core (View, Metrics, Scene, Activity) to Kotlin"
files:
  - Metrics.java → Metrics.kt
  - GameView.java → GameView.kt
  - Scene.java → Scene.kt
  - GameActivity.java → GameActivity.kt

test: ./gradlew :a2dg:build
      ./gradlew :a2dg:test
```

### Phase 3: 게임 오브젝트 (a2dg 라이브러리)

#### Step 3-1: 기본 스프라이트 마이그레이션
```
commit: "refactor(a2dg): Migrate sprite classes to Kotlin"
files:
  - Sprite.java → Sprite.kt
  - AnimSprite.java → AnimSprite.kt
  - SheetSprite.java → SheetSprite.kt
  - VertScrollBackground.java → VertScrollBackground.kt
  - HorzScrollBackground.java → HorzScrollBackground.kt
  - TiledBackground.java → TiledBackground.kt
```

#### Step 3-2: 특수 오브젝트 마이그레이션
```
commit: "refactor(a2dg): Migrate special game objects to Kotlin"
files:
  - Score.java → Score.kt
  - JoyStick.java → JoyStick.kt
  - Button.java → Button.kt

test: ./gradlew :a2dg:build
      ./gradlew :a2dg:test
```

### Phase 4: 게임 로직 (app 모듈)

#### Step 4-1: 메인 액티비티 마이그레이션
```
commit: "refactor(app): Migrate main activities to Kotlin"
files:
  - MainActivity.java → MainActivity.kt
  - MainGameActivity.java → MainGameActivity.kt
```

#### Step 4-2: 게임 씬 기본 로직 마이그레이션
```
commit: "refactor(app): Migrate main game scene to Kotlin"
files:
  - MainScene.java → MainScene.kt
  - PauseScene.java → PauseScene.kt
```

#### Step 4-3: 게임 오브젝트 마이그레이션
```
commit: "refactor(app): Migrate game objects to Kotlin"
files:
  - Cannon.java → Cannon.kt
  - Shell.java → Shell.kt
  - Fly.java → Fly.kt
  - Explosion.java → Explosion.kt
```

#### Step 4-4: 게임 유틸리티 마이그레이션
```
commit: "refactor(app): Migrate game utilities to Kotlin"
files:
  - WaveGen.java → WaveGen.kt
  - MapSelector.java → MapSelector.kt
  - DesertMapBg.java → DesertMapBg.kt

test: ./gradlew :app:build
      ./gradlew :app:connectedAndroidTest
```

### 최종 정리 (Cleanup Phase)

```
commit: "refactor: Remove Java source files after Kotlin migration"
- 모든 .java 파일 삭제

commit: "docs: Add Kotlin migration completion notes"
- MIGRATION_COMPLETED.md 작성
```

## 📊 총 커밋 수: 12개

| Phase | 커밋 수 | 파일 수 |
|-------|--------|--------|
| 준비 | 2 | 4 + 8 |
| Phase 1 | 3 | 14 |
| Phase 2 | 1 | 4 |
| Phase 3 | 2 | 8 |
| Phase 4 | 3 | 11 |
| 정리 | 2 | - |
| **합계** | **13** | **44** |

## 🔧 각 단계별 실행 방법

### 1. 단일 파일 마이그레이션 후 커밋
```bash
# Kotlin 파일로 마이그레이션
# (Android Studio 자동 변환 또는 수동 작성)

# 마이그레이션된 파일만 스테이징
git add TuDefence/a2dg/src/main/java/.../InterfaceName.kt

# Java 원본 파일 제거
rm TuDefence/a2dg/src/main/java/.../InterfaceName.java

# 정리된 상태로 커밋
git add TuDefence/a2dg/src/main/java/.../InterfaceName.java
git commit -m "refactor(a2dg): Migrate IGameObject to Kotlin"
```

### 2. 여러 파일 일괄 마이그레이션
```bash
# 5개 인터페이스 파일을 모두 마이그레이션 후

# Kotlin 파일들만 추가
git add TuDefence/a2dg/src/main/java/*/interfaces/*.kt

# Java 파일들 제거 (staging)
git add TuDefence/a2dg/src/main/java/*/interfaces/*.java

# 커밋
git commit -m "refactor(a2dg): Migrate interfaces to Kotlin"
```

### 3. 각 단계 완료 후 검증
```bash
# Phase 1 완료 후
./gradlew :a2dg:build

# 빌드 성공 확인 후
git push origin main

# 테스트 실행 (선택사항)
./gradlew :a2dg:test
```

## 💡 커밋 메시지 규칙

Conventional Commits 형식을 따릅니다:

```
<type>(<scope>): <subject>

<body (선택사항)>
```

### 타입 (type)
- `refactor`: 코드 재구조화 (마이그레이션)
- `build`: Gradle 설정 변경
- `test`: 테스트 파일 수정
- `docs`: 문서 작성
- `chore`: 빌드 시스템, 의존성 관리 등

### 스코프 (scope)
- `a2dg`: 게임 엔진 라이브러리
- `app`: 메인 앱 모듈
- `gradle`: Gradle 설정

### 예시
```
refactor(a2dg): Migrate interfaces to Kotlin

- IGameObject.java → IGameObject.kt
- ITouchable.java → ITouchable.kt
- IRecyclable.java → IRecyclable.kt
- IBoxCollidable.java → IBoxCollidable.kt
- ILayerProvider.java → ILayerProvider.kt

Migration includes:
- Null-safety improvements
- Kotlin property syntax
- Extension functions where applicable

Test: ./gradlew :a2dg:build ✓
```

## 📈 마이그레이션 진행 추적

각 커밋 후 다음 명령으로 진행 상황을 확인할 수 있습니다:

```bash
# 마이그레이션된 파일 수 확인
find TuDefence -name "*.kt" -type f | grep -E "(src/main|src/test)" | wc -l

# 남은 Java 파일 수 확인
find TuDefence -name "*.java" -type f | grep -E "(src/main|src/test)" | grep -v "build" | wc -l

# 커밋 히스토리 보기
git log --oneline --graph --all
```

## 🎯 최종 체크리스트

- [ ] 준비 단계: 2개 커밋
- [ ] Phase 1: 3개 커밋 (14개 파일)
- [ ] Phase 2: 1개 커밋 (4개 파일)
- [ ] Phase 3: 2개 커밋 (8개 파일)
- [ ] Phase 4: 3개 커밋 (11개 파일)
- [ ] 정리: 2개 커밋
- [ ] 전체 빌드 및 테스트 성공
- [ ] 최종 푸시

---

**작성일**: 2025-02-17
**프로젝트**: TuDefence
**상태**: 준비 완료

