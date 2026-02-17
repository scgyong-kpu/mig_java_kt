#!/bin/bash
# TuDefence Java to Kotlin Migration - Automated Commit Helper
# 마이그레이션 단계별 커밋을 자동화하는 스크립트

set -e

PROJECT_ROOT="D:/Lectures/2025_1/mig"
TUDEFENCE_ROOT="$PROJECT_ROOT/TuDefence"

# 색상 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

print_header() {
    echo -e "${BLUE}========================================${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}========================================${NC}"
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_info() {
    echo -e "${YELLOW}ℹ $1${NC}"
}

# Phase 1: 인터페이스 마이그레이션
migrate_phase1_interfaces() {
    print_header "Phase 1: 인터페이스 마이그레이션"

    local files=(
        "IGameObject"
        "ITouchable"
        "IRecyclable"
        "IBoxCollidable"
        "ILayerProvider"
    )

    cd "$TUDEFENCE_ROOT"

    for file in "${files[@]}"; do
        print_info "마이그레이션 대기 중: $file.java"
    done

    print_info "다음 단계:"
    print_info "1. Android Studio에서 다음 파일들을 변환:"
    for file in "${files[@]}"; do
        echo "   - a2dg/src/main/java/.../interfaces/$file.java"
    done
    print_info "2. 변환 완료 후 다음 명령 실행:"
    echo "   cd $PROJECT_ROOT"
    echo "   git add TuDefence/a2dg/src/main/java/**/interfaces/*.kt"
    echo "   git add TuDefence/a2dg/src/main/java/**/interfaces/*.java"
    echo "   git commit -m \"refactor(a2dg): Migrate interfaces to Kotlin\""
}

# Phase 1: 유틸리티 마이그레이션
migrate_phase1_utils() {
    print_header "Phase 1: 유틸리티 마이그레이션"

    local files=(
        "RectUtil"
        "Gauge"
        "CollisionHelper"
    )

    cd "$TUDEFENCE_ROOT"

    for file in "${files[@]}"; do
        print_info "마이그레이션 대기 중: $file.java"
    done

    print_info "다음 단계:"
    print_info "1. Android Studio에서 다음 파일들을 변환:"
    for file in "${files[@]}"; do
        echo "   - a2dg/src/main/java/.../util/$file.java"
    done
    print_info "2. 변환 완료 후 다음 명령 실행:"
    echo "   cd $PROJECT_ROOT"
    echo "   git add TuDefence/a2dg/src/main/java/**/util/*.kt"
    echo "   git add TuDefence/a2dg/src/main/java/**/util/*.java"
    echo "   git commit -m \"refactor(a2dg): Migrate utility classes to Kotlin\""
}

# 빌드 검증
validate_build() {
    local module=$1
    print_header "빌드 검증: $module"

    cd "$TUDEFENCE_ROOT"

    if ./gradlew ":$module:build" --quiet; then
        print_success "빌드 성공"
        return 0
    else
        print_error "빌드 실패"
        return 1
    fi
}

# Java 파일 개수 카운트
count_java_files() {
    cd "$PROJECT_ROOT"
    local count=$(find TuDefence -name "*.java" -type f | grep -E "(src/main|src/test)" | grep -v "build" | wc -l)
    echo $count
}

# Kotlin 파일 개수 카운트
count_kotlin_files() {
    cd "$PROJECT_ROOT"
    local count=$(find TuDefence -name "*.kt" -type f | grep -E "(src/main|src/test)" | wc -l)
    echo $count
}

# 진행 상황 표시
show_progress() {
    print_header "마이그레이션 진행 상황"

    local java_count=$(count_java_files)
    local kotlin_count=$(count_kotlin_files)
    local total=$((java_count + kotlin_count))

    if [ $total -gt 0 ]; then
        local percentage=$((kotlin_count * 100 / total))
        echo "Kotlin 파일: $kotlin_count / $total ($percentage%)"
        echo "Java 파일: $java_count / $total"

        # 진행률 바
        local filled=$((percentage / 5))
        local empty=$((20 - filled))
        printf "진행률: ["
        printf "%${filled}s" | tr ' ' '='
        printf "%${empty}s" | tr ' ' '-'
        printf "] $percentage%%\n"
    fi
}

# 메인 메뉴
show_menu() {
    clear
    print_header "TuDefence Java → Kotlin 마이그레이션 헬퍼"

    show_progress

    echo ""
    echo "다음 옵션 중 선택하세요:"
    echo "1. Phase 1-1: 인터페이스 마이그레이션"
    echo "2. Phase 1-2: 유틸리티 마이그레이션"
    echo "3. Phase 1-3: 맵/리소스 마이그레이션"
    echo "4. 빌드 검증 (a2dg)"
    echo "5. 빌드 검증 (app)"
    echo "6. Git 상태 확인"
    echo "7. 종료"
    echo ""
    read -p "선택 (1-7): " choice

    case $choice in
        1) migrate_phase1_interfaces ;;
        2) migrate_phase1_utils ;;
        3) print_info "Phase 1-3 마이그레이션은 수동으로 진행하세요" ;;
        4) validate_build "a2dg" ;;
        5) validate_build "app" ;;
        6) cd "$PROJECT_ROOT" && git status ;;
        7) exit 0 ;;
        *) print_error "잘못된 선택입니다" ;;
    esac

    echo ""
    read -p "계속하려면 Enter를 누르세요..."
    show_menu
}

# 메인 실행
if [ "$1" == "--progress" ]; then
    show_progress
elif [ "$1" == "--validate" ]; then
    validate_build "$2"
else
    show_menu
fi

