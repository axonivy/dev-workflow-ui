## Plan: YAML-Driven Icon Class Replacement

Replace icon classes in source/config files by treating icon-replacement/icon-mapping.yaml as the source of truth for old->new class mappings. Recommended approach: create a dedicated feature branch first, then implement a one-time replacement utility that only touches files under the approved root folders dev-workflow-ui, dev-workflow-ui-test, dev-workflow-ui-test-data, dev-workflow-ui-web-test, and system-cms, while still skipping generated/build output such as target/ and src_generated/.

**Steps**
1. Create and switch to a new feature branch before any edits. Suggested branch name: feature/copilot-icon-replacement. This blocks all later steps.
2. Before any replacement work, search for all potential icon-class targets inside the approved root folders (dev-workflow-ui, dev-workflow-ui-test, dev-workflow-ui-test-data, dev-workflow-ui-web-test, and system-cms), while excluding every target/ and src_generated/ folder. Use this regex for candidate values: ^(fa|si|ti|tif)\s+(fa|si|ti|tif)-[a-zA-Z0-9\-]+$.
3. Validate the mapping input in icon-replacement/icon-mapping.yaml and confirm the expected replacement format is full CSS class to full CSS class (for example, si si-view-1 -> ti ti-eye), not partial token replacement. The created replacement-target Markdown file must be saved under icon-replacement (for example, icon-replacement/potential-targets-report.md). This blocks replacement logic.
4. Before starting replacements, generate a Markdown inventory file that lists every potential target found in step 1, with location references and placeholders for mapping status and replacement value. Suggested output path: icon-replacement/potential-targets-report.md.
5. Inventory the replacement targets under the approved root folders only: dev-workflow-ui, dev-workflow-ui-test, dev-workflow-ui-test-data, dev-workflow-ui-web-test, and system-cms. Within those roots, exclude all target/ and src_generated/ folders. This can run in parallel with step 6 once scope is fixed.
6. Decide the execution mechanism. Preferred option: add a small repo-local script that reads icon-mapping.yaml and applies deterministic replacements to text files, then run it once and keep or remove it based on team preference. Alternate option: do IDE-assisted bulk replacements driven manually by the YAML entries if the mapping stays very small. This depends on step 3.
7. Implement replacement handling for static literals first: XHTML attributes such as icon="si si-*", class="si si-*", YAML values such as cssIcon: si si-*, and JSON process data values. This depends on steps 3-6.
8. Handle Java-backed icon strings deliberately. For methods returning full CSS classes, replace via direct mapping. For methods that return icon names or compose class names dynamically, update only when the mapping clearly applies; otherwise leave unchanged and document the gap. This depends on steps 3-6.
9. Review dynamic and externally sourced cases separately, especially model methods and values that come from custom fields or external stage metadata. Do not force replacements where the source value is not statically known. This depends on step 8.
10. Fill the Markdown inventory from step 4 for every potential target: mark whether a mapping entry existed, and if mapped, record the exact replacement that was applied; if not mapped, mark as no mapping and unchanged.
11. Verify with targeted searches that every mapping key from icon-mapping.yaml has either been replaced in maintained sources or explicitly identified as unused/no-match. Then run the relevant module tests or at minimum build checks for the touched modules. This depends on steps 7-10.
12. Do a final diff review to ensure no generated/build files changed and no PrimeIcons (pi pi-*) usages were accidentally modified unless a mapping explicitly required it. This depends on step 11.

**Relevant files**
- icon-replacement/icon-mapping.yaml — source of truth for replacements; currently only one mapping exists and the file is not consumed anywhere automatically.
- webContent — primary static icon usage in XHTML, including icon= and class= patterns.
- src/ch/ivyteam/workflowui/starts/StartableModel.java — returns a full CSS class string for the default start icon.
- src/ch/ivyteam/workflowui/intermediateEvents/IntermediateEventDetailsBean.java — hardcoded icon mappings in Java switch logic.
- src/ch/ivyteam/workflowui/user/UserComponentModel.java — hardcoded user/member icon class selection.
- dev-workflow-ui-test-data/cms/cms_en.yaml — config-side icon examples using cssIcon values.
- dev-workflow-ui-test-data/processes/TestData.p.json — process test data with embedded cssIcon values.
- build/build.groovy — useful to confirm there is no existing icon-replacement automation in the build.

**Verification**
1. Run the potential-target discovery with regex ^(fa|si|ti|tif)\s+(fa|si|ti|tif)-[a-zA-Z0-9\-]+$ across the approved root folders and confirm all target and src_generated paths are excluded.
2. Confirm the Markdown inventory file exists before replacement and contains every discovered potential target with file locations.
3. Search for each mapping key from icon-mapping.yaml across maintained source/config paths and confirm zero remaining matches or an intentional exclusion.
4. Confirm the Markdown inventory is fully populated after replacement: mapped yes/no and final replacement value per target.
5. Search for the replacement values and confirm they now appear only where expected.
6. Search target/ and src_generated/ to confirm they were not edited.
7. Run the smallest relevant build/test command for the touched module set; if full test execution is too heavy, at least run compile or package on the affected Maven modules.
8. Manually inspect a few representative UI pages/components that use static icons and a few Java-driven icons to confirm rendering and naming compatibility.

**Decisions**
- Included scope: files under dev-workflow-ui, dev-workflow-ui-test, dev-workflow-ui-test-data, dev-workflow-ui-web-test, and system-cms only; target/ and src_generated/ remain excluded.
- Recommended execution path: YAML-driven scripted replacement, because icon-mapping.yaml is not referenced anywhere today, and this allows deterministic generation plus post-fill of the potential-target Markdown inventory.
- Excluded for now: automatic runtime translation of icon classes and broad migration of unrelated icon libraries.
- Special handling required: dynamic icon composition and externally sourced icon values should be reviewed, not blindly replaced.

**Further Considerations**
1. If the mapping file remains small, manual replacement may be faster than introducing a permanent script; if it will grow, the script pays off immediately.
2. If future migrations are expected, consider keeping the replacement utility under version control with a narrow, documented input format.
3. If Tabler replaces Simple Icons broadly, a follow-up pass may be needed for dynamic icon-name producers rather than only full CSS class literals.

**Potential-Target Report Template**
Use Markdown file path: icon-replacement/potential-targets-report.md

Required sections in the report:
1. Header
- Title: Potential Icon-Class Targets Report
- Generated at: ISO timestamp
- Mapping file: icon-replacement/icon-mapping.yaml
- Regex used: ^(fa|si|ti|tif)\\s+(fa|si|ti|tif)-[a-zA-Z0-9\\-]+$
- Included roots: dev-workflow-ui, dev-workflow-ui-test, dev-workflow-ui-test-data, dev-workflow-ui-web-test, system-cms
- Excluded paths: **/target/**, **/src_generated/**

2. Summary
- Total matches
- Unique potential targets
- Targets with mapping
- Targets without mapping
- Total replacements applied

3. Unique Targets Table
Use this exact column order:
| potential_target | mapped | mapped_to | occurrences | replacements_applied | notes |

Allowed values:
- mapped: yes | no
- mapped_to: replacement class when mapped=yes, otherwise -
- replacements_applied: integer count
- notes: free text, for example dynamic value, ignored, or manual review needed

4. Occurrences Table
Use this exact column order:
| potential_target | file | line | matched_text | mapped | mapped_to | replacement_applied |

Allowed values:
- mapped: yes | no
- replacement_applied: yes | no
- file: workspace-relative path
- line: 1-based line number

5. Unmapped Targets Section
- List each unique target where mapped=no
- Include reason when known: no-entry-in-mapping, dynamic-composition, external-source, or intentionally-unchanged

Population rules:
- Generate all rows after discovery and before replacement with mapped and mapped_to prefilled from icon-mapping.yaml.
- After replacement, update replacements_applied counts in the unique table and replacement_applied per row in the occurrences table.
- If a target has mapped=yes but replacement_applied=no for some rows, explain in notes.
- Do not include files from excluded paths.

Starter skeleton:

# Potential Icon-Class Targets Report

- Generated at: <ISO-8601>
- Mapping file: icon-replacement/icon-mapping.yaml
- Regex used: ^(fa|si|ti|tif)\\s+(fa|si|ti|tif)-[a-zA-Z0-9\\-]+$
- Included roots: dev-workflow-ui, dev-workflow-ui-test, dev-workflow-ui-test-data, dev-workflow-ui-web-test, system-cms
- Excluded paths: **/target/**, **/src_generated/**

## Summary
- Total matches: <n>
- Unique potential targets: <n>
- Targets with mapping: <n>
- Targets without mapping: <n>
- Total replacements applied: <n>

## Unique Targets
| potential_target | mapped | mapped_to | occurrences | replacements_applied | notes |
|---|---|---|---:|---:|---|

## Occurrences
| potential_target | file | line | matched_text | mapped | mapped_to | replacement_applied |
|---|---|---:|---|---|---|---|

## Unmapped Targets
- <target>: <reason>