#!/usr/bin/bash

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"
MAPPING_FILE="$SCRIPT_DIR/icon-mapping.yaml"
MAPPING_EXTENDED_FILE="$SCRIPT_DIR/icon-mapping-extended.yaml"
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

REGEX_FIXED_SHORT_MATCH='(?<![a-zA-Z0-9])(fa|si|pi)-[a-zA-Z0-9\-]+'
REGEX_FIXED_EXTENDED_MATCH='(?<![a-zA-Z0-9])(fa|si|pi)\s(fa|si|pi)-[a-zA-Z0-9\-]+'
REGEX_FIXED_BOTH_MATCH='(?<![a-zA-Z0-9])((fa|si|pi)\s)?(fa|si|pi)-[a-zA-Z0-9\-]+'
REGEX_COMPLEX_MATCH='((fa|si|pi)\s?)?(fa|si|pi)[-\s]?#\{.*'

if ! command -v rg >/dev/null 2>&1; then
  echo "ripgrep (rg) is required but not found in PATH." >&2
  exit 1
fi

# --- DRY RUN FLAG ---
DRY_RUN=false

# Parse args for --dry-run
for arg in "$@"; do
  if [[ "$arg" == "--dry-run" ]]; then
    DRY_RUN=true
    break
  fi
done

mkdir -p "$OUT_DIR"
: > "$TMP_ALL"
: > "$TMP_REGULAR"
: > "$TMP_COMPLEX"
: > "$TMP_JAVA"
: > "$REPLACED_PAIRS_FILE"

read_and_extend_mapping_yaml() {
  local pattern_key="^(fa|si|pi)[[:space:]]+(fa|si|pi)-[a-zA-Z0-9\-]+$"
  local pattern_value="^ti[[:space:]]+ti-[a-zA-Z0-9\-]+$"
  local infile="$MAPPING_FILE"
  local outfile="$MAPPING_EXTENDED_FILE"
  : > "$outfile"  # Clear or create the output file

  while IFS=: read -r key value || [ -n "$key$value" ]; do
    
    # Trim whitespace
    key="$(echo "$key" | xargs)"
    value="$(echo "$value" | xargs)"

    # Skip empty or comment lines
    [[ -z "$key" || "$key" == \#* ]] && continue

    # Validate key and value
    if ! [[ "$key" =~ $pattern_key ]]; then
      echo "Invalid key: '$key'"
      continue
    fi
    if ! [[ "$value" =~ $pattern_value ]]; then
      echo "Invalid value: '$value'"
      continue
    fi

    # Write original key-value pair
    echo "$key: $value" >> "$outfile"

    # Strip first 3 non-whitespace characters from key and value
    new_key="$(echo "$key" | sed 's/^[^ ]\+ //')"
    new_value="$(echo "$value" | sed 's/^ti //')"

    echo "$new_key: $new_value" >> "$outfile"
  done < "$infile"
}

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


read_and_extend_mapping_yaml

# Regular candidates: exact class pair snippets.
emit_matches "Regular" "$REGEX_FIXED_BOTH_MATCH" "$TMP_REGULAR" "${INCLUDE_ROOTS[@]}" || true

# Complex candidates: expression/composed style snippets.
emit_matches "Complex" "$REGEX_COMPLEX_MATCH" "$TMP_COMPLEX" "${INCLUDE_ROOTS[@]}" || true

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

# Find all matches between the mapping and the found candidates, and mark which mapping (if any) they match to.
awk -v mapping="$MAPPING_EXTENDED_FILE" '
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

# Apply replacements only in approved static file extensions. Only if DRY_RUN is false, otherwise just record what would happen
while IFS=$'\t' read -r file key val; do
  [[ -n "$file" ]] || continue
  abs_file="$ROOT_DIR/$file"
  if [[ "$file" =~ $ALLOWED_REPLACE_EXT_REGEX ]]; then
    if [[ -f "$abs_file" ]] && grep -Fq -- "$key" "$abs_file"; then
      printf '%s\t%s\n' "$file" "$key" >> "$REPLACED_PAIRS_FILE"
      if [[ "$DRY_RUN" != true ]]; then
        # Do replacmenet, if DRY_RUN is false
        perl -0777 -i -pe "s/\Q${key}\E/${val}/g" "$abs_file"
      fi
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