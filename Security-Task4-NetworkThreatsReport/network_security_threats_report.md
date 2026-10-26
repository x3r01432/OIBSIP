# Common Network Security Threats — Research Report

**Author:** Steven Manuel Rios · OIBSIP Security Analyst Track · Task 4

## Introduction
Network security threats matter more today than at any point in computing history. Organisations run on always-on connectivity — cloud services, remote work, APIs, and IoT — which means the "network" is now the primary battleground for attackers. A single exploited path can expose customer data, halt operations, and trigger regulatory penalties. Understanding how the most common attacks work, and how to stop them, is the foundation of a defensible posture. This report covers four high-impact threats: DoS/DDoS, Man-in-the-Middle (MITM), IP spoofing, and DNS poisoning.

## 1. Denial-of-Service / Distributed Denial-of-Service (DoS/DDoS)
**How it works:** A DoS attack overwhelms a target (server, service, or link) with more requests or traffic than it can handle, exhausting CPU, memory, connection tables, or bandwidth so legitimate users are denied service. A *Distributed* DoS uses thousands of compromised machines (a botnet) to amplify volume. Common techniques include SYN floods (half-open TCP connections), UDP/ICMP floods, and reflection/amplification (e.g., DNS or NTP, where a small spoofed request triggers a large response aimed at the victim).

**Real-world example:** In October 2016 the **Mirai botnet** — built from hijacked IoT devices (cameras, routers) using default credentials — launched a ~1 Tbps DDoS against DNS provider **Dyn**, knocking Twitter, Reddit, Netflix, and GitHub offline across the US East Coast.

**Impact:** Revenue loss during downtime, SLA/contract penalties, reputational damage, and cover for secondary intrusions.

**Mitigations:**
1. Deploy upstream DDoS scrubbing / CDN services (e.g., Cloudflare, AWS Shield) that absorb and filter volumetric traffic before it reaches origin.
2. Rate-limiting, SYN cookies, and connection throttling on servers and load balancers.
3. Anycast network distribution plus over-provisioned bandwidth so load spreads across many points of presence.

## 2. Man-in-the-Middle (MITM) Attacks
**How it works:** The attacker secretly positions themselves between two communicating parties, relaying and possibly altering traffic while both sides believe they talk directly. Techniques include ARP spoofing on a LAN, rogue Wi-Fi access points ("evil twin"), SSL stripping (downgrading HTTPS to HTTP), and session-cookie theft.

**Real-world example:** The **Superfish** adware pre-installed on Lenovo laptops (2014–2015) installed a self-signed root certificate that let it intercept HTTPS traffic to inject ads — effectively a built-in MITM that also exposed users to third-party interception because the private key was shared.

**Impact:** Theft of credentials, session tokens, and sensitive data; silent manipulation of transactions; loss of confidentiality and integrity.

**Mitigations:**
1. Enforce TLS everywhere with HSTS so browsers refuse downgraded/plaintext connections.
2. Use certificate pinning and validate certificate chains; monitor for unexpected CAs.
3. On local networks, use dynamic ARP inspection, 802.1X port security, and encrypted VPNs on untrusted Wi-Fi.

## 3. IP Spoofing
**How it works:** The attacker forges the source IP address in packet headers to impersonate a trusted host, hide their origin, or bypass IP-based access controls. It underpins reflection/amplification DDoS, blind TCP session hijacking, and trust-relationship abuse.

**Real-world example:** IP spoofing is the enabling technique behind **DNS and NTP amplification DDoS** campaigns (e.g., the 2018 **memcached amplification** attacks that reached 1.3+ Tbps against GitHub), where spoofed source addresses cause servers to flood the victim with responses.

**Impact:** Enables large-scale reflected DDoS, evasion of attribution, and bypass of weak IP-allowlist authentication.

**Mitigations:**
1. Implement **BCP 38 / ingress & egress filtering** (RFC 2827) at network edges to drop packets with forged source addresses.
2. Never rely on source IP alone for authentication; use cryptographic authentication (mutual TLS, signed tokens).
3. Use randomised TCP initial sequence numbers and encrypted sessions to defeat blind hijacking.

## 4. DNS Poisoning / Spoofing (bonus 4th threat)
**How it works:** The attacker corrupts the DNS resolution process so a hostname resolves to an attacker-controlled IP. This can be done by injecting forged responses into a resolver's cache (cache poisoning), exploiting predictable transaction IDs, or compromising a resolver. Victims who type a legitimate domain are silently sent to a malicious server.

**Real-world example:** The **2008 Kaminsky vulnerability** demonstrated that weak DNS transaction-ID randomisation made cache poisoning practical at scale, prompting an industry-wide emergency patch. More recently, **DNS hijacking campaigns (e.g., "Sea Turtle", 2019)** redirected government and infrastructure domains by tampering with DNS records.

**Impact:** Mass redirection to phishing/malware sites, interception of email (MX record tampering), and credential theft at scale.

**Mitigations:**
1. Deploy **DNSSEC** to cryptographically sign and validate DNS records.
2. Use source-port and transaction-ID randomisation; run trusted, patched resolvers (and consider DNS-over-HTTPS/TLS).
3. Monitor DNS records for unauthorised changes and lock registrar accounts with MFA.

## Comparison Table
| Threat | Primary attack vector | Who is at risk | Difficulty to execute | Ease of mitigation |
|---|---|---|---|---|
| DoS/DDoS | Volumetric/protocol flooding via botnets | Any internet-facing service | Low–Medium (DDoS-for-hire exists) | Medium (needs upstream scrubbing) |
| MITM | Interception on shared/compromised paths | Users on untrusted networks | Medium | Medium–High (TLS/HSTS largely solve it) |
| IP Spoofing | Forged packet source addresses | Networks without edge filtering | Medium | High (BCP 38 is well understood) |
| DNS Poisoning | Forged/injected DNS responses | All users of a resolver/domain | Medium–High | Medium (DNSSEC adoption still partial) |

## Conclusion — 3 takeaways for a network administrator
1. **Encrypt and authenticate everything.** TLS with HSTS, mutual authentication, and DNSSEC neutralise the majority of MITM and spoofing risks.
2. **Filter at the edge.** Ingress/egress filtering (BCP 38) and upstream DDoS protection stop spoofed and volumetric traffic before it does damage.
3. **Assume the network is hostile and monitor continuously.** Logging, anomaly detection, and rapid patching turn silent compromises into detectable, containable events.

## References
1. NIST SP 800-61r2, *Computer Security Incident Handling Guide* — https://csrc.nist.gov/pubs/sp/800/61/r2/final
2. CISA, *Understanding Denial-of-Service Attacks* — https://www.cisa.gov/news-events/news/understanding-denial-service-attacks
3. MITRE ATT&CK, *Network Denial of Service (T1498)* and *Adversary-in-the-Middle (T1557)* — https://attack.mitre.org/
4. Cloudflare Learning Center, *What is a DDoS attack? / DNS cache poisoning* — https://www.cloudflare.com/learning/
5. RFC 2827 (BCP 38), *Network Ingress Filtering* — https://www.rfc-editor.org/rfc/rfc2827
