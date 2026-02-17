# TuDefence Java → Kotlin 마이그레이션 계획 - 최종 요약

## 📌 개요

이 프로젝트는 TuDefence Android 게임을 **Java에서 Kotlin으로 단계적으로 마이그레이션**하는 계획입니다.

- **총 44개 Java 파일** 마이그레이션 예정
- **13개의 의미 있는 git 커밋**으로 진행 상황 추적
- **3-5일 소요** (1일 6-8시간 작업 기준)

---

## 📚 제공 문서

### 1. **MIGRATION_COMMIT_STRATEGY.md**
   - 단계별 커밋 계획 (13개 커밋)
   - 커밋 메시지 규칙 (Conventional Commits)
   - 파일별 마이그레이션 로드맵

### 2. **MIGRATION_EXECUTION_GUIDE.md** ⭐ 실행 가이드
   - 상세한 단계별 실행 방법
   - 각 Phase별 구체적인 파일 목록
   - 커밋 명령어 예시
   - 빌드 검증 방법
   - 문제 해결 팁

### 3. **migration_helper.ps1** (Windows 환경)
   - 대화형 마이그레이션 헬퍼
   - 진행 상황 모니터링
   - 빌드 검증 자동화
   - Git 상태 확인

### 4. **migration_helper.sh** (Linux/Mac 환경)
   - Bash 기반 헬퍼 스크립트
   - 동일한 기능 제공

---

## 🚀 빠른 시작

### 준비 단계
```bash
cd D:\Lectures\2025_1\mig

# 마이그레이션 헬퍼 실행
powershell -ExecutionPolicy Bypass -File migration_helper.ps1

# 또는 직접 실행
powershell -ExecutionPolicy Bypass -File migration_helper.ps1 --progress
```

### 단계별 진행

#### 1️⃣ **Gradle 설정 업데이트** (준비 단계)
```bash
# 파일 수정:
# - gradle/libs.versions.toml (Kotlin 버전 추가)
# - TuDefence/build.gradle.kts
# - TuDefence/app/build.gradle.kts
# - TuDefence/a2dg/build.gradle.kts

git add .
git commit -m "build(gradle): Add Kotlin plugin and dependencies"
git push origin main
```

#### 2️⃣ **Phase 1: 게임 프레임워크 기초** (14개 파일, a2dg 라이브러리)
```bash
# 1-1. 인터페이스 (5개)
# IGameObject, ITouchable, IRecyclable, IBoxCollidable, ILayerProvider

# 1-2. 유틸리티 (3개)
# RectUtil, Gauge, CollisionHelper

# 1-3. 맵/리소스 (6개)
# Tileset, TiledMap, MapLayer, Converter, Sound, BitmapPool

git commit -m "refactor(a2dg): Migrate [카테고리] to Kotlin"
```

#### 3️⃣ **Phase 2: 게임 엔진 코어** (4개 파일)
```bash
# Metrics, GameView, Scene, GameActivity
git commit -m "refactor(a2dg): Migrate framework core to Kotlin"
```

#### 4️⃣ **Phase 3: 게임 오브젝트** (8개 파일)
```bash
# 스프라이트, 특수 객체
git commit -m "refactor(a2dg): Migrate [스프라이트/객체] to Kotlin"
```

#### 5️⃣ **Phase 4: 게임 로직** (11개 파일, app 모듈)
```bash
# 메인 액티비티, 게임 씬, 게임 객체, 유틸리티
git commit -m "refactor(app): Migrate [카테고리] to Kotlin"
```

#### 6️⃣ **최종 정리**
```bash
# Java 파일 제거
git commit -m "refactor: Remove Java files after Kotlin migration"
```

---

## 📊 마이그레이션 구조

```
mig (git root)
├── TuDefence/
│   ├── app/                    # 메인 게임 앱 (11개 파일)
│   ├── a2dg/                   # 게임 엔진 라이브러리 (22개 파일)
│   ├── build.gradle.kts
│   └── gradle/
│       └── libs.versions.toml
├── MIGRATION_COMMIT_STRATEGY.md        # 커밋 전략
├── MIGRATION_EXECUTION_GUIDE.md        # 실행 가이드 ⭐
├── migration_helper.ps1                # Windows 헬퍼
├── migration_helper.sh                 # Linux/Mac 헬퍼
└── TuDefence_Java_to_Kotlin_Migration_Plan.md  # 초기 계획
```

---

## 🔄 Git 커밋 계획 (13개)

| # | 단계 | 파일 수 | 커밋 메시지 |
|---|------|--------|-----------|
| 1 | 준비 | 4 | `build(gradle): Add Kotlin plugin and dependencies` |
| 2 | 준비 | 0 | `refactor: Create Kotlin source directories` |
| 3 | Phase 1-1 | 5 | `refactor(a2dg): Migrate interfaces to Kotlin` |
| 4 | Phase 1-2 | 3 | `refactor(a2dg): Migrate utility classes to Kotlin` |
| 5 | Phase 1-3 | 6 | `refactor(a2dg): Migrate map and resource classes to Kotlin` |
| 6 | Phase 2-1 | 4 | `refactor(a2dg): Migrate framework core to Kotlin` |
| 7 | Phase 3-1 | 5 | `refactor(a2dg): Migrate sprite classes to Kotlin` |
| 8 | Phase 3-2 | 3 | `refactor(a2dg): Migrate special game objects to Kotlin` |
| 9 | Phase 4-1 | 2 | `refactor(app): Migrate main activities to Kotlin` |
| 10 | Phase 4-2 | 2 | `refactor(app): Migrate game scenes to Kotlin` |
| 11 | Phase 4-3 | 4 | `refactor(app): Migrate game objects to Kotlin` |
| 12 | Phase 4-4 | 3 | `refactor(app): Migrate game utilities to Kotlin` |
| 13 | 정리 | 0 | `refactor: Remove Java files after Kotlin migration` |

**총 파일 수: 44개** (테스트, 생성 파일 제외)

---

## ✅ 마이그레이션 체크리스트

### 사전 준비
- [ ] Git 저장소 확인: `D:\Lectures\2025_1\mig`
- [ ] 모든 가이드 문서 읽기
- [ ] Android Studio 최신 버전 확인

### 준비 단계
- [ ] Gradle 설정 업데이트
- [ ] 커밋 및 푸시

### 마이그레이션 단계
- [ ] Phase 1: 게임 프레임워크 (14개 파일)
- [ ] Phase 2: 게임 엔진 코어 (4개 파일)
- [ ] Phase 3: 게임 오브젝트 (8개 파일)
- [ ] Phase 4: 게임 로직 (11개 파일)

### 최종 검증
- [ ] 전체 빌드 성공: `./gradlew clean build`
- [ ] 모든 테스트 통과: `./gradlew test`
- [ ] 에뮬레이터에서 정상 실행
- [ ] Git 히스토리 확인: 13개 커밋

---

## 📈 진행 상황 추적

### 방법 1: PowerShell 헬퍼 사용
```powershell
cd D:\Lectures\2025_1\mig
powershell -ExecutionPolicy Bypass -File migration_helper.ps1 --progress
```

### 방법 2: 명령줄 확인
```bash
# Kotlin 파일 개수
Get-ChildItem -Path TuDefence -Recurse -Filter "*.kt" | 
  Where-Object { $_.FullName -match "src\(main|test)" } | 
  Measure-Object | Select-Object Count

# Java 파일 개수
Get-ChildItem -Path TuDefence -Recurse -Filter "*.java" | 
  Where-Object { $_.FullName -match "src\(main|test)" -and $_.FullName -notmatch "\\build\\" } | 
  Measure-Object | Select-Object Count
```

### 방법 3: Git 히스토리
```bash
git log --oneline --graph
```

---

## 🎯 주요 전략

### 1. **의존성 순서 (Bottom-Up)**
- 하위 라이브러리 (a2dg) → 상위 앱 (app)
- 각 Phase는 독립적으로 검증 가능

### 2. **점진적 마이그레이션**
- Java와 Kotlin 혼합 컴파일 지원
- 각 Phase마다 빌드 검증
- 롤백 가능 (git revert)

### 3. **명확한 커밋 메시지**
- Conventional Commits 형식
- Phase와 파일 카테고리 명시
- 빌드 상태 포함

### 4. **자동화 도구**
- 진행 상황 모니터링
- 빌드 검증 자동화
- Git 작업 가이드

---

## 💡 Kotlin 마이그레이션 하이라이트

### 인터페이스
```kotlin
// Java interface → Kotlin interface
interface IGameObject {
    fun update()
    fun draw(canvas: Canvas)
}
```

### 유틸리티
```kotlin
// Static method → Extension function
fun RectF.contains(x: Float, y: Float): Boolean = contains(x, y)
```

### 데이터 클래스
```kotlin
// POJO → Data class
data class Tileset(val id: Int, val name: String)
```

### 싱글톤
```kotlin
// Static getInstance() → Object
object Metrics {
    val screenWidth: Int = 0
    val screenHeight: Int = 0
}
```

### 컬렉션
```kotlin
// ArrayList → MutableList
val objects: MutableList<IGameObject> = mutableListOf()
objects.forEach { it.update() }
```

---

## 📞 도움말

### PowerShell 헬퍼 명령어
```powershell
# 진행 상황 확인
powershell -File migration_helper.ps1 --progress

# 빌드 검증
powershell -File migration_helper.ps1 --validate a2dg
powershell -File migration_helper.ps1 --validate app

# Git 상태
powershell -File migration_helper.ps1 --git-status
powershell -File migration_helper.ps1 --git-log

# 마이그레이션 가이드
powershell -File migration_helper.ps1 --guide
```

### 자주 묻는 질문

**Q: Phase 1을 완료하고 Phase 2를 시작하기 전에 push 해야 하나?**
A: 네, 각 Phase마다 push하는 것이 권장됩니다. 이렇게 하면:
- GitHub에 백업 저장
- 팀원과 진행 상황 공유 가능
- 문제 발생 시 특정 Phase로 롤백 가능

**Q: Java 파일을 모두 삭제해야 하나?**
A: Phase 진행 중에는 Java 파일을 유지하고, 최종 마이그레이션 완료 후 삭제합니다.
이를 통해 마이그레이션 과정 중 참고 가능합니다.

**Q: 테스트 파일도 마이그레이션해야 하나?**
A: 기존 테스트는 유지하고, 새 테스트는 Kotlin으로 작성하는 것을 권장합니다.

---

## 🎓 학습 리소스

- [Kotlin 공식 문서](https://kotlinlang.org/docs/)
- [Android Kotlin 가이드](https://developer.android.com/kotlin)
- [Kotlin 코딩 컨벤션](https://kotlinlang.org/docs/coding-conventions.html)
- [Java to Kotlin 변환 가이드](https://kotlinlang.org/docs/java-to-kotlin-interop.html)

---

## 📅 일정 계획 (예상)

| 날짜 | 단계 | 예상 시간 |
|------|------|---------|
| Day 1 | 준비 + Phase 1 | 7시간 |
| Day 2 | Phase 2 + Phase 3 | 7시간 |
| Day 3 | Phase 4 | 8시간 |
| Day 4 | 정리 및 검증 | 5시간 |
| **총** | | **27시간** |

---

## 🚀 시작하기

### 1단계: 이 문서 읽기
✅ 완료!

### 2단계: MIGRATION_EXECUTION_GUIDE.md 읽기
```bash
cd D:\Lectures\2025_1\mig
cat MIGRATION_EXECUTION_GUIDE.md
```

### 3단계: Gradle 설정 업데이트 (준비 단계)
**MIGRATION_EXECUTION_GUIDE.md의 "단계 0"을 따르세요**

### 4단계: Phase 1 시작
**MIGRATION_EXECUTION_GUIDE.md의 "Phase 1"을 따르세요**

---

## ✨ 마이그레이션 완료 후

```bash
# 최종 상태 확인
cd D:\Lectures\2025_1\mig
git log --oneline | head -20

# 빌드 및 테스트
cd TuDefence
./gradlew clean build
./gradlew test
```

모든 단계가 완료되면:
- ✅ 44개 Java 파일 → 44개 Kotlin 파일
- ✅ 13개의 의미 있는 git 커밋
- ✅ 0 오류, 0 경고
- ✅ 모든 테스트 통과
- ✅ 게임 정상 동작

---

**프로젝트**: TuDefence Java → Kotlin 마이그레이션
**상태**: 📋 계획 완성, 실행 준비 완료
**마지막 업데이트**: 2025-02-17

Happy Coding! 🎉

