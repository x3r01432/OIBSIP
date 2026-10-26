# Security · Task 10 — Full Network Security Assessment

An end-to-end, multi-tool security assessment of a self-owned test host, written up as a professional report.

## Deliverables
- **[`network_security_assessment.md`](network_security_assessment.md)** — the full report: scope, 3 assessment phases, findings register (with Finding ID / Description / Severity / Affected Asset / Recommended Fix), a 1-page executive summary, and a prioritised remediation roadmap with effort estimates.
- **[`nmap_results.txt`](nmap_results.txt)** — Phase 1 reconnaissance.
- **[`wireshark_capture.pcap`](wireshark_capture.pcap)** — Phase 2 traffic capture.
- **[`evidence/`](evidence/)** — Nikto output, HTTP requests, DNS queries, TCP handshake extract.

## Methodology
PTES-style phases — Reconnaissance (Nmap) → Traffic Analysis (Wireshark) → Web Vulnerability Scan (Nikto) → Findings & Remediation — with CVSS-style severity. Overall risk posture assessed as **HIGH**, driven by an exploitable SQL injection and unauthenticated admin services (Docker API, VNC).

> ⚠️ **Ethics:** All scanning/exploitation in this task targeted only `127.0.0.1` on a host I own and operate. Never run these tools against systems you do not own or lack written authorisation to test.
