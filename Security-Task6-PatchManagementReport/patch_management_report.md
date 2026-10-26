# The Importance of Patch Management — Research Report

**Author:** Steven Manuel Rios · OIBSIP Security Analyst Track · Task 6

## Introduction
**Patch management** is the systematic process of acquiring, testing, and deploying updates ("patches") to software, firmware, and operating systems to fix bugs, add features, and — most critically — remediate security vulnerabilities. It sits at the heart of the **vulnerability lifecycle**: a flaw is discovered, disclosed, assigned a CVE, and a patch is released; patch management is how an organisation actually closes that window of exposure before attackers exploit it. Unpatched systems are consistently one of the largest attack surfaces in cybersecurity because the vulnerabilities are *publicly known* and often have *ready-made exploits*.

## Why patches matter: the vulnerability lifecycle
1. **Discovery** — a researcher, vendor, or attacker finds a flaw.
2. **Reporting & cataloguing** — it is responsibly disclosed and assigned a **CVE** (Common Vulnerabilities and Exposures) identifier by MITRE, and scored with **CVSS** (Common Vulnerability Scoring System, 0.0–10.0) reflecting exploitability and impact.
3. **Patch release** — the vendor issues a fix.
4. **Exploitation window** — between disclosure and patching, attackers reverse-engineer the patch or use published proof-of-concept exploits. The faster an organisation patches, the smaller this window.

Because CVEs and CVSS scores are public, attackers can trivially scan the internet for systems missing a specific patch. "N-day" exploitation (attacking a known, patched-but-unapplied flaw) is often easier than finding new zero-days.

## Real-world breaches caused by unpatched systems
- **WannaCry (May 2017):** This ransomware worm spread via **EternalBlue**, an SMBv1 exploit. Microsoft had released patch **MS17-010 two months earlier (March 2017)**, but hundreds of thousands of unpatched Windows machines across 150+ countries were infected, crippling the UK's NHS and causing billions in damage. A single missing patch enabled a global crisis.
- **Equifax (2017):** Attackers exploited a known **Apache Struts** vulnerability (**CVE-2017-5638**) for which a patch had been available for **months**. The failure to patch exposed the personal data of ~**147 million** people, leading to a settlement of up to **~$700 million** with the FTC/CFPB and states.

## Consequences of not patching
- **Data breaches & ransomware:** Known-vulnerability exploitation is a leading cause of breaches and ransomware entry.
- **Financial penalties:** Equifax's ~$700M settlement; GDPR fines up to 4% of global revenue; direct breach costs — IBM's *Cost of a Data Breach* report has placed the global average breach cost at roughly **$4–4.9 million** in recent years.
- **Compliance violations:** PCI-DSS, HIPAA, and ISO 27001 all require timely patching; failure means audit findings, lost certifications, and liability.
- **Operational disruption & reputational damage** that can outlast the incident itself.

## Patch Management Lifecycle
1. **Discovery / Inventory** — maintain an accurate asset inventory and monitor vendor advisories, CVE feeds, and CISA's Known Exploited Vulnerabilities (KEV) catalog to learn what needs patching.
2. **Assessment / Prioritisation** — triage by CVSS score, exploit availability, asset criticality, and exposure (internet-facing vs internal). Patch actively-exploited, high-severity, internet-facing flaws first.
3. **Testing** — apply patches in a staging environment to catch regressions and compatibility issues before production.
4. **Deployment** — roll out in controlled waves (pilot → broad) using automation, with maintenance windows and rollback plans.
5. **Verification** — confirm patches installed successfully via scanning/reporting, and that the vulnerability is actually remediated; document for compliance.

## Best practices — prioritised 7-step checklist
1. **Maintain a complete, continuously updated asset inventory** — you cannot patch what you do not know you own.
2. **Automate vulnerability scanning and patch deployment** to reduce manual lag and human error.
3. **Prioritise by risk** using CVSS + CISA KEV + asset criticality, not just "newest first."
4. **Define and enforce patch SLAs** (e.g., critical/actively-exploited within 24–72 hours, high within 7 days).
5. **Test in staging** before production rollout; keep rollback plans.
6. **Deploy in phased waves** with monitoring and a maintenance-window schedule.
7. **Verify and report** remediation, and feed metrics (mean-time-to-patch, % compliant) back into the program.

## Challenges and how to overcome them
| Challenge | Why it blocks patching | How to overcome |
|---|---|---|
| Legacy / end-of-life systems | No vendor patches available | Isolate/segment, apply virtual patching (IPS/WAF), plan migration |
| Downtime concerns | Patching needs reboots/outages | Scheduled maintenance windows, HA/clustering, rolling updates |
| Testing requirements | Fear of breaking production | Automated staging + canary deployments with fast rollback |
| Scale & inventory gaps | Too many assets, unknown devices | Automated discovery + centralised patch-management tooling |
| Patch fatigue / prioritisation | Overwhelming volume of CVEs | Risk-based prioritisation (KEV, CVSS, exposure) |

## Conclusion
Patch management is not glamorous, but the two most costly breaches in recent memory — WannaCry and Equifax — were both *preventable with patches that already existed*. A risk-based, automated, well-verified patching program is one of the highest-return investments in security.

## References
1. NIST SP 800-40 Rev. 4, *Guide to Enterprise Patch Management Planning* — https://csrc.nist.gov/pubs/sp/800/40/r4/final
2. CISA, *Known Exploited Vulnerabilities (KEV) Catalog* — https://www.cisa.gov/known-exploited-vulnerabilities-catalog
3. MITRE CVE Program & FIRST CVSS — https://www.cve.org/ and https://www.first.org/cvss/
4. IBM Security, *Cost of a Data Breach Report* — https://www.ibm.com/reports/data-breach
5. US FTC, *Equifax Data Breach Settlement* — https://www.ftc.gov/enforcement/refunds/equifax-data-breach-settlement
