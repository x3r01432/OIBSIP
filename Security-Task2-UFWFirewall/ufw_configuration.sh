#!/usr/bin/env bash
# ufw_configuration.sh — Basic UFW firewall policy (OIBSIP Security Task 2)
# Run with: sudo bash ufw_configuration.sh
# NOTE: This applies rules and ENABLES the firewall. Review before running on a
#       machine you administer. Do NOT run on a box you depend on for remote access
#       without first allowing your management port (SSH is allowed below).
set -euo pipefail

echo "[*] Installing UFW if missing..."
command -v ufw >/dev/null 2>&1 || sudo apt-get install -y ufw

echo "[*] Setting safe default policy: deny incoming, allow outgoing..."
sudo ufw default deny incoming
sudo ufw default allow outgoing

echo "[*] Rule 1: allow SSH (22/tcp) so we never lock ourselves out..."
sudo ufw allow ssh                     # 22/tcp

echo "[*] Rule 2: deny plain HTTP (80/tcp) — force HTTPS only..."
sudo ufw deny http                     # 80/tcp

echo "[*] Rule 3 (extra): allow HTTPS (443/tcp)..."
sudo ufw allow https                   # 443/tcp

echo "[*] Rule 4 (extra): deny a suspicious subnet (example 203.0.113.0/24)..."
sudo ufw deny from 203.0.113.0/24

echo "[*] Rule 5 (extra): rate-limit SSH to blunt brute-force attempts..."
sudo ufw limit ssh

echo "[*] Enabling UFW..."
sudo ufw --force enable

echo "[*] Current ruleset:"
sudo ufw status verbose
