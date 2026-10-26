# Network Security Assessment Report

**Assessor:** Steven Manuel Rios · OIBSIP Security Analyst Track · Task 10
**Date:** 2026-10-26 · **Classification:** Internal / Educational

---

## 1. Scope
| Item | Definition |
|---|---|
| **Target** | A single host I own and operate — the project build box (loopback `127.0.0.1` and its primary interface). |
| **IP range** | `127.0.0.1/32` (loopback) for the deployed test web application. |
| **Services in scope** | All listening TCP services discovered on the host, with focus on the intentionally-vulnerable "Vuln Bank" web app on `127.0.0.1:8080`. |
| **Time window** | 2026-10-26, ~11:54–12:00 PDT. |
| **Authorisation** | Self-owned host; no third-party systems were touched. All offensive tooling was confined to localhost. |
| **Methodology** | PTES-style phases: Reconnaissance → Traffic Analysis → Web Vulnerability Scan → Findings & Remediation. Severity assigned with CVSS-style reasoning. |

## 2. Phase 1 — Reconnaissance (Nmap)
Command: `nmap -sV -O 127.0.0.1` and a `-sV -sC -p 1-10000` sweep. Full output in `nmap_results.txt`.

**Discovered hosts:** 1 (127.0.0.1, up).

**Representative open ports / services:**
| Port | Service | Version/Notes |
|---|---|---|
| 1337–1340 | various dev/http servers | app-internal tooling |
| 2375/tcp | **Docker API** | Docker 29.1.4, API exposed **unauthenticated** |
| 5900, 5909 | **VNC** | protocol 3.8, **"Server does not require authentication"** |
| 6080, 6081 | WebSockify / noVNC | directory listing enabled |
| 8080/tcp | **HTTP (PHP 8.4.27)** | the in-scope Vuln Bank app |
| 8545/tcp | JSON-RPC (Ethereum-style) | `Access-Control-Allow-Origin: *` |

OS detection: fingerprint returned but no exact match (TTL 64 / Linux-consistent); the host is a Linux container.

## 3. Phase 2 — Traffic Analysis (Wireshark/tshark)
Capture: loopback + primary interface, merged into `wireshark_capture.pcap`. Evidence in `evidence/`.

- **HTTP (cleartext):** Captured GET requests to the app, including `GET /login.php?user=admin&pass=secret123` — **credentials transmitted in plaintext** and fully visible in the capture.
- **DNS:** Observed outbound queries (e.g., `example.com`) in cleartext — resolver traffic is unencrypted.
- **TCP:** Confirmed complete 3-way handshakes (SYN → SYN-ACK → ACK) on port 8080 (see `evidence/tcp_handshake.txt`).
- **ARP:** Included in capture filter for completeness.
- **Observation:** Any party on-path could read the credentials and browsing targets because the app uses HTTP, not HTTPS.

## 4. Phase 3 — Web Vulnerability Scan (Nikto)
Command: `nikto -h http://127.0.0.1:8080`. Output in `evidence/nikto_results.txt`.

Key findings: missing security headers (**no CSP, HSTS, X-Content-Type-Options, Referrer-Policy, Permissions-Policy**), `X-Powered-By: PHP/8.4.27` version disclosure. *Note:* several Nikto "hits" (e.g., `.htpasswd`, `.bash_history`, JAMon, SIPS) are **false positives** caused by the PHP built-in dev server returning HTTP 200 for every path — documented as a limitation of noisy scanning.

Manual testing confirmed a **critical SQL injection** in `login.php` (both `id` and `user`/`pass` parameters) enabling authentication bypass and full database exfiltration (credentials + SSNs).

## 5. Findings Register
| ID | Description | Severity | Affected Asset | Recommended Fix |
|---|---|---|---|---|
| F-01 | SQL injection (auth bypass + data exfiltration incl. PII/SSN) | **Critical** | `127.0.0.1:8080/login.php` | Parameterised queries / prepared statements; input validation |
| F-02 | Docker Engine API exposed without authentication | **Critical** | `127.0.0.1:2375` | Bind to localhost only / require mTLS; never expose unauthenticated |
| F-03 | VNC servers require no authentication | **High** | `127.0.0.1:5900, 5909` | Enable auth, bind to localhost, tunnel over SSH/VPN |
| F-04 | Credentials & traffic sent over cleartext HTTP | **High** | app at :8080 | Enforce HTTPS/TLS + HSTS |
| F-05 | Passwords stored as unsalted MD5 | **High** | app database | Use bcrypt/argon2 with per-user salt |
| F-06 | Missing HTTP security headers | **Medium** | app at :8080 | Add CSP, HSTS, X-Content-Type-Options, etc. |
| F-07 | Sensitive JSON-RPC with `Access-Control-Allow-Origin: *` | **Medium** | `127.0.0.1:8545` | Restrict CORS and bind locally |
| F-08 | Software/version disclosure (`X-Powered-By`) | **Low** | app at :8080 | Suppress version banners |
| F-09 | Directory listing enabled (WebSockify) | **Low/Info** | `127.0.0.1:6080/6081` | Disable autoindex |

## 6. Executive Summary (for management)
The assessment of our test host found a **critical, immediately exploitable weakness**: the web application's login page can be bypassed and its entire database — including usernames, password hashes, and Social Security Numbers — extracted by anyone who can reach it, using nothing more than text typed into a form field (SQL injection). Separately, administrative services (Docker and VNC) were reachable **without any password**, which would let an attacker take full control of the host. Supporting weaknesses include unencrypted traffic that exposes credentials in transit and weak password storage.

**Overall risk posture: HIGH.** None of these issues require sophisticated tooling to exploit. The good news is that every finding has a well-understood, low-to-moderate-effort fix. We recommend treating F-01, F-02, and F-03 as emergency remediations.

## 7. Remediation Roadmap (prioritised)
| Order | Finding | Fix | Effort |
|---|---|---|---|
| 1 | F-02 Docker API | Unbind / require mTLS | Easy |
| 2 | F-03 VNC no-auth | Enable auth + bind localhost | Easy |
| 3 | F-01 SQL injection | Parameterised queries | Medium |
| 4 | F-04 Cleartext HTTP | Deploy TLS + HSTS | Medium |
| 5 | F-05 Weak hashing | Migrate to bcrypt/argon2 | Medium |
| 6 | F-06 Security headers | Add header middleware | Easy |
| 7 | F-07 CORS/JSON-RPC | Restrict origins + bind local | Easy |
| 8 | F-08 / F-09 Info leaks | Suppress banners, disable autoindex | Easy |

## 8. References
- OWASP Web Security Testing Guide — https://owasp.org/www-project-web-security-testing-guide/
- PTES Technical Guidelines — http://www.pentest-standard.org/
- FIRST CVSS — https://www.first.org/cvss/
- OWASP Top 10 — https://owasp.org/www-project-top-ten/

## Files
- `nmap_results.txt` — full reconnaissance output
- `wireshark_capture.pcap` — merged traffic capture
- `evidence/` — Nikto output, HTTP requests, DNS queries, TCP handshake extract
