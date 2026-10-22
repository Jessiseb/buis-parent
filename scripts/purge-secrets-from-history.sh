#!/usr/bin/env bash
# ============================================================
# Git 历史密钥清洗脚本
# 用途：从 git 全量历史中移除已泄露的凭据字面量
# 前提：已在各代码托管平台吊销（rotate）全部相关凭据
# 警告：本脚本会重写全部提交哈希，执行前务必备份仓库
#
# 用法：
#   1) 把要清洗的凭据逐行写入密钥清单文件（每行一个原始字面量）
#   2) bash scripts/purge-secrets-from-history.sh [清单文件] [仓库目录]
#
# 密钥清单文件本身含明文密钥，请勿提交到仓库
# ============================================================
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SECRET_LIST="${1:-$SCRIPT_DIR/.secret-list.txt}"
REPO_DIR="${2:-$(cd "$SCRIPT_DIR/.." && pwd)}"

if [ ! -f "$SECRET_LIST" ]; then
  echo "错误：找不到密钥清单文件 $SECRET_LIST"
  echo "请创建一个每行一个原始凭据字面量的文件，或参考 docs/配置与密钥部署说明.md"
  exit 1
fi

cd "$REPO_DIR"
echo "==> 仓库：$REPO_DIR"
echo "==> 密钥清单：$SECRET_LIST（$(grep -cve '^\s*$' "$SECRET_LIST") 条）"

# ---------- 1. 备份 ----------
BACKUP="$(cd "$REPO_DIR/.." && pwd)/buis-parent-backup-$(date +%Y%m%d-%H%M%S).git"
echo "==> 备份到 $BACKUP"
git clone --bare . "$BACKUP"

# ---------- 2. 生成替换规则 ----------
# 替换值为占位符，历史中不再出现真实凭据
REPLACEMENTS="$(mktemp)"
while IFS= read -r needle; do
  [ -z "$needle" ] && continue
  case "$needle" in \#*) continue ;; esac
  printf 'literal:%s==>***REMOVED***\n' "$needle" >> "$REPLACEMENTS"
done < "$SECRET_LIST"
echo "==> 替换规则共 $(wc -l < "$REPLACEMENTS") 条"

# ---------- 3. 执行重写 ----------
echo "==> 开始重写全部历史"
git filter-repo --replace-text "$REPLACEMENTS" --force --refs \
  $(git for-each-ref --format='%(refname)' refs/heads | tr '\n' ' ')

# ---------- 4. 校验 ----------
echo "==> 校验：逐个凭据检查历史中是否仍有残留"
LEFTOVER=0
while IFS= read -r needle; do
  [ -z "$needle" ] && continue
  case "$needle" in \#*) continue ;; esac
  if git log --all --oneline -S "$needle" 2>/dev/null | head -1 | grep -q .; then
    echo "  ✗ 仍有残留: ${needle:0:8}..."
    LEFTOVER=1
  else
    echo "  ✓ 已清除: ${needle:0:8}..."
  fi
done < "$SECRET_LIST"

rm -f "$REPLACEMENTS"

echo ""
if [ "$LEFTOVER" -eq 0 ]; then
  echo "OK：全部历史中已无上述凭据"
else
  echo "!!! 校验未通过，请人工检查 !!!"
fi
echo "==> 备份位于：$BACKUP"
echo "==> 下一步：把清洗后的历史 force-push 到远端所有分支"
echo "    git push --force --mirror <remote-url>"
echo "    （GitHub/Gitee 上如已有人 fork，需另行通知其自行清理）"
