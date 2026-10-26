# Social Engineering Attacks — Research Report

**Author:** Steven Manuel Rios · OIBSIP Security Analyst Track · Task 5

## Introduction
Social engineering is the art of manipulating people into divulging information or performing actions that compromise security. Rather than breaking cryptography or exploiting code, it exploits human psychology — trust, authority, urgency, fear, and helpfulness. It is consistently ranked among the most effective attack vectors: Verizon's annual *Data Breach Investigations Report* has repeatedly found that the **human element** (phishing, misuse, error, stolen credentials) is involved in roughly **three-quarters of breaches**, and phishing remains one of the top initial-access techniques. Because it targets people, no firewall fully stops it — defence requires awareness plus technical controls. This report examines phishing, pretexting, baiting, and quid pro quo.

## 1. Phishing
**Types:**
- **Spear phishing** — highly targeted emails tailored to a specific person using researched details.
- **Whaling** — spear phishing aimed at executives ("big fish") to authorise payments or data release.
- **Vishing** — voice phishing over the phone (e.g., fake IT support or bank calls).
- **Smishing** — phishing via SMS/text, often with malicious links or fake delivery notices.

**How it works:** The attacker impersonates a trusted entity (bank, employer, vendor, government) and creates urgency or fear to make the victim click a malicious link, open a weaponised attachment, or enter credentials on a spoofed site.

**Documented case study:** The **2016 breach of the Democratic National Committee / John Podesta** began with a spear-phishing email disguised as a Google security alert; the victim entered credentials on a fake login page, handing attackers access to tens of thousands of emails.

**Prevention (4):**
1. Deploy email authentication (SPF, DKIM, **DMARC**) and anti-phishing gateways.
2. Enforce **phishing-resistant MFA** (FIDO2/hardware keys) so stolen passwords alone are useless.
3. Run continuous security-awareness training with simulated phishing.
4. Provide an easy "report phishing" button and a culture that rewards reporting.

## 2. Pretexting
**Definition:** The attacker invents a believable fabricated scenario (a "pretext") and assumes a role — IT technician, auditor, new executive, vendor — to extract information or access. Unlike phishing's blast approach, pretexting is a sustained, interactive con built on researched, plausible detail.

**How an attacker builds a false scenario:** They gather OSINT (org charts, job titles, vendor names, email formats), craft a credible identity, and layer small, reasonable requests that escalate — first confirming "routine" details, then requesting the sensitive action while invoking authority or urgency.

**Documented case study:** In the **2020 Twitter breach**, attackers used phone-based pretexting (vishing) against employees, posing as internal IT, to obtain credentials and access internal admin tools — then hijacked high-profile accounts for a crypto scam.

**Prevention (3):**
1. Mandatory **out-of-band verification** (call back a known number) for any sensitive or financial request.
2. Least-privilege access and strict internal processes so no single employee can be talked into a high-impact action.
3. Train staff to recognise authority/urgency pressure and to verify identity before acting.

## 3. Baiting
**Definition:** Baiting lures victims with something enticing. **Physical baiting** leaves infected USB drives in parking lots, lobbies, or desks, relying on curiosity. **Digital baiting** offers "free" downloads, pirated media, or cracked software laced with malware.

**Documented case study:** Research by **Stajano & others and the well-documented USB-drop studies (e.g., the University of Illinois study, 2016)** showed that a large fraction of dropped USB drives are picked up and plugged in — the Illinois team found ~45–98% of drops were connected, with users opening files. The technique is widely used in real penetration tests and by attackers to seed malware past the perimeter.

**Prevention (3):**
1. Disable USB auto-run and restrict/endpoint-control removable media (device-control policies).
2. Train employees never to connect unknown devices and to hand found media to security.
3. Use application allow-listing and EDR so unexpected executables cannot run.

## 4. Quid Pro Quo (bonus)
**Explanation:** The attacker offers a benefit or service in exchange for information or access — e.g., calling employees claiming to be IT offering to "fix" an issue if the user disables antivirus or reveals a password. The perceived fair exchange lowers the victim's guard.

**Prevention:** Verify unsolicited "help" through official channels; publish that legitimate IT will never ask for passwords; and require ticketed, authenticated support interactions.

## Comparison Table
| Attack type | Primary target | Psychological lever exploited | Best countermeasure |
|---|---|---|---|
| Phishing | Mass users / specific individuals | Urgency, fear, trust in brands | DMARC + phishing-resistant MFA + training |
| Pretexting | Employees with access/info | Authority, plausibility, helpfulness | Out-of-band identity verification |
| Baiting | Curious/opportunistic users | Curiosity, greed (free stuff) | Removable-media control + EDR |
| Quid Pro Quo | Help-seeking employees | Reciprocity (fair exchange) | Verify support via official channels |

## Organisational Recommendations — 5-point employee security-awareness checklist
1. **Train quarterly** with realistic simulated phishing, vishing, and USB-drop exercises; measure and coach.
2. **Verify before trusting** — require call-back verification for any request involving money, credentials, or sensitive data.
3. **Report easily and blamelessly** — one-click reporting and a no-punishment culture so incidents surface fast.
4. **Enforce MFA and least privilege** so a single human mistake cannot cascade into a breach.
5. **Control the channels** — DMARC/SPF/DKIM on email, removable-media policies, and clear rules that IT never asks for passwords.

## References
1. Verizon, *Data Breach Investigations Report (DBIR)* — https://www.verizon.com/business/resources/reports/dbir/
2. CISA, *Avoiding Social Engineering and Phishing Attacks* — https://www.cisa.gov/news-events/news/avoiding-social-engineering-and-phishing-attacks
3. NIST SP 800-50 / Phish Scale, *Security Awareness & Training* — https://csrc.nist.gov/
4. SANS Security Awareness, *The Human Risk* resources — https://www.sans.org/security-awareness-training/
5. Tischer et al., *"Users Really Do Plug in USB Drives They Find"*, IEEE S&P 2016 — https://doi.org/10.1109/SP.2016.26
