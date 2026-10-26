# Security · Task 1 — Network Scanning with Nmap

## What is Nmap?
**Nmap** (Network Mapper) is the industry-standard open-source tool for network discovery and security auditing. It sends crafted packets to a target and analyses the responses to determine which hosts are up, which TCP/UDP ports are open, what services and versions run on them, and often the operating system.

## Why network scanning matters
Scanning is the **reconnaissance** foundation of both defence and offence. Defenders use it to inventory their attack surface, find unexpected/forgotten services, and verify firewall rules; attackers use it to find a way in. You cannot secure a service you do not know is listening.

## Installation (Debian/Ubuntu)
```bash
sudo apt-get update && sudo apt-get install -y nmap
nmap --version   # verified: Nmap 7.95
```

## Scans performed (target: localhost, a host I own)
| # | Command | Purpose |
|---|---|---|
| 1 | `nmap 127.0.0.1` | Basic open-port discovery |
| 2 | `sudo nmap -sV 127.0.0.1` | Service & version detection |
| 3 | `sudo nmap -O 127.0.0.1` | OS detection (TCP/IP fingerprint) |
| 4 | `sudo nmap -sV -sC -p 1-10000 127.0.0.1` | Version + default scripts across ports |

Full output: [`output/nmap_scan_results.txt`](output/nmap_scan_results.txt).

## Open ports found & security analysis
| Port | Service | What it does | Security risk |
|---|---|---|---|
| 2375 | Docker API | Remote Docker Engine control | **Critical** — unauthenticated API = full host takeover |
| 5900/5909 | VNC | Remote desktop | **High** — fingerprint shows *no authentication required* |
| 6080/6081 | WebSockify/noVNC | VNC-over-websocket + dir listing | Medium — info disclosure |
| 8080 | HTTP (PHP 8.4.27) | The test web app | High — see Tasks 3/7/9 (SQLi, missing headers) |
| 8545 | JSON-RPC | API endpoint | Medium — `Access-Control-Allow-Origin: *` |

> **Note on screenshots:** this work ran in a headless environment with no GUI, so terminal/tool output is captured verbatim as text files in `output/` (these are the evidence in lieu of GUI screenshots).

> ⚠️ **Ethics:** All scanning/exploitation in this task targeted only `127.0.0.1` on a host I own and operate. Never run these tools against systems you do not own or lack written authorisation to test.
