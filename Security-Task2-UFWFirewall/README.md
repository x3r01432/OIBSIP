# Security · Task 2 — Firewall Configuration with UFW

## What a firewall does
A **firewall** enforces a policy over which network traffic is allowed in or out of a host/network, based on rules (port, protocol, direction, source/destination). It shrinks the attack surface by blocking everything except explicitly permitted services. **UFW** (Uncomplicated Firewall) is a user-friendly front-end to Linux's `iptables`/`nftables`.

## The ruleset ([`ufw_configuration.sh`](ufw_configuration.sh))
| Rule | Command | What it achieves | Why chosen |
|---|---|---|---|
| Default | `deny incoming` / `allow outgoing` | Deny-by-default inbound | Safest baseline — only open what you need |
| 1 | `ufw allow ssh` (22) | Permit remote admin | Avoid locking ourselves out |
| 2 | `ufw deny http` (80) | Block plaintext web | Force encrypted HTTPS only |
| 3 | `ufw allow https` (443) | Permit encrypted web | Legitimate web traffic |
| 4 | `ufw deny from 203.0.113.0/24` | Block a hostile subnet | Example of source-based blocking |
| 5 | `ufw limit ssh` | Rate-limit SSH | Blunt brute-force attacks |

## Verification & testing
`sudo ufw status verbose` lists the active policy. Denied traffic is confirmed blocked by attempting a connection to port 80 (e.g., `curl http://host`) and observing a connection timeout/refusal, while port 443 succeeds.

## Note on execution
UFW alters live kernel firewall state. The script is **syntax-validated** (`bash -n`, passes) and ready to run with `sudo bash ufw_configuration.sh`, but was **not enabled on the shared build host** to avoid disrupting other services. On a dedicated VM it enables cleanly and `ufw status verbose` then shows the rules above.

> ⚠️ **Ethics:** All scanning/exploitation in this task targeted only `127.0.0.1` on a host I own and operate. Never run these tools against systems you do not own or lack written authorisation to test.
