#!/usr/bin/env bash
set -euo pipefail

CLEANUP_MODE=0

usage() {
  cat <<'EOF'
Usage: run_icon_replacement.sh [--cleanup]

Options:
  --cleanup   Remove all files in icon-replacement/out except report.csv after execution.
  -h, --help  Show this help.
EOF
}

while [[ $# -gt 0 ]]; do
  case "$1" in
    --cleanup)
      CLEANUP_MODE=1
      shift
      ;;
    -h|--help)
      usage
      exit 0
      ;;
    *)
      echo "Unknown option: $1" >&2
      usage >&2
      exit 1
      ;;
  esac
done

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"
MAPPING_FILE="$SCRIPT_DIR/icon-mapping.yaml"
OUT_DIR="$SCRIPT_DIR/out"
REPORT_CSV="$OUT_DIR/report.csv"
WORK_TSV="$OUT_DIR/report.work.tsv"
TMP_ALL="$OUT_DIR/discovery_all.tsv"
TMP_REGULAR="$OUT_DIR/discovery_regular.tsv"
TMP_COMPLEX="$OUT_DIR/discovery_complex.tsv"
TMP_JAVA="$OUT_DIR/discovery_java.tsv"
PAIRS_FILE="$OUT_DIR/replacement_pairs.tsv"
REPLACED_PAIRS_FILE="$OUT_DIR/replaced_pairs.tsv"

INCLUDE_ROOTS=(
  "$ROOT_DIR/dev-workflow-ui"
  "$ROOT_DIR/dev-workflow-ui-test"
  "$ROOT_DIR/dev-workflow-ui-test-data"
  "$ROOT_DIR/dev-workflow-ui-web-test"
  "$ROOT_DIR/system-cms"
)

ALLOWED_REPLACE_EXT_REGEX='\.(xhtml|xml|html|yaml|yml|json|java)$'

if [[ ! -f "$MAPPING_FILE" ]]; then
  echo "Mapping file not found: $MAPPING_FILE" >&2
  exit 1
fi

if ! command -v rg >/dev/null 2>&1; then
  echo "ripgrep (rg) is required but not found in PATH." >&2
  exit 1
fi

if ! command -v perl >/dev/null 2>&1; then
  echo "perl is required but not found in PATH." >&2
  exit 1
fi

mkdir -p "$OUT_DIR"
: > "$TMP_ALL"
: > "$TMP_REGULAR"
: > "$TMP_COMPLEX"
: > "$TMP_JAVA"
: > "$REPLACED_PAIRS_FILE"

# Validate mapping format: simple YAML key: value with full class pair style.
awk '
BEGIN { ok=1 }
/^[[:space:]]*#/ || /^[[:space:]]*$/ { next }
{
  line=$0;
  colon=index(line, ":");
  if (colon > 1 && substr(line, 1, 1) != "#") {
    key=substr(line, 1, colon-1);
    val=substr(line, colon+1);
    gsub(/^[[:space:]]+|[[:space:]]+$/, "", key);
    gsub(/^[[:space:]]+|[[:space:]]+$/, "", val);
    if (key !~ /^(fa|si|pi)[[:space:]]+(fa|si|pi)-[A-Za-z0-9-]+$/) {
      print "Invalid mapping key format: " key > "/dev/stderr";
      ok=0;
    }
    if (val !~ /^(fa|si|pi|ti|tif)[[:space:]]+(fa|si|pi|ti|tif)-[A-Za-z0-9-]+$/) {
      print "Invalid mapping value format: " val > "/dev/stderr";
      ok=0;
    }
  } else {
    print "Invalid mapping line: " $0 > "/dev/stderr";
    ok=0;
  }
}
END { if (!ok) exit 1 }
' "$MAPPING_FILE"

emit_matches() {
  local type="$1"
  local pattern="$2"
  local out_file="$3"
  shift 3
  rg -n --no-heading --with-filename --pcre2 -o "$pattern" \
    --glob '!**/target/**' \
    --glob '!**/src_generated/**' \
    "$@" \
  | awk -v t="$type" -v root="$ROOT_DIR" 'BEGIN { OFS="\t" } {
      raw=$0;
      c1=index(raw, ":");
      if (c1 == 0) next;
      file=substr(raw, 1, c1-1);
      rest=substr(raw, c1+1);
      c2=index(rest, ":");
      if (c2 == 0) next;
      line=substr(rest, 1, c2-1);
      matchText=substr(rest, c2+1);
      if (index(file, root "/") == 1) {
        file=substr(file, length(root) + 2);
      }
      print matchText, t, file, line
    }' \
  >> "$out_file"
}

# Regular candidates: exact class pair snippets.
emit_matches "Regular" '(fa|si|pi)\s+(fa|si|pi)-[A-Za-z0-9-]+' "$TMP_REGULAR" "${INCLUDE_ROOTS[@]}" || true

# Complex candidates: expression/composed style snippets.
emit_matches "Complex" '((fa|si|pi)\s?)?(fa|si|pi)[-\s]?#\{.*' "$TMP_COMPLEX" "${INCLUDE_ROOTS[@]}" || true

# Java review-only candidates: likely icon-returning logic or icon class literals.
rg -n --no-heading --with-filename --pcre2 \
  '(?i)(return\s+".*(fa|si|pi)\s+(fa|si|pi)-[A-Za-z0-9-]+"|\bicon\b|"(fa|si|pi)\s+(fa|si|pi)-[A-Za-z0-9-]+")' \
  --glob '**/*.java' \
  --glob '!**/target/**' \
  --glob '!**/src_generated/**' \
  "${INCLUDE_ROOTS[@]}" \
| awk -F: -v root="$ROOT_DIR" 'BEGIN { OFS="\t" } {
    raw=$0;
    c1=index(raw, ":");
    if (c1 == 0) next;
    file=substr(raw, 1, c1-1);
    rest=substr(raw, c1+1);
    c2=index(rest, ":");
    if (c2 == 0) next;
    line=substr(rest, 1, c2-1);
    matchText=substr(rest, c2+1);
    if (index(file, root "/") == 1) {
      file=substr(file, length(root) + 2);
    }
    print matchText, "Java", file, line
  }' \
> "$TMP_JAVA" || true

cat "$TMP_REGULAR" "$TMP_COMPLEX" "$TMP_JAVA" > "$TMP_ALL"

awk -v mapping="$MAPPING_FILE" '
BEGIN {
  FS="\t"; OFS="\t";
  while ((getline line < mapping) > 0) {
    if (line ~ /^[[:space:]]*#/ || line ~ /^[[:space:]]*$/) continue;
    colon=index(line, ":");
    if (colon > 1 && substr(line, 1, 1) != "#") {
      key=substr(line, 1, colon-1);
      val=substr(line, colon+1);
      gsub(/^[[:space:]]+|[[:space:]]+$/, "", key);
      gsub(/^[[:space:]]+|[[:space:]]+$/, "", val);
      map[key]=val;
      mapOrder[++mapCount]=key;
    }
  }
  close(mapping);
}
{
  target=$1; type=$2; file=$3; line=$4;
  key=""; val="";
  if (type == "Regular") {
    regularSeen[target]=1;
    if (target in map) {
      key=target;
      val=map[target];
    }
  }
  print target, type, file, line, key, val, "";
}
END {
  for (i=1; i<=mapCount; i++) {
    k=mapOrder[i];
    if (!(k in regularSeen)) {
      print "", "", "", "", k, map[k], "";
    }
  }
}
' "$TMP_ALL" > "$WORK_TSV"

# Build unique replacement pairs by file+key for replaceable file types only.
awk '
BEGIN { FS="\t"; OFS="\t" }
{
  target=$1; type=$2; file=$3; key=$5; val=$6;
  if (type == "Regular" && key != "" && val != "" && file != "") {
    print file, key, val;
  }
}
' "$WORK_TSV" | sort -u > "$PAIRS_FILE"

# Apply replacements only in approved static file extensions.
while IFS=$'\t' read -r file key val; do
  [[ -n "$file" ]] || continue
  abs_file="$ROOT_DIR/$file"
  if [[ "$file" =~ $ALLOWED_REPLACE_EXT_REGEX ]]; then
    if [[ -f "$abs_file" ]] && grep -Fq -- "$key" "$abs_file"; then
      perl -0777 -i -pe "s/\Q${key}\E/${val}/g" "$abs_file"
      printf '%s\t%s\n' "$file" "$key" >> "$REPLACED_PAIRS_FILE"
    fi
  fi
done < "$PAIRS_FILE"

# Write final CSV with enrichment and replaced marker.
{
  echo 'potential_target,potential_target_type,file,line,matched_mapping_key,matched_mapping_value,replaced'
  awk -v replacedPairs="$REPLACED_PAIRS_FILE" '
  BEGIN {
    FS="\t"; OFS=",";
    while ((getline p < replacedPairs) > 0) {
      split(p, f, "\t");
      pair=f[1] "|" f[2];
      replacedPair[pair]=1;
    }
    close(replacedPairs);
  }
  function esc(v,    t) {
    t=v
    gsub(/"/,"\"\"",t)
    return "\"" t "\""
  }
  {
    target=$1; type=$2; file=$3; line=$4; key=$5; val=$6; replaced=$7;
    pair=file "|" key;
    if (type == "Regular" && key != "" && pair in replacedPair) {
      replaced="replaced";
    }
    print esc(target), esc(type), esc(file), esc(line), esc(key), esc(val), esc(replaced);
  }
  ' "$WORK_TSV"
} > "$REPORT_CSV"

# Update replaced markers in working TSV as well for follow-up runs.
awk -v pairs="$REPLACED_PAIRS_FILE" '
BEGIN {
  FS="\t"; OFS="\t";
  while ((getline p < pairs) > 0) {
    split(p, f, "\t");
    key = f[1] "|" f[2];
    replaced[key]=1;
  }
  close(pairs);
}
{
  pair=$3 "|" $5;
  if ($2 == "Regular" && $5 != "" && (pair in replaced)) {
    $7="replaced";
  }
  print $0;
}
' "$WORK_TSV" > "$WORK_TSV.tmp"
mv "$WORK_TSV.tmp" "$WORK_TSV"

if [[ "$CLEANUP_MODE" -eq 1 ]]; then
  find "$OUT_DIR" -maxdepth 1 -type f ! -name 'report.csv' -delete
fi

echo "Report generated: $REPORT_CSV"
echo "Working data: $WORK_TSV"
