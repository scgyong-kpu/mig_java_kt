# TuDefence Java to Kotlin Migration - PowerShell Helper
# 마이그레이션 단계별 커밋을 자동화하는 PowerShell 스크립트

$ErrorActionPreference = "Stop"

$ProjectRoot = "D:\Lectures\2025_1\mig"
$TuDefenceRoot = "$ProjectRoot\TuDefence"

# 색상 정의
function Print-Header {
    param([string]$Message)
    Write-Host "========================================" -ForegroundColor Blue
    Write-Host $Message -ForegroundColor Blue
    Write-Host "========================================" -ForegroundColor Blue
}

function Print-Success {
    param([string]$Message)
    Write-Host "✓ $Message" -ForegroundColor Green
}

function Print-Error {
    param([string]$Message)
    Write-Host "✗ $Message" -ForegroundColor Red
}

function Print-Info {
    param([string]$Message)
    Write-Host "ℹ $Message" -ForegroundColor Yellow
}

# Java 파일 개수 카운트
function Count-JavaFiles {
    $count = @(Get-ChildItem -Path $TuDefenceRoot -Recurse -Filter "*.java" |
               Where-Object { $_.FullName -match "(src\\main|src\\test)" -and $_.FullName -notmatch "\\build\\" } |
               Measure-Object).Count
    return $count
}

# Kotlin 파일 개수 카운트
function Count-KotlinFiles {
    $count = @(Get-ChildItem -Path $TuDefenceRoot -Recurse -Filter "*.kt" |
               Where-Object { $_.FullName -match "(src\\main|src\\test)" } |
               Measure-Object).Count
    return $count
}

# 진행 상황 표시
function Show-Progress {
    Print-Header "마이그레이션 진행 상황"

    $javaCount = Count-JavaFiles
    $kotlinCount = Count-KotlinFiles
    $total = $javaCount + $kotlinCount

    if ($total -gt 0) {
        $percentage = [math]::Floor(($kotlinCount * 100) / $total)
        Write-Host "Kotlin 파일: $kotlinCount / $total ($percentage%)"
        Write-Host "Java 파일: $javaCount / $total"

        # 진행률 바
        $filled = [math]::Floor($percentage / 5)
        $empty = 20 - $filled
        $bar = ("=" * $filled) + ("-" * $empty)
        Write-Host "진행률: [$bar] $percentage%"
    }
}

# 빌드 검증
function Validate-Build {
    param([string]$Module)

    Print-Header "빌드 검증: $Module"

    Push-Location $TuDefenceRoot

    try {
        & .\gradlew.bat ":$Module`:build" --quiet
        Print-Success "빌드 성공"
    } catch {
        Print-Error "빌드 실패"
        Write-Host $_.Exception.Message
    } finally {
        Pop-Location
    }
}

# 단일 파일 마이그레이션 헬퍼
function Show-FileMigrationHelper {
    param([string]$Category, [string[]]$Files)

    Print-Header "$Category 마이그레이션"

    Write-Host "마이그레이션 대기 중인 파일:"
    foreach ($file in $Files) {
        Print-Info "$file.java"
    }

    Write-Host ""
    Print-Info "다음 단계:"
    Write-Host "1. Android Studio에서 다음 파일들을 Code > Convert Java File to Kotlin으로 변환"
    Write-Host "2. 변환 완료 후 아래 명령 실행:"
    Write-Host ""
    Write-Host "   cd $ProjectRoot"
    Write-Host "   git add ."
    Write-Host "   git commit -m ""refactor: Migrate $Category to Kotlin"""
    Write-Host ""
    Write-Host "3. 빌드 검증:"
    Write-Host "   .\gradlew.bat build"
    Write-Host ""
}

# Git 상태 확인
function Show-GitStatus {
    Print-Header "Git 상태"

    Push-Location $ProjectRoot
    try {
        & git status
    } finally {
        Pop-Location
    }
}

# Git 로그 확인
function Show-GitLog {
    Print-Header "Git 커밋 히스토리"

    Push-Location $ProjectRoot
    try {
        & git log --oneline --graph -10
    } finally {
        Pop-Location
    }
}

# Phase별 마이그레이션 가이드
function Show-MigrationGuide {
    Print-Header "마이그레이션 단계별 가이드"

    Write-Host ""
    Write-Host "준비 단계:"
    Write-Host "  1. Gradle 설정 업데이트" -ForegroundColor Cyan
    Write-Host "     commit: 'build(gradle): Add Kotlin plugin and dependencies'"
    Write-Host ""

    Write-Host "Phase 1: 게임 프레임워크 기초 (14개 파일)"
    Write-Host "  1-1. 인터페이스 (5개): IGameObject, ITouchable, IRecyclable, IBoxCollidable, ILayerProvider"
    Write-Host "  1-2. 유틸리티 (3개): RectUtil, Gauge, CollisionHelper"
    Write-Host "  1-3. 맵/리소스 (6개): Tileset, TiledMap, MapLayer, Converter, Sound, BitmapPool"
    Write-Host ""

    Write-Host "Phase 2: 게임 엔진 코어 (4개 파일)"
    Write-Host "  2-1. Metrics, GameView, Scene, GameActivity"
    Write-Host ""

    Write-Host "Phase 3: 게임 오브젝트 (8개 파일)"
    Write-Host "  3-1. 스프라이트: Sprite, AnimSprite, SheetSprite, VertScrollBackground, HorzScrollBackground, TiledBackground"
    Write-Host "  3-2. 특수 객체: Score, JoyStick, Button"
    Write-Host ""

    Write-Host "Phase 4: 게임 로직 (11개 파일)"
    Write-Host "  4-1. 메인 액티비티: MainActivity, MainGameActivity"
    Write-Host "  4-2. 게임 씬: MainScene, PauseScene"
    Write-Host "  4-3. 게임 객체: Cannon, Shell, Fly, Explosion"
    Write-Host "  4-4. 게임 유틸: WaveGen, MapSelector, DesertMapBg"
    Write-Host ""
}

# 메인 메뉴
function Show-Menu {
    Clear-Host
    Print-Header "TuDefence Java → Kotlin 마이그레이션 헬퍼"

    Show-Progress

    Write-Host ""
    Write-Host "다음 옵션 중 선택하세요:" -ForegroundColor Cyan
    Write-Host "1. 마이그레이션 단계별 가이드"
    Write-Host "2. 인터페이스 마이그레이션 (Phase 1-1)"
    Write-Host "3. 유틸리티 마이그레이션 (Phase 1-2)"
    Write-Host "4. 맵/리소스 마이그레이션 (Phase 1-3)"
    Write-Host "5. 빌드 검증 (a2dg)"
    Write-Host "6. 빌드 검증 (app)"
    Write-Host "7. Git 상태 확인"
    Write-Host "8. Git 커밋 히스토리"
    Write-Host "9. 종료"
    Write-Host ""

    $choice = Read-Host "선택 (1-9)"

    switch ($choice) {
        "1" { Show-MigrationGuide }
        "2" { Show-FileMigrationHelper "인터페이스" @("IGameObject", "ITouchable", "IRecyclable", "IBoxCollidable", "ILayerProvider") }
        "3" { Show-FileMigrationHelper "유틸리티" @("RectUtil", "Gauge", "CollisionHelper") }
        "4" { Show-FileMigrationHelper "맵/리소스" @("Tileset", "TiledMap", "MapLayer", "Converter", "Sound", "BitmapPool") }
        "5" { Validate-Build "a2dg" }
        "6" { Validate-Build "app" }
        "7" { Show-GitStatus }
        "8" { Show-GitLog }
        "9" { exit }
        default { Print-Error "잘못된 선택입니다" }
    }

    Write-Host ""
    Read-Host "계속하려면 Enter를 누르세요"
    Show-Menu
}

# 실행
if ($args.Count -gt 0) {
    switch ($args[0]) {
        "--progress" { Show-Progress }
        "--validate" { Validate-Build $args[1] }
        "--guide" { Show-MigrationGuide }
        "--git-log" { Show-GitLog }
        "--git-status" { Show-GitStatus }
        default { Write-Host "Unknown option: $($args[0])" }
    }
} else {
    Show-Menu
}

