#!/usr/bin/env bash
# ============================================================
# Git 历史密钥清洗脚本
# 用途：从 git 全量历史中移除已泄露的凭据字面量
# 前提：已在各代码托管平台吊销（rotate）全部相关凭据
# 警告：本脚本会重写全部提交哈希，执行前务必备份仓库
# ============================================================
set -euo pipefail

REPO_DIR="${1:-$(cd "$(dirname "$0")/.." && pwd)}"
cd "$REPO_DIR"

echo "==> 仓库：$REPO_DIR"

# ---------- 1. 备份 ----------
BACKUP="../buis-parent-backup-$(date +%Y%m%d-%H%M%S)"
echo "==> 备份到 $BACKUP"
cp -r .git "$BACKUP"

# ---------- 2. 待替换的凭据清单 ----------
# 格式：literal:原值==>替换值
# 替换值统一使用占位符，历史中不再出现真实凭据
cat > /tmp/secret-replacements.txt <<'EOF'
literal:***REMOVED***==>***REMOVED_AI_API_KEY***
literal:***REMOVED***==>***REMOVED_AI_API_KEY***
literal:***REMOVED***==>***REMOVED_OSS_AK***
literal:***REMOVED***==>***REMOVED_OSS_SK***
literal:***REMOVED***==>***REMOVED_OSS_AK_OLD***
literal:***REMOVED***==>***REMOVED_OSS_SK_OLD***
literal:***REMOVED***==>***REMOVED_COZE_PAT***
literal:***REMOVED***==>***REMOVED_WENXIN_APP_ID***
literal:***REMOVED***==>***REMOVED_WENXIN_SECRET***
literal:***REMOVED***==>***REMOVED_WENXIN_SECRET***
literal:***REMOVED***==>***REMOVED_JWT_SECRET***
literal:***REMOVED***==>***REMOVED_DB_PASSWORD***
literal:***REMOVED***==>***REMOVED_DB_PASSWORD***
literal:***REMOVED***==>***REMOVED_MAIL_PASSWORD***
literal:3119932256@qq.com==>***REMOVED_MAIL_ACCOUNT***
literal:119.91.227.162==>10.0.0.11
literal:42.194.233.38==>10.0.0.12
literal:43.136.119.58==>10.0.0.13
EOF

echo "==> 替换清单共 $(wc -l < /tmp/secret-replacements.txt) 条"

# ---------- 3. 执行重写 ----------
echo "==> 开始重写全部历史（--force，重写所有分支与 tag）"
git filter-repo \
  --replace-text /tmp/secret-replacements.txt \
  --force \
  --refs "$(git for-each-ref --format='%(refname)' refs/heads refs/remotes refs/tags | tr '\n' ' ')"

# ---------- 4. 校验 ----------
echo "==> 校验：搜索残留凭据"
PATTERN='sk-1c578|sk-7696a8|LTAI5t|pat_AOZh3Zem|h9bKT9qF|p95Sn97X|6C247AB3|***REMOVED***|172110159zk|***REMOVED***|jVFagF3P|119\.91\.227\.162|42\.194\.233\.38|43\.136\.119\.58|3119932256@qq'
if git grep -I -E "$PATTERN" $(git rev-list --all) 2>/dev/null | head -5; then
  echo "!!! 仍有残留，请人工检查 !!!"
else
  echo "OK：全部历史中已无上述凭据"
fi

echo ""
echo "==> 清洗完成。备份位于：$BACKUP"
echo "==> 下一步：把清洗后的历史 force-push 到远端所有分支"
echo "    git push --force --mirror <remote-url>"
echo "    （GitHub/Gitee 上如已有人 fork，需另行通知其自行清理）"
