"""Check mechanical AGENTS.md limits and architectural import boundaries."""

import re
from pathlib import Path


def masked_source(source):
    pattern = r'"""[\s\S]*?"""|"(?:\\.|[^"\\])*"|\'(?:\\.|[^\'\\])*\'|//[^\n]*|/\*[\s\S]*?\*/'
    return re.sub(pattern, lambda match: re.sub(r"[^\n]", " ", match.group()), source)


def check_file(path):
    source = path.read_text(encoding="utf-8")
    lines = source.splitlines()
    failures = []
    if len(lines) > 180:
        failures.append(f"{path}: {len(lines)} lines exceed 180")
    for number, line in enumerate(lines, 1):
        if len(line) > 120 or "\t" in line:
            failures.append(f"{path}:{number}: width or tab violation")
        indent = len(line) - len(line.lstrip(" "))
        if line.strip() and indent % 4:
            failures.append(f"{path}:{number}: indentation is not a multiple of four")
        if line.startswith("import ") and line.endswith(".*"):
            failures.append(f"{path}:{number}: wildcard import")
        if line.startswith("import com.google.firebase") and path.name != "FirebaseCrashReporter.kt":
            failures.append(f"{path}:{number}: Firebase boundary violation")
    masked = masked_source(source)
    for match in re.finditer(r"\bfun\s+(?:[\w<>?]+\.)?\w+\s*\(", masked):
        depth = 1
        offset = match.end()
        while offset < len(masked) and depth:
            depth += (masked[offset] == "(") - (masked[offset] == ")")
            offset += 1
        body = re.search(r"[={]", masked[offset:])
        if not body:
            continue
        start = offset + body.start()
        if masked[start] == "=":
            continue
        depth = 1
        end = start + 1
        while end < len(masked) and depth:
            depth += (masked[end] == "{") - (masked[end] == "}")
            end += 1
        length = masked[match.start():end].count("\n") + 1
        if length > 60:
            failures.append(f"{path}: function at line {masked[:match.start()].count(chr(10)) + 1}: {length} > 60")
    if path.parent.name == "ui" and path.suffix == ".kt":
        if re.search(r"^import .*\.(data|reporting)\.", source, re.M) and path.name != "DemoViewModel.kt":
            failures.append(f"{path}: UI bypasses ViewModel")
        if re.search(r"^import android\.", source, re.M):
            failures.append(f"{path}: platform import outside permission adapter")
    if path.name == "DemoViewModel.kt" and re.search(r"^import android\.", source, re.M):
        failures.append(f"{path}: ViewModel has Android framework dependency")
    return failures


def main():
    rules = re.findall(r"^\d+\. ", Path("AGENTS.md").read_text(encoding="utf-8"), re.M)
    failures = [] if len(rules) <= 8 else ["AGENTS.md exceeds eight rules"]
    files = sorted(path for path in Path("app/src").rglob("*.kt"))
    for path in files:
        failures.extend(check_file(path))
    gate = Path("app/src/main/java/dev/example/permissioncrashdemo/permission/PermissionGate.kt").read_text()
    if "READ_CONTACTS" in gate or "CAMERA" in gate:
        failures.append("PermissionGate is tied to a particular permission")
    print(f"Checked {len(files)} Kotlin files and {len(rules)} project rules.")
    for failure in failures:
        print(f"FAIL: {failure}")
    print("Mechanical checks passed; naming, MVVM behavior, and device flows still require review." if not failures else "FAILED")
    raise SystemExit(bool(failures))


if __name__ == "__main__":
    main()
