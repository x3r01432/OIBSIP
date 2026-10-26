# Security · Task 7 — Vulnerability Scanning with Nikto

## What Nikto does
**Nikto** is an open-source web-server scanner. It fires thousands of requests at a target to find dangerous files/CGIs, outdated server software, misconfigurations, missing security headers, and known vulnerabilities — a fast first-pass web audit.

## Installation
Nikto is a Perl program; installed from source:
```bash
sudo apt-get install -y perl libnet-ssleay-perl libxml-writer-perl libjson-perl
sudo git clone https://github.com/sullo/nikto.git /opt/nikto
perl /opt/nikto/program/nikto.pl -Version   # verified: Nikto 2.6.1
```

## Scan (target: local app I own)
```bash
perl /opt/nikto/program/nikto.pl -h http://127.0.0.1:8080 -o output/nikto_scan_results.txt
```
Output: [`output/nikto_scan_results.txt`](output/nikto_scan_results.txt) · console: [`output/nikto_console.txt`](output/nikto_console.txt).

## Findings (by severity)
| Severity | Finding | Risk | Fix |
|---|---|---|---|
| Medium | Missing CSP, HSTS, X-Content-Type-Options, Referrer-Policy, Permissions-Policy | XSS/clickjacking/MIME-sniffing/downgrade exposure | Add security headers |
| Low | `X-Powered-By: PHP/8.4.27` version disclosure | Aids targeted exploits | Suppress version banner |
| Informational | Admin login page `/login.php` found | Enumeration | Rate-limit, restrict |
| False positives | `.htpasswd`, `.bash_history`, JAMon, SIPS, etc. | None | — see note below |

## Nikto is a "noisy" scanner — what that means
Nikto makes a very large number of requests and does **not** try to be stealthy — it will light up any IDS/IPS and fill server logs. It is a *signature* scanner, so against the PHP built-in dev server (which returns HTTP 200 for every path) it reports many **false positives** (e.g., non-existent `.htpasswd`). Results always require **manual verification**.

## Nikto vs Nmap
- **Nmap** = *network-layer* scanner: which hosts/ports/services exist (breadth across a network).
- **Nikto** = *application-layer* scanner: deep web-server/content checks against a single HTTP service.
They are complementary: Nmap finds the web server, Nikto audits it. Cross-reference findings against the **OWASP Top 10**.

> **Note on screenshots:** this work ran in a headless environment with no GUI, so terminal/tool output is captured verbatim as text files in `output/` (these are the evidence in lieu of GUI screenshots).

> ⚠️ **Ethics:** All scanning/exploitation in this task targeted only `127.0.0.1` on a host I own and operate. Never run these tools against systems you do not own or lack written authorisation to test.
