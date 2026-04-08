## Plan: Tightened YAML Icon Replacement

Execute a deterministic, one-time icon migration driven by `icon-replacement/icon-mapping.yaml`, with a CSV audit trail at `icon-replacement/out/report.csv`. The flow is discovery first, mapping enrichment second, controlled replacement third, verification last. Replacement is intentionally limited to safe Regular literals.

**Important**
Do not run any Maven verification. Do not run any `mvn` commands.

**Steps**

### 1) Preconditions
1. Confirm branch context in the run log: `feature/XIVY-18408-tabler-icons`.
2. Validate mapping format in `icon-replacement/icon-mapping.yaml`: each key/value must be full class pairs (example structure: old class pair -> new class pair).
3. Ensure output directory exists for `icon-replacement/out/report.csv`.

### 2) Discovery Inventory (No Edits Yet)
1. Scan only these roots:
- `dev-workflow-ui`
- `dev-workflow-ui-test`
- `dev-workflow-ui-test-data`
- `dev-workflow-ui-web-test`
- `system-cms`
2. Exclude all paths containing `/target/` and `/src_generated/`.
3. Discover and classify:
- Regular with `^(fa|si|pi)\s+(fa|si|pi)-[a-zA-Z0-9\-]+$`
- Complex with `^((fa|si|pi)\s?)?(fa|si|pi)[\-\s]?#{.*$` (review-only)
- Java candidates from `.java` files (review-only)
4. Create `icon-replacement/out/report.csv` with header:
`potential_target,potential_target_type,file,line,matched_mapping_key,matched_mapping_value,replaced`
5. Populate initial rows with columns 1-4; keep columns 5-7 empty initially.

### 3) Mapping Enrichment
1. For each Regular row, fill:
- `matched_mapping_key` when exact key match exists
- `matched_mapping_value` with mapped target value
2. For every mapping key not present in any Regular row, append a synthetic row with columns 1-4 empty and columns 5-6 filled from mapping; leave `replaced` empty.
3. Keep Complex and Java rows present in report but unmodified for replacement fields.

### 4) Controlled Replacement
1. Replace only exact-match Regular literals in static contexts:
- XHTML/XML/HTML attributes
- YAML scalar icon fields
- JSON string icon values
2. Do not replace Complex or Java candidates in this pass.
3. Mark `replaced` as `replaced` per successfully updated row; otherwise keep blank.

### 5) Verification and Closure
1. Re-run discovery and confirm scope compliance (only approved roots, no excluded folders).
2. Confirm CSV completeness:
- all discovered rows present
- all mapping keys represented (matched or synthetic)
3. Confirm integrity:
- only Regular rows can be marked `replaced`
- Complex/Java rows unchanged by policy
4. Confirm no `mvn` command execution (explicit constraint).

**Relevant files**
- `icon-replacement/plan_lean.md` - source plan being tightened
- `icon-replacement/icon-mapping.yaml` - mapping source of truth
- `icon-replacement/out/report.csv` - required audit artifact
- `dev-workflow-ui` - in-scope root
- `dev-workflow-ui-test` - in-scope root
- `dev-workflow-ui-test-data` - in-scope root
- `dev-workflow-ui-web-test` - in-scope root
- `system-cms` - in-scope root

**Decisions**
- Included: only the five approved roots.
- Excluded: all `/target/` and `/src_generated/` paths.
- Replacement policy: Regular static literals only, exact mapping matches only.
- Deferred policy: Complex and Java candidates are inventory-only in this lean pass.
- Report format: CSV only for this lean workflow.
